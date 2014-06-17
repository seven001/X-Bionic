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

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


/**
 * 用来下载图片，并读取图片以及显示图片
 * 
 * @author Li Bin
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
	 *            图片的网络路�?,这个url也作为图片在缓存中的key
	 * @param view
	 *            显示图片用的控件
	 */
	@SuppressLint("NewApi") 
	public void fetch(String url, View view) {
		@SuppressWarnings("unchecked")
		WeakReference<ImageWorkerTask> weakTask = (WeakReference<ImageWorkerTask>) view
				.getTag();
		if (weakTask != null) {
			if (view instanceof ImageView) {
				((ImageView) view).setImageBitmap(null);
			}else {
				view.setBackground(null);
			}
		}
		ImageWorkerTask task = new ImageWorkerTask(url, view);
		weakTask = new WeakReference<ImageWorkerTask>(task);
		view.setTag(weakTask);
		task.execute();
	}

	// 图片下载异步任务内部�?
	private class ImageWorkerTask extends AsyncTask<Void, Void, Boolean> {
		private String url;
		private WeakReference<View> weakImageView;
		private int reqWidth;
		private int reqHeight;

		protected ImageWorkerTask(String url, View view) {
			this.url = url;
			weakImageView = new WeakReference<View>(view);
			if(view instanceof ImageView) {
				this.reqWidth = DisplayUtil.px2Dip(view.getContext(),
						view.getLayoutParams().width);
				this.reqHeight = DisplayUtil.px2Dip(view.getContext(),
						view.getLayoutParams().height);
			}
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
				Drawable drawable = mImageCache.getDrawableFormLacal(url);
				if (weakImageView != null) {
					View view = weakImageView.get();
					if (view != null && bm != null) {
						if(view instanceof ImageView) {
							((ImageView) view).setImageBitmap(bm);
						}else {
							view.setBackground(drawable);
						}
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
	public static boolean downLoadImage(String url) {
		File imageFile = new File(StorageHelper.getAppImageDir(),
				String.valueOf(url.hashCode()));
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
			Log.e(IMAGE_FETCHER_DEBUG_TAG, "文件未找到?");
		} catch (IOException e) {
			Log.e(IMAGE_FETCHER_DEBUG_TAG, e.getLocalizedMessage());
		} finally {
			HttpHelper.closeStream(out);
			HttpHelper.closeStream(is);
		}
		return isSucc;
	}
}
