package com.example.com.jglx.android.app.adapter;

	import java.util.ArrayList;
import java.util.List;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.Utils;
import com.example.com.jglx.android.app.info.ServerGridItem;
import com.example.com.jglx.android.app.ui.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

	public class ServerGridItemAdapter extends BaseAdapter	{

	    private LayoutInflater inflater; 
	    private List<ServerGridItem> gridItemList;
		private RelativeLayout itemll; 
		private Activity context;
		public ArrayList<String> list_title;
		public ArrayList<Integer> list_images;
	 
		
		  ViewHolder viewHolder; 
		  PopupWindow pop; 
	    public ServerGridItemAdapter(ArrayList<String> list_title, ArrayList<Integer> list_images,Activity context) 
	    { 
	        super(); 
	        gridItemList = new ArrayList<ServerGridItem>(); 
	        inflater = LayoutInflater.from(context); 
	        this.context=context;
	        this.list_images=list_images;
	        
	        for (int i = 0; i < list_images.size(); i++) 
	        { 
	        	ServerGridItem picture = new ServerGridItem(list_title.get(i), list_images.get(i)); 
	            gridItemList.add(picture); 
	        } 
	    } 
	    @Override
	    public int getCount( )
	    {
	        if (null != gridItemList) 
	        { 
	            return gridItemList.size(); 
	        } 
	        else
	        { 
	            return 0; 
	        } 
	    }

	    @Override
	    public Object getItem( int position )
	    {
	        return gridItemList.get(position); 
	    }

	    @Override
	    public long getItemId( int position )
	    {
	        return position; 
	    }

	    @Override
	    public View getView( int position, View convertView, ViewGroup parent )
	    {
	       
	        if (convertView == null) 
	        { 
	            convertView = inflater.inflate(R.layout.grid_item, null);
	            itemll=(RelativeLayout)convertView.findViewById(R.id.itemll);
	            Point mPoint = new Point();
            	mPoint=Utils.getPICSize(context.getWindowManager());
            	  
            	itemll.setLayoutParams(new GridView.LayoutParams((mPoint.x-4)/3,(mPoint.x-4)/3));
	            viewHolder = new ViewHolder(); 
	            viewHolder.title = (TextView) convertView.findViewById(R.id.title); 
	            viewHolder.image = (ImageView) convertView.findViewById(R.id.image); 
	            if(LXApplication.ismore){
	            	
	            		viewHolder.image.setImageResource(R.drawable.fuwu_more);
	            		
	            }
	            convertView.setTag(viewHolder); 
	        } else
	        { 
	            viewHolder = (ViewHolder) convertView.getTag(); 
	        } 
	        viewHolder.title.setText(gridItemList.get(position).getTitle());
	        
	        viewHolder.title.setTextSize(12);
	        viewHolder.title.setPadding(0, 10, 0, 0);
	        
	        Drawable topDrawable = convertView.getResources().getDrawable(list_images.get(position));
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
			viewHolder.title.setCompoundDrawables(null, topDrawable, null, null);
			
			viewHolder.title.setCompoundDrawablePadding(35);
//			viewHolder.title.draw(gridItemList.get(position).getImageId());;
//	        viewHolder.image.setImageResource(gridItemList.get(position).getImageId()); 
	        return convertView; 
	    }
	    /*ViewHolder*/
	    static class ViewHolder 
	    { 
	        public TextView title;
	        public ImageView image;
	        
	    } 
	}
