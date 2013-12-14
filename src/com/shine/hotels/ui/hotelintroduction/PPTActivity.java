package com.shine.hotels.ui.hotelintroduction;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shine.hotels.R;
import com.shine.hotels.service.MusicService;
import com.shine.hotels.service.MusicService.MusicBinder;
import com.squareup.picasso.Picasso;

public class PPTActivity extends Activity {
	public static final String INTENT_KEY_PICS = "pics";

//	 private Gallery mGallery;
	private List<String> mPics;
	private ViewPager mViewPager;
	private Handler mHandler = new Handler();

	private static final int DURING_TIME = 4 * 1000;
	
	private ChangePicsTask mTask = new ChangePicsTask();
	
//	boolean mBound = false;
//	private ServiceConnection mConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName arg0) {
//            mBound = false;
//        }
//    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppt_activity_layout);

//		 mGallery = (Gallery)findViewById(R.id.ppt_gallery);
//		 GalleryAdapter adapter = new GalleryAdapter();
//		 mGallery.setAdapter(adapter);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		RecommendAdapter adapter = new RecommendAdapter();
		mViewPager.setAdapter(adapter);

		mPics = getIntent().getStringArrayListExtra(INTENT_KEY_PICS);
		if (mPics != null && mPics.size() > 0) {
			adapter.notifyDataSetChanged();
			if (mPics.size() > 1) {
				mHandler.postDelayed(mTask, DURING_TIME);
			}
		}
		
//		Intent intent = new Intent(this, MusicService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
//	    Log.e("shine", "ppt onDestroy");
//	    if (mBound) {
//            unbindService(mConnection);
//            mBound = false;
//        }
	    mHandler.removeCallbacks(mTask);
	    mViewPager.removeAllViews();
	    mViewPager = null;
	    System.gc();
	    super.onDestroy();
	}

	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int action = event.getAction();
        final int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            mHandler.removeCallbacks(mTask);
            finish();
            return super.dispatchKeyEvent(event);
        } else {
            return true;
        }

    }

	private int mCurrentPos;

	private enum TYPE {
		ONE, TWO
	};

	private TYPE A = TYPE.ONE;

	private class ChangePicsTask implements Runnable {

		@Override
		public void run() {
			/*
			 * int newPos = mCurrentPos + 1; if (newPos < mPics.size()) {
			 * mViewPager.setCurrentItem(newPos); mCurrentPos = newPos; } else
			 * if (newPos == mPics.size()) {
			 * 
			 * mViewPager.setCurrentItem(0); mCurrentPos = 0; }
			 */

			if (A == TYPE.ONE) {
				int newpos = mCurrentPos + 1;
				if (newpos < mPics.size()) {
					mViewPager.setCurrentItem(newpos);
//				    mGallery.setSelection(newpos);
					mCurrentPos = newpos;
				} else {
					A = TYPE.TWO;
				}
			} else {
				int newpos = mCurrentPos - 1;
				if (newpos >= 0) {
					mViewPager.setCurrentItem(newpos);
//				    mGallery.setSelection(newpos);
					mCurrentPos = newpos;
				} else {
					A = TYPE.ONE;
				}
			}

			mHandler.postDelayed(mTask, DURING_TIME);
		}
	}

	private class RecommendAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (mPics == null)
				return null;

			ImageView image = getImageView(mPics.get(position));
			container.addView(image);

			return image;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			if (mPics != null)
				return mPics.size();

			return 0;
		}

		private ImageView getImageView(String pic) {
			ImageView imageView = new ImageView(PPTActivity.this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			imageView.setLayoutParams(lp);
			imageView.setBackgroundColor(PPTActivity.this.getResources()
					.getColor(R.color.black_text));
			Picasso.with(PPTActivity.this).load(pic)
					.into(imageView);

			return imageView;
		}
	}

	private class GalleryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mPics != null)
				return mPics.size();
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mPics != null)
				return mPics.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			if (mPics != null)
				return position;
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				ImageView imageView = new ImageView(PPTActivity.this);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				imageView.setLayoutParams(lp);

				convertView = imageView;
			}

			String pic = mPics.get(position);

			Picasso.with(PPTActivity.this).load(pic).skipMemoryCache()
					.into((ImageView) convertView);

			return convertView;
		}

	}

}
