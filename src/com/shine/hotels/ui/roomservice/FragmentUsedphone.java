package com.shine.hotels.ui.roomservice;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.shine.hotels.controller.UsefulTelController;
import com.shine.hotels.io.model.UsefulTel;
import com.shine.hotels.io.model.UsefulTelList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.roomservice.UsedphoneTableAdapter.TableCell;
import com.shine.hotels.ui.roomservice.UsedphoneTableAdapter.TableRow;
import com.shine.hotels.util.PageUtils;
import com.shine.hotels.util.Utils;

import de.greenrobot.event.EventBus;

/**
 * 客房服务-常用电话
 * @author guoliang
 *
 */
public class FragmentUsedphone extends BaseFragment {
	
    private UsefulTelController mController;
    
    UsedphoneTableAdapter mNormalAdapter;
    UsedphoneTableAdapter mHotelAdapter;

	ArrayList<TableRow> normal = new ArrayList<TableRow>(); 
	ArrayList<TableRow> hotel = new ArrayList<TableRow>(); 
	
	ListView commonListview ;
	ListView hotelListview ;
	
	private int pageSize = 10 ; // 每页显示的条数
    
	private int noramlRecordCount ; // 总共的条数
	private int hotelRecordCount ;
	    
	private int normalCurrentPage = 1 ; // 当前页面
	private int hotelCurrentPage = 1 ;
    
    private PageUtils normalPageUtils ;
    private PageUtils hotelPageUtils ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_usedphone, container, false) ;
		

		commonListview = (ListView)v.findViewById(R.id.usedphone_common_listview) ;
		mNormalAdapter = new UsedphoneTableAdapter(getActivity(), normal);  
		commonListview.setAdapter(mNormalAdapter);  
		commonListview.setSelector(new ColorDrawable(Color.TRANSPARENT)) ;
		commonListview.setDividerHeight(0) ;

		hotelListview = (ListView)v.findViewById(R.id.usedphone_hotel_listview) ;
		mHotelAdapter = new UsedphoneTableAdapter(getActivity(), hotel);  
		hotelListview.setAdapter(mHotelAdapter);  
		hotelListview.setSelector(new ColorDrawable(Color.TRANSPARENT)) ;
		hotelListview.setDividerHeight(0) ;
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (UsefulTelController)ControllerManager.newController(getActivity(),
                UsefulTelController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_PHONE_NO)
                .getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<UsefulTelList> event) {
        UsefulTelList result = event.result;
        
        List<UsefulTel> list = result.getLists();
        
    	int width = Utils.dip2px(getActivity(), 160) ;
        int height = Utils.dip2px(getActivity(), 40) ;
        
        normal.clear() ;
        hotel.clear() ;
        
        for (UsefulTel tel : list) {
            if (tel.getType() == UsefulTel.TYPE_NORMAL) {
                TableCell[] cells = new TableCell[2];
                cells[0] = new TableCell(tel.getOwner(), width, height, TableCell.STRING);  
                cells[1] = new TableCell(tel.getNumber(), width, height, TableCell.STRING); 
                normal.add(new TableRow(cells));
                
                noramlRecordCount = noramlRecordCount + 1 ;
                
            } else if (tel.getType() == UsefulTel.TYPE_HOTEL) {
                TableCell[] cells = new TableCell[2];
                cells[0] = new TableCell(tel.getOwner(), width, height, TableCell.STRING);  
                cells[1] = new TableCell(tel.getNumber(), width, height, TableCell.STRING); 
                hotel.add(new TableRow(cells));
                
                hotelRecordCount = hotelRecordCount + 1 ;
            }
        }
        
        normalPageUtils = new PageUtils(pageSize, noramlRecordCount, normalCurrentPage) ;
        hotelPageUtils = new PageUtils(pageSize, hotelRecordCount, hotelCurrentPage) ;
        
        
        mHotelAdapter.notifyDataSetChanged();
        mNormalAdapter.notifyDataSetChanged();
    }

	@Override
	public boolean onKeyDown() {
		if (normalPageUtils.getPageCount()!=normalPageUtils.getCurrentPage()) {
			normalCurrentPage = normalCurrentPage + 1 ;
			normalPageUtils.setCurrentPage(normalCurrentPage) ;
			commonListview.setSelection(normalPageUtils.getFromIndex()) ;
	        
			mNormalAdapter.notifyDataSetChanged();  
		}
		if (hotelPageUtils.getPageCount()!=hotelPageUtils.getCurrentPage()) {
			hotelCurrentPage = hotelCurrentPage + 1 ;
			hotelPageUtils.setCurrentPage(hotelCurrentPage) ;
			hotelListview.setSelection(hotelPageUtils.getFromIndex()) ;
			
			mHotelAdapter.notifyDataSetChanged() ;
		}
		
		return true;
	}

	@Override
	public boolean onKeyUp() {
		if (1!=normalPageUtils.getCurrentPage()) {
			normalCurrentPage = normalCurrentPage - 1 ;
			normalPageUtils.setCurrentPage(normalCurrentPage) ;
			commonListview.setSelection(normalPageUtils.getFromIndex()) ;
	        
			mNormalAdapter.notifyDataSetChanged();  
		}
		if (1!=hotelPageUtils.getCurrentPage()) {
			hotelCurrentPage = hotelCurrentPage -1 ;
			hotelPageUtils.setCurrentPage(hotelCurrentPage) ;
			hotelListview.setSelection(hotelPageUtils.getFromIndex()) ;
			
			mHotelAdapter.notifyDataSetChanged() ;
		}
		
		return true;
	}


//    /**
//	 * 获取常用电话号码
//	 * @return
//	 */
//	private ArrayList<TableRow> getCommonDate() {
        //ArrayList<TableRow> table = new ArrayList<TableRow>(); 

    	// 每行的数据  
//        TableCell[] cells = new TableCell[2]; 
//        cells[0] = new TableCell("匪警", width, height, TableCell.STRING);  
//        cells[1] = new TableCell("110", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells));
//        
//        TableCell[] cells2 = new TableCell[2]; 
//        cells2[0] = new TableCell("火警", width, height, TableCell.STRING);  
//        cells2[1] = new TableCell("119", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells2));
//        
//        TableCell[] cells3 = new TableCell[2]; 
//        cells3[0] = new TableCell("急救中心", width, height, TableCell.STRING);  
//        cells3[1] = new TableCell("120", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells3));
//        
//        TableCell[] cells4 = new TableCell[2]; 
//        cells4[0] = new TableCell("道路交通事故报警", width, height, TableCell.STRING);  
//        cells4[1] = new TableCell("122", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells4));
//        
//        TableCell[] cells5 = new TableCell[2]; 
//        cells5[0] = new TableCell("天气预报", width, height, TableCell.STRING);  
//        cells5[1] = new TableCell("121", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells5));
//        
//        TableCell[] cells6 = new TableCell[2]; 
//        cells6[0] = new TableCell("报时服务", width, height, TableCell.STRING);  
//        cells6[1] = new TableCell("117", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells6));
//        
//        TableCell[] cells7 = new TableCell[2]; 
//        cells7[0] = new TableCell("国际人工长途电话", width, height, TableCell.STRING);  
//        cells7[1] = new TableCell("103", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells7));
//        
//        TableCell[] cells8 = new TableCell[2]; 
//        cells8[0] = new TableCell("国际他人付费电话", width, height, TableCell.STRING);  
//        cells8[1] = new TableCell("108", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells8));
//        
//        TableCell[] cells9 = new TableCell[2]; 
//        cells9[0] = new TableCell("国内邮政编码查询", width, height, TableCell.STRING);  
//        cells9[1] = new TableCell("184", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells9));
//        
//        TableCell[] cells10 = new TableCell[2]; 
//        cells10[0] = new TableCell("国内邮政特快专递", width, height, TableCell.STRING);  
//        cells10[1] = new TableCell("11185", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells10));
            
//        return null;//table ;
//    }
	
//	/**
//	 * 获取酒店电话号码
//	 * @return
//	 */
//	private ArrayList<TableRow> getHotelDate() {
//        ArrayList<TableRow> table = new ArrayList<TableRow>();  
//        
//    	// 每行的数据  
//        TableCell[] cells = new TableCell[2]; 
//        cells[0] = new TableCell("前台", width, height, TableCell.STRING);  
//        cells[1] = new TableCell("88485888", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells));
//        
//        TableCell[] cells2 = new TableCell[2]; 
//        cells2[0] = new TableCell("饭厅", width, height, TableCell.STRING);  
//        cells2[1] = new TableCell("88488555", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells2));
//        
//        TableCell[] cells3 = new TableCell[2]; 
//        cells3[0] = new TableCell("本房间", width, height, TableCell.STRING);  
//        cells3[1] = new TableCell("88481288-268", width, height, TableCell.STRING);  
//        table.add(new TableRow(cells3));
//        
//        return table ;
//    }
//	
	
}