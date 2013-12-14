
package com.shine.hotels.ui;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shine.hotels.R;
import com.shine.hotels.controller.BaseController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.WorldTimeController;
import com.shine.hotels.controller.Events.AddType;
import com.shine.hotels.controller.Events.GetAllWeather;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.TravelController;
import com.shine.hotels.io.model.Weather;
import com.shine.hotels.io.model.WeatherList;
import com.shine.hotels.io.model.WorldTime;
import com.shine.hotels.ui.guidebook.FragmentWeather;

import de.greenrobot.event.EventBus;

/**
 * 
 * 
 */
public class AddWeatherFragment extends BaseFragment implements OnItemSelectedListener {
    private BaseController mController;
    private ListView mListView;
    private AddAdapter mAdapter;
    private int mSelectedPosition;
    private String mAddedCity;
    private Button mAdd;
    private Button mCancel;
    
    public static final String CITY_STATE = "0" ;
    
//    private int mType;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().registerSticky(this, AddType.class);
        EventBus.getDefault().register(this, GetAllWeather.class);
        
        SharedPreferences setting = getActivity().getSharedPreferences(FragmentWeather.PREFS_NAME, 0);
        mAddedCity = setting.getString(FragmentWeather.KEY_WEATHER_CITY, "");
    }

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        
        mController = null;
        super.onDestroy();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().getStickyEvent(AddType.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        
        mListView = (ListView)v.findViewById(R.id.add_list);
        mAdapter = new AddAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setSelection(0);
        mListView.setOnItemSelectedListener(this);

        mAdd = (Button)v.findViewById(R.id.add_yes);
        mAdd.setFocusable(false);
        
        mCancel = (Button)v.findViewById(R.id.cancel);
        mCancel.setFocusable(false);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	if (mSelectedPosition!=position)
    		mSelectedPosition = position;
        mAdapter.notifyDataSetChanged();
        
    }
    
    @Override
    public boolean onKeyDown() {
        mListView.requestFocus();
        return true;
    }

    @Override
    public boolean onKeyUp() {
        mListView.requestFocus();
        return true;
    }
    
    @Override
    public boolean onKeyLeft() {
        if (mCancel.isSelected()) {
            mCancel.setSelected(false);
            mAdd.setSelected(true);
        } else {
            mCancel.setSelected(true);
            mAdd.setSelected(false);
        }
        mListView.requestFocus();
        return true;
    }

    @Override
    public boolean onKeyRight() {
        if (mAdd.isSelected()) {
            mAdd.setSelected(false);
            mCancel.setSelected(true);
        } else {
            mAdd.setSelected(true);
            mCancel.setSelected(false);
        }
        mListView.requestFocus();
        return true;
    }
    
    @Override
    public boolean onKeyCenter() {
        if (mAdd.isSelected() && mAdapter.getCount() > 0) {
            Weather weather = (Weather)mAdapter.getItem(mSelectedPosition);

            if (null != weather) {
                EventBus.getDefault().post(weather);

                mController = ControllerManager.getStickyController(getActivity(),
                        TravelController.class);
                Builder builder = new Builder();
                Request request = builder.obtain(Request.Action.GET_WEATHERS_BACK)
                        .putStringParam("code", weather.getCitycode()).getResult();
                mController.handle(request);

            }

            Toast toast = Toast.makeText(getActivity(), R.string.add_successfully,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 60, 30);
            toast.show();
        } 
        doBack();
        return true;
    }
    
    public void onEvent(AddType type) {
        switch (type.type) {

            case AddType.WEATHER: {
                
                mController = ControllerManager.getStickyController(getActivity(),
                        TravelController.class);
                Builder builder = new Builder();
                Request request = builder.obtain(Request.Action.GET_WEATHERS)
                        .putStringParam("city", CITY_STATE).getResult();
                mController.handle(request);
                break;
            }

            default:
                break;
        }
    }
    
    public void onEvent(GetAllWeather event) {
        WeatherList data = event.data;
        
        List<Weather> list = data.getWeathers();
        
        if (mAdapter != null && list != null &&
                list.size() > 0) {
            mAdapter.updateData(list);
            mAdd.setSelected(true);
            mListView.requestFocus();
        }
    }
    
    private class AddAdapter extends BaseAdapter {
        private List<Weather> mList;
        private LayoutInflater mInflater;
        
        public AddAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
        
        public void updateData(List<Weather> list) {
            mList = list;
            notifyDataSetChanged();
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
                convertView = mInflater.inflate(R.layout.add_weather_item, null);
                
                Holder holder = new Holder();
                holder.background = (ImageView)convertView.findViewById(R.id.add_background);
                holder.left = (TextView)convertView.findViewById(R.id.add_weather_left);
                holder.right = (TextView)convertView.findViewById(R.id.add_weather_right);
                
                convertView.setTag(holder);
            }
            Weather weather = mList.get(position);
            Holder holder = (Holder)convertView.getTag();
            
            if (mSelectedPosition == position) {
            	holder.left.setTextColor(Color.rgb(185, 107, 6));
            	holder.left.setText(weather.getCode());
                holder.left.setVisibility(View.VISIBLE);
                String rightTxt = getActivity().getString(R.string.today) ;
                rightTxt += weather.getTodayhigh()+"℃/" ;
                rightTxt += weather.getTodaylow()+"℃ " ;
                rightTxt += getActivity().getString(R.string.tomorrow) ;
                rightTxt += weather.getTomorrowhigh()+"℃/" ;
                rightTxt += weather.getTomorrowlow()+"℃" ;
                holder.right.setText(rightTxt);
                holder.right.setTextColor(Color.rgb(185, 107, 6)) ;
                holder.background.setImageResource(R.drawable.tanchu_city_down);
                
            } else {
                holder.left.setVisibility(View.INVISIBLE);
                holder.right.setText(weather.getCode());
                holder.right.setTextColor(Color.rgb(160, 134, 121)) ;
                holder.background.setImageResource(R.drawable.tanchu_city_up);
            }
            
            return convertView;
        }
        
        class Holder {
            ImageView background;
            TextView left;
            TextView right;
        }
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        
    }

}
