package com.shine.hotels.ui.appreciatemovie;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.shine.hotels.controller.AppreciatemovieShowController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.AppreciatemovieIdEvent;
import com.shine.hotels.controller.Events.AppreciatemovieShowEvent;
import com.shine.hotels.controller.Events.PayBackEvent;
import com.shine.hotels.controller.Events.VODPayEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.AppreciatemovieShow;
import com.shine.hotels.io.model.PlayintromsgData;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.FullScreenPlayActivity;
import com.shine.hotels.ui.HostActivity;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.ui.VODPayFragment;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 电影欣赏-节目介绍
 * @author guoliang
 *
 */
public class FragmentAppreciatemovieShow extends BaseFragment implements OnClickListener {
	
	AppreciatemovieShowController mController ;
	
	AppreciatemovieShow showData ;
	
	int mMovieId ;
	
	private TextView mMovieName;
	private TextView mDirector;
	private TextView mCast;
	private TextView mDuring;
	private TextView mIntroduce;
	private ImageView mPoster;
	private Button mPlay;
	private Button mPreview;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.fragment_movie_show, container, false);
	    mMovieName = (TextView)v.findViewById(R.id.show_name);
	    mDirector = (TextView)v.findViewById(R.id.show_director);
	    mCast = (TextView)v.findViewById(R.id.show_cast);
	    mDuring = (TextView)v.findViewById(R.id.show_during);
	    mIntroduce = (TextView)v.findViewById(R.id.show_intro);
	    mPoster = (ImageView)v.findViewById(R.id.show_image);
	    mPlay = (Button)v.findViewById(R.id.show_play);
	    mPlay.setOnClickListener(this);
	    mPlay.requestFocus();
	    mPreview = (Button)v.findViewById(R.id.show_preview);
	    mPreview.setOnClickListener(this);
		return v;
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        EventBus.getDefault().registerSticky(this, AppreciatemovieIdEvent.class);
//        EventBus.getDefault().register(this, AppreciatemovieShowEvent.class);
    }
	
	@Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this, AppreciatemovieIdEvent.class);
        EventBus.getDefault().register(this, AppreciatemovieShowEvent.class);
        
        EventBus.getDefault().getStickyEvent(AppreciatemovieIdEvent.class);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	@Override
    public void onDestroy() {
        super.onDestroy();
    }
	
	@Override
    public void onClick(View v) {
	    if (showData == null) return;
	    
	    stopMusicPlay();

	    if (v == mPlay) {
            List<PlayintromsgData> list = showData.getData();
            if (list == null || list.size() == 0) {
                Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
                intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, showData.getMovieplayurl().get(0));
                intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE, FullScreenPlayActivity.TYPE_PLAY);
                getActivity().startActivity(intent);
            } else {
                Fragment confirm = new VODPayFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.top_layout, confirm, UIConfig.FRAGMENT_TAG_PAY);
                transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, 
                        R.animator.fragment_slide_right_exit);
                transaction.addToBackStack(UIConfig.FRAGMENT_TAG_PAY);
                transaction.commit();
                
                VODPayEvent event = new VODPayEvent();
                event.result = showData;
                event.id = mMovieId;
                event.index = 0;
                EventBus.getDefault().postSticky(event);
            }
            
        } else if (v == mPreview) {
            Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, showData.getMovieplayurl().get(0));
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE, FullScreenPlayActivity.TYPE_PREVIEW);
            getActivity().startActivity(intent);
        }
    }
	
	boolean mIsFrist = true;
	@Override
	public void onBackToFont() {
	    super.onBackToFont();
	    
	    mPlay.requestFocus();
	    
	    if (!mIsFrist && mController != null) {
	        Builder builder = new Builder();
	        Request request = builder.obtain(Request.Action.GET_APPRECIATE_MOVIE_SHOW).putStringParam("id", mMovieId+"").getResult();
	        mController.handle(request);
	    }
	    mIsFrist = false;
	}
	
	public void onEvent(AppreciatemovieIdEvent event) {
		mMovieId = event.ids[0] ;
		Log.d("shine", "show event id=" + mMovieId);

        mController = (AppreciatemovieShowController)ControllerManager.getStickyController(getActivity(),
        		AppreciatemovieShowController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_APPRECIATE_MOVIE_SHOW).putStringParam("id", mMovieId+"").getResult();
        mController.handle(request);
        
    }
	
	public void onEvent(AppreciatemovieShowEvent event) {
		showData = event.result ;
		
		List<PlayintromsgData> list = showData.getData();
		if (list != null)
		for (PlayintromsgData data : list) {
		    Log.d("pay", "data id:" + data.getId() + " type:" + data.getType() + " title:" + data.getIntrotitle());
		    
		}
		
		// 处理showData返回内容
		updateContent();
	}
	
	public void onEvent(PayBackEvent event) {
	    showData.setData(null);
	}
	
	private void stopMusicPlay() {
	    Intent intent = new Intent();
        intent.setAction(BroadcastAction.MUSIC_STOP_PLAYING);
        getActivity().sendBroadcast(intent);
	}
	
	private void updateContent() {
	    mMovieName.setText(showData.getMoviename());
	    mDirector.setText(" " + showData.getDirector());
	    mDuring.setText(" " + showData.getRunningtime());
	    mCast.setText(" " + showData.getProtagonist());
	    mIntroduce.setText(" " + showData.getIntro());
	    Picasso.with(getActivity()).load(showData.getMovieposter()).placeholder(R.drawable.movie_item_default).into(mPoster);
	}

}