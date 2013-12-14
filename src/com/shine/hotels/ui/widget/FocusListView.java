package com.shine.hotels.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

public class FocusListView extends ListView {
    private int mSelected;

    public FocusListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public FocusListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public FocusListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        
//        Log.w("shine", "listview onFocusChanged gainFocus=" + gainFocus + " pos=" + getSelectedItemPosition() + " mSel=" + mSelected);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            setSelection(mSelected);
        } else {
            mSelected = getSelectedItemPosition();
        }
    }
}
