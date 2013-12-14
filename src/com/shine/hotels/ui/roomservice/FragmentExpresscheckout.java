package com.shine.hotels.ui.roomservice;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.ExpresscheckoutController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Expresscheckout;
import com.shine.hotels.io.model.ExpresscheckoutList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.FragmentBillaffirm;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.util.HtmlImageGetter;

import de.greenrobot.event.EventBus;

/**
 * 客房服务-快速结账
 * @author guoliang
 *
 */
public class FragmentExpresscheckout extends BaseFragment {
	
	ExpresscheckoutController mController ;
	
	TextView textview ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_expresscheckout, container, false) ;
		
		textview = (TextView)v.findViewById(R.id.expresscheckout_txt) ;
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (ExpresscheckoutController)ControllerManager.newController(getActivity(),
        		ExpresscheckoutController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_EXPRESSCHECKOUT)
                .getResult();
        mController.handle(request);
    }
	
	@Override
	public void onResume() {
		super.onResume() ;
		textview.requestFocus() ;
	};
	
	@Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
	
	public void onEvent(GetResultEvent<ExpresscheckoutList> event) {
        ExpresscheckoutList result = event.result;
        
        List<Expresscheckout> list = result.getLists();
        Expresscheckout expresscheckout = list.get(0) ;
        
        String data = expresscheckout.getExpressinfo() ;
        System.out.println(data+"========tu[p====2222222222===");
        //默认图片，无图片或没加载完显示此图片  
        Drawable defaultDrawable = getActivity().getResources().getDrawable(R.drawable.ok);  
        
        Spanned sp = Html.fromHtml(data, new HtmlImageGetter(textview, "/esun_msg", defaultDrawable), null);  
        
        textview.setText(sp);  

    }

	@Override
	public boolean onKeyCenter() {
	    Log.d("shine", "express onKeyCenter");
		Fragment confirm = new FragmentBillaffirm();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.top_layout, confirm, UIConfig.FRAGMENT_TAG_ADD);
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, 
                R.animator.fragment_slide_right_exit);
        transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ADD);
        transaction.commit();
		
        return true;
	}

}