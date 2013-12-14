package com.shine.hotels.ui.roomservice;

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
import com.shine.hotels.controller.MemoservController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Memoserv;
import com.shine.hotels.io.model.MemoservList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.roomservice.MemoservTableAdapter.TableCell;
import com.shine.hotels.ui.roomservice.MemoservTableAdapter.TableRow;
import com.shine.hotels.util.Utils;

import de.greenrobot.event.EventBus;

/**
 * 客房服务-留言服务
 * @author guoliang
 *
 */
public class FragmentMemoserv extends BaseFragment {
	
	MemoservController mController ;
	
	MemoservTableAdapter tableAdapter ;
	
	ArrayList<TableRow> table = new ArrayList<TableRow>();  

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_memoserv, container, false) ;
		
		ListView listview = (ListView)v.findViewById(R.id.memoserv_listview) ;
		tableAdapter = new MemoservTableAdapter(getActivity(), table);  
		listview.setAdapter(tableAdapter); 
		
		listview.setDividerHeight(0) ;
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (MemoservController)ControllerManager.newController(getActivity(),
        		MemoservController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_MESSAGE)
                .getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<MemoservList> event) {
		MemoservList result = event.result;
        
        List<Memoserv> list = result.getLists();

        int height = Utils.dip2px(getActivity(), 39) ; 
        
        table.clear() ;
        
        for (Memoserv memoserv : list) {
        	// 每行的数据  
            TableCell[] cells = new TableCell[3]; 
            cells[0] = new TableCell(""+memoserv.getIndex(), Utils.dip2px(getActivity(), 100), height, TableCell.STRING);  
            cells[1] = new TableCell(memoserv.getContent(), Utils.dip2px(getActivity(), 360), height, TableCell.STRING);  
            cells[2] = new TableCell(memoserv.getDate(), Utils.dip2px(getActivity(), 50), height, TableCell.STRING);  
            table.add(new TableRow(cells));
        }
        
        tableAdapter.notifyDataSetChanged();
    }

//	/**
//	 * 获取账单数据
//	 * @return
//	 */
//	private ArrayList<TableRow> getDate() {
//        ArrayList<TableRow> table = new ArrayList<TableRow>();  
//        TableCell[] titles = new TableCell[3];
//        int height = 27 ;
//        int height_content = 37 ;
//        // 定义标题  
//        titles[0] = new TableCell("索引", 60, height, TableCell.STRING);  
//        titles[1] = new TableCell("留言内容", 340, height, TableCell.STRING);  
//        titles[2] = new TableCell("时间", 50, height, TableCell.STRING);  
//        
//        TableRow tr = new TableRow(titles) ; 
//        
//        table.add(tr);  
//        
//
//        // 把表格的行添加到表格  
//        for (int i = 0; i < 6; i++) {
//        	// 每行的数据  
//            TableCell[] cells = new TableCell[3]; 
//            cells[0] = new TableCell(""+i, titles[0].width, height_content, TableCell.STRING);  
//            cells[1] = new TableCell("留言内容"+i, titles[1].width, height_content, TableCell.STRING);  
//            cells[2] = new TableCell("14：0"+i, titles[2].width, height_content, TableCell.STRING);  
//            table.add(new TableRow(cells));
//        }
//        
//        return table ;
//    }
}