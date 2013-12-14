package com.shine.hotels.ui.tools;

import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.BackgroundMusicEvent;
import com.shine.hotels.controller.Events.GetToolDataEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.ToolbarController;
import com.shine.hotels.io.model.ToolBarData;
import com.shine.hotels.ui.widget.HotelClock;
import com.shine.hotels.util.Utils;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
/**
 * 
 * 左侧边栏，工具fragment
 */
public class FragmentTools extends Fragment {
//	private ImageView cd;
	TextView toolsNowdateTxt;
	TextView toolsWeekTxt;
	TextView toolsCityTxt;
	TextView toolsTemperatureTxt;
	TextView toolsMsgnumTxt;
	TextView toolsSongnameTxt;
	ImageView toolsClearPic ;
	ImageView toolsMsgnumPic ;
	AnimationDrawable cdAnimation;
	ImageView mLogeIv;

	private ToolbarController mController;
	
	private ToolBarData toolbar ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tools, container, false);
        
        //mLeftClock = (HotelClock) v.findViewById(R.id.tools_clock);
        HotelClock clock = (HotelClock) v.findViewById(R.id.tools_clock);
        clock.start();
        
        // 获取日期信息
        toolsNowdateTxt = (TextView)v.findViewById(R.id.tools_now_date_txt) ;
        
        // 获取星期
        toolsWeekTxt = (TextView)v.findViewById(R.id.tools_week_txt) ;
        
        // 获取城市
        toolsCityTxt = (TextView)v.findViewById(R.id.tools_city_txt) ;
        
        // 获取气温
        toolsTemperatureTxt = (TextView)v.findViewById(R.id.tools_temperature_txt) ;
        toolsTemperatureTxt.setTextColor(Color.DKGRAY) ;
        
        // 信息背景
        toolsMsgnumPic = (ImageView)v.findViewById(R.id.tools_msgnum_pic) ;
        
        // 获取信息条数
        toolsMsgnumTxt = (TextView)v.findViewById(R.id.tools_msgnum_txt) ;
        
        // 获取歌曲名称
        toolsSongnameTxt = (TextView)v.findViewById(R.id.tools_songname_txt) ;
        toolsSongnameTxt.setTextColor(Color.DKGRAY) ;
        
        // 获取天气
        toolsClearPic = (ImageView)v.findViewById(R.id.tools_clear_pic) ;
        
        ImageView cd = (ImageView) v.findViewById(R.id.tools_play_music);
        cdAnimation = (AnimationDrawable)cd.getBackground();
        
        mLogeIv = (ImageView)v.findViewById(R.id.tools_logo_pic);
        
        return v ;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

//		mController = (ToolbarController)ControllerManager.newController(getActivity(),
//				ToolbarController.class);
//		Builder builder = new Builder();
//        Request request = builder.obtain(Request.Action.GET_LEFT_TOOLBAR).getResult();
//        mController.handle(request);
	}
	
    @Override
    public void onStart() {
        super.onStart();
        
        EventBus.getDefault().register(this);
        
        if (mController == null) {
            mController = (ToolbarController)ControllerManager.newController(getActivity(),
                    ToolbarController.class);
        }
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_LEFT_TOOLBAR).getResult();
        mController.handle(request);
    }
	
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
	
	@Override
    public void onDestroy() {
//        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }
	
	public void onEvent(GetToolDataEvent event) {
		
		Resources resources = getResources();//获得res资源对象
		Configuration config = resources.getConfiguration();//获得设置对象
		Locale language = config.locale ;
		
		String dateFormate = "yyyy年MM月dd日" ;
		if (language == Locale.US) {
			dateFormate = "MM-dd-yyyy" ;
		}
		
		String toolsNowdateTxtValue = Utils.getDate(dateFormate) ;
        toolsNowdateTxt.setText(toolsNowdateTxtValue) ;
        
        String toolsWeekTxtValue ;
        if (language == Locale.US) {
			toolsWeekTxtValue = Utils.getWeek(1) ;
		} else {
			toolsWeekTxtValue = Utils.getWeek(0) ;
		}
        toolsWeekTxt.setText(toolsWeekTxtValue) ;
        
        toolbar = event.data;
        
        toolsCityTxt.setText(toolbar.getCity()) ;
        
        toolsTemperatureTxt.setText(toolbar.getTemperature()) ;
        
        if (toolbar.getMsgNum() > 0) {
            toolsMsgnumPic.setVisibility(ImageView.VISIBLE);
            toolsMsgnumTxt.setVisibility(TextView.VISIBLE);
            toolsMsgnumTxt.setText(toolbar.getMsgNum() + "");
        } else {
            toolsMsgnumPic.setVisibility(ImageView.INVISIBLE);
            toolsMsgnumTxt.setVisibility(TextView.INVISIBLE);
        }
        
        Bitmap bm = BitmapFactory.decodeFile(toolbar.getWeather()); 
        if (null!=bm)
        	toolsClearPic.setImageBitmap(bm); 
        
        
        Picasso.with(getActivity()).load(toolbar.getLogo()).into(mLogeIv);
	}
	
	public void onEvent(BackgroundMusicEvent event) {
	    if (event.isPlaying) {
	        cdAnimation.start();
	        toolsSongnameTxt.setText(event.data.getMusicname());
	    } else {
	        cdAnimation.stop();
	        toolsSongnameTxt.setText("");
	    }
	}
	
//	private void initInfos() {
//		String toolsNowdateTxtValue = Utils.getDate("yyyy年MM月dd日") ;
//        toolsNowdateTxt.setText(toolsNowdateTxtValue) ;
//        
//        String toolsWeekTxtValue = Utils.getWeek() ;
//        toolsWeekTxt.setText(toolsWeekTxtValue) ;
//        
//        toolsCityTxt.setText("北京") ;
//        
//        toolsTemperatureTxt.setText("-1℃-3℃") ;
//        
//        toolsMsgnumTxt.setText("3") ;
//        
//        toolsSongnameTxt.setText("Bedroom Lounge Sa") ;
//	}

}
