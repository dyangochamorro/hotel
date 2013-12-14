package com.shine.hotels.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.hotels.R;
import com.squareup.picasso.Picasso;

public class HomepageAdapter extends BaseAdapter {
	
//	private AnimationSet mAnimationSet;
	
	private int selectItem ;  
	
	private Context mContext;
	
//	// 定义数组，即图片资源
//	private Integer[] mImageIds = {
//			R.drawable.icon_service,
//			R.drawable.icon_trip,
//			R.drawable.icon_introduct,
//			R.drawable.icon_tv,
//			R.drawable.icon_video,	
//	};
//	
//	// 定义数组，即文字资源
//	private String[] txts = {"客房服务", "旅行指南", "酒店介绍", "电视欣赏", "点播服务"} ;
	
	// 定义数组，即图片资源
	private String[] mImageUrls ;
	// 定义数组，即文字资源
	private String[] txts ;
	
	
	//声明ImageAdapter
	public HomepageAdapter(Context c, String[] mImageUrls, String[] txts){
		mContext = c;
		
		this.mImageUrls = mImageUrls ;
		this.txts = txts ;
		
		// 设置默认选中值
		if (null!=mImageUrls && mImageUrls.length>0)
			selectItem = (mImageUrls.length)/2 ;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position % mImageUrls.length;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelectItem(int selectItem) {
        if (this.selectItem != selectItem) {                  
	        this.selectItem = selectItem;  
	        notifyDataSetChanged();                 
        }  
    }  
	
	public void update(String[] mImageUrls, String[] txts) {
		this.mImageUrls = mImageUrls ;
		this.txts = txts ;
	}
	      

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cover_flow_item, null);

            Holder holder = new Holder();
            holder.image = (ImageView)convertView.findViewById(R.id.cover_flow_image);
            holder.text = (TextView)convertView.findViewById(R.id.cover_flow_text);

            convertView.setTag(holder);
        }

        if (null == mImageUrls || mImageUrls.length <= 0)
            return convertView;

        Holder holder = (Holder)convertView.getTag();
        
        String url = mImageUrls[position % mImageUrls.length];

        Picasso.with(mContext).load(url).placeholder(R.drawable.icon_introduct).into(holder.image);

        holder.text.setText(txts[position % txts.length]);

        return convertView;
    }
	
	class Holder {
	    ImageView image;
	    TextView text;
	}

}