
package com.shine.hotels.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.shine.hotels.HotelsApplication;
import com.shine.hotels.R;
import com.shine.hotels.controller.AppreciatemovieShowController;
import com.shine.hotels.controller.BaseController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.PayBackEvent;
import com.shine.hotels.controller.Events.VODPayEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.AppreciatemovieShow;
import com.shine.hotels.io.model.PlayintromsgData;

import de.greenrobot.event.EventBus;

/**
 * 
 * 
 */
public class VODPayFragment extends BaseFragment implements OnClickListener, OnItemClickListener {
    private BaseController mController;
    private ListView mListView;
    private PayAdapter mAdapter;
    private AppreciatemovieShow mShowData;
    private int mMovieId;
    private int mIndex = 0;
    
//    Button add;
    Button cancel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().registerSticky(this, VODPayEvent.class);
        //EventBus.getDefault().register(this, GetAllWeather.class);
        
        mController = ControllerManager.getStickyController(getActivity(),
                AppreciatemovieShowController.class);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pay, container, false);
        
        mListView = (ListView)v.findViewById(R.id.pay_list);
        mAdapter = new PayAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setSelection(0);
        mListView.setOnItemClickListener(this);

//        add = (Button)v.findViewById(R.id.pay_yes);
//        add.setOnClickListener(this);
        
        cancel = (Button)v.findViewById(R.id.cancel);
        cancel.requestFocus();
        cancel.setOnClickListener(this);
        
//        EventBus.getDefault().getStickyEvent(VODPayEvent.class);
        mAdapter.updateData(mShowData.getData());
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_yes: {
                int position = mListView.getSelectedItemPosition();
                Log.d("shine", "pos=" + position);
                
                PlayintromsgData data = (PlayintromsgData)mAdapter.getItem(position);
                int type = data.getType();
                sendPayBack(type, mMovieId);
                
                doBack();
                break;
            }

            case R.id.cancel: {
                doBack();
                break;
            }

            default:
                break;
        }

    }
    
//    @Override
//    public boolean onKeyLeft() {
//        cancel.requestFocus();
//        
//        return true;
//    }
//
//    @Override
//    public boolean onKeyRight() {
//        cancel.requestFocus();
//        
//        return true;
//    }
    
    public void onEvent(VODPayEvent event) {
        mShowData = event.result;
        mMovieId = event.id;
        mIndex = event.index;
//        if(mAdapter == null) {
//            mAdapter = new PayAdapter(getActivity());
//        }
//        mAdapter.updateData(mShowData.getData());
    }
    
    private class PayAdapter extends BaseAdapter {
        private List<PlayintromsgData> mList;
        private LayoutInflater mInflater;
        
        public PayAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
        
        public void updateData(List<PlayintromsgData> list) {
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
                convertView = mInflater.inflate(R.layout.pay_item, null);
                
                Holder holder = new Holder();
                holder.title = (TextView)convertView.findViewById(R.id.pay_title);
                holder.detail = (TextView)convertView.findViewById(R.id.pay_detail);
                
                convertView.setTag(holder);
            }
            PlayintromsgData pay = mList.get(position);
            
            if (pay != null) {
                Holder holder = (Holder)convertView.getTag();
                holder.title.setText(pay.getIntrotitle());
                holder.detail.setText(pay.getIntrocontent());
                
            }
            //((TextView)convertView).setText(weather.getCity());
            
            return convertView;
        }
        
        class Holder {
            TextView title;
            TextView detail;
        }
        
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlayintromsgData data = (PlayintromsgData)mAdapter.getItem(position);
        int type = data.getType();
        sendPayBack(type, mMovieId);
        
        Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
        List<String> urls = mShowData.getMovieplayurl();
        if (urls != null && mIndex < urls.size()) {
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, urls.get(mIndex));
        }
        intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE, FullScreenPlayActivity.TYPE_PLAY);
        getActivity().startActivity(intent);
        
        doBack();
    }
    
    private void sendPayBack(int type, int id) {
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_PAY_BACK)
                .putStringParam("type", String.valueOf(type))
                .putStringParam("id", String.valueOf(id)).getResult();
        mController.handle(request);
        
        PayBackEvent payback = new PayBackEvent();
        payback.id = id;
        EventBus.getDefault().post(payback);
    }
    
}
