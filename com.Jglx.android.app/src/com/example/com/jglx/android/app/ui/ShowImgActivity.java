package com.example.com.jglx.android.app.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.ImageUtil;
import com.example.com.jglx.android.constants.URLs;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 展示图片
 * 
 * @author jjj
 * 
 * @date 2015-8-20
 */
public class ShowImgActivity extends Activity {
	private ViewPager mPager;
	private TextView mCurTv;
	private TextView mToalTv;
	private int curImg;
	private PhotoView[] imageViews;
	private String[] imgs;// 图片地址
	private Bitmap mBitmap;
	private String mFileName;
	private String mSaveMessage;

	private String imgPath = URLs.SERVICE_HOST_IMAGE;// 需要下载的网络图片地址
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		AppStatic.getInstance().addActivity(this);
		setContentView(R.layout.activity_showimg);

		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		AppStatic.getInstance().removeActivity(this);
	}

	private void initView() {
		mPager = (ViewPager) findViewById(R.id.shomImg_viewPager);
		mCurTv = (TextView) findViewById(R.id.shomImg_curTv);
		mToalTv = (TextView) findViewById(R.id.shomImg_toalTv);
		findViewById(R.id.shomImg_backIv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						ShowImgActivity.this.finish();
					}
				});
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurTv.setText(String.valueOf(arg0 + 1));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initData() {
		curImg = getIntent().getIntExtra("curImg", 0);
		imgs = getIntent().getStringArrayExtra("imgs");
		mCurTv.setText(String.valueOf(curImg));

		int imgSize = imgs.length;
		mToalTv.setText(String.valueOf(imgSize));

		imageViews = new PhotoView[imgSize];
		PhotoView mPhotoView = null;
		for (int i = 0; i < imgSize; i++) {

			String path = imgs[i];
			mPhotoView = new PhotoView(this);
			mPhotoView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mPhotoView.setScaleType(ScaleType.FIT_CENTER);
			String imagePath = "";

			imagePath = path;

			ImageLoader.getInstance().displayImage(imgPath + path, mPhotoView);
			mPhotoView.setOnLongClickListener(onLongClickListener);
			imageViews[i] = mPhotoView;

			
			// RetangleImageView iView = new RetangleImageView(
			// ShowImgActivity.this);
			// iView.setUrl(imgs[i]);
			// imageViews[i] = iView;
			// iView.setOnLongClickListener(onLongClickListener);
		}
		ImgAdapter imgAdapter = new ImgAdapter();
		mPager.setAdapter(imgAdapter);
		mPager.setCurrentItem(curImg - 1);

	}

	private void showSaveDialog() {
		if (mDialog == null) {
			View dView = LayoutInflater.from(this).inflate(
					R.layout.dialog_saveimg, null);
			dView.findViewById(R.id.dialog_saveImg_saveTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							if (!TextUtils.isEmpty(imgPath)) {// 地址不能为空
								Toast.makeText(ShowImgActivity.this,
										"正在下载图片...", Toast.LENGTH_SHORT).show();
								new Thread(saveFileRunnable).start();
							}
							mDialog.dismiss();
						}
					});
			dView.findViewById(R.id.dialog_saveImg_cancelTv)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDialog.dismiss();
						}
					});
			mDialog = DialogUtil.getMenuDialog(this, dView);
			mDialog.show();
		} else if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	OnLongClickListener onLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View arg0) {
			imgPath = imgPath + imgs[curImg - 1];
			String pString = imgs[curImg - 1];
			mFileName = pString.replace("/", "");

			showSaveDialog();
			return false;
		}
	};

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ImageUtil.filePath);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ImageUtil.filePath + fileName);

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private Runnable saveFileRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				mBitmap = BitmapFactory.decodeStream(getImageStream(imgPath));

				if (mBitmap != null) {
					saveFile(mBitmap, mFileName);
					messageHandler.sendEmptyMessage(1);
				}

			} catch (IOException e) {
				mSaveMessage = e.getMessage().toString();
				messageHandler.sendEmptyMessage(2);
				e.printStackTrace();

			} catch (Exception e1) {
				mSaveMessage = e1.getMessage().toString();
				messageHandler.sendEmptyMessage(0);
				e1.printStackTrace();
			}

		}

	};

	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case 0:
				Toast.makeText(ShowImgActivity.this, mSaveMessage,
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(ShowImgActivity.this,
						"图片已保存至目录LinXin/images/" + mFileName, Toast.LENGTH_LONG)
						.show();
				break;
			case 2:
				Toast.makeText(ShowImgActivity.this, mSaveMessage,
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		}
	};

	class ImgAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews == null ? 0 : imageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViews[position
					% imageViews.length]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(imageViews[position
					% imageViews.length], 0);
			return imageViews[position % imageViews.length];
		}
	}
}
