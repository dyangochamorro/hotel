package com.shine.hotels.ui.guidebook;

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
 * 旅行指南(主页)
 * @author guoliang
 *
 */
public class FragmentGuidebook extends BaseFragment implements OnItemClickListener {
	
	private HomepageAdapter adapter ;
	private FragmentTransaction transaction ;
	private MenuController mController ;
	
	// 定义数组，即图片资源
	private String[] mImageUrls ;
//    private Integer[] mImageIds = {
//            R.drawable.icon_taxi,
//            R.drawable.icon_train,
//            R.drawable.icon_flight,
//            R.drawable.icon_time,
//            R.drawable.icon_temperature,  
//    };
    
    // 定义数组，即文字资源
	private String[] txts ;
//    private String[] txts = {"租车服务", "列车查询", "航班信息", "世界时间", "天气预报"} ;

	List<Menu> menus ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View v = inflater.inflate(R.layout.fragment_guidebook, container, false) ;
		
		adapter = new HomepageAdapter(getActivity(), mImageUrls, txts) ;

		//获取Gallery对象
		CoverFlow cover = (CoverFlow)v.findViewById(R.id.guidebook_coverFlow);
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
        Request request = builder.obtain(Request.Action.GET_NAVIGATION_MENU).putStringParam("tag", "LXZN").getResult();
        mController.handle(request);
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);

        super.onStop();
    }
	
//	@Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//
//        super.onDestroy();
//    }
	
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
        Log.d("shine", "guidebook click:" + pos);
        
        if (null!=menus && menus.size()>0 && null!=menus.get(pos)) {
        	String tag = menus.get(pos).getTag() ;
        	if ("ZCFW".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment carrental = new FragmentCarrental();
				transaction.replace(R.id.bottom_main_layout, carrental, UIConfig.FRAGMENT_TAG_GUIDEBOOK_CARRENTAL);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_GUIDEBOOK_CARRENTAL);
				transaction.commit();
        	} else if ("LCCX".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment traininformation = new FragmentTraininformation();
				transaction.replace(R.id.bottom_main_layout, traininformation, UIConfig.FRAGMENT_TAG_GUIDEBOOK_TRAININFORMATION);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_GUIDEBOOK_TRAININFORMATION);
				transaction.commit();
        	} else if ("HBXX".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment flightinfo = new FragmentFlightinfo();
				transaction.replace(R.id.bottom_main_layout, flightinfo, UIConfig.FRAGMENT_TAG_GUIDEBOOK_FLIGHTINFO);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_GUIDEBOOK_FLIGHTINFO);
				transaction.commit();
        	} else if ("SJSJ".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment worldtime = new FragmentWorldtime();
				transaction.replace(R.id.bottom_main_layout, worldtime, UIConfig.FRAGMENT_TAG_GUIDEBOOK_WORLDTIME);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_GUIDEBOOK_WORLDTIME);
				transaction.commit();
        	} else if ("TQYB".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment weather = new FragmentWeather();
				transaction.replace(R.id.bottom_main_layout, weather, UIConfig.FRAGMENT_TAG_GUIDEBOOK_WEATHER);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_GUIDEBOOK_WEATHER);
				transaction.commit();
        	}
        }
    }

}
