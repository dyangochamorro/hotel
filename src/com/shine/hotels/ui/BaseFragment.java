
package com.shine.hotels.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class BaseFragment extends Fragment {

    public boolean onKeyDown() {
        return false;
    }

    public boolean onKeyUp() {
        return false;
    }

    public boolean onKeyCenter() {
        return false;
    }

    public boolean onKeyLeft() {
        return false;
    }

    public boolean onKeyRight() {
        return false;
    }
    
    public boolean onKeyVolumeUp() {
        return false;
    }
    
    public boolean onKeyVolumeDown() {
        return false;
    }
    
    public boolean onKeyVolumeMute() {
        return false;
    }

    public boolean onKeyBack() {
        doBack();

        return true;
    }
    
    public void onBackToFont() {}
    
    protected void doBack() {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager
                .beginTransaction();
        supportFragmentManager.popBackStack();
        transaction.remove(this);
        transaction.commit();
    }
    
}
