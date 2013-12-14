package cn.com.shine.hotel.tv;

import cn.com.shine.hotel.inter.MstarBaseInterface;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MstarBaseActivity extends Activity implements MstarBaseInterface {

	private int delayMillis = 3000;

	private int delayMessage = 88888888;

	protected boolean alwaysTimeout = false;

	private Handler timerHander = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == delayMessage) {
				onTimeOut();
			}

		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		if (timerHander.hasMessages(delayMessage)) {
			timerHander.removeMessages(delayMessage);
			timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timerHander.removeMessages(delayMessage);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onTimeOut() {
		// TODO Auto-generated method stub
		if (alwaysTimeout)
			timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
	}

}
