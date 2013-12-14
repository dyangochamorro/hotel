package com.shine.hotels.ui.hotelintroduction;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.hotels.HotelsApplication;
import com.shine.hotels.R;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.HotelintroductionShowEvent;
import com.shine.hotels.controller.Events.SubHotelShowEvent;
import com.shine.hotels.controller.HotelintroductionShowController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.HotelintroductionShow;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.FullScreenPlayActivity;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 酒店介绍-内容展示
 * @author guoliang
 *
 */
public class FragmentHotelintroductionShow extends BaseFragment {
	
	HotelintroductionShowController mController ;
	
	HotelintroductionShow showData ;
	
	private TextView mContentTv;
	private TextView mTitleTv;
	private List<String> mPics;
	private String mLogo;
	private String mTvUrl = "";
	private Handler mHandler = new Handler();
	
	Button playTv;
	Button showGallery;
	ImageView show;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
	    View v = inflater.inflate(R.layout.fragment_hotel_intro_show, null);
	    mContentTv = (TextView)v.findViewById(R.id.show_content);
	    mTitleTv = (TextView)v.findViewById(R.id.show_title);
	    show = (ImageView)v.findViewById(R.id.show_gallery);
        
        playTv = (Button)v.findViewById(R.id.show_video_btn);
        playTv.requestFocus();
        playTv.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mTvUrl)) return;
                
                stopMusicPlay();
                
                Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
                intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, mTvUrl);
                intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE, FullScreenPlayActivity.TYPE_PLAY);
                getActivity().startActivity(intent);
            }
        });
        
        showGallery = (Button)v.findViewById(R.id.show_gallery_btn);
        showGallery.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PPTActivity.class);
                intent.putStringArrayListExtra(PPTActivity.INTENT_KEY_PICS, (ArrayList<String>)mPics);
                getActivity().startActivity(intent);
            }
        });

		return v;
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().registerSticky(this, SubHotelShowEvent.class);
        EventBus.getDefault().register(this, HotelintroductionShowEvent.class);
        
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
//	@Override
//	public boolean onKeyLeft() {
//	    if (mContentTv.isFocused() || mContentTv.isSelected()) {
//            playTv.requestFocus();
//            
//            return true;
//        }
//	    return false;
//	}
//	
//	@Override
//    public boolean onKeyRight() {
//	    if (playTv.isFocused() || playTv.isSelected()) {
//	        mContentTv.requestFocus();
//	        
//	        return true;
//	    }
//        return false;
//    }
	
	private void stopMusicPlay() {
        Intent intent = new Intent();
        intent.setAction(BroadcastAction.MUSIC_STOP_PLAYING);
        getActivity().sendBroadcast(intent);
    }
	
	public void onEvent(SubHotelShowEvent event) {
		int id = event.data.getDataId();

        mController = (HotelintroductionShowController)ControllerManager.newController(getActivity(),
        		HotelintroductionShowController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_HOTELINTRODUCTION_SHOW).putStringParam("id", id+"").getResult();
        mController.handle(request);
        
    }
	
    public void onEvent(HotelintroductionShowEvent event) {
        showData = event.result;
        if (showData == null) return;

        // 处理showData返回内容
        mContentTv.setText(showData.getContent());
        mTitleTv.setText(showData.getTitle());
        mPics = showData.getPics();
        if (mPics == null || mPics.size() == 0) {
            showGallery.setClickable(false);
        }
        mLogo = showData.getPic();
        mTvUrl = showData.getTvUrl();
        if (TextUtils.isEmpty(mTvUrl)) {
            playTv.setClickable(false);
        }

        Picasso.with(getActivity()).load(mLogo).placeholder(R.drawable.hotel_detail_default).into(show);
    }
	
}