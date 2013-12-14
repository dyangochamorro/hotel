
package com.shine.hotels.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shine.hotels.R;
import com.shine.hotels.controller.BaseController;
import com.shine.hotels.controller.ButlerserviceController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;

import de.greenrobot.event.EventBus;

/**
 * 
 * 账单确认
 */
public class FragmentButlerAffirm extends BaseFragment implements OnClickListener {
	
    private BaseController mController;
    
    private Button confirm ;
    
    private Button cancel ;
    
    private TextView tv;
    // 1:确定 2：取消
    private Integer FLAG = 1 ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        
        mController = null;
        
        super.onDestroy();
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_billaffirm, container, false);

        tv=(TextView) v.findViewById(R.id.tv);
        tv.setText(R.string.room_service);
        
        confirm = (Button)v.findViewById(R.id.add_yes);
        confirm.requestFocus();
        confirm.setOnClickListener(this);
        
        cancel = (Button)v.findViewById(R.id.cancel);  
        cancel.setOnClickListener(this);
        
        View f = v.findFocus();
        if (f != null) {
            Log.d("shine", "f=" + f.getClass());
        }
        
        return v;
    }


//	@Override
//	public boolean onKeyCenter() {
//		if (1==FLAG) {
//			EventBus.getDefault().register(this);
//
//            mController = ControllerManager.getStickyController(getActivity(),
//            		ExpresscheckoutController.class);
//            Builder builder = new Builder();
//            Request request = builder.obtain(Request.Action.GET_EXPRESSCHECKOUT_CONFIRM).getResult();
//            mController.handle(request);
//	        
//		} else {
//			FragmentTransaction transaction = getActivity().getSupportFragmentManager()
//                    .beginTransaction();
//            transaction.hide(FragmentBillaffirm.this);
//            transaction.remove(FragmentBillaffirm.this);
//            transaction.commit();
//		}
//		
//		return true;
//	}
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_yes:

                // mController =
                // ControllerManager.getStickyController(getActivity(),
                // ExpresscheckoutController.class);
                // Builder builder = new Builder();
                // Request request =
                // builder.obtain(Request.Action.GET_EXPRESSCHECKOUT_CONFIRM).getResult();
                // mController.handle(request);
                //
                // doBack();
                //
                // Toast toast = Toast.makeText(getActivity(),
                // R.string.toast_msg, Toast.LENGTH_LONG) ;
                // toast.setGravity(Gravity.CENTER, 60, 30);
                // toast.show();

                mController = (ButlerserviceController)ControllerManager.newController(
                        getActivity(), ButlerserviceController.class);
                Builder builder = new Builder();
                Request request = builder.obtain(Request.Action.HOUSEKEEPER_CONFIRM).getResult();
                mController.handle(request);

                doBack();

                Toast toast = Toast.makeText(getActivity(), R.string.toast_msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 60, 30);
                toast.show();

                break;

            case R.id.cancel:
                doBack();
                break;

            default:
                break;
        }

    }

	@Override
	public boolean onKeyLeft() {
	    confirm.requestFocus();
		
		FLAG = 1 ;
		
		return true;
	}

	@Override
	public boolean onKeyRight() {
	    cancel.requestFocus();
		
		FLAG = 2 ;
		
		return true;
	}
    
}
