package com.example.com.jglx.android.app.ui;

import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.constants.URLs;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class My_ErWeiMaActivity extends BaseActivity {

	// 二维码的长与宽
	private int QR_HEIGHT = 600;
	private int QR_WIDTH = 600;
	private ImageView my_erweima;
	private CircleImageView my_touxian;
	private TextView michen;
	private TextView xiaoquming;
	private TextView my_age;
	private RelativeLayout my_xinbi_rl;
	private ImageView my_sex_mw;
	private TextView mEWMShowTv;
	private UserInfo_2 user;
	private Bitmap bit;

	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my__er_wei_ma);

		type = getIntent().getStringExtra("activityType");

		initView();
		if (!TextUtils.isEmpty(type)) {
			setTitleTextRightText("", "小区二维码", "", true);
			create(URLs.ERweiMa + AppStatic.getInstance().getUser().BuildingID, type);
		} else {
			setTitleTextRightText("", "我的二维码", "", true);
			create(URLs.ERweiMa + AppStatic.getInstance().getUser().UserID, type);
		}
	}

	/**
	 * 二维码的生成的方法
	 * 
	 */
	private void create(String text, String type) {
		try {
			// 需要引入core包
			QRCodeWriter writer = new QRCodeWriter();
			if (text == null || "".equals(text) || text.length() < 1) {
				return;
			}
			// 把输入的文本转为二维码
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
					QR_WIDTH, QR_HEIGHT);

			System.out.println("w:" + martix.getWidth() + "h:"
					+ martix.getHeight());
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 创建二维码图片
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

			// 自定义个性化二维码 中间放图标或者用户图像
			// 1-缩放用户图像大小50*50二维码中间的自定义图像区 不能太大 ，个人亲测超过50扫描会有障碍。
			// Bitmap icon = BitmapFactory.decodeResource(getResources(),
			// R.drawable.erweima);

			if (!TextUtils.isEmpty(type)) {// 小区二维码
				bit = BitmapFactory.decodeResource(getResources(),
						R.drawable.default_group);

			} else {// 我的二维码
				if (LXApplication.LocalTouxian != ""
						&& LXApplication.LocalTouxian != null) {
					bit = BitmapFactory.decodeFile(LXApplication.LocalTouxian);
				} else if (user.Logo == "" || user.Logo == null) {

					bit = BitmapFactory.decodeResource(getResources(),
							R.drawable.default_head);

				} else {
					ImageLoader imagld = LXApplication.getImageLoader();
					bit = imagld.loadImageSync(URLs.get_imag + user.Logo);
					if (bit == null) {
						bit = BitmapFactory.decodeResource(getResources(),
								R.drawable.default_head);
					}

				}
			}

			Bitmap icon2 = scaleBitmap(bit, 50, 50);// 二维码中间的自定义图像区 不能太大
													// ，个人亲测超过50扫描会有障碍。
			// 2-将用户图像画在二维码中间， 千万别覆盖四个角的扫描线
			Bitmap finalBitmap = mergeBitmap(bitmap, icon2);

			// 设置给view展示
			my_erweima.setImageBitmap(finalBitmap);

			// 及时回收不要的图片
			if (bit != null & !bit.isRecycled()) {
				icon2.recycle();
				bit.recycle();
				bitmap.recycle();
			}

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public Bitmap scaleBitmap(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的缩放后的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * @param src
	 * @param icon
	 * @return
	 */
	public Bitmap mergeBitmap(Bitmap src, Bitmap icon) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = icon.getWidth();
		int wh = icon.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawBitmap(icon, (w - ww) / 2, (h - wh) / 2, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newb;
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		// TODO Auto-generated method stub
		my_erweima = (ImageView) findViewById(R.id.my_erweima);
		michen = (TextView) findViewById(R.id.michen);

		// 小区名
		xiaoquming = (TextView) findViewById(R.id.xiaoquming);

		// 我的年龄
		my_age = (TextView) findViewById(R.id.my_age);

		// 我的性别图
		my_sex_mw = (ImageView) findViewById(R.id.my_sex_mw);
		// 我的性别
		my_xinbi_rl = (RelativeLayout) findViewById(R.id.my_xinbi_rl);
		my_touxian = (CircleImageView) findViewById(R.id.my_touxian);
		mEWMShowTv = (TextView) findViewById(R.id.my_erweima_showTv);

		if (TextUtils.isEmpty(type)) {
			my_age.setText(String.valueOf(AppStatic.getInstance().getUser().Age));
			michen.setText(AppStatic.getInstance().getUser().NickName);
			xiaoquming.setText(AppStatic.getInstance().getUser().BuildingName);
			if (AppStatic.getInstance().getUser().Sex == 1) {

				my_sex_mw.setImageResource(R.drawable.sex_man);
				my_xinbi_rl.setBackgroundColor(R.drawable.retangle_blue);
			}
			System.out.println("我的二维码头像" + AppStatic.getInstance().getUser().Logo);
			user = AppStatic.getInstance().getUser();
			if (LXApplication.LocalTouxian != ""
					&& LXApplication.LocalTouxian != null) {
				Bitmap bit = BitmapFactory
						.decodeFile(LXApplication.LocalTouxian);
				my_touxian.setImageBitmap(bit);
			} else if (user.Logo == "" || user.Logo == null) {
				my_touxian.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.default_head));

			} else {

				my_touxian.setUrl(user.Logo);
			}
		} else {
			mEWMShowTv.setText("扫一扫上面的二维码图案，入驻小区哟");
			my_xinbi_rl.setVisibility(View.GONE);
			my_touxian.setImageResource(R.drawable.default_group);
			xiaoquming.setVisibility(View.GONE);
			michen.setText(AppStatic.getInstance().getUser().BuildingName);
		}

	}
 
}
