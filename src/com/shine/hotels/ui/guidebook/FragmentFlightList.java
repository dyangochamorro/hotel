
package com.shine.hotels.ui.guidebook;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.FlightinfoController;
import com.shine.hotels.io.model.Flight;
import com.shine.hotels.ui.BaseFragment;

public class FragmentFlightList extends BaseFragment {
    private ListView mListView;
    private FlightAdapter mAdapter;
    private FlightinfoController mController;
    private int mPosition = -1;
    
    public static FragmentFlightList newInstance(int position) {
        FragmentFlightList fragment = new FragmentFlightList();
        fragment.setController(position);
        
        return fragment;
    }
    
    public void setController(int position) {
        mPosition = position;
        mController = FragmentFlightinfo.getController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (ListView)inflater.inflate(R.layout.fragment_flightinfo_list, container, false);
        
        mAdapter = new FlightAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        
        return mListView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        loadData();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    private void loadData() {
        if (mController != null) {
            List<Flight> data = mController.getFlights(mPosition);
            mAdapter.setData(data);
        }
    }
    
    public static class FlightAdapter extends ArrayAdapter<Flight> {
        private final LayoutInflater mInflater;
        
        public FlightAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        public void setData(List<Flight> data) {
            clear();
            if (data != null) {
                for (Flight appEntry : data) {
                    add(appEntry);
                }
            }
        }

        /**
         * Populate new items in the list.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.fragment_flightinfo_item, parent, false);
                
                Holder holder = new Holder();
                holder.column1 = (TextView)view.findViewById(R.id.flightinfo_item_col_1);
                holder.column2 = (TextView)view.findViewById(R.id.flightinfo_item_col_2);
                holder.column3 = (TextView)view.findViewById(R.id.flightinfo_item_col_3);
                holder.column4 = (TextView)view.findViewById(R.id.flightinfo_item_col_4);
                holder.column5 = (TextView)view.findViewById(R.id.flightinfo_item_col_5);
                holder.column6 = (TextView)view.findViewById(R.id.flightinfo_item_col_6);
                holder.column7 = (TextView)view.findViewById(R.id.flightinfo_item_col_7);
                holder.column8 = (TextView)view.findViewById(R.id.flightinfo_item_col_8);
                holder.column9 = (TextView)view.findViewById(R.id.flightinfo_item_col_9);
                holder.column10 = (TextView)view.findViewById(R.id.flightinfo_item_col_10);
                holder.column11 = (TextView)view.findViewById(R.id.flightinfo_item_col_11);
                
                view.setTag(holder);
                
            } else {
                view = convertView;
            }
            
            Holder holder = (Holder)view.getTag();
            Flight f = getItem(position);
            if (f != null) {
                holder.column1.setText(f.getAirline());
                holder.column2.setText(f.getNumber());
                holder.column3.setText(f.getExpectedDepart());
                holder.column4.setText(f.getActualDepart());
                holder.column5.setText(f.getDeparture());
                holder.column6.setText(f.getDepartureTerminal());
                holder.column7.setText(f.getExpectedArrive());
                holder.column8.setText(f.getActualArrive());
                holder.column9.setText(f.getDestination());
                holder.column10.setText(f.getDestinationTerminal());
                holder.column11.setText(f.getStatus());
            }
            
            return view;
        }

    }
    
    private static class Holder {
        TextView column1;
        TextView column2;
        TextView column3;
        TextView column4;
        TextView column5;
        TextView column6;
        TextView column7;
        TextView column8;
        TextView column9;
        TextView column10;
        TextView column11;
    }

}
