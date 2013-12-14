package com.shine.hotels.ui.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class MusicFlow extends Gallery {

    private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 50;
    private int mMaxZoom = -380;
    private int mCoveflowCenter;
    private boolean mAlphaMode = true;
    private boolean mCircleMode = false;

    int lastPosition;  
    
    public MusicFlow(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }

    public MusicFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
    }

    public MusicFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }

    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    public boolean getCircleMode() {
        return mCircleMode;
    }

    public void setCircleMode(boolean isCircle) {
        mCircleMode = isCircle;
    }

    public boolean getAlphaMode() {
        return mAlphaMode;
    }

    public void setAlphaMode(boolean isAlpha) {
        mAlphaMode = isAlpha;
    }

    public int getMaxZoom() {
        return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                + getPaddingLeft();
    }

//    private static int getCenterOfView(View view) {
//        return view.getLeft() + view.getWidth() / 2;
//    }
    
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        final int childCenter = child.getLeft() + child.getWidth() / 2;
        final int childWidth = child.getWidth();

        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);
        float rate = Math.abs((float)(mCoveflowCenter - childCenter) / childWidth);
        
        if (rate > .5f) {
            rate = 0.8f;
        } 

        mCamera.save();
        final Matrix matrix = t.getMatrix();
        float zoomAmount = (float)(rate * 220.0);
        mCamera.translate(0.0f, 0.0f, zoomAmount);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-(childWidth / 2), -(childWidth / 2));
        matrix.postTranslate((childWidth / 2), (childWidth / 2));
        mCamera.restore();

        return true;
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override  
    protected int getChildDrawingOrder(int childCount, int i) {  
        int mFirstPosition = getFirstVisiblePosition();  
  
        int mSelectedPosition = getSelectedItemPosition();  
  
        int selectedIndex = mSelectedPosition - mFirstPosition;  
  
        if (i == 0){  
            lastPosition = 0;  
        }  
  
        int ret = 0;  
  
        if (selectedIndex < 0) {  
            return i;  
        }  
  
        if (i == childCount - 1) {  
  
            ret = selectedIndex;  
  
        } else if (i >= selectedIndex) {  
  
            lastPosition++;  
  
            ret = childCount - lastPosition;  
  
        } else {  
  
            ret = i;  
  
        }  
  
        return ret;  
  
    }  
}
