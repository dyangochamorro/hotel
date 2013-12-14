package com.shine.hotels.ui.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.GridView;

public class ScaleGridView extends GridView {
    private Camera mCamera = new Camera();

    public ScaleGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ScaleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ScaleGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (getSelectedView() == child) {
            final int childWidth = child.getWidth();
            
            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);
            float rate = 1.5f;
            
            mCamera.save();
            final Matrix matrix = t.getMatrix();
            float zoomAmount = (float)(rate * 200.0);
            mCamera.translate(0.0f, 0.0f, zoomAmount);
            mCamera.getMatrix(matrix);
            matrix.preTranslate(-(childWidth / 2), -(childWidth / 2));
            matrix.postTranslate((childWidth / 2), (childWidth / 2));
            mCamera.restore();
        }
        return true;
    }

}
