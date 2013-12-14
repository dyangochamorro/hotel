package com.shine.hotels.ui.subtitle;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.SubtitleController;
import com.shine.hotels.io.model.Subtitle;

import de.greenrobot.event.EventBus;

/**
 * 滚动字幕
 * @author guoliang
 *
 */
public class FragmentSubtitle extends Fragment {
	
	SubtitleController sController ;
	
	Subtitle subtitle ;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View v = null ;
		
		return v;
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        sController = (SubtitleController)ControllerManager.newController(getActivity(),
        		SubtitleController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_SUBTITLE)
                .getResult();
        sController.handle(request);
    }
	
	@Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }
	
	public void onEvent(GetResultEvent<Subtitle> event) {
		subtitle = event.result;
        
        // 处理List返回内容
        
    }
}