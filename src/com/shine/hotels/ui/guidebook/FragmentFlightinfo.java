
package com.shine.hotels.ui.guidebook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.FlightinfoController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.FlightList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.guidebook.FragmentFlightList.FlightAdapter;
import com.shine.hotels.ui.widget.TabPageIndicator;

import de.greenrobot.event.EventBus;

/**
 * 旅行指南-航班信息
 */
public class FragmentFlightinfo extends BaseFragment {
    TabPageIndicator mPageIndicator;

    ViewPager mViewPager;

//    FlightInfoAdapter mPagerAdapter;
    RecommendAdapter mAdapter;
    LayoutInflater mInflater;
    
    private int[] mListSelected;
//    private ListView mCurrentList;

    private static FlightinfoController sController;
    private int mCurrentViewPagerItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flightinfo_layout, container, false);
        mInflater = inflater;

        mPageIndicator = (TabPageIndicator)v.findViewById(R.id.titles);
        mViewPager = (ViewPager)v.findViewById(R.id.pager);
//        mPagerAdapter = new FlightInfoAdapter(getFragmentManager());
        mAdapter = new RecommendAdapter();
        mViewPager.setAdapter(mAdapter);

        mPageIndicator.setViewPager(mViewPager);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        sController = (FlightinfoController)ControllerManager.newController(getActivity(),
                FlightinfoController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_FLIGHTINFO).getResult();
        sController.handle(request);
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
    
    @Override
    public void onBackToFont() {
        super.onBackToFont();
        
        int item = mViewPager.getCurrentItem();
        ListView list = (ListView)mViewPager.getChildAt(item);
        if (list != null) list.requestFocus();
    }

    @Override
    public boolean onKeyDown() {
        int item = mViewPager.getCurrentItem();
        ListView list = (ListView)mViewPager.getChildAt(item);
        if (list != null) list.requestFocus();
        return true;
    }

    @Override
    public boolean onKeyUp() {
        int item = mViewPager.getCurrentItem();
        ListView list = (ListView)mViewPager.getChildAt(item);
        if (list != null) list.requestFocus();
        return true;
    }

    @Override
    public boolean onKeyLeft() {
        if (mViewPager != null) {
//            int next = mViewPager.getCurrentItem();
            int next = 0;
            if (mCurrentViewPagerItem != 0) {
                next = mCurrentViewPagerItem - 1;
            }
            mViewPager.setCurrentItem(next);
            mCurrentViewPagerItem = next;
        }
        
        return true;
    }

    @Override
    public boolean onKeyRight() {
        if (mViewPager != null) {
            int next = mCurrentViewPagerItem;
            if (mCurrentViewPagerItem != mAdapter.getCount() - 1) {
                next = mCurrentViewPagerItem + 1;
            } 
            // next = (next == 0 ? 1 : next);
            mViewPager.setCurrentItem(next);
            mCurrentViewPagerItem = next;
        }

        return true;
    }
    
    public static FlightinfoController getController() {
        return sController;
    }

    public void onEvent(GetResultEvent<FlightList[]> event) {
        mAdapter.updateFlightInfo(event.result);
        mPageIndicator.notifyDataSetChanged();
    }
    
    private class RecommendAdapter extends PagerAdapter {
        FlightList[] mFlightInfos;
        
        public void updateFlightInfo(FlightList[] infos) {
            if (infos == null)
                return;

            mFlightInfos = infos;
            notifyDataSetChanged();
            
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mFlightInfos == null) return null;

            FlightList list = mFlightInfos[position];

//            Log.i("shine", "instantiateItem pos=" + position);
            ListView listView = (ListView)mInflater.inflate(R.layout.fragment_flightinfo_list, container, false);
            FlightAdapter adapter = new FlightAdapter(getActivity());
            listView.setAdapter(adapter);
            adapter.setData(list.getFlights());
            listView.requestFocus();
            
            container.addView(listView);

            return listView;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            if (mFlightInfos != null) return mFlightInfos.length;

            return 0;
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
            if (mFlightInfos != null) {
                return mFlightInfos[position].getCategory();
            }
            return "";
        }

    }

}
