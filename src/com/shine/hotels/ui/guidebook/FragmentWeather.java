
package com.shine.hotels.ui.guidebook;

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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.AddType;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.TravelController;
import com.shine.hotels.io.model.Weather;
import com.shine.hotels.io.model.WeatherList;
import com.shine.hotels.ui.AddWeatherFragment;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.UIConfig;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 
 * 旅行指南-天气预报
 */
public class FragmentWeather extends BaseFragment {
//    private Button mLeft;
//
//    private Button mRight;

    private Button mAddMore;
    private HorizontalScrollView mScrollView;
    private LinearLayout mLayout;
    private WeatherAdapter mAdapter;
    private int mCurrentPageIndex = 1;

    private TravelController mController;
    public static final String PREFS_NAME = "local_weather";
    public static final String KEY_WEATHER_CITY = "weather_city";
    public static final String CITY_STATE = "1" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        mAddMore = (Button)v.findViewById(R.id.weather_add_more);
        mAddMore.setFocusable(false);
        mLayout = (LinearLayout)v.findViewById(R.id.weather_layout);
        mLayout.setFocusable(false);
        mScrollView = (HorizontalScrollView)v.findViewById(R.id.weather_view);
        mScrollView.setFocusable(false);
        
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        EventBus.getDefault().register(this);

        mAdapter = new WeatherAdapter(getActivity());
        
        mController = (TravelController)ControllerManager.newController(getActivity(),
                TravelController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_WEATHERS)
                .putStringParam("city", CITY_STATE).getResult();
        mController.handle(request);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null)
            mAdapter.destroy();

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mController = null;

        super.onDetach();
    }
    
    @Override
    public void onBackToFont() {
        mAddMore.setSelected(true);
    }
    
    @Override
    public boolean onKeyLeft() {
//        if (mCurrentPageIndex > 1) {
//            mScrollView.pageScroll(View.FOCUS_LEFT);
//            mCurrentPageIndex--;
//        }
        
        mScrollView.pageScroll(View.FOCUS_LEFT);
        return true;
    }

    @Override
    public boolean onKeyRight() {
//        final int cnt = mAdapter.getCount();
//        Log.d("key", "onKeyRight cnt=" + cnt + " page index=" + mCurrentPageIndex);
//        if (cnt == 0) return false;
        
//        final int pageCnt = ((cnt - 1) / 5) + 1;
//        if (mCurrentPageIndex < pageCnt) {
//            if (mScrollView.pageScroll(View.FOCUS_RIGHT))
//                mCurrentPageIndex++;
//        }
        mScrollView.pageScroll(View.FOCUS_RIGHT);
        return true;
    }
    
    @Override
    public boolean onKeyCenter() {
    	Fragment confirm = new AddWeatherFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.top_layout, confirm, UIConfig.FRAGMENT_TAG_ADD_WEATHER);
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, 
                R.animator.fragment_slide_right_exit);
        transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ADD_WEATHER);
        transaction.commit();
        
        AddType event = new AddType();
        event.type = AddType.WEATHER;
        EventBus.getDefault().postSticky(event);
        
//        mCityContainer.clearFocus();
    	
    	return true ;
    }
    
    public void onEvent(GetResultEvent<WeatherList> event) {
        List<Weather> list = event.result.getWeathers(); 
        
        if (null != list && list.size() > 0) {
            mLayout.removeAllViews();
            mAdapter.updateContent(mLayout, list);
//            int cnt = mLayout.getChildCount();
//            View view = mLayout.getChildAt(cnt - 1);
//            view.requestFocus();
        }
        
    }
    
    public void onEvent(Weather weather) {
        // add local prefs
        SharedPreferences setting = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = setting.edit();
        StringBuffer sb = new StringBuffer(setting.getString(KEY_WEATHER_CITY, ""));
        sb.append(",");
        sb.append(weather.getCode());
        editor.putString(KEY_WEATHER_CITY, sb.toString());
        editor.commit();
        
        List<Weather> list = mAdapter.getList();
        list.add(weather);
        mLayout.removeAllViews();
        mAdapter.updateContent(mLayout, list);
        
        mScrollView.fullScroll(View.FOCUS_RIGHT);
    }

    private static class WeatherAdapter extends BaseAdapter {
        private Context mContext;

        private List<Weather> mWeather;

        public WeatherAdapter(Context context) {
            mContext = context;
        }

        public void destroy() {
            mContext = null;
            mWeather = null;
        }
        
        public List<Weather> getList() {
            return mWeather;
        }
        
        public void updateContent(LinearLayout layout, List<Weather> datas) {
            mWeather = datas;
            
            for (int i = 0; i < mWeather.size(); i++) {
                layout.addView(getView(i, null, layout));
            }
        }

        @Override
        public int getCount() {
            if (mWeather != null) {
                return mWeather.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mWeather != null) {
                return mWeather.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mWeather != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_item,
                        null);
                Holder holder = new Holder();
                holder.city = (TextView)convertView.findViewById(R.id.weather_item_city);
                // holder.today =
                // (TextView)convertView.findViewById(R.id.weather_item_today);
                holder.todayTemp = (TextView)convertView
                        .findViewById(R.id.weather_item_today_temperature);
                holder.tomrrowTemp = (TextView)convertView
                        .findViewById(R.id.weather_item_tomorrow_temperature);
                holder.thirdTemp = (TextView)convertView
                        .findViewById(R.id.weather_item_after_tomorrow_temperature);
                holder.weather = (ImageView)convertView.findViewById(R.id.weather_item_image);

                convertView.setTag(holder);
            }

            Weather weather = (Weather)getItem(position);

            updateView(convertView, weather);

            return convertView;
        }

        private void updateView(View convertView, Weather weather) {
            Holder holder = (Holder)convertView.getTag();
            if (holder != null) {
                holder.city.setText(weather.getCode());
                holder.todayTemp.setText(weather.getTodaylow() + "ºC ~" + weather.getTodayhigh() + "ºC");
                holder.tomrrowTemp.setText(weather.getTomorrowlow() + "ºC ~" + weather.getTomorrowhigh() + "ºC");
                holder.thirdTemp.setText(weather.getAftertomorrowlow() + "ºC ~" + weather.getAftertomorrowhith() + "ºC");
                Picasso.with(mContext).load(weather.getTodaycode()).placeholder(R.drawable.weather_sun).into(holder.weather);
            }
        }

        private class Holder {
            TextView city;

            // TextView today;

            ImageView weather;

            TextView todayTemp;

            TextView tomrrowTemp;

            TextView thirdTemp;
        }

//        void updateWeather(List<Weather> weather) {
//            mWeather = weather;
//
//            notifyDataSetChanged();
//        }

    }
    
}
