package com.example.com.jglx.android.app.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.Action_ditail;
import com.example.com.jglx.android.app.info.Goodsditail_info;
import com.example.com.jglx.android.app.info.Share_ditail_info;
import com.example.com.jglx.android.app.util.ShareUtil;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.RetangleImageView;
import com.example.com.jglx.android.constants.URLs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 活动详情
 * 
 * @author jjj
 * 
 * @date 2015-8-25
 */
public class ActionDetailActivity extends BaseActivity {
	private WebView mWebView;
	private Dialog mShareDialog;
	public String AcitvityID;
	private RetangleImageView the_big_tv;
	private CircleImageView the_logo;
	private TextView action_bt;

	private Action_ditail action_ditail;
	private LinearLayout context_image;
	public DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Goodsditail_info goods_info;
	
	private Share_ditail_info Share_ditail_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_actiondetail);
		setTitleText("", "活动详情", R.drawable.icon_share, true);
		AcitvityID = getIntent().getStringExtra("AcitvityID");
		
		
		imageLoader = ImageLoader.getInstance();
		initview();
		
		getData();
		getgoods_ditail();

	}

	private void get_share_date() {
		String shareFlag="";
		if(action_ditail.getType().equals("1")){
			 shareFlag="1";
		}else if(action_ditail.getType().equals("3")){
			 shareFlag="3";
		}else{
			return;
		}
		RequstClient.app_share("", shareFlag, new CustomResponseHandler(this, false){
			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("这是Tmd  分享详情"+content);
				try {
					JSONObject obj=new JSONObject(content);
					
					if(!obj.getString("Status").equals("0")){
						String errorMsg=obj.getString("Msg");
						T.showShort(ActionDetailActivity.this, errorMsg);
						return;
					}
					
					
					Share_ditail_info=new Gson().fromJson(new JSONObject(content).getJSONObject("Data").toString(), Share_ditail_info.class);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
	}

	private void getgoods_ditail() {
		RequstClient.Action_goods_ditail(AcitvityID, new CustomResponseHandler(
				this, false) {
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("商品详情：" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						return;
					}
					Log.v("商品详情：", content);

					goods_info = new Gson().fromJson(new JSONObject(content)
							.getJSONObject("Data").toString(),
							Goodsditail_info.class);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void initview() {
		the_big_tv = (RetangleImageView) findViewById(R.id.the_big_tv);
		context_image = (LinearLayout) findViewById(R.id.context_image);
		the_logo = (CircleImageView) findViewById(R.id.the_logo);
		action_bt = (TextView) findViewById(R.id.action_bt);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.default_head).delayBeforeLoading(0)
				.cacheOnDisc(false).resetViewBeforeLoading(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.showStubImage(R.drawable.default_head)
				// .showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				// .imageScaleType(ImageScaleType.EXACTLY)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	@SuppressLint("ResourceAsColor")
	public void setImage() {
		the_big_tv.setUrl(action_ditail.getCoverImage());
		the_logo.setUrl(action_ditail.getLogoImage());

		for (int i = 0; i < action_ditail.getContext().size(); i++) {

			ImageView image = new ImageView(this);

			image.setAdjustViewBounds(true);
			image.setScaleType(ScaleType.CENTER_CROP);
			image.setBackgroundColor(R.color.white);
			System.out.println(URLs.SERVICE_HOST_IMAGE
					+ action_ditail.getContext().get(i));
			imageLoader.displayImage(URLs.SERVICE_HOST_IMAGE
					+ action_ditail.getContext().get(i), image);
			context_image.addView(image);

			// System.out.println("活动里面的正文图片"+action_ditail.getContext().get(i));
			// RetangleImageView ri=new RetangleImageView(this);
			// ri.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT));
			// ri.setScaleType(ScaleType.FIT_XY );
			// ri.setUrl(action_ditail.getContext().get(i));
			// context_image.addView(ri);
		}
		if (action_ditail.getType().equals("1")) {
			action_bt.setText("立即充值");
		} else if (action_ditail.getType().equals("0")
				&& action_ditail.getApply().equals("0")) {
			action_bt.setText("立即报名");
		} else if (action_ditail.getType().equals("0")
				&& action_ditail.getApply().equals("1")) {
			action_bt.setText("已报名");
		} else if (action_ditail.getType().equals("2")) {
			action_bt.setText("立即购买");
		} else if (action_ditail.getType().equals("3")) {
			action_bt.setText("立即报名");
		} else {
			action_bt.setVisibility(View.GONE);

		}
		action_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (action_ditail.getType().equals("1")) {

					Intent intent = new Intent(ActionDetailActivity.this,
							Fuwu_HuafeiActivity.class);
					LXApplication.ishuafei_action = "1";
					startActivity(intent);
					System.out.println("11111111111111111话费活动");
				} else if (action_ditail.getType().equals("2")) {
					Intent intent = new Intent(ActionDetailActivity.this,
							ShopDetailActivity.class);
					intent.putExtra("ActivityID", AcitvityID);
					intent.putExtra("GoodsPic", action_ditail.getLogoImage());
					startActivity(intent);
				} else if (action_ditail.getType().equals("0")
						&& action_ditail.getApply().equals("0")) {
					baoming_huodong();
				}else if(action_ditail.getType().equals("3")){
					
					Intent intent = new Intent(ActionDetailActivity.this,ApplyGroupManagerActivity.class);
					startActivity(intent);
				}
				

			}
		});
	}

	public void baoming_huodong() {
		RequstClient.Current_user_registration_activity(action_ditail
				.getActivityID(), new CustomResponseHandler(this, true) {
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("报名服务器返回情况：" + content);
				action_bt.setText("已报名");
				action_ditail.setApply("1");
				Toast.makeText(ActionDetailActivity.this, "报名成功", 100);
			}
		});

	}

	private void getData() {
		RequstClient.Query_activity_details(AcitvityID,
				new CustomResponseHandler(this, false) {

					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);
						System.out.println("活动详情：" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								return;
							}
							Log.v("活动详情__", content);

							action_ditail = new Gson().fromJson(new JSONObject(
									content).getJSONObject("Data").toString(),
									Action_ditail.class);

							get_share_date();
							
							setImage();

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});

	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		if(Share_ditail_info==null){
			mShareDialog =new ShareUtil(this).getShareDialog();
			if (mShareDialog != null && !mShareDialog.isShowing()) {
				mShareDialog.show();
			}
			return;
		}
		mShareDialog = new ShareUtil(ActionDetailActivity.this, Share_ditail_info.getTitle(), Share_ditail_info.getDesc(), Share_ditail_info.getLogo(), Share_ditail_info.getUrl()).getShareDialog();
		if (mShareDialog != null && !mShareDialog.isShowing()) {
			mShareDialog.show();
		}
	}

}
