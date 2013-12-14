package com.shine.hotels.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView {

	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
//	    Log.d("maqr", "isFocused text:" + getText());
		return true;
	}
}


