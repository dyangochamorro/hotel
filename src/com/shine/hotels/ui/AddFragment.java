
package com.shine.hotels.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
import com.shine.hotels.controller.Events.AddType;
import com.shine.hotels.controller.Events.GetWorldTimesRight;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.WorldTimeController;
import com.shine.hotels.io.model.WorldTime;
import com.shine.hotels.io.model.WorldTimeList;
import com.shine.hotels.ui.guidebook.FragmentWorldtime;

import de.greenrobot.event.EventBus;

/**
 * 
 * 
 */
public class AddFragment extends BaseFragment implements OnItemSelectedListener {
    private BaseController mController;
    private ListView mListView;
    private AddTimeAdapter mAdapter;
    private String mAddedCity;
    private int mSelectedPosition;
    private Button add ;
    private Button cancel ;
    
    public static final String CITY_STATE = "0" ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().registerSticky(this, AddType.class);
        EventBus.getDefault().registerSticky(this, GetWorldTimesRight.class);
        
        SharedPreferences setting = getActivity().getSharedPreferences(FragmentWorldtime.PREFS_NAME, 0);
        mAddedCity = setting.getString(FragmentWorldtime.KEY_TIME_CITY, "");
    }

    @Override
    public void onDestroy() {
        mController = null;
        super.onDestroy();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().getStickyEvent(AddType.class);
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        
        mListView = (ListView)v.findViewById(R.id.add_list);
        mAdapter = new AddTimeAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setSelection(0);
        mListView.setOnItemSelectedListener(this);

        add = (Button)v.findViewById(R.id.add_yes);
        add.setFocusable(false);
        
        cancel = (Button)v.findViewById(R.id.cancel);
        cancel.setFocusable(false);
        return v;
    }

    @Override
    public boolean onKeyCenter() {
        if (add.isSelected() && mAdapter.getCount() > 0) {
            WorldTime time = (WorldTime)mAdapter.getItem(mSelectedPosition);

            EventBus.getDefault().post(time);

            mController = ControllerManager.getStickyController(getActivity(),
                    WorldTimeController.class);
            Builder builder = new Builder();
            Request request = builder.obtain(Request.Action.GET_WORLDTIME_BACK)
                    .putStringParam("code", time.getCode()).getResult();
            mController.handle(request);
            
            Toast toast = Toast.makeText(getActivity(), R.string.add_successfully, Toast.LENGTH_SHORT) ;
            toast.setGravity(Gravity.CENTER, 60, 30);
            toast.show();  
        }
        doBack();
        return true;
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        if (cancel.isSelected()) {
            cancel.setSelected(false);
            add.setSelected(true);
        } else {
            cancel.setSelected(true);
            add.setSelected(false);
        }
		
		return true;
	}

	@Override
	public boolean onKeyRight() {
	    if (add.isSelected()) {
	        add.setSelected(false);
	        cancel.setSelected(true);
	    } else {
	        add.setSelected(true);
            cancel.setSelected(false);
	    }
		
		return true;
	}
    
    public void onEvent(AddType type) {
        switch (type.type) {
        
            case AddType.WORLD_TIME: {
//                Log.d("shine", "AddType.WORLD_TIME");
                
                mController = ControllerManager.getStickyController(getActivity(),
                        WorldTimeController.class);
                Builder builder = new Builder();
                Request request = builder.obtain(Request.Action.GET_WORLDTIME)
                        .putStringParam("city", CITY_STATE).getResult();
                mController.handle(request);
                break;
            }

            default:
                break;
        }
    }
    
    public void onEvent(GetWorldTimesRight event) {
        WorldTimeList data = event.data;
//        Log.d("shine", "onEvent GetWorldTimesRight");
        
        List<WorldTime> list = data.getWorldTimes();
        
        if (mAdapter != null && list != null &&
                list.size() > 0) {
            mAdapter.updateData(list);
            mListView.requestFocus();
            add.setSelected(true);
        }
    }
    
    private class AddTimeAdapter extends BaseAdapter {
        private List<WorldTime> mList;
        private LayoutInflater mInflater;
        
        public AddTimeAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
        
        public void updateData(List<WorldTime> list) {
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
                convertView = mInflater.inflate(R.layout.add_time_item, null);
                
                Holder holder = new Holder();
//                holder.background = (ImageView)convertView.findViewById(R.id.add_background);
                holder.left = (TextView)convertView.findViewById(R.id.add_worldtime_left);
                holder.right = (TextView)convertView.findViewById(R.id.add_worldtime_right);
                
                convertView.setTag(holder);
            }
            WorldTime time = mList.get(position);
            
            Holder holder = (Holder)convertView.getTag();
            
            if (mSelectedPosition == position) {
                String z = time.getTimezone();
                int iz = Integer.valueOf(z);
                iz = (iz + 8) % 12;
                // if (iz >= 0) {
                // iz += 8;
                // } else {
                // iz = 8 + iz;
                // }
              String zone = "";
              if (iz >= 0) {
                  zone = "GMT+" + iz;
              } else {
                  zone = "GMT-" + Math.abs(iz);
              }
//              Log.d("clock", "zone=" + zone);
                Calendar cal = Calendar.getInstance(TimeZone  
                        .getTimeZone(zone));  
                int hour = cal.get(Calendar.HOUR_OF_DAY);  
                int minute = cal.get(Calendar.MINUTE);  
                int second = cal.get(Calendar.SECOND);
                Log.d("shine", "hour=" + hour + " min=" + minute + " sec=" + second);
                
                SimpleDateFormat sdf = new SimpleDateFormat("mm");
                holder.left.setText(hour + ":" + sdf.format(cal.getTime()));
                holder.left.setVisibility(View.VISIBLE);
                holder.right.setText(time.getCity());
            } else {
                holder.left.setVisibility(View.INVISIBLE);
                holder.right.setText(time.getCity());
            }
            
            return convertView;
        }
        
        class Holder {
            TextView left;
            TextView right;
        }
        
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
    
}
