package com.imcore.x_bionic.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.util.DisplayUtil;
import com.imcore.x_bionic.util.StorageHelper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

//import com.imcore.yunmingtea.http.HttpHelper;
//import com.imcore.yunmingtea.util.DisplayUtil;

/**
 * 用来下载图片，并读取图片以及显示图片
 * 
 * @author chen
 */
public class ImageFetcher {
	private ImageCache mImageCache;

	private final static String IMAGE_FETCHER_DEBUG_TAG = "ImageFetcher";

	public ImageFetcher() {
		this.mImageCache = ImageCache.getInstance();
	}

	/**
	 * 获取图片对象
	 * 
	 * @param url
	 *            图片的网络路径 ,这个url也作为图片在缓存中的key
	 * @param view
	 *            显示图片用的控件
	 */
	public void fetch(String url, ImageView view) {
		new ImageWorkerTask(url, view).execute();
	}

	// 图片下载异步任务内部类
	private class ImageWorkerTask extends AsyncTask<Void, Void, Boolean> {
		private String url;
		private WeakReference<ImageView> weakImageView;
		private int reqWidth;
		private int reqHeight;

		protected ImageWorkerTask(String url, ImageView view) {
			this.url = url;
			weakImageView = new WeakReference<ImageView>(view);
			this.reqWidth = DisplayUtil.px2Dip(view.getContext(),
					view.getWidth());
			this.reqHeight = DisplayUtil.px2Dip(view.getContext(),
					view.getHeight());
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if (!mImageCache.isCached(url)) {
				boolean isSucc = downLoadImage(url);
				return isSucc;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Bitmap bm = null;
				if (this.reqWidth != 0 && this.reqHeight != 0) {
					bm = mImageCache.get(url, this.reqWidth, this.reqHeight);
				} else {
					bm = mImageCache.get(url);
				}
				if (weakImageView != null) {
					ImageView view = weakImageView.get();
					if (view != null) {
						// 判断当前ImageView是否是在AdapterView中被重用的
						@SuppressWarnings("unchecked")
						WeakReference<ImageWorkerTask> weakTask = (WeakReference<ImageWorkerTask>) view
								.getTag();
						if (weakTask != null) {
							// 这里表示被重用，被重用的话，清除旧图片
							view.setImageBitmap(null);
						} else {
							weakTask = new WeakReference<ImageWorkerTask>(this);
							view.setTag(weakTask);
						}

						view.setImageBitmap(bm);
					}
				}
			}
		}
	}

	/**
	 * 从指定的url下载图片到本地存储器
	 * 
	 * @param url
	 * @return 返回true表示图片下载成功
	 */
	private boolean downLoadImage(String url) {
		File imageFile = new File(StorageHelper.getAppImageDir() + "/"
				+ url.hashCode());
		InputStream is = null;
		FileOutputStream out = null;

		boolean isSucc = false;
		try {
			if (!imageFile.exists()) {
				imageFile.createNewFile();
			}

			out = new FileOutputStream(imageFile.getAbsolutePath());
			is = HttpHelper.getInputStream(url);
			if (is != null) {
				byte[] buffer = new byte[128];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				isSucc = true;
			}
		} catch (FileNotFoundException e) {
			Log.e(IMAGE_FETCHER_DEBUG_TAG, "文件未找到");
		} catch (IOException e) {
			Log.e(IMAGE_FETCHER_DEBUG_TAG, e.getLocalizedMessage());
		} finally {
			HttpHelper.closeStream(out);
			HttpHelper.closeStream(is);
		}
		return isSucc;
	}
}
