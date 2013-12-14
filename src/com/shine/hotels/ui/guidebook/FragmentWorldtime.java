package com.shine.hotels.ui.guidebook;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.BaseController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.AddType;
import com.shine.hotels.controller.Events.GetWorldTimes;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.WorldTimeController;
import com.shine.hotels.io.model.WorldTime;
import com.shine.hotels.ui.AddFragment;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.ui.widget.HotelClock;

import de.greenrobot.event.EventBus;

/**
 * 
 * 旅行指南-世界时间
 */
public class FragmentWorldtime extends BaseFragment {
	private Button mAddBtn;
	public static final String PREFS_NAME = "local_time";
    public static final String KEY_TIME_CITY = "time_city";
    
    public static final String CITY_STATE = "1" ;
    
    private BaseController mController;
    private HorizontalScrollView mScrollView;
    private GridLayout mGridLayout;
    private GridAdapter mAdapter;
    
//    private int mScrollViewOffset;
    private int mCurrentPageIndex = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_worldtime, container, false);
		
		mAdapter = new GridAdapter(getActivity());
		mGridLayout = (GridLayout)v.findViewById(R.id.time_grid);
		mGridLayout.setFocusable(false);
		mScrollView = (HorizontalScrollView)v.findViewById(R.id.worldtime_view);
		mScrollView.setFocusable(false);
		mAddBtn = (Button)v.findViewById(R.id.add_city);
		mAddBtn.setFocusable(false);

//		mScrollViewOffset = getResources().getDimensionPixelSize(R.dimen.time_h_scrollview_width);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		EventBus.getDefault().register(this);
		
        mController = (WorldTimeController)ControllerManager.newController(getActivity(),
                WorldTimeController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_WORLDTIME)
                .putStringParam("city", CITY_STATE).getResult();
        mController.handle(request);
		
	}
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	@Override
	public boolean onKeyLeft() {
//	    if (mCurrentPageIndex > 1) {
//	        mScrollView.pageScroll(View.FOCUS_LEFT);
//            mCurrentPageIndex--;
//            return true;
//	    }
	    mScrollView.pageScroll(View.FOCUS_LEFT);
	    return true;
	}

	@Override
    public boolean onKeyRight() {
//	    final int cnt = mAdapter.getCount();
//	    if (cnt == 0) return false;
//	    
//	    final int pageCnt = ((cnt - 1) / 6) + 1;
//	    if (mCurrentPageIndex < pageCnt) {
//	        mScrollView.pageScroll(View.FOCUS_RIGHT);
//	        mCurrentPageIndex++;
//	        return false;
//	    }
	    mScrollView.pageScroll(View.FOCUS_RIGHT);
        return true;
    }

    public void onEvent(WorldTime time) {
	    SharedPreferences setting = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = setting.edit();
        StringBuffer sb = new StringBuffer(setting.getString(KEY_TIME_CITY, ""));
        sb.append(",");
        sb.append(time.getCode());
        editor.putString(KEY_TIME_CITY, sb.toString());
        editor.commit();
        
        List<WorldTime> list = mAdapter.getList();
        list.add(time);
        mGridLayout.removeAllViews();
        mAdapter.updateContent(mGridLayout, list);
        mGridLayout.requestLayout();
        mScrollView.fullScroll(View.FOCUS_RIGHT);
	}
    
    public void onEvent(GetWorldTimes times) {
        List<WorldTime> list = times.data.getWorldTimes();
        if (null!=list && list.size()>0) {
        	mGridLayout.removeAllViews();
            mAdapter.updateContent(mGridLayout, list);
            int cnt = mGridLayout.getChildCount();
            View view = mGridLayout.getChildAt(cnt - 1);
            view.requestFocus();
        }
    }
	
	private static class GridAdapter extends BaseAdapter {
        private static final int MODE = 3;
        
        private List<WorldTime> mList;

        private Context mContext;

        private LayoutInflater mLayoutInflater;

        public GridAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        
        public void updateContent(GridLayout layout, List<WorldTime> datas) {
            mList = datas;
            
            for (int i = 0; i < mList.size(); i++) {
                layout.addView(getView(i, null, layout));
            }
        }
        
        public List<WorldTime> getList() {
            return mList;
        }
        
        @Override
        public int getCount() {
            if (mList != null) {
                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList != null) {
                return mList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mList != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LinearLayout layout = (LinearLayout)mLayoutInflater.inflate(
                        R.layout.time_item, null);

                GridItemHold hold = new GridItemHold();
                hold.clock = (HotelClock)layout.findViewById(R.id.clock);
                hold.name = (TextView)layout.findViewById(R.id.worldtime_cityname);
                layout.setTag(hold);
                
                convertView = layout;
            }

            WorldTime data = mList.get(position);
            GridItemHold hold = (GridItemHold)convertView.getTag();
            String z = data.getTimezone();
            int iz = Integer.valueOf(z);
            iz = (iz + 8) % 12;
//            if (iz >= 0) {
//                iz += 8;
//            } else {
//                iz = 8 + iz;
//            }
            String zone = "";
            if (iz >= 0) {
                zone = "GMT+" + iz;
            } else {
                zone = "GMT-" + Math.abs(iz);
            }
            Log.d("clock", "zone=" + zone);
            hold.clock.setTimeZone(zone);
            hold.clock.start();
            hold.name.setText(data.getCity());

            return convertView;
        }

    }

    private static class GridItemHold {
        HotelClock clock;

        TextView name;
    }

    @Override
    public boolean onKeyCenter() {
    	Fragment confirm = new AddFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.top_layout, confirm, UIConfig.FRAGMENT_TAG_ADD);
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, 
                R.animator.fragment_slide_right_exit);
        transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ADD);
        transaction.commit();
        
        AddType event = new AddType();
        event.type = AddType.WORLD_TIME;
        EventBus.getDefault().postSticky(event);
        
        mScrollView.clearFocus();
        
        return true ;
    }

}
