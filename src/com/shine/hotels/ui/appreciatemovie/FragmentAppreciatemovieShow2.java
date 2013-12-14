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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.ui.VODPayFragment;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 电影欣赏-节目介绍
 * @author guoliang
 *
 */
public class FragmentAppreciatemovieShow2 extends BaseFragment implements OnItemSelectedListener, OnClickListener {
	
	AppreciatemovieShowController mController ;
	
	AppreciatemovieShow showData ;
	
	int[] ids ;
	
	private TextView mMovieName;
	private ImageView mPoster;
	private Button mPreview;
	private GridView mGridView;
	private LayoutInflater mInflater;
	
	private int mSelectedPos;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    mInflater = inflater;
	    
	    View v = inflater.inflate(R.layout.fragment_movie_show_2, container, false);
	    mGridView = (GridView)v.findViewById(R.id.show_grid);
	    mGridView.setOnItemSelectedListener(this);
	    mGridView.setAdapter(new GridAdapter());
	    mGridView.setFocusable(true);
//	    mGridView.requestFocus();
	    
	    mMovieName = (TextView)v.findViewById(R.id.show_name);
	    mPoster = (ImageView)v.findViewById(R.id.show_poster);
		return v;
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().registerSticky(this, AppreciatemovieIdEvent.class);
        EventBus.getDefault().register(this, AppreciatemovieShowEvent.class);
    }
	
	@Override
    public void onStart() {
        super.onStart();
        
        EventBus.getDefault().getStickyEvent(AppreciatemovieIdEvent.class);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	boolean mIsFrist = true;
	@Override
	public void onBackToFont() {
	    super.onBackToFont();
	    
        mGridView.requestFocus();
        
        if (!mIsFrist && mController != null) {
            Builder builder = new Builder();
            Request request = builder.obtain(Request.Action.GET_APPRECIATE_MOVIE_SHOW).putStringParam("id", ids[0]+"").getResult();
            mController.handle(request);
        }
        mIsFrist = false;
	}
	
	public void onEvent(AppreciatemovieIdEvent event) {
		ids = event.ids;
		//Log.d("shine", "show event id=" + id);

        mController = (AppreciatemovieShowController)ControllerManager.getStickyController(getActivity(),
        		AppreciatemovieShowController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_APPRECIATE_MOVIE_SHOW).putStringParam("id", ids[0]+"").getResult();
        mController.handle(request);
        
    }
	
	public void onEvent(AppreciatemovieShowEvent event) {
		showData = event.result ;
		
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
	    Picasso.with(getActivity()).load(showData.getMovieposter()).placeholder(R.drawable.poster).into(mPoster);
	}
	
	private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (ids != null) return ids.length;
            
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (ids != null) return ids[position];
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (ids != null) return position;
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Button btn = (Button) mInflater.inflate(R.layout.movie_show_grid_item, null);
                
                convertView = btn;
            }
            
            String num = String.format(getString(R.string.tv_num_txt), (position + 1));
            ((Button) convertView).setText(num);
            convertView.setTag(getItem(position));
            
            return convertView;
        }
	    
	}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("shine", "onItemSelected pos=" + position);
        mSelectedPos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean onKeyCenter() {
        stopMusicPlay();

        List<PlayintromsgData> list = showData.getData();
        if (list == null || list.size() == 0) {
            Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
            List<String> urls = showData.getMovieplayurl();
            if (urls != null && urls.size() > 0) {
                String url = showData.getMovieplayurl().get(mSelectedPos);
                intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, url);
                intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE, FullScreenPlayActivity.TYPE_PLAY);
                getActivity().startActivity(intent);
            }
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
            if (mSelectedPos < ids.length) {
                event.id = ids[0];
                event.index = mSelectedPos;
            }
            EventBus.getDefault().postSticky(event);
        }

//        String url = showData.getMovieplayurl().get(mSelectedPos);
//        Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
//        intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, url);
//        getActivity().startActivity(intent);

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == mPreview) {
            Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, showData.getMovieplayurl().get(0));
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE, FullScreenPlayActivity.TYPE_PREVIEW);
            getActivity().startActivity(intent);
        }
    }

}