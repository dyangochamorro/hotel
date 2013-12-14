package com.shine.hotels.ui.guidebook;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.TraininfomationController;
import com.shine.hotels.io.model.Train;
import com.shine.hotels.io.model.TrainList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.guidebook.TraininformationTableAdapter.TableCell;
import com.shine.hotels.ui.guidebook.TraininformationTableAdapter.TableRow;
import com.shine.hotels.util.Utils;

import de.greenrobot.event.EventBus;

/**
 * 旅行指南-列车信息
 * @author guoliang
 *
 */
public class FragmentTraininformation extends BaseFragment {
	
	TraininfomationController mController ;
	
	ArrayList<TableRow> table = new ArrayList<TableRow>(); 
	
	TraininformationTableAdapter tableAdapter ;
    
    ListView listview ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_traininformation, container, false) ;
		
		listview = (ListView)v.findViewById(R.id.traininformation_listview) ;
        
		tableAdapter = new TraininformationTableAdapter(getActivity(), table);  
		listview.setAdapter(tableAdapter); 
		
		listview.setDividerHeight(0) ;
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (TraininfomationController)ControllerManager.newController(getActivity(),
        		TraininfomationController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_SCHEDULE)
                .getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	@Override
    public void onDestroy() {
        if (null!=table)
        	table.clear() ;
        
        super.onDestroy();
    }
	
	public void onEvent(GetResultEvent<TrainList> event) {
		TrainList result = event.result;
        
        List<Train> list = result.getTrains() ;
        
        int height = Utils.dip2px(getActivity(), 36) ;
        
        table.clear() ;
        
        for (Train train : list) {
        	// 每行的数据  
            TableCell[] cells = new TableCell[10]; 
            cells[0] = new TableCell(train.getNumber(), Utils.dip2px(getActivity(), 64), height, TableCell.STRING);  
            cells[1] = new TableCell(train.getType(), Utils.dip2px(getActivity(), 66), height, TableCell.STRING);  
            cells[2] = new TableCell(train.getOriginating(), Utils.dip2px(getActivity(), 72), height, TableCell.STRING);  
            cells[3] = new TableCell(train.getDeparture(), Utils.dip2px(getActivity(), 62), height, TableCell.STRING);  
            cells[4] = new TableCell(train.getDepartureTime(), Utils.dip2px(getActivity(), 70), height, TableCell.STRING);  
            cells[5] = new TableCell(train.getTerminal(), Utils.dip2px(getActivity(), 78), height, TableCell.STRING);  
            cells[6] = new TableCell(train.getTerminalTime(), Utils.dip2px(getActivity(), 78), height, TableCell.STRING);  
            cells[7] = new TableCell(train.getPeriod(), Utils.dip2px(getActivity(), 76), height, TableCell.STRING);  
            cells[8] = new TableCell(train.getLast(), Utils.dip2px(getActivity(), 80), height, TableCell.STRING);  
            cells[9] = new TableCell(train.getMileage(), Utils.dip2px(getActivity(), 82), height, TableCell.STRING);  
            
            table.add(new TableRow(cells));
        }
        
        tableAdapter.notifyDataSetChanged();
    }

}