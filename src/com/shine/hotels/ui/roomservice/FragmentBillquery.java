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
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.BillqueryController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Bill;
import com.shine.hotels.io.model.BillList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.roomservice.BillqueryTableAdapter.TableCell;
import com.shine.hotels.ui.roomservice.BillqueryTableAdapter.TableRow;
import com.shine.hotels.util.PageUtils;
import com.shine.hotels.util.Utils;

import de.greenrobot.event.EventBus;

/**
 * 客房服务-账单查询
 * @author guoliang
 *
 */
public class FragmentBillquery extends BaseFragment {
	
	private BillqueryController mController;
	
	private BillqueryTableAdapter billqueryAdapter;

	private ArrayList<TableRow> list_data = new ArrayList<TableRow>(); 
	
	private Integer total = 0 ;
	
	private ListView listview ;
	
	private TextView textview ;
	
	private int pageSize = 14 ; // 每页显示的条数
	    
	private int recordCount ; // 总共的条数
	    
	private int currentPage = 1 ; // 当前页面
    
    private PageUtils pageUtils ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_billquery, container, false) ;
		
		textview = (TextView)v.findViewById(R.id.total_txt) ;
		
		listview = (ListView)v.findViewById(R.id.billquery_listview) ;
		billqueryAdapter = new BillqueryTableAdapter(getActivity(), list_data);  
		listview.setAdapter(billqueryAdapter);  
		
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT)) ;
		listview.setDividerHeight(0) ;
		
		listview.setItemsCanFocus(true) ;
		
		listview.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (BillqueryController)ControllerManager.newController(getActivity(),
        		BillqueryController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_BILLING)
                .getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<BillList> event) {
        BillList result = event.result;
        
        int height = Utils.dip2px(getActivity(), 23) ;
        
        List<Bill> list = result.getLists();
        
        recordCount = list.size() ;
        
        pageUtils = new PageUtils(pageSize, recordCount, currentPage) ;
        
        list_data.clear();
        total = 0 ;
        
        for (Bill bill : list) {
            TableCell[] cells = new TableCell[4];
            cells[0] = new TableCell(bill.getIndex(), Utils.dip2px(getActivity(), 56), height, TableCell.STRING);  
            cells[1] = new TableCell(bill.getTing(), Utils.dip2px(getActivity(), 183), height, TableCell.STRING); 
            cells[2] = new TableCell(bill.getAmount(), Utils.dip2px(getActivity(), 42), height, TableCell.STRING) ;
            cells[3] = new TableCell(bill.getPrice(), Utils.dip2px(getActivity(), 96), height, TableCell.STRING) ;
            list_data.add(new TableRow(cells));
            
            total += bill.getPrice() ;
        }
        
        textview.setText(""+total) ;
        
        billqueryAdapter.notifyDataSetChanged();
    }

	@Override
	public boolean onKeyDown() {
		if (pageUtils.getPageCount()!=pageUtils.getCurrentPage()) {
			currentPage = currentPage + 1 ;
			pageUtils.setCurrentPage(currentPage) ;
			listview.setSelection(pageUtils.getFromIndex()) ;
            
			billqueryAdapter.notifyDataSetChanged();  
		}
		
		return true;
	}

	@Override
	public boolean onKeyUp() {
		if (1!=pageUtils.getCurrentPage()) {
			currentPage = currentPage - 1 ;
			pageUtils.setCurrentPage(currentPage) ;
			listview.setSelection(pageUtils.getFromIndex()) ;
            
			billqueryAdapter.notifyDataSetChanged();    
		}
		return true;
	}


	
//	/**
//	 * 获取账单数据
//	 * @return
//	 */
//	private ArrayList<TableRow> getDate() {
//        ArrayList<TableRow> table = new ArrayList<TableRow>();  
//        TableCell[] titles = new TableCell[4];
//        int height = 23 ;
//        // 定义标题  
//        titles[0] = new TableCell("索引", 50, height, TableCell.STRING);  
//        titles[1] = new TableCell("项目", 140, height, TableCell.STRING);  
//        titles[2] = new TableCell("数量", 50, height, TableCell.STRING);  
//        titles[3] = new TableCell("费用（RMB）", 93, height, TableCell.STRING);  
//        
//        TableRow tr = new TableRow(titles) ; 
//        
//        table.add(tr);  
//        
//
//        // 把表格的行添加到表格  
//        for (int i = 0; i < 12; i++) {
//        	// 每行的数据  
//            TableCell[] cells = new TableCell[4]; 
//            cells[0] = new TableCell("No"+i, titles[0].width, height, TableCell.STRING);  
//            cells[1] = new TableCell("Project"+i, titles[1].width, height, TableCell.STRING);  
//            cells[2] = new TableCell(""+i, titles[2].width, height, TableCell.STRING);  
//            cells[3] = new TableCell(""+(10*i+1), titles[3].width, height, TableCell.STRING);  
//            table.add(new TableRow(cells));
//        }
//        
//        return table ;
//    }
	
//	/**
//	 * 获取消费总额
//	 * @return
//	 */
//	private int getTotal() {
//		return 1111 ;
//	}
}