
package com.shine.hotels.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.shine.hotels.R;
import com.shine.hotels.controller.Events.AddType;

import de.greenrobot.event.EventBus;

/**
 * 
 * 
 */
public class FragmentConfirm extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {

//        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_confirm, container, false);

        Button btn = (Button)v.findViewById(R.id.yes);
        btn.requestFocus();
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.hide(FragmentConfirm.this);
                transaction.commit();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onEvent(AddType type) {
        switch (type.type) {
            case AddType.WORLD_TIME:
                Log.d("shine", "AddType.WORLD_TIME");
                break;

            case AddType.WEATHER:

                break;

            default:
                break;
        }
    }

}
