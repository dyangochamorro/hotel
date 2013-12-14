package com.shine.hotels.ui.roomservice;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.MenuController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Menu;
import com.shine.hotels.io.model.MenuList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.ui.home.HomepageAdapter;
import com.shine.hotels.ui.widget.CoverFlow;

import de.greenrobot.event.EventBus;

/**
 * 客房服务(主页)
 * @author guoliang
 *
 */
public class FragmentRoom extends BaseFragment implements OnItemClickListener {
	
	private HomepageAdapter adapter ;
	
	private FragmentTransaction transaction ;
	
	private MenuController mController ;
	
	//定义整型数组，即图片资源
	private String[] mImageUrls ;
//	private Integer[] mImageIds = {
//			R.drawable.icon_bill,
//			R.drawable.icon_phone,
//			R.drawable.icon_calculator,
//			R.drawable.icon_butler,	
//			R.drawable.icon_message,	
//	};
	
	// 定义数组，即文字资源
	private String[] txts ;
//	private String[] txts = {"账单查询", "常用电话", "快速结账", "管家服务", "留意服务"} ;
	
	List<Menu> menus ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_roomservice, container, false) ;
		
		adapter = new HomepageAdapter(getActivity(), mImageUrls, txts) ;

		//获取Gallery对象
		CoverFlow cover = (CoverFlow)v.findViewById(R.id.room_coverFlow);
		//添加ImageAdapter给Gallery对象
		cover.setAdapter(adapter) ;
		cover.setSelection(Integer.MAX_VALUE / 2 - 1) ;
		cover.setOnItemClickListener(this);
		
        return v;
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (MenuController)ControllerManager.newController(getActivity(), MenuController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_NAVIGATION_MENU).putStringParam("tag", "KFFW").getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<MenuList> event) {
        MenuList result = event.result;
        
    	menus = result.getLists() ;
    	
    	if (null!=menus && menus.size()>0) {
    		mImageUrls = new String[menus.size()] ;
    		txts = new String[menus.size()] ;
    		
    		for (int i=0;i<menus.size();i++) {
    			Menu menu = menus.get(i) ;
    			mImageUrls[i] = menu.getMenupic() ;
    			txts[i] = menu.getMenuname() ;
    		}
    	}
    	
    	adapter.update(mImageUrls, txts) ;
    	
    	adapter.notifyDataSetChanged() ;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = position % mImageUrls.length;
        Log.d("shine", "room click:" + pos);
        
        if (null!=menus && menus.size()>0 && null!=menus.get(pos)) {
        	String tag = menus.get(pos).getTag() ;
        	if ("ZDCX".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment billquery = new FragmentBillquery();
				transaction.replace(R.id.bottom_main_layout, billquery, UIConfig.FRAGMENT_TAG_ROOM_BILLQUERY);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ROOM_BILLQUERY);
				transaction.commit();
        	} else if ("CYDH".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment usedphone = new FragmentUsedphone();
				transaction.replace(R.id.bottom_main_layout, usedphone, UIConfig.FRAGMENT_TAG_ROOM_USEDPHONE);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ROOM_USEDPHONE);
				transaction.commit();
        	} else if ("KSJZ".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment expresscheckout = new FragmentExpresscheckout();
				transaction.replace(R.id.bottom_main_layout, expresscheckout, UIConfig.FRAGMENT_TAG_ROOM_EXPRESSCHECKOUT);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ROOM_EXPRESSCHECKOUT);
				transaction.commit();
        	} else if ("GJFW".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment butlerservice = new FragmentButlerservice();
				transaction.replace(R.id.bottom_main_layout, butlerservice, UIConfig.FRAGMENT_TAG_ROOM_BUTLERSERVICE);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ROOM_BUTLERSERVICE);
				transaction.commit();
        	} else if ("LYFW".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment memoserv = new FragmentMemoserv();
				transaction.replace(R.id.bottom_main_layout, memoserv, UIConfig.FRAGMENT_TAG_ROOM_MEMOSERV);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ROOM_MEMOSERV);
				transaction.commit();
        	}
        }
    }

}