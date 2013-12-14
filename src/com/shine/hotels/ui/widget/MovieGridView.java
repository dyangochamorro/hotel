package com.shine.hotels.ui.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.GridView;

public class MovieGridView extends GridView {
    private Camera mCamera = new Camera();
    private boolean mIsSeletedable = false;
//    private FocusListener mFocusListener;
    
    public MovieGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }

    public MovieGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
    }

    public MovieGridView(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }
    
    @Override
    protected void onAttachedToWindow() {
//        Log.w("shine", "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    public void setSelection(int position) {
//        Log.w("shine", "setSelection=" + position);
        super.setSelection(position);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
//        Log.w("shine", "onFocusChanged gainFocus=" + gainFocus);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            setSeletedable(true);
            setSelection(0);
        } else {
            setSelection(-1);
            setSeletedable(false);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        Log.w("shine", "onWindowFocusChanged:" + hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public void clearFocus() {
//        Log.w("shine", "clearFocus");
        super.clearFocus();
    }
    
    private void setSeletedable(boolean flag) {
        mIsSeletedable = flag;
    }
    
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (!mIsSeletedable) return false;
        
        View selected = getSelectedView();
        if (child == selected) {
//            final int childCenter = child.getLeft() + child.getWidth() / 2;
            final int childWidth = child.getWidth();

            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);

            mCamera.save();
            final Matrix matrix = t.getMatrix();
            float zoomAmount = (float)(1.05f * 90.0);
            mCamera.translate(0.0f, 0.0f, -zoomAmount);
            mCamera.getMatrix(matrix);
            matrix.preTranslate(-(childWidth / 2), -(childWidth / 2));
            matrix.postTranslate((childWidth / 2), (childWidth / 2));
            mCamera.restore();
            
            return true;
        }
        return super.getChildStaticTransformation(child, t);
    }
    
}
