package com.shine.hotels.ui.guidebook;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shine.hotels.R;
import com.shine.hotels.controller.CarrentalController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.CarRental;
import com.shine.hotels.io.model.CarRentalList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.guidebook.CarrentalTableAdapter.TableCell;
import com.shine.hotels.ui.guidebook.CarrentalTableAdapter.TableRow;
import com.shine.hotels.util.Utils;

import de.greenrobot.event.EventBus;

/**
 * 客房服务-租车服务
 * @author guoliang
 *
 */
public class FragmentCarrental extends BaseFragment {
	
	CarrentalController mController ;
	
	CarrentalTableAdapter tableAdapter ;
	
	ArrayList<TableRow> table = new ArrayList<TableRow>(); 

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_carrental, container, false) ;
		
		ListView listview = (ListView)v.findViewById(R.id.carrental_listview) ;
		tableAdapter = new CarrentalTableAdapter(getActivity(), table);  
		listview.setAdapter(tableAdapter); 
		
		listview.setDividerHeight(0) ;
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (CarrentalController)ControllerManager.newController(getActivity(),
        		CarrentalController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_CAR)
                .getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<CarRentalList> event) {
        CarRentalList result = event.result;
        
        int height = Utils.dip2px(getActivity(), 33) ;
        
        List<CarRental> list = result.getLists();
        
        table.clear() ;
        
        for (CarRental car : list) {
        	// 每行的数据  
            TableCell[] cells = new TableCell[4]; 
            cells[0] = new TableCell(car.getCar(), Utils.dip2px(getActivity(), 250), height, TableCell.STRING);  
            cells[1] = new TableCell(""+car.getSeatsNum(), Utils.dip2px(getActivity(), 160), height, TableCell.STRING);  
            cells[2] = new TableCell(""+car.getRent(), Utils.dip2px(getActivity(), 170), height, TableCell.STRING);  
            cells[3] = new TableCell(car.getDriver(), Utils.dip2px(getActivity(), 140), height, TableCell.STRING);  
            table.add(new TableRow(cells));
        }
        
        tableAdapter.notifyDataSetChanged();
    }

//	/**
//	 * 获取租车服务数据
//	 * @return
//	 */
//	private ArrayList<TableRow> getDate() {
//        ArrayList<TableRow> table = new ArrayList<TableRow>();  
//        TableCell[] titles = new TableCell[4];
//        int height = 24 ;
//        int height_content = 21 ;
//        // 定义标题  
//        titles[0] = new TableCell("车型", 200, height, TableCell.STRING);  
//        titles[1] = new TableCell("座位数", 140, height, TableCell.STRING);  
//        titles[2] = new TableCell("单价（RMB/小时）", 140, height, TableCell.STRING);  
//        titles[3] = new TableCell("司机", 120, height, TableCell.STRING);  
//        
//        TableRow tr = new TableRow(titles) ; 
//        
//        table.add(tr);  
//        
//
//        // 把表格的行添加到表格  
//        for (int i = 0; i < 6; i++) {
//        	// 每行的数据  
//            TableCell[] cells = new TableCell[4]; 
//            cells[0] = new TableCell("宝马Li"+i, titles[0].width, height_content, TableCell.STRING);  
//            cells[1] = new TableCell(""+i, titles[1].width, height_content, TableCell.STRING);  
//            cells[2] = new TableCell("50"+i, titles[2].width, height_content, TableCell.STRING);  
//            cells[3] = new TableCell("吴先生"+i, titles[3].width, height_content, TableCell.STRING);  
//            table.add(new TableRow(cells));
//        }
//        
//        return table ;
//    }
}