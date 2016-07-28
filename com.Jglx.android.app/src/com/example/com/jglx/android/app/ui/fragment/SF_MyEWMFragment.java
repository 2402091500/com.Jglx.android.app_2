package com.example.com.jglx.android.app.ui.fragment;

import java.util.Hashtable;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.ui.PersonCenterActivity;
import com.example.com.jglx.android.app.util.AppUtil;
import com.example.com.jglx.android.constants.URLs;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 个人中心-二维码
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class SF_MyEWMFragment extends Fragment {
	// 二维码的长与宽
	private int QR_HEIGHT = 600;
	private int QR_WIDTH = 600;
	private Bitmap bit;

	private ImageView mEWMIv;
	
	private int hw=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.myfragment_ewm, null);
		mEWMIv = (ImageView) view.findViewById(R.id.myFragment_ewm_iv);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		QR_HEIGHT=AppUtil.screenHeight/3;
		QR_WIDTH=AppUtil.screenWidth*2/3;
		hw=getResources().getDimensionPixelSize(R.dimen.icon_ewm_height_width);
		create(URLs.ERweiMa + PersonCenterActivity.userInfo_2.UserID);
	}

	/**
	 * 二维码的生成的方法
	 * 
	 */
	private void create(String text) {
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

			if (TextUtils.isEmpty(PersonCenterActivity.userInfo_2.Logo)) {

				bit = BitmapFactory.decodeResource(getResources(),
						R.drawable.default_head);

			} else {
				ImageLoader imagld = LXApplication.getImageLoader();
				bit = imagld.loadImageSync(URLs.get_imag
						+ PersonCenterActivity.userInfo_2.Logo);
				if (bit == null) {
					bit = BitmapFactory.decodeResource(getResources(),
							R.drawable.default_head);
				}

			}

			Bitmap icon2 = scaleBitmap(bit, hw, hw);// 二维码中间的自定义图像区 不能太大
													// ，个人亲测超过50扫描会有障碍。
			// 2-将用户图像画在二维码中间， 千万别覆盖四个角的扫描线
			Bitmap finalBitmap = mergeBitmap(bitmap, icon2);

			// 设置给view展示
			mEWMIv.setImageBitmap(finalBitmap);

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

}
