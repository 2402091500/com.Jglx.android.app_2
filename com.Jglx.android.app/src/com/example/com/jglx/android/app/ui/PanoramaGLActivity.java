package com.example.com.jglx.android.app.ui;


import java.util.ArrayList;
import java.util.List;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.PanoramaGLAdapter;
import com.example.com.jglx.android.app.info.QuanJin_Info;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.ImageDownLoader;
import com.example.com.jglx.android.constants.URLs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLImage;
import com.panoramagl.PLView;
import com.panoramagl.enumerations.PLCubeFaceOrientation;
import com.panoramagl.transitions.PLTransitionBlend;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PanoramaGLActivity extends PLView {

	private TextView back;
	private ListView lv_gallery;
	private PanoramaGLAdapter adapter;
	private DisplayImageOptions options;
	private int position;
	public QuanJin_Info info=new QuanJin_Info();
	
	 private Dialog dialog;
	 public static List<QuanJin_Info> mList = new ArrayList<QuanJin_Info>();
	 public List<String> sului_list_jindian = new ArrayList<String>();
	
	public ImageDownLoader loader = new ImageDownLoader(this);
	private ImageLoader imageLoader;
	public String str="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected View onContentViewCreated(View contentView) {
		
		imageLoader=ImageLoader.getInstance();
		 position=getIntent().getIntExtra("position", 1);
		 mList=(List<QuanJin_Info>) getIntent().getSerializableExtra("list_info");
		 sului_list_jindian=getIntent().getStringArrayListExtra("list");

		 
		 
		 info=mList.get(position);
		    
		init_image_loader();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ViewGroup mainView = (ViewGroup) this.getLayoutInflater().inflate(
				R.layout.activity_panorama_gl, null);
		
		// 添加360视图
				mainView.addView(contentView, 0);

				back = (TextView) mainView.findViewById(R.id.back);
				back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						PanoramaGLActivity.this.finish();
					}
				});
				
				lv_gallery = (ListView) mainView.findViewById(R.id.lv_gallery);
				lv_gallery.setVisibility(View.VISIBLE);
				adapter = new PanoramaGLAdapter(this,sului_list_jindian);
				lv_gallery.setAdapter(adapter);
				//全景图切换
				lv_gallery.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						 info=mList.get(arg2);
						 load_quanjingtu();
						
					}
				});
		str=((info.ImageZP.contains(".jpg")||info.ImageZP.contains(".png"))&&
				(info.ImageZN.contains(".jpg")||info.ImageZN.contains(".png"))&&
				(info.ImageXP.contains(".jpg")||info.ImageXP.contains(".png"))&&
				(info.ImageXN.contains(".jpg")||info.ImageXN.contains(".png"))&&
				(info.ImageYP.contains(".jpg")||info.ImageYP.contains(".png"))&&
				(info.ImageYN.contains(".jpg")||info.ImageYN.contains(".png")))==true?"0":"1";
		
		     load_quanjingtu();
		     System.out.println("info.ImageZN"+info.ImageZN);
		     System.out.println("info.ImageZP"+info.ImageZP);
		     System.out.println("info.ImageXN"+info.ImageXN);
		     System.out.println("info.ImageXP"+info.ImageXP);
		     System.out.println("info.ImageYP"+info.ImageYP);
		     System.out.println("info.ImageYN"+info.ImageYN);
		     
		
		
		
		return super.onContentViewCreated(mainView);
	}
	public void load_quanjingtu() {
		dialog= DialogUtil.getCenterProgressDialog(PanoramaGLActivity.this, R.string.dialog_loading, true);
		dialog.show();
		new Thread(){
			

			public void run() {
				Looper.prepare();
				 loadPanorama(0, info.ImageZP, info.ImageZN, info.ImageXP, info.ImageXN, info.ImageYP, info.ImageYN);
			 };
		 }.start();
	}
	private void loadPanorama(int index, String imageZP, String imageZN,
			String imageXP, String imageXN, String imageYP, String imageYN) {try {
				Context context = this.getApplicationContext();
				PLIPanorama panorama = null;
				// 锁定全景视图
				this.setLocked(true);
				// 全景全景视图
				switch (index) {
				// 立方体全景 (supports up 1024x1024 image per face)
				case 0:
					PLCubicPanorama cubicPanorama = new PLCubicPanorama();
                    System.out.println("立体全景看房"+URLs.SERVICE_HOST_IMAGE+imageZP);
               if( str.equals("0")){
                    cubicPanorama.setImage
                    (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+imageZN,options)), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
                    cubicPanorama.setImage
                    (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+imageZP,options)), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
					
                    cubicPanorama.setImage
                    (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+imageXN,options)), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
                    cubicPanorama.setImage
                    (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+imageXP,options)), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
                    
                    cubicPanorama.setImage
                    (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+imageYP,options)), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
                    cubicPanorama.setImage
                    (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+imageYN,options)), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
                   }else{
                	   cubicPanorama.setImage
                	   (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+"Property/default/cubezn.jpg",options)), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
                	   cubicPanorama.setImage
                	   (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+"Property/default/cubezp.jpg",options)), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
                	   
                	   cubicPanorama.setImage
                	   (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+"Property/default/cubexn.jpg",options)), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
                	   cubicPanorama.setImage
                	   (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+"Property/default/cubexp.jpg",options)), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
                	   
                	   cubicPanorama.setImage
                	   (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+"Property/default/cubeyp.jpg",options)), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
                	   cubicPanorama.setImage
                	   (new  PLImage(imageLoader.loadImageSync(URLs.SERVICE_HOST_IMAGE+"Property/default/cubeyn.jpg",options)), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
                	   
                   }
					panorama = cubicPanorama;
					 dialog.dismiss();
						lv_gallery.setEnabled(true);
					break;
				default:
					break;
				}
				if (panorama != null) {
					// 设置摄像机的旋转角度
					panorama.getCamera().lookAt(0.0f, 170.0f);
					/*
					 * // 添加一个热点 panorama.addHotspot(new PLHotspot(1, new
					 * PLImage(PLUtils .getBitmap(context, R.raw.hotspot), false),
					 * 0.0f, 170.0f, 0.05f, 0.05f));
					 */
					// 重置视图
					this.reset();
					// 加载全景图
					this.startTransition(new PLTransitionBlend(2.0f), panorama); // 或者
																					// this.setPanorama(panorama);
				}
				// 开启全景视图
				this.setLocked(false);
				
			} catch (Throwable e) {
				Toast.makeText(this, "图片有误，无法加载",
						Toast.LENGTH_SHORT).show();
			}}
	public void init_image_loader() {
		options = new DisplayImageOptions.Builder()
         .cacheInMemory(true)
         .showImageOnFail(R.drawable.default_head)
         .delayBeforeLoading(0)
          .cacheOnDisc(true)
           .resetViewBeforeLoading(true)
          .displayer(new FadeInBitmapDisplayer(100))
          .showStubImage(R.drawable.default_img_big)
            .showImageOnLoading(R.drawable.default_img_big) //设置图片在下载期间显示的图片  
       .showImageForEmptyUri(R.drawable.default_img_big)
         .bitmapConfig(Bitmap.Config.RGB_565)
//         .imageScaleType(ImageScaleType.EXACTLY)
         .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
         .build();
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		imageLoader.clearMemoryCache();  
        imageLoader.clearDiscCache();
	}
    
	
}
