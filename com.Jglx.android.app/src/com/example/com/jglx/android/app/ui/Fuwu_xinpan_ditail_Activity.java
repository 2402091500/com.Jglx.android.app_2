package com.example.com.jglx.android.app.ui;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.HorizontalListViewAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.ActionInfo;
import com.example.com.jglx.android.app.info.QuanJin_Info;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.info.Xinpan_ditail;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.ImageDownLoader;
import com.example.com.jglx.android.app.view.HorizontalListView;
import com.example.com.jglx.android.app.view.RetangleImageView;
import com.example.com.jglx.android.constants.URLs;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView.ScaleType;

/*******************************************************************************************************
* @ClassName: Fuwu_xinpan_ditail_Activity 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lilifeng
* @date 2015年10月20日 上午11:19:08 
*  
********************************************************************************************************
*/ 
public class Fuwu_xinpan_ditail_Activity extends BaseActivity {

	private FrameLayout videoview;
	private WebView videowebview;
	private xWebChromeClient xwebchromeclient;
	private Boolean islandport = true;// true表示此时是竖屏，false表示此时横屏。
	private View xCustomView;
	private WebChromeClient.CustomViewCallback xCustomViewCallback;
	public String PropertyID;

	public Xinpan_ditail info = new Xinpan_ditail();
	public QuanJin_Info qj_info = new QuanJin_Info();

	public static List<QuanJin_Info> mList = new ArrayList<QuanJin_Info>();
	public List<String> sului_list_jindian = new ArrayList<String>();
	public List<String> suolui_list_manyou= new ArrayList<String>();
	private HorizontalListView jdhx;
	private HorizontalListViewAdapter hListViewAdapter_jindian;

	public ImageDownLoader loader = new ImageDownLoader(this);

	private HorizontalListView jgmy;
	private HorizontalListViewAdapter hListViewAdapter_manyou;
	private TextView ditail;
	private ImageView quwei;
	private ImageView jianzu;
	private LinearLayout wuyea;

	private ImageLoader imageLoader;
	public DisplayImageOptions options;
	private RetangleImageView hongbao;

	private String hongbao_money;
	private TextView huodongxianqing;
	public static Boolean can_hong_bao = true;
	private Bitmap mp4_s;
	private TextView tell;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu_xinpan_ditail);
		setTitleTextRightText("", "", "", true);
		PropertyID = getIntent().getStringExtra("PropertyID");
		imageLoader = ImageLoader.getInstance();
		
		initview();
		
		
		  handler = new Handler(){
			  @Override
			public void handleMessage(Message msg) {
				  
				  switch ((Integer)msg.obj) {
				case 1:
					
					setIMage();
					break;
				case 2:
					setIMage_360();
					break;

			
				}
			}
		  };  
	       
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				getData();				
			}
		}).start();
		 new Thread(new Runnable() {
			 
			 @Override
			 public void run() {
				 get360Data();				
			 }
		 }).start();
//		  Thread t=new Thread(){
//			@Override
//			public void run() {
//				getData();
//				
//				
//			}  
//		  };
//		  t.start();
//		  Thread t1=new Thread(){
//			  @Override
//			  public void run() {
//				  get360Data();
//				  
//				  
//			  }  
//		  };
//		  t1.start();
//		getData();
//		get360Data();


	}

	public void set_title(String name) {
		
		setTitleTextRightText("", name, "", true);
	}

	private void get360Data() {
		 final Long l=System.currentTimeMillis();
		RequstClient.Get_loupan_allImage(PropertyID, new CustomResponseHandler(
				this, false) {
			
			@Override
			public void onFailure(String error, String errorMessage) {
				// TODO Auto-generated method stub
				super.onFailure(error, errorMessage);
				T.showShort(Fuwu_xinpan_ditail_Activity.this, "请检查网络连接！");
				get360Data();
				System.out.println("加载360list失败" + error + errorMessage);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				Long l1=System.currentTimeMillis();
				System.out.println("请求网络用时"+(l1-l));

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						get360Data();
						return;
					}
					Log.v("全景图列表---", content);
					mList = new Gson().fromJson(new JSONObject(content)
							.getJSONArray("Data").toString(),
							new TypeToken<List<QuanJin_Info>>() {
							}.getType());
					if (mList.size() != 0) {
						if(sului_list_jindian.size()!=0){
							sului_list_jindian.clear();
						}

						for (int i = 0; i < mList.size(); i++) {
							sului_list_jindian.add(mList.get(i).IconPath);
						}
					}
					
					 Message mg=Message.obtain();
						mg.obj=2;
						handler.sendMessage(mg);
					

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	public void getHongbao() {
		if (can_hong_bao) {
			can_hong_bao = false;

			RequstClient.Get_loupan_money(PropertyID,
					new CustomResponseHandler(this, false) {

						@Override
						public void onSuccess(int statusCode, String content) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, content);
							System.out.println("获得到的红包：" + content);

							JSONObject obj;
							try {
								obj = new JSONObject(content);
								if (obj.getString("State").equals("1")) {
									hongbao_money = "0";
								} else {
									hongbao_money = info.getRedMoney();
								}
								Intent intent = new Intent(
										Fuwu_xinpan_ditail_Activity.this,
										Qian_hongBao_Activity.class);
								intent.putExtra("hongbao_money", hongbao_money);
								intent.putExtra("logo", info.getIcon());
								startActivity(intent);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		can_hong_bao = true;
		try {
			videowebview.getClass().getMethod("onResume")
					.invoke(videowebview, (Object[]) null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			videowebview.getClass().getMethod("onPause")
					.invoke(videowebview, (Object[]) null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initview() {
		 //视频
		videoview = (FrameLayout) findViewById(R.id.video_view);
		videowebview = (WebView) findViewById(R.id.v_webview);
		WebSettings ws = videowebview.getSettings();

		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕

		ws.setUseWideViewPort(false);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);
		xwebchromeclient = new xWebChromeClient();
		videowebview.setWebChromeClient(xwebchromeclient);
		videowebview.setWebViewClient(new xWebViewClientent());
		
		
		
		if(sului_list_jindian.size()==0){
			for(int i=0;i<=4;i++){
				
				sului_list_jindian.add("defalt");
			}
		}
		if(suolui_list_manyou.size()==0){
			for(int i=0;i<=4;i++){
				
				suolui_list_manyou.add("defalt");
			}
		}
		// 经典户型
		jdhx = (HorizontalListView) findViewById(R.id.jdhx);
		hListViewAdapter_jindian = new HorizontalListViewAdapter(
				this, sului_list_jindian,"1");
		jdhx.setAdapter(hListViewAdapter_jindian);
		
		// 景观漫游
		jgmy = (HorizontalListView) findViewById(R.id.jgmy);
		hListViewAdapter_manyou = new HorizontalListViewAdapter(
				this,suolui_list_manyou,"2");
		jgmy.setAdapter(hListViewAdapter_manyou);
		
		//项目介绍
		ditail = (TextView) findViewById(R.id.ditail);
        //区位
		quwei = (ImageView) findViewById(R.id.quwei);
		quwei.setImageResource(R.drawable.default_img_big);
		//建筑
		jianzu = (ImageView) findViewById(R.id.jianzu);
		jianzu.setImageResource(R.drawable.default_img_big);
		// 活动详情
		huodongxianqing = (TextView) findViewById(R.id.huodongxianqing);
		//红包
		hongbao = (RetangleImageView) findViewById(R.id.hongbao);
		hongbao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHongbao();

			}

		});
        //物业
		wuyea = (LinearLayout) findViewById(R.id.wuyea);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.default_img_big).delayBeforeLoading(0)
				.cacheOnDisc(false).resetViewBeforeLoading(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.showStubImage(R.drawable.default_img_big)
				 .showImageOnLoading(R.drawable.default_img_big) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_img_big)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				// .imageScaleType(ImageScaleType.EXACTLY)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		//联系电话
		tell=(TextView)findViewById(R.id.tell);
       
	}

	public void setIMage_360() {
		// 经典户型
				hListViewAdapter_jindian.notifyDataSetChanged();
				jdhx.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						if(mList.size()!=0){
							
							Intent intent = new Intent(Fuwu_xinpan_ditail_Activity.this,
									PanoramaGLActivity.class);
							intent.putExtra("position", arg2);
							intent.putExtra("list_info", (Serializable)mList) ; 
							intent.putExtra("list", (Serializable)sului_list_jindian) ; 
							intent.putStringArrayListExtra("list", (ArrayList<String>) sului_list_jindian)  ;




							System.out.println("第几个户型"+arg2);
							startActivity(intent);
						}else{
							
						}
						
					}
				});
	}
	public void setIMage() {
		//标题
		set_title(info.getName());
		
		//视频
		System.out.println("视频播放地址：" + URLs.SERVICE_HOST_IMAGE
				+ info.getVideo());
		videowebview.loadUrl(URLs.SERVICE_HOST_IMAGE + info.getVideo());
		videowebview.onResume();
		
		
		// 景观漫游
		if(suolui_list_manyou.size()!=0){
			suolui_list_manyou.clear();
		}
		suolui_list_manyou.addAll(info.getLandscapeImages());
		hListViewAdapter_manyou.notifyDataSetChanged();
		jgmy.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String[] array = (String[]) info.getLandscapeImages().toArray(
						new String[0]);
				Intent intent = new Intent(Fuwu_xinpan_ditail_Activity.this,
						ShowImgActivity.class);
				intent.putExtra("imgs", array);
				intent.putExtra("curImg", arg2);
				startActivity(intent);
			}
		});
		
        //活动详情
		if (!info.getAcitvityID().equals("0") ) {
			huodongxianqing.setVisibility(View.VISIBLE);
			huodongxianqing.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							Fuwu_xinpan_ditail_Activity.this,
							ActionDetailActivity.class);
					intent.putExtra("AcitvityID", info.getAcitvityID());
					startActivity(intent);

				}
			});
		} 
			
		
		

		//项目介绍
		ditail.setText("      "+info.getDetail());
		
		//区位展示
		imageLoader.displayImage(URLs.SERVICE_HOST_IMAGE
				+info.getLocatImage(), quwei,options);

		//建筑规划
		imageLoader.displayImage(URLs.SERVICE_HOST_IMAGE
				+info.getPlaneImage(), jianzu,options);

		//物业
		if (info.getManageImages().size() != 0) {

			for (int d = 0; d < info.getManageImages().size(); d++) {
				ImageView wuye_image = new ImageView(this);

				wuye_image.setAdjustViewBounds(true);
				wuye_image.setScaleType(ScaleType.CENTER_CROP);
				System.out.println(URLs.SERVICE_HOST_IMAGE
						+ info.getManageImages().get(0));
				imageLoader.displayImage(URLs.SERVICE_HOST_IMAGE
						+ info.getManageImages().get(d), wuye_image,options);
			
				wuyea.addView(wuye_image);

			}
		}
		//联系电话
				tell.setText(info.getTelphone());
				tell.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String number=tell.getText().toString();
						
						 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
			                startActivity(intent);  
						
					}
				});

	}

	/**
	 * 处理各种通知、请求等事件
	 * 
	 * @author
	 */
	public class xWebViewClientent extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			return false;
		}
	}

	/**
	 * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
	 */
	public class xWebChromeClient extends WebChromeClient {
		private Bitmap xdefaltvideo=BitmapFactory.decodeResource(getResources(),
				R.drawable.x1);
		private View xprogressvideo;

		@Override
		// 播放网络视频时全屏会被调用的方法
		public void onShowCustomView(View view,
				WebChromeClient.CustomViewCallback callback) {
			if (islandport) {
			} else {

			}
			quanpin_and_not(false);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			videowebview.setVisibility(View.GONE);
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			videoview.addView(view);
			xCustomView = view;
			xCustomViewCallback = callback;
			videoview.setVisibility(View.VISIBLE);
		}

		@Override
		// 视频播放退出全屏会被调用的
		public void onHideCustomView() {
			quanpin_and_not(true);

			if (xCustomView == null)// 不是全屏播放状态
				return;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);

			videoview.removeView(xCustomView);
			xCustomView = null;
			videoview.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			videowebview.setVisibility(View.VISIBLE);
		}

		public void quanpin_and_not(Boolean b) {
			if (b) {

				base_title_goneor_not(true);
				hongbao.setVisibility(View.VISIBLE);
			} else {
				hongbao.setVisibility(View.GONE);
				base_title_goneor_not(false);
			}
		}

		// 视频加载添加默认图标
		@Override
		public Bitmap getDefaultVideoPoster() {
			// Log.i(LOGTAG, "here in on getDefaultVideoPoster");
			if (xdefaltvideo == null) {
				if(mp4_s==null){
					
					xdefaltvideo = BitmapFactory.decodeResource(getResources(),
							R.drawable.x1);
				}else{
					
					return mp4_s;
				}
			}
			return mp4_s;
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			// Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater
						.from(Fuwu_xinpan_ditail_Activity.this);
				xprogressvideo = inflater.inflate(R.layout.view_loading_video,
						null);
			}
			return xprogressvideo;
		}

		// 网页标题
		@Override
		public void onReceivedTitle(WebView view, String title) {
			(Fuwu_xinpan_ditail_Activity.this).setTitle(title);
		}

	}

	public void getData() {
		 final Long l=System.currentTimeMillis();
		RequstClient.Get_loupan_Ditail(PropertyID, new CustomResponseHandler(
				this, true) {
			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				 Long l1=System.currentTimeMillis();
				 System.out.println("请求网络用时"+(l1-l));
				System.out.println("Get_loupan_Ditail----" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					info = new Gson().fromJson(new JSONObject(content)
							.getJSONObject("Data").toString(),
							Xinpan_ditail.class);
					
					 mp4_s=createVideoThumbnail(URLs.SERVICE_HOST_IMAGE + info.getVideo(), 1024, 500);
					
					 
					 Message mg=Message.obtain();
						mg.obj=1;
						handler.sendMessage(mg);
					

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				T.showShort(Fuwu_xinpan_ditail_Activity.this, "请检查网络连接！");
				getData();
			}
		});

	}
	
	
	/*
	*      获取网络视频缩略图
	* @param @param url
	* @param @param width
	* @param @param height
	* @param @return    设定文件 
	* @return Bitmap    返回类型 
	* @throws 
	*
	*/ 
	private Bitmap createVideoThumbnail(String url, int width, int height) {
		  Bitmap bitmap = null;
		  MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		  int kind = MediaStore.Video.Thumbnails.MINI_KIND;
		  try {
		   if (Build.VERSION.SDK_INT >= 14) {
		    retriever.setDataSource(url, new HashMap<String, String>());
		   } else {
		    retriever.setDataSource(url);
		   }
		   bitmap = retriever.getFrameAtTime();
		  } catch (IllegalArgumentException ex) {
		   // Assume this is a corrupt video file
		  } catch (RuntimeException ex) {
		   // Assume this is a corrupt video file.
		  } finally {
		   try {
		    retriever.release();
		   } catch (RuntimeException ex) {
		    // Ignore failures while cleaning up.
		   }
		  }
		  if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
		   bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		     ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		  }
		  return bitmap;
		 }

}
