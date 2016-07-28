package com.panoramagl.example;

import com.panoramagl.PLImage;



import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLSpherical2Panorama;
import com.panoramagl.PLTexture;
import com.panoramagl.PLView;
import com.panoramagl.enumerations.PLCubeFaceOrientation;
import com.panoramagl.example.R;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.transitions.PLTransitionBlend;
import com.panoramagl.utils.PLUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class PanoramaGLActivity extends PLView
{
	/**init methods*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Context context = this.getApplicationContext();
		PLIPanorama panorama = null;
		//Load panorama
		PLCubicPanorama cubicPanorama = new PLCubicPanorama();
    	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.p_1f), false), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
    	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.p_1b), false), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
    	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.p_1l), false), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
    	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.p_1r), false), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
    	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.p_1u), false), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
    	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.p_1d), false), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
        panorama = cubicPanorama;
//        if(panorama != null)
//    	{
//    		//Set camera rotation
//    		panorama.getCamera().lookAt(0.0f, 0.0f);
//	        //Reset view
//	        this.reset();
//	        //Load panorama
//	        this.startTransition(new PLTransitionBlend(2.0f), panorama); //or use this.setPanorama(panorama);
//    	}
//		PLSpherical2Panorama panorama = new PLSpherical2Panorama();
//		panorama.getCamera().lookAt(30.0f, 90.0f);
//        panorama.setImage(new PLImage(PLUtils.getBitmap(this, R.raw.spherical_pano), false));
        this.setPanorama(panorama);
	}
	
	/**
     * This event is fired when root content view is created
     * @param contentView current root content view
     * @return root content view that Activity will use
     */
	@Override
	protected View onContentViewCreated(View contentView)
	{
		//Load layout
		ViewGroup mainView = (ViewGroup)this.getLayoutInflater().inflate(R.layout.activity_main, null);
		//Add 360 view
    	mainView.addView(contentView, 0);
    	//Return root content view
		return super.onContentViewCreated(mainView);
	}
}