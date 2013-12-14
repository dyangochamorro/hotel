package com.shine.hotels.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.shine.hotels.io.model.Subtitle;

public class SubtitleTextView extends TextView implements Runnable {
	public final static String TAG = SubtitleTextView.class.getSimpleName();

	private float textLength = 0f;// 文本长度
	private int viewWidth = 0;
	private float step = 0f;// 文字的横坐标
	private int y = 0;// 文字的纵坐标
	private int x = 0;//不滚动时的横坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = null;// 绘图样式
	private String text = "";// 文本内容
	private boolean first = true;
	private int currentScroll = 0 ;// 滚动速度

	public SubtitleTextView(Context context, Subtitle subtitle) {
		super(context);

		// 文本内容
		text = subtitle.getScrolltextcontent() ;
		// 字幕背景色
		setBackgroundColor(Color.parseColor(subtitle.getScrolltextbackgourd()));
		// 字体颜色         
		setTextColor(Color.parseColor(subtitle.getScrolltextcolor()));
		// 字体大小
		setTextSize(Integer.parseInt(subtitle.getScrolltextsize())) ;
		// 滚动字幕的坐标
		String scrolltextcoordinate = subtitle.getScrolltextcoordinate() ;
		x = Integer.parseInt(scrolltextcoordinate.split(",")[0]) ;
		y = Integer.parseInt(scrolltextcoordinate.split(",")[1]) ;
		// 字幕滚动的速度
		currentScroll = Integer.parseInt(subtitle.getScrolltexttempo()) ;
	}

	public SubtitleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubtitleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下
	 */
	public void init(WindowManager windowManager) {
		paint = getPaint();
		paint.setARGB(255, 0, 255, 0);
//		text = getText().toString();
		textLength = paint.measureText(text);
		viewWidth = getWidth();
		if (viewWidth == 0) {
			if (windowManager != null) {
				Display display = windowManager.getDefaultDisplay();
				viewWidth = display.getWidth();
			}
		}
		if (textLength > viewWidth) {
			isStarting = true;
		}
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
//		y = getTextSize() + getPaddingTop();
//		x = getPaddingLeft();
	}

	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;

	}

	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;

	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	/**
	 * 开始滚动
	 */
	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	/**
	 * 停止滚动
	 */
	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	public void onDraw(Canvas canvas) {
		if (first) {
			viewWidth = getWidth();
			Log.i("width", "width="+viewWidth);
			first = false;
		}
		if (!isStarting) {
			canvas.drawText(text, x, y, paint);
			return;
		}
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
		step += 0.5;
		if (step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();
	}

	public void setText(String text) {
		this.text = text;
		textLength = paint.measureText(text);
		if (textLength > viewWidth) {
			isStarting = true;
		} else {
			isStarting = false;
		}
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
//		y = getTextSize() + getPaddingTop();
//		x = getPaddingLeft();
		invalidate();
	}

	@Override
	public void run() {
		x -= 2;// 滚动速度                 
		scrollTo(x, 0);                 
		if (isStarting) {                         
			return;                 
		}                 
		if (getScrollX() <= -(this.getWidth())) {                         
			scrollTo(viewWidth, 0);                         
			x = viewWidth; 
			// return;                 
		}                 
		postDelayed(this, currentScroll); 
	}
}