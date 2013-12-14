package com.shine.hotels.ui.home;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.MenuController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Menu;
import com.shine.hotels.io.model.MenuList;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.FragmentVOD;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.ui.appreciatetv.FragmentAppreciatetv;
import com.shine.hotels.ui.guidebook.FragmentGuidebook;
import com.shine.hotels.ui.hotelintroduction.FragmentHotelintroduction;
import com.shine.hotels.ui.roomservice.FragmentRoom;
import com.shine.hotels.ui.widget.CoverFlow;

import de.greenrobot.event.EventBus;

/**
 * 
 * 主页
 */
public class FragmentHome extends BaseFragment implements OnItemClickListener {

	private HomepageAdapter adapter;

	private FragmentTransaction transaction;

	private MenuController mController;

	// 定义数组，即图片资源
	private String[] mImageUrls;

	// 定义数组，即文字资源
	private String[] txts;
	// private String[] txts = {"客房服务", "旅行指南", "酒店介绍", "电视欣赏", "点播服务"} ;

	List<Menu> menus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_homepage, container, false);

		adapter = new HomepageAdapter(getActivity(), mImageUrls, txts);

		// 获取Gallery对象
		CoverFlow cover = (CoverFlow) v.findViewById(R.id.homepage_coverFlow);
		// 添加ImageAdapter给Gallery对象
		cover.setAdapter(adapter);
		cover.setSelection(Integer.MAX_VALUE / 2 - 1);
		cover.setOnItemClickListener(this);

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mController = (MenuController) ControllerManager.newController(
				getActivity(), MenuController.class);
		Builder builder = new Builder();
		Request request = builder.obtain(Request.Action.GET_NAVIGATION_MENU)
				.putStringParam("tag", "SY").getResult();
		mController.handle(request);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();

		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);

		super.onStop();
	}

	public void onEvent(GetResultEvent<MenuList> event) {
		MenuList result = event.result;

		if (result == null)
			return;

		menus = result.getLists();

		if (null != menus && menus.size() > 0) {
			mImageUrls = new String[menus.size()];
			txts = new String[menus.size()];

			for (int i = 0; i < menus.size(); i++) {
				Menu menu = menus.get(i);
				mImageUrls[i] = menu.getMenupic();
				txts[i] = menu.getMenuname();
			}
		}

		adapter.update(mImageUrls, txts);

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mImageUrls == null || mImageUrls.length == 0)
			return;

		int pos = position % mImageUrls.length;
		Log.d("shine", "home click:" + pos);
		if (null != menus && menus.size() > 0 && null != menus.get(pos)) {
			String tag = menus.get(pos).getTag();
			if ("KFFW".equalsIgnoreCase(tag)) {
				transaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				Fragment room = new FragmentRoom();
				transaction.replace(R.id.bottom_main_layout, room,
						UIConfig.FRAGMENT_TAG_ROOM_SERVICE);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_ROOM_SERVICE);
				transaction.commit();
			} else if ("LXZN".equalsIgnoreCase(tag)) {
				transaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				Fragment guidbook = new FragmentGuidebook();
				transaction.replace(R.id.bottom_main_layout, guidbook,
						UIConfig.FRAGMENT_TAG_GUIDEBOOK);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_GUIDEBOOK);
				transaction.commit();
			} else if ("JDJS".equalsIgnoreCase(tag)) {
				transaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				Fragment hotel = new FragmentHotelintroduction();
				transaction.replace(R.id.bottom_main_layout, hotel,
						UIConfig.FRAGMENT_TAG_HOTEL_INTRO);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_HOTEL_INTRO);
				transaction.commit();
			} else if ("DSXS".equalsIgnoreCase(tag)) {
				transaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				Fragment tv = new FragmentAppreciatetv();
				transaction.replace(R.id.bottom_main_layout, tv,
						UIConfig.FRAGMENT_TAG_TV);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_TV);
				transaction.commit();
				// Intent intent = new Intent(getActivity(),
				// HotelTVActivity.class);
				// getActivity().startActivity(intent);
			} else if ("DBFW".equalsIgnoreCase(tag)) {
				transaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				Fragment vod = new FragmentVOD();
				transaction.replace(R.id.bottom_main_layout, vod,
						UIConfig.FRAGMENT_TAG_VOD);
				transaction.addToBackStack(UIConfig.FRAGMENT_TAG_VOD);
				transaction.commit();
			} else if ("SWFW".equalsIgnoreCase(tag)) {
				runAppBrowser();
			}
		}
	}

	private void runAppBrowser() {
		ComponentName componentName = new ComponentName("com.android.browser",
				"com.android.browser.BrowserActivity");
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(componentName);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		this.startActivity(intent);
	}

	@Override
	public boolean onKeyBack() {
		Activity host = getActivity();
		if (host != null) {
			host.finish();
		}

		return false;
	}

}
