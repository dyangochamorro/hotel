package com.shine.hotels.ui.roomservice;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.shine.hotels.R;
import com.shine.hotels.controller.ButlerserviceController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Butlerservice;
import com.shine.hotels.io.model.ButlerserviceList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.FragmentBillaffirm;
import com.shine.hotels.ui.FragmentButlerAffirm;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.util.HtmlImageGetter;

import de.greenrobot.event.EventBus;

/**
 * 客房服务-管家服务
 * @author guoliang
 *
 */
public class FragmentButlerservice extends BaseFragment {
	
	ButlerserviceController mController ;
	
	TextView textview ;
	
	String data = "" ;
	
	List<String> listData = new ArrayList<String>() ;
	
	ArrayAdapter<String> listAdapter ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_butlerservice, container, false) ;
		
		textview = (TextView)v.findViewById(R.id.butlerservice_txt) ;
        
        GridView gridview = (GridView)v.findViewById(R.id.butlerservice_gridview) ;
        gridview.setFocusable(false);
        gridview.setEnabled(false);

        listAdapter = new ArrayAdapter<String>(getActivity(), 
        											R.layout.fragment_butlerservice_list,
        											listData) ;
        
        //添加并且显示 
        gridview.setAdapter(listAdapter);

		return v;
		
	}
	
	@Override
	public void onResume() {
		super.onResume() ;
		textview.requestFocus() ;
	};
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (ButlerserviceController)ControllerManager.newController(getActivity(),
        		ButlerserviceController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.HOUSEKEEPER)
                .getResult();
        mController.handle(request);
    }
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<ButlerserviceList> event) {
		ButlerserviceList result = event.result;
        
        List<Butlerservice> list = result.getLists();
        
        listData.clear() ;
        
        if (null!=list && list.size()>0) {
        	for (Butlerservice butlerservice : list) {
        		if (butlerservice.getType()==1) {
        			data = butlerservice.getOwner() ;
        		} else if (butlerservice.getType()==2) {
        			listData.add(butlerservice.getOwner()) ;
				}
        	}
        	
        	//默认图片，无图片或没加载完显示此图片  
            Drawable defaultDrawable = getActivity().getResources().getDrawable(R.drawable.ok);  
            
            Spanned sp = Html.fromHtml(data, new HtmlImageGetter(textview, "/esun_msg", defaultDrawable), null);  
            textview.setText(sp);  
        	
        	listAdapter.notifyDataSetChanged() ;
        }

    }
	
	@Override
	public boolean onKeyCenter() {   
        Fragment confirm = new FragmentButlerAffirm();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.top_layout, confirm, UIConfig.FRAGMENT_TAG_ADD);
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, 
                R.animator.fragment_slide_right_exit);
        transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ADD);
        transaction.commit();
        
        return true ;
	}
	
	
}