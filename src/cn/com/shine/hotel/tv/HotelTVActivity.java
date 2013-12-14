package cn.com.shine.hotel.tv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.com.shine.hotel.adapter.ProgramListAdapter;
import cn.com.shine.hotel.bean.ADbean;
import cn.com.shine.hotel.bean.ProgramBean;
import cn.com.shine.hotel.parse.ADSAXParseService;
import cn.com.shine.hotel.parse.SAXParseService;
import cn.com.shine.hotel.service.ChannelDesk;
import cn.com.shine.hotel.service.DataBaseDesk.EN_MS_VIDEOITEM;
import cn.com.shine.hotel.service.TvDeskProvider;
import cn.com.shine.hotel.util.Constant;
import cn.com.shine.hotel.util.CustomGallery;

import com.mstar.tv.service.aidl.EN_INPUT_SOURCE_TYPE;
import com.mstar.tv.service.aidl.EN_MEMBER_SERVICE_TYPE;
import com.mstar.tv.service.interfaces.ITvServiceServer;
import com.mstar.tv.service.interfaces.ITvServiceServerCommon;
import com.mstar.tv.service.skin.AudioSkin;
import com.shine.hotels.HotelsApplication;
import com.shine.hotels.R;
import com.tvos.atv.AtvScanManager.EnumAtvManualTuneMode;
import com.tvos.common.TvManager;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.TvOsType.EnumScalerWindow;
import com.tvos.common.vo.VideoWindowType;

public class HotelTVActivity extends Activity implements OnClickListener,
		OnItemSelectedListener, OnItemClickListener {
	
	private LinearLayout linearLayout;
	private TextView tView;
	//2向上切台
		private Button root_bt_shang;
		//3向下切台
		private Button root_bt_xia;
		//4频道切换显示
		private TextView root_tv_chanel;
		//5静音，或有音
		private ImageView root_iv_volume;
		//6声音调大或调小
		private SeekBar root_sb_volume_big;
		
		//9广告的切换
//		private ViewFlipper root_iv_switchpicture;
		
		private int SILENCE_FLAG=0;
		
		int channel_size;
	private static final String TAG = "shine";

	private Button hotel_back_finish;

	private SurfaceView surfaceView = null;
	private android.view.SurfaceHolder.Callback callback;
	private LinearLayout linear_layout_root;
	private Handler handlertv = new Handler();
	private DisplayMetrics outMetrics;
	/*
	 * private int x = 50; private int y = 50; private int width = 800; private
	 * int height = 600;
	 */
	int channel_current;// 当前选中频道
//	private CustomGallery glllery;
	ChannelDesk cd = null;//
	TvDeskProvider serviceProvider;

	// protected TvDeskProvider tvManagerProvider;
	// TVRootApp rootapp = null;
	private AudioSkin audioSkin = null;

	private Button bt;
	int time_flag=0;
	public List<ADbean> list;
	private List<ProgramBean> listprogram;
	private ProgramListAdapter adapter;

	private int delayMillis = 6000;
	private int delayMessage = 88888888;
	protected boolean alwaysTimeout = false;
	private Handler timerHander = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == delayMessage) {
				onTimeOut();
			} else if (msg.what == 201) {
				if (listprogram != null) {
					adapter = new ProgramListAdapter(listprogram,
							HotelTVActivity.this);
//					glllery.setAdapter(adapter);
//					glllery.setSelection(Integer.MAX_VALUE / 2);
					channel_size=listprogram.size();
					int channel_num=(Integer.MAX_VALUE/2)%channel_size+1;
					
					root_tv_chanel.setText(""+channel_num);
					
				} else {

				}
			}else if (msg.what==109) {
				Log.i(TAG, "知悉了================1·=");
				tView.setVisibility(View.INVISIBLE);
			}else if (msg.what==111) {
				serviceProvider.getPictureManagerInstance().ExecVideoItem(
						EN_MS_VIDEOITEM.MS_VIDEOITEM_CONTRAST, (short
								)50);
				serviceProvider.getPictureManagerInstance().ExecVideoItem(
						EN_MS_VIDEOITEM.MS_VIDEOITEM_BRIGHTNESS, (short
								)50);

			}else if (msg.what==103) {
				for (int i = 0; i < list.size(); i++) {
					ImageView iv=new ImageView(HotelTVActivity.this);
					iv.setImageBitmap(BitmapFactory.decodeFile(Constant.PATH_SD+list.get(i).getAddress()));
				}
				Message msgM=new Message();
				msgM.what=104;
				timerHander.sendMessageDelayed(msgM, 3000);
		
			}else if (msg.what==104) {
				Message ms=new Message();
				ms.what=104;
				time_flag++;
				String str=list.get(time_flag%list.size()).getTime().toString();
				int ii=Integer.parseInt(str);
				timerHander.sendMessageDelayed(ms, ii);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roottv);
		findview();
		init();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// getListProgramInfo();
//				listprogram = getPictureList_TV(Constant.TV_XML);
//
//				Message msg = new Message();
//				msg.what = 201;
//				timerHander.sendMessage(msg);
//			}
//		}).start();
//		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				list = getPictureList_AD(Constant.AD_XML);
//				Message msg = new Message();
//				msg.what = 103;
//				timerHander.sendMessage(msg);
//			}
//		}).start();
	}

	private void findview() {
		
		
	//	surfaceView = (SurfaceView) this.findViewById(R.id.surfview_tv);
//		glllery = (CustomGallery) this.findViewById(R.id.audiotv_cg_volume);

		
//		hotel_back_finish = (Button) this.findViewById(R.id.roottv_bt_back);

//		root_bt_shang=(Button) this.findViewById(R.id.roottv_bt_shangplay);
//		root_bt_xia=(Button) this.findViewById(R.id.roottv_bt_xiaplay);
//		root_tv_chanel=(TextView) this.findViewById(R.id.roottv_tv_pindaoinfo);
//		root_iv_volume=(ImageView) this.findViewById(R.id.roottv_iv_volume);
//		root_sb_volume_big=(SeekBar) this.findViewById(R.id.roottv_sb_bigvlume);
		
		//root_bt_play=(Button) this.findViewById(R.id.roottv_bt_play);
		
		linearLayout=(LinearLayout) this.findViewById(R.id.ll_fl);
		linearLayout.setOnClickListener(this);
	}

	private void init() {
		
		audioSkin = new AudioSkin(this);
		audioSkin.connect(null);
		if (audioSkin.GetMuteFlag()) {
			audioSkin.setMuteFlag(false);
		}
		HotelsApplication app = (HotelsApplication) getApplication();
		serviceProvider = (TvDeskProvider) app.getTvDeskProvider();
		cd = serviceProvider.getChannelManagerInstance();
		handlertv.postAtTime(pip_thread, 300);
//		glllery.setOnItemSelectedListener(this);
//		glllery.setOnItemClickListener(this);
		hotel_back_finish.setOnClickListener(this);
		
		root_bt_shang.setOnClickListener(this);
		root_bt_xia.setOnClickListener(this);
		
	
		root_iv_volume.setOnClickListener(this);
		root_sb_volume_big.setOnSeekBarChangeListener(listener);
		if (audioSkin!=null) {
			root_sb_volume_big.setProgress(audioSkin.getVolume());
		}
	}
	
	OnSeekBarChangeListener listener=new OnSeekBarChangeListener() {
		
		
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			int audio=root_sb_volume_big.getProgress();
			if (audio==0) {
				SILENCE_FLAG=1;
				root_iv_volume.setBackgroundResource(R.drawable.no_sound);
			}else {
				SILENCE_FLAG=0;
				root_iv_volume.setBackgroundResource(R.drawable.sound);
			}
			audioSkin.setVolume(root_sb_volume_big.getProgress());
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		//sheng yin da xiao
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
		//	int audio=audioinfo_seekbar_audio.getProgress();
			//audioinfo_tv_audio.setText(String.valueOf(arg1));
			
		}
	};
	
	
	//==================
	private List<ADbean> getPictureList_AD(String name) {
		try {
			list = getPictureByParseXml_AD(getInputStream(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public InputStream getInputStream(String xml) {
		InputStream is = null;
		File file = new File(Constant.PATH_SD+xml);
		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	public List<ADbean> getPictureByParseXml_AD(InputStream is)
			throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ADSAXParseService hander = new ADSAXParseService();
		parser.parse(is, hander);
		return hander.getList();
	}
	
	
	
	//===================
	
	private List<ProgramBean> getPictureList_TV(String name) {
		System.out.println("==路径名字：============" + name + "================");

		try {
			listprogram = getPictureByParseXml_TV(getInputStream(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listprogram;
	}

	public List<ProgramBean> getPictureByParseXml_TV(InputStream is)
			throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		SAXParseService hander = new SAXParseService();
		parser.parse(is, hander);
		return hander.getList();
	}
	//==============================================

	Runnable pip_thread = new Runnable() {
		@Override
		public void run() {
		    Log.e("shine", "pip thread run");
			createSurfaceView();
			BackHomeSource();
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
					R.integer.videoWindowType_width);

			videoWindowType.height = getResources().getInteger(
					R.integer.videoWindowType_height);

			TvManager.getPictureManager().selectWindow(
					EnumScalerWindow.E_MAIN_WINDOW);

			TvManager.getPictureManager().setDisplayWindow(videoWindowType);

			TvManager.getPictureManager().scaleWindow();

		} catch (TvCommonException e) {
			e.printStackTrace();
		}
	}

	public void BackHomeSource() {
		
	    Log.w(TAG, "BackHomeSource...");
		ITvServiceServer tvService = ITvServiceServer.Stub
				.asInterface(ServiceManager.checkService(Context.TV_SERVICE));
		if (tvService == null) {
			Log.e (TAG, "Unable to find ITvService interface.");
		} else {
			try {
			    ITvServiceServerCommon commonService = tvService
                        .getCommonManager();
                EN_INPUT_SOURCE_TYPE currentSource = commonService
                        .GetCurrentInputSource();
                if (currentSource
                        .equals(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_STORAGE) == false) {
                    commonService
                            .SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_STORAGE);
                }

                EN_INPUT_SOURCE_TYPE currentSource1 = commonService
                        .GetCurrentInputSource();
                
                if (currentSource1
                        .equals(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_STORAGE)) {
                    commonService.SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_ATV);

					try {

						VideoWindowType videoWindowType = new VideoWindowType();

						videoWindowType.x = getResources().getInteger(
								R.integer.videoWindowType_x);

						videoWindowType.y = getResources().getInteger(
								R.integer.videoWindowType_y);

						videoWindowType.width = getResources().getInteger(
								R.integer.videoWindowType_width);

						videoWindowType.height = getResources().getInteger(
								R.integer.videoWindowType_height);

						TvManager.getPictureManager().selectWindow(
								EnumScalerWindow.E_MAIN_WINDOW);

						TvManager.getPictureManager().setDisplayWindow(
								videoWindowType);

					} catch (TvCommonException e) {
						e.printStackTrace();

					}
//					int channel = tvService.getChannelManager()
//							.getCurrentChannelNumber();
					int channel = 5;
					Log.w("shine", "channel=" + channel);
					if ((channel < 0) || (channel > 255)) {
						channel = 0;
					}
					tvService.getChannelManager().programSel(channel,
							EN_MEMBER_SERVICE_TYPE.E_SERVICETYPE_ATV);
				} else {
					setPipscale();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
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
				Log.v(TAG, "===surfaceDestroyed===");
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					Log.v(TAG, "===surfaceCreated===");
					TvManager.getPlayerManager().setDisplay(holder);
				} catch (TvCommonException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Log.v(TAG, "===surfaceChanged===");
			}

		};

		surfaceView.getHolder().addCallback(
				(android.view.SurfaceHolder.Callback) callback);

		
		 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1440, 850); 
		 params.setMargins(0,0, 0, 0); 
		 linearLayout.addView(surfaceView, params);
		 
	}

	// 过三秒执行此方法
	public void onTimeOut() {
		// TODO Auto-generated method stub
//		glllery.setVisibility(View.GONE);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		timerHander.removeMessages(delayMessage);
		timerHander.removeMessages(104);
		if (surfaceView != null) {
			surfaceView.getHolder().removeCallback(
					(android.view.SurfaceHolder.Callback) callback);
			surfaceView = null;
		}
		super.onPause();

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		audioSkin.setMuteFlag(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (timerHander.hasMessages(delayMessage)) {
				timerHander.removeMessages(delayMessage);
				timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (timerHander.hasMessages(delayMessage)) {
				timerHander.removeMessages(delayMessage);
				timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0.getId()) {
		case R.id.ll_fl:
//			glllery.setVisibility(View.VISIBLE);
			timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
			break;
//		case R.id.roottv_bt_back:
//                ITvServiceServer tvService = ITvServiceServer.Stub.asInterface(ServiceManager
//                        .checkService(Context.TV_SERVICE));
//                if (tvService != null) {
//                    try {
//                        ITvServiceServerCommon commonService = tvService.getCommonManager();
//                        commonService.SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_DTV);
//                        System.out.println("======ITvServiceServerCommon====");
//                    } catch (RemoteException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//                finish();
//		case R.id.roottv_iv_volume:
//			if (SILENCE_FLAG==0) {
//				SILENCE_FLAG=1;
//				root_iv_volume.setBackgroundResource(R.drawable.no_sound);
//				audioSkin.setMuteFlag(true);
//			}else if (SILENCE_FLAG==1) {
//				SILENCE_FLAG=0;
//				root_iv_volume.setBackgroundResource(R.drawable.sound);
//				audioSkin.setMuteFlag(false);
//			}
//			break;
//		case R.id.roottv_bt_shangplay:
//			//cd.programUp();
//			int str=Integer.parseInt(root_tv_chanel.getText().toString());
//			str=str+1;
//			if (str>channel_size) {
//				root_tv_chanel.setText(""+1);
//				String one=listprogram.get(0).getChannel_frequee();
//				int oneint=Integer.parseInt(one);
//				audioSkin.setMuteFlag(true);
//				ll.removeView(surfaceView);
//				changeByFrequency(oneint);
//				audioSkin.setMuteFlag(false);
//				Message ms=new Message();
//				ms.what=111;
//				timerHander.sendMessageDelayed(ms, 700);
//				ll.addView(surfaceView);
//			}else {
//				root_tv_chanel.setText(""+str);
//				String one=listprogram.get(str-1).getChannel_frequee();
//				int oneint=Integer.parseInt(one);
//				audioSkin.setMuteFlag(true);
//				ll.removeView(surfaceView);
//				changeByFrequency(oneint);
//				audioSkin.setMuteFlag(false);
//				Message ms=new Message();
//				ms.what=111;
//				timerHander.sendMessageDelayed(ms, 700);
//				ll.addView(surfaceView);
//			}
//			
//			break;
//	case R.id.roottv_bt_xiaplay:
//		int str1=Integer.parseInt(root_tv_chanel.getText().toString());
//		str1=str1-1;
//		if (str1<=0) {
//			root_tv_chanel.setText(""+channel_size);
//			String one=listprogram.get(channel_size-1).getChannel_frequee();
//			int oneint=Integer.parseInt(one);
//			audioSkin.setMuteFlag(true);
//			ll.removeView(surfaceView);
//			changeByFrequency(oneint);
//			audioSkin.setMuteFlag(false);
//			Message ms=new Message();
//			ms.what=111;
//			timerHander.sendMessageDelayed(ms, 700);
//			ll.addView(surfaceView);
//		}else {
//			root_tv_chanel.setText(""+str1);
//			String one=listprogram.get(str1-1).getChannel_frequee();
//			int oneint=Integer.parseInt(one);
//			audioSkin.setMuteFlag(true);
//			ll.removeView(surfaceView);
//			changeByFrequency(oneint);
//			audioSkin.setMuteFlag(false);
//			Message ms=new Message();
//			ms.what=111;
//			timerHander.sendMessageDelayed(ms, 700);
//			ll.addView(surfaceView);
//		}
//		
//		break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		int size = listprogram.size();
		String str = listprogram.get(arg2 % size).getChannel_frequee();
		int frequee = Integer.parseInt(str);
		audioSkin.setMuteFlag(true);
		changeByFrequency(frequee);
		Message ms=new Message();
		ms.what=111;
		timerHander.sendMessageDelayed(ms, 700);
	audioSkin.setMuteFlag(false);
	}
	// 换台
	short s=0;
	private int changeByFrequency(int targetFrequency) {
//		serviceProvider.getPictureManagerInstance().ExecVideoItem(
//				EN_MS_VIDEOITEM.MS_VIDEOITEM_CONTRAST, s);
//		serviceProvider.getPictureManagerInstance().ExecVideoItem(
//				EN_MS_VIDEOITEM.MS_VIDEOITEM_BRIGHTNESS, s);

		cd.setChannelChangeFreezeMode(false);
		cd.atvSetManualTuningStart(1000, targetFrequency,
				EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_UP);
//		serviceProvider.getPictureManagerInstance().ExecVideoItem(
//						EN_MS_VIDEOITEM.MS_VIDEOITEM_CONTRAST, s);
//				serviceProvider.getPictureManagerInstance().ExecVideoItem(
//						EN_MS_VIDEOITEM.MS_VIDEOITEM_BRIGHTNESS, s);
		cd.setChannelChangeFreezeMode(true);
		cd.saveAtvProgram(0);
		cd.atvSetManualTuningEnd();
//		serviceProvider.getPictureManagerInstance().ExecVideoItem(
//				EN_MS_VIDEOITEM.MS_VIDEOITEM_CONTRAST, s);
//		serviceProvider.getPictureManagerInstance().ExecVideoItem(
//				EN_MS_VIDEOITEM.MS_VIDEOITEM_BRIGHTNESS, s);

		int nCurrentFrequency = cd.atvGetCurrentFrequency();
		if (targetFrequency != nCurrentFrequency) {
			System.out.println("nCurrentFrequency: " + nCurrentFrequency);
			changeByFrequency(targetFrequency);
		}
		
		return nCurrentFrequency;
	}

}
