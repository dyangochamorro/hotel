package cn.com.shine.hotel.tv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.com.shine.hotel.bean.ADbean;
import cn.com.shine.hotel.service.ChannelDesk;
import cn.com.shine.hotel.service.TvDeskProvider;

import com.mstar.tv.service.aidl.EN_INPUT_SOURCE_TYPE;
import com.mstar.tv.service.interfaces.ITvServiceServer;
import com.mstar.tv.service.interfaces.ITvServiceServerCommon;
import com.mstar.tv.service.skin.AudioSkin;
import com.shine.hotels.HotelsApplication;
import com.shine.hotels.R;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetToolDataEvent;
import com.shine.hotels.controller.Events.SubtitleEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.SubtitleController;
import com.shine.hotels.io.model.Subtitle;
import com.shine.hotels.io.model.ToolBarData;
import com.shine.hotels.ui.HostActivity;
import com.shine.hotels.ui.SubtitleReceiver;
import com.shine.hotels.util.Utils;
import com.shine.hotels.util.ViewServer;
import com.tvos.common.AudioManager.EnumAtvSystemStandard;
import com.tvos.common.TvManager;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.TvOsType.EnumScalerWindow;
import com.tvos.common.vo.VideoWindowType;

import de.greenrobot.event.EventBus;

public class FullTVActivity extends Activity {
	public static final String INTENT_KEY_DATA = "KEY_DATA";
	public static final String INTENT_KEY_SELECTED = "KEY_SELECTED";

	private ArrayList<String> mDatas;
	private int mCurrentSelected = 0;
	private TextView mNumTv;
	private TextView mVolTv;

	private ProgressDialog dialog;
	private LinearLayout ll;
	private ImageView root_iv_volume;
	private SeekBar root_sb_volume_big;
	private int SILENCE_FLAG = 0;
	int channel_size;
	private static final String TAG = "wsl";

	// private Button hotel_back_finish;

	private SurfaceView surfaceView = null;
	private android.view.SurfaceHolder.Callback callback;
	private Handler handlertv = new Handler();
	// private Handler handlerxml = new Handler();
	int channel_current;//
	private LayoutParams layoutParams;
	ChannelDesk cd = null;//
	TvDeskProvider serviceProvider;
//	private AudioSkin audioSkin = null;

	int time_flag = 0;
	public List<ADbean> list;

	protected boolean alwaysTimeout = false;

	private LinearLayout mProcessLayout;
	private ProgressBar mProgressBar;
	private ProgressBar mLoading;
	private TextView mTotalTv;
	private ImageView mMuteIv;
	private ImageView mMsgIv;

	private int exam_flag = 0;
	HotelsApplication app;

	private static final int VOL_ADDON = 5;

	private SubtitleController mController;
	private volatile boolean mKeyEventForbidden = true;
	
	private static Object lock = new Object();

	String url;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2001:
			    showInfoBox();
				mLoading.setVisibility(View.GONE);
				mKeyEventForbidden = false;
				break;

			default:
				break;
			}
		}
	};

	private int mCurChannel = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fulltv);
		ViewServer.get(this).addWindow(this);
		
		findview();
		
		new InitTask().execute();
		Intent intent = getIntent();
		mDatas = intent.getStringArrayListExtra(INTENT_KEY_DATA);
		url = intent.getStringExtra(INTENT_KEY_SELECTED);
		if (mDatas != null) {
			if (TextUtils.isEmpty(url)) {
				mCurrentSelected = Integer.parseInt(mDatas.get(0));
				mNumTv.setText(String.valueOf(1));
			} else {
				mCurrentSelected = Integer.parseInt(url);
				int size = mDatas.size();
				int currchannel = 0;
				for (int i = 0; i < size; i++) {
					if (mDatas.get(i).equals(url)) {
						currchannel = i;
						mCurChannel = currchannel;
						break;
					}
				}
				mNumTv.setText(String.valueOf(currchannel + 1));
			}
		}

		mSubtitleReceiver = new SubtitleReceiver(myHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastAction.SUBTITLE);
		registerReceiver(mSubtitleReceiver, filter);

		EventBus.getDefault().register(this);

		mController = (SubtitleController) ControllerManager.newController(
				getApplicationContext(), SubtitleController.class);
		Builder builder = new Builder();
		Request request = builder.obtain(Request.Action.GET_SUBTITLE)
				.getResult();
		mController.handle(request);
		Request request2 = builder.obtain(Request.Action.GET_LEFT_TOOLBAR)
				.getResult();
		mController.handle(request2);
	}

	Runnable pip_thread = new Runnable() {
		@Override
		public void run() {
			BackHomeSource();
			Message msg = new Message();
			msg.what = 2001;
			handler.sendMessage(msg);
		}
	};

	private void findview() {
		mNumTv = (TextView) findViewById(R.id.number);
		mVolTv = (TextView) findViewById(R.id.vol);
		ll = (LinearLayout) this.findViewById(R.id.ll_fl);
		mProcessLayout = (LinearLayout) findViewById(R.id.vol_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.vol_progress);
		mLoading = (ProgressBar) findViewById(R.id.loading);
		mMuteIv = (ImageView) findViewById(R.id.vol_icon);
		mMsgIv = (ImageView) findViewById(R.id.message);
		mPopupText = (TextView) findViewById(R.id.pop_up_txt);
		createSurfaceView();
	}

	private class InitTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			init();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

//			handlertv.postDelayed(pip_thread, 300);
			mAudioSkin = new AudioSkin(FullTVActivity.this);
			mAudioSkin.connect(null);
			if (mAudioSkin.GetMuteFlag()) {
				mAudioSkin.setMuteFlag(false);
			}
			mVol = 20;
			mAudioSkin.setVolume(mVol);
			mProgressBar.setProgress(mVol);
		}
	}

	private void init() {
		// stop music
		Intent intent = new Intent();
		intent.setAction(BroadcastAction.MUSIC_STOP_PLAYING);
		sendBroadcast(intent);

//		audioSkin = new AudioSkin(this);
//		audioSkin.connect(null);
//		if (audioSkin.GetMuteFlag()) {
//			audioSkin.setMuteFlag(false);
//		}
		app = (HotelsApplication) getApplication();
		serviceProvider = (TvDeskProvider) app.getTvDeskProvider();
		cd = serviceProvider.getChannelManagerInstance();
		EnumAtvSystemStandard pAtv_SYSTEM_STANDARD = cd.atvGetSoundSystem();
		cd.atvSetForceSoundSystem(EnumAtvSystemStandard.E_DK);
		
		pip_thread.run();
	}

	OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			int audio = root_sb_volume_big.getProgress();
//			audioSkin.setVolume(root_sb_volume_big.getProgress());
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		// sheng yin da xiao
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

		}
	};

	private void setPipscale() {
		try {
			VideoWindowType videoWindowType = new VideoWindowType();
			videoWindowType.x = getResources().getInteger(
					R.integer.videoWindowType_x);
			videoWindowType.y = getResources().getInteger(
					R.integer.videoWindowType_y);
			videoWindowType.width = getResources().getInteger(
					R.integer.videoWindowType_width_K);
			videoWindowType.height = getResources().getInteger(
					R.integer.videoWindowType_height_K);
			TvManager.getPictureManager().selectWindow(
					EnumScalerWindow.E_MAIN_WINDOW);
			TvManager.getPictureManager().setDisplayWindow(videoWindowType);
			TvManager.getPictureManager().scaleWindow();
		} catch (TvCommonException e) {
			e.printStackTrace();
		}
	}

    public void BackHomeSource() {

        synchronized (lock) {
            if (tvService != null) {
                try {

                    ITvServiceServerCommon commonService = tvService.getCommonManager();
                    EN_INPUT_SOURCE_TYPE currentSource = commonService.GetCurrentInputSource();
                    if (currentSource.equals(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_STORAGE) == false) {
                        commonService.SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_STORAGE);
                    }

                    EN_INPUT_SOURCE_TYPE currentSource1 = commonService.GetCurrentInputSource();
                    if (currentSource1.equals(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_STORAGE)) {
                        commonService.SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_ATV);

                        try {
                            VideoWindowType videoWindowType = new VideoWindowType();

                            videoWindowType.x = getResources().getInteger(
                                    R.integer.videoWindowType_x);
                            videoWindowType.y = getResources().getInteger(
                                    R.integer.videoWindowType_y);
                            videoWindowType.width = getResources().getInteger(
                                    R.integer.videoWindowType_width_K);
                            videoWindowType.height = getResources().getInteger(
                                    R.integer.videoWindowType_height_K);
                            TvManager.getPictureManager().selectWindow(
                                    EnumScalerWindow.E_MAIN_WINDOW);
                            TvManager.getPictureManager().setDisplayWindow(videoWindowType);
                        } catch (TvCommonException e) {
                            e.printStackTrace();
                        }
                        cd.atvSetChannel((short)(mCurrentSelected - 1), true);
                    } else {
                        setPipscale();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

	private void createSurfaceView() {
		surfaceView = new SurfaceView(getApplicationContext());
		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		callback = new android.view.SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.v("shine", "===surfaceDestroyed===");
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					Log.v("shine", "===surfaceCreated===");
					TvManager.getPlayerManager().setDisplay(holder);
				} catch (TvCommonException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Log.v("shine", "===surfaceChanged===");
			}

		};

		surfaceView.getHolder().addCallback(
				(android.view.SurfaceHolder.Callback) callback);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				layoutParams.MATCH_PARENT, layoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0);
		ll.addView(surfaceView, params);

	}

	@Override
	protected void onStart() {
		super.onStart();

		// showInfoBox();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	protected void onStop() {
	    if (mSubtitleReceiver != null) {
	        unregisterReceiver(mSubtitleReceiver);
	    }
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// unregisterReceiver(mSubtitleReceiver);

		EventBus.getDefault().unregister(this);

		stopTv();

		ViewServer.get(this).removeWindow(this);
		super.onDestroy();

	}

	private boolean mIsStoping;
	ITvServiceServer tvService = ITvServiceServer.Stub
            .asInterface(ServiceManager.checkService(Context.TV_SERVICE));
	private void stopTv() {
	    if (mIsStoping) return;
	    
	    mIsStoping = true;
	    
		volumeMute();

		new Thread(new Runnable() {
            
            @Override
            public void run() {
                synchronized (lock) {
                    if (tvService != null) {
                        try {
                            ITvServiceServerCommon commonService = tvService.getCommonManager();
                            commonService.SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_DTV);
                            Log.i("shine", "======ITvServiceServerCommon====");
                            
                            mAudioSkin.setMuteFlag(false);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
//                    lock.notifyAll();
                }
                    
            }
        }).start();;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (mKeyEventForbidden)
			return true;
//		Log.d("shine", "keyCode:" + event.getKeyCode() + "|action:" +
//		        event.getAction());

		final int keyCode = event.getKeyCode();
		final int action = event.getAction();

		boolean isHit = false;
		if (action == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_CHANNEL_UP:
			case KeyEvent.KEYCODE_DPAD_UP:
				setChannelUp();
				isHit = true;
				break;
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
			case KeyEvent.KEYCODE_DPAD_DOWN:
				setChannelDown();
				isHit = true;
				break;
			case KeyEvent.KEYCODE_ENTER:
				break;
			case 36:
			case KeyEvent.KEYCODE_DPAD_LEFT:
				setVolumeDown();
				isHit = true;
				break;
			case 35:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				setVolumeUp();
				isHit = true;
				break;
			case KeyEvent.KEYCODE_BACK:
				stopTv();
				break;
			case 37:
				volumeMute();
				break;
			default:
				break;
			}
		}

		if (isHit)
			return true;
		else
			return super.dispatchKeyEvent(event);
	}

	private void setChannelUp() {
		showInfoBox();
		final int size = mDatas.size();
		int currchannel = 0;
		if (mDatas != null) {
//			for (int i = 0; i < size; i++) {
//				if (mDatas.get(i).equals(String.valueOf(mCurrentSelected))) {
//					currchannel = i;
//					break;
//				}
//			}
		    currchannel = mCurChannel;
			currchannel = currchannel + 1;
			if (currchannel < size) {
				mCurrentSelected = Integer.parseInt(mDatas.get(currchannel));
				mCurChannel = currchannel;
				cd.atvSetChannel((short) (mCurrentSelected - 1), true);
				// cd.programSel(mCurrentSelected - 1,
				// MEMBER_SERVICETYPE.E_SERVICETYPE_ATV);
				int str = currchannel + 1;
				mNumTv.setText(String.valueOf(str));
			} else {
				mCurrentSelected = Integer.parseInt(mDatas.get(0));
				mCurChannel = 0;
				cd.atvSetChannel((short) (mCurrentSelected - 1), true);
				// cd.programSel(mCurrentSelected - 1,
				// MEMBER_SERVICETYPE.E_SERVICETYPE_ATV);
				mNumTv.setText(String.valueOf(1));
			}
		}
	}

	private void setChannelDown() {
		showInfoBox();
		int currchannel = 0;
		final int size = mDatas.size();
		if (mDatas != null) {
//			for (int i = 0; i < size; i++) {
//				if (mDatas.get(i).equals(String.valueOf(mCurrentSelected))) {
//					currchannel = i;
//					break;
//				}
//			}
		    currchannel = mCurChannel;
			currchannel = currchannel - 1;
			if (currchannel >= 0) {
				mCurrentSelected = Integer.parseInt(mDatas.get(currchannel));
				mCurChannel = currchannel;
				cd.atvSetChannel((short) (mCurrentSelected - 1), true);
				// cd.programSel(mCurrentSelected - 1,
				// MEMBER_SERVICETYPE.E_SERVICETYPE_ATV);
				int str = currchannel + 1;
				mNumTv.setText(String.valueOf(str));
			} else {
				mCurrentSelected = Integer.parseInt(mDatas.get(size - 1));
				mCurChannel = size - 1;
				cd.atvSetChannel((short) (mCurrentSelected - 1), true);
				/*
				 * cd.programSel(mCurrentSelected - 1,
				 * MEMBER_SERVICETYPE.E_SERVICETYPE_ATV);
				 */
				mNumTv.setText(String.valueOf(size));
			}
		}
	}

	private void setVolumeUp() {
		if (mAudioSkin == null)
			return;

		showInfoBox();

		if (mAudioSkin.GetMuteFlag()) {
			mAudioSkin.setMuteFlag(false);
			mMuteIv.setImageResource(R.drawable.vol_sound);
		}

		mVol += VOL_ADDON;
		mVol = mVol < 100 ? mVol : 100;

		mAudioSkin.setVolume(mVol);

		mVolTv.setText("" + mVol);

		mProgressBar.setProgress(mVol);
	}

	private void setVolumeDown() {
		if (mAudioSkin == null)
			return;

		showInfoBox();

		if (mAudioSkin.GetMuteFlag()) {
			mAudioSkin.setMuteFlag(false);
			mMuteIv.setImageResource(R.drawable.vol_sound);
		}

		mVol -= VOL_ADDON;
		mVol = mVol > 0 ? mVol : 0;

		mAudioSkin.setVolume(mVol);

		mVolTv.setText("" + mVol);

		mProgressBar.setProgress(mVol);
	}

	private void volumeMute() {
		if (mAudioSkin == null)
			return;

		showInfoBox();

		if (mAudioSkin.GetMuteFlag()) {
			mAudioSkin.setMuteFlag(false);
			mMuteIv.setImageResource(R.drawable.vol_sound);
		} else {
			mAudioSkin.setMuteFlag(true);
			mMuteIv.setImageResource(R.drawable.vol_mute);
		}
	}

	private void showInfoBox() {
		if (inSwitching)
			return;

		// Log.v("order", "removeCallbacks:" + mHideInfo.toString());
		mHandler.removeCallbacks(mHideInfo);
		mHandler.postDelayed(mHideInfo, 5 * 1000);
		if (playboxshowing) {
			return;
		}

		inSwitching = true;
		playboxshowing = true;

		Animation animation = null;
		TranslateAnimation animation1 = null;

		animation1 = new TranslateAnimation(0, 0, mProcessLayout.getHeight(), 0);
		animation1.setDuration(500);
		animation1.startNow();

		animation = AnimationUtils.loadAnimation(FullTVActivity.this,
				R.anim.player_show_playbox);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// mPlayBtn.requestFocus();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				inSwitching = false;
				mProgressBar.setProgress(mVol);

				// updateTime();
			}
		});
		mProcessLayout.startAnimation(animation);
		mProcessLayout.setVisibility(View.VISIBLE);
	}

	private void hideInfoBox() {
		if (inSwitching)
			return;

		inSwitching = true;

		Animation animation = null;
		TranslateAnimation animation1 = null;

		animation1 = new TranslateAnimation(0, 0, 0, mProcessLayout.getHeight());
		animation1.setDuration(500);
		animation1.startNow();

		Animation out = AnimationUtils.loadAnimation(FullTVActivity.this,
				android.R.anim.fade_out);
		out.setFillAfter(true);

		animation = AnimationUtils.loadAnimation(FullTVActivity.this,
				R.anim.player_hide_playbox);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mProcessLayout.setVisibility(View.GONE);
				inSwitching = false;
				playboxshowing = false;

			}
		});
		mProcessLayout.startAnimation(animation);
	}

	private AudioSkin mAudioSkin;
	private int mVol;
	private boolean playboxshowing = false;
	private boolean inSwitching = false;// 切换模式期间不允许再次切换（快速切换来不及显示动画）
	private Handler mHandler = new Handler();
	private Runnable mHideInfo = new Runnable() {

		@Override
		public void run() {
			hideInfoBox();
		}
	};

	private Subtitle subtitle;
	private TextView mPopupText;
	private SubtitleReceiver mSubtitleReceiver;
	Handler myHandler = new Handler() {// 在主线程中创建Handler对象
		public void handleMessage(Message msg) {// 处理消息
			switch (msg.what) {
			case HostActivity.SHOW_SUBTEXT:
				Log.d("shine", "handle show subtext");
				showPoptext(subtitle);
				break;
			case HostActivity.HIDE_SUBTEXT:
				hidePoptest();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void showPoptext(Subtitle subtitle) {
		if (mPopupText != null && subtitle != null) {
			mPopupText.setVisibility(View.VISIBLE);
			mPopupText.setText(subtitle.getScrolltextcontent());
			if (!TextUtils.isEmpty(subtitle.getScrolltextbackgourd()))
				mPopupText.setBackgroundColor(Color.parseColor(subtitle
						.getScrolltextbackgourd()));
			mPopupText.setTextColor(Color.parseColor(subtitle
					.getScrolltextcolor()));
			mPopupText.setTextSize(Float.parseFloat(subtitle
					.getScrolltextsize()));

			String coordinate = subtitle.getScrolltextcoordinate();
			int top = Integer.parseInt(coordinate.split(",")[0]);
			int left = Integer.parseInt(coordinate.split(",")[1]);

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPopupText
					.getLayoutParams();
			params.setMargins(left, 0, 0, top);// 通过自定义坐标来放置你的控件
			mPopupText.setLayoutParams(params);
		}
	}

	private void hidePoptest() {
		mPopupText.setVisibility(View.INVISIBLE);
	}

	public void onEvent(SubtitleEvent event) {
		this.subtitle = event.result;

		if (null != subtitle) {
			// Log.i("shine", "subtitle onEvent " + subtitle.toString());

			AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			// 开始播放滚动字幕设置
			Intent intent = new Intent();
			intent.setAction(BroadcastAction.SUBTITLE);
			intent.putExtra(HostActivity.SHOW_SUBTEXT_KEY, true);
			int requestCode = 0;
			PendingIntent pendIntent = PendingIntent.getBroadcast(
					getApplicationContext(), requestCode, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Calendar c = Calendar.getInstance();
			// 比当前时间小（超过了当前时间立即执行）
			long triggerAtTime = c.getTimeInMillis() + 2 * 1000;

			// 比当前时间大（延迟执行）
			if (Utils.compareTo(new Date(), subtitle.getScrolltextstarttime()) < 0) {
				Date show = Utils.parser(subtitle.getScrolltextstarttime());
				triggerAtTime = show.getTime();
			}

			alarmMgr.set(AlarmManager.RTC, triggerAtTime, pendIntent);

			// 暂停播放滚动字幕设置
			Intent intent2 = new Intent();
			intent2.setAction(BroadcastAction.SUBTITLE);
			intent2.putExtra(HostActivity.SHOW_SUBTEXT_KEY, false);
			PendingIntent pendIntent2 = PendingIntent.getBroadcast(
					getApplicationContext(), 1, intent2,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Date show = Utils.parser(subtitle.getScrolltextendtime());
			long time = show.getTime();
			alarmMgr.set(AlarmManager.RTC, time, pendIntent2);
		} else {
			mPopupText.setVisibility(View.INVISIBLE);
		}
	}

	public void onEvent(GetToolDataEvent event) {
		Log.d("tv", "onEvent GetToolDataEvent...");
		ToolBarData toolbar = event.data;

		if (toolbar != null && toolbar.getMsgNum() > 0) {
			mMsgIv.setVisibility(View.VISIBLE);
		} else {
			mMsgIv.setVisibility(View.GONE);
		}

	}
}
