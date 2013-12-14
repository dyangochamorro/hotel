package com.shine.hotels.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.shine.hotels.R;
import com.shine.hotels.controller.BaseController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.TravelController;
/**
 * 
 * 
 */
public class TravelFragment extends Fragment implements OnClickListener {
    private BaseController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_room_service, container, false);
        Button pre = (Button)v.findViewById(R.id.pre);
        pre.requestFocus();
        pre.setOnClickListener(this);
        
        Button next = (Button)v.findViewById(R.id.next);
        next.setOnClickListener(this);
        
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mController = ControllerManager.newController(getActivity(), TravelController.class);
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.pre: {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(this);
                transaction.commit();
                break;
            }
                
            case R.id.next: {
                Fragment confirm = new FragmentConfirm();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.top_layout, confirm, UIConfig.FRAGMENT_TAG_CONFIRM);
                transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, 
                        R.animator.fragment_slide_right_exit);
                transaction.commit();
                break;
            }

            default:
                break;
        }
    }

}
