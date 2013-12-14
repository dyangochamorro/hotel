
package com.shine.hotels.ui.hotelintroduction;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Events.SubHotelShowEvent;
import com.shine.hotels.controller.HotelintroductionController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.HotelintroductionData;
import com.shine.hotels.io.model.HotelintroductionMenu;
import com.shine.hotels.io.model.HotelintroductionMenuList;
import com.shine.hotels.ui.AlwaysMarqueeTextView;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.UIConfig;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 酒店介绍
 * 
 * @author guoliang
 */
public class FragmentHotelintroduction extends BaseFragment implements OnItemSelectedListener, OnItemClickListener {

    HotelintroductionController mController;
    private FragmentTransaction transaction ;

    HotelintroductionMenuList menuList;
    private ListView mListView;
    private GridView mGridView;

    private IntroMenuAdapter mMenuAdapter;
    private IntroListAdapter mListAdapter;
    private Gallery mGallery;
    
    private int menuSelectedPosition ;
    private int listSelectedPosition ;
    
    private RelativeLayout pinpaiRelativeLayout ;
    
    private int selectedBackgroud = 3 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_hotel_intro, null);
        
        pinpaiRelativeLayout = (RelativeLayout)v.findViewById(R.id.rl_pinpai) ;
        
        mListView = (ListView)v.findViewById(R.id.intro_listview);
        mListView.setOnItemClickListener(this);
        mGridView = (GridView)v.findViewById(R.id.intro_gridview);
        mGridView.setOnItemClickListener(this);
        
        mGallery = (Gallery)v.findViewById(R.id.intro_category);
        mMenuAdapter = new IntroMenuAdapter(getActivity());
        mGallery.setAdapter(mMenuAdapter);
        mGallery.setOnItemSelectedListener(this);
        mGallery.setEnabled(false);
        mGallery.setFocusable(false);
        
        mListAdapter = new IntroListAdapter(getActivity());

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        mController = (HotelintroductionController)ControllerManager.newController(getActivity(),
                HotelintroductionController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_HOTELINTRODUCTION_MENU).getResult();
        mController.handle(request);
        
        mListView.requestFocus() ;
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onBackToFont() {
        mListView.requestFocus();
    }

    public void onEvent(GetResultEvent<HotelintroductionMenuList> event) {
        menuList = event.result;

        if (null!=menuList && menuList.getLists().size()>0) {
        	List<HotelintroductionMenu> list = menuList.getLists() ;
        	// 处理List返回内容
            mMenuAdapter.updateData(list);
            
            if (list.size()>2) {
            	mGallery.setSelection(2) ;
            	
            	menuSelectedPosition = 2;
                HotelintroductionMenu menu = list.get(2);
                if (menu == null)
                    return;

                if (menu.getType() == HotelintroductionMenu.TYPE_LIST) {
                    mGridView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mListView.setAdapter(mListAdapter);
                    mListAdapter.updateContent(menu.getData());
                    
                    mListView.requestFocus() ;
                } else {
                    mListView.setVisibility(View.GONE);
                    mGridView.setVisibility(View.VISIBLE);
                    mGridView.setAdapter(mListAdapter);
                    mListAdapter.updateContent(menu.getData());
                    
                    mGridView.requestFocus() ;
                }
            }
        }
        
    }

    private static class IntroMenuAdapter extends BaseAdapter {
        List<HotelintroductionMenu> mList;

        Context mContext;

        public IntroMenuAdapter(Context context) {
            mContext = context;
        }

        public void updateData(List<HotelintroductionMenu> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mList != null) {
                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList != null) {
                return mList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mList != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.hotel_intro_gallery_item, null);
                
                Holder holder = new Holder();
                holder.text = (AlwaysMarqueeTextView)convertView.findViewById(R.id.intro_item_text);
                holder.image = (ImageView)convertView.findViewById(R.id.intro_item_img);
                
                convertView.setTag(holder);
            }

            Holder holder = (Holder)convertView.getTag();
            HotelintroductionMenu menu = mList.get(position);
            if (menu != null) {
                holder.text.setText(menu.getSortingname());
                Picasso.with(mContext).load(menu.getSortingpic()).placeholder(R.drawable.youhui).into(holder.image);
            }

            return convertView;
        }
        
        class Holder {
            AlwaysMarqueeTextView text;
            ImageView image;
        }

    }

    private static class IntroListAdapter extends BaseAdapter {
        Context mContext;

        List<HotelintroductionData> mDatas;

        public IntroListAdapter(Context context) {
            mContext = context;
        }

        public void updateContent(List<HotelintroductionData> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mDatas != null) {
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null) {
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mDatas != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.hotel_intro_list_item, null);
                
                Holder holder = new Holder();
                holder.image = (ImageView)convertView.findViewById(R.id.intro_list_item_img);
                
                convertView.setTag(holder);
            }

            Holder holder = (Holder)convertView.getTag();
            HotelintroductionData data = mDatas.get(position);

            Picasso.with(mContext).load(data.getDataContent()).placeholder(R.drawable.hotel_intro_default).into(holder.image);
            
            return convertView;
        }
        
        class Holder {
            ImageView image;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	menuSelectedPosition = position;
        HotelintroductionMenu menu = menuList.getLists().get(position);
        if (menu == null)
            return;

        if (menu.getType() == HotelintroductionMenu.TYPE_LIST) {
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mListView.setAdapter(mListAdapter);
            mListAdapter.updateContent(menu.getData());
            
            mListView.requestFocus() ;
        } else {
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(mListAdapter);
            mListAdapter.updateContent(menu.getData());
            
            mGridView.requestFocus() ;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	listSelectedPosition = position ;
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment room = new FragmentHotelintroductionShow();
        transaction.add(R.id.bottom_main_layout, room, UIConfig.FRAGMENT_TAG_HOTEL_INTRO_SHOW);
        transaction.hide(this);
        transaction.addToBackStack(UIConfig.FRAGMENT_TAG_HOTEL_INTRO_SHOW);
        transaction.commit();
        
        HotelintroductionData data = (HotelintroductionData)mListAdapter.getItem(position);
        
        
        SubHotelShowEvent event = new SubHotelShowEvent();
        event.data = data;
        EventBus.getDefault().postSticky(event);
        
        if (parent == mListView) {
            
        } else if (parent == mGridView) {
            
        }
        
    }
    
    @Override
    public boolean onKeyLeft() {

    	int selectedItem = mGallery.getSelectedItemPosition() ;
//    	Log.i("shine", "onKeyLeft selectedItem = " + selectedItem);
    	// 改变中间ListView值
		if (menuSelectedPosition>0) {
			menuSelectedPosition-- ;
		} else {
			return false ;
		}
		
	    HotelintroductionMenu menu = menuList.getLists().get(menuSelectedPosition);
	    if (menu == null)
	        return false ;
	
	    if (menu.getType() == HotelintroductionMenu.TYPE_LIST) {
	        mGridView.setVisibility(View.GONE);
	        mListView.setVisibility(View.VISIBLE);
	        mListView.setAdapter(mListAdapter);
	        mListAdapter.updateContent(menu.getData());
	        
	        mListView.requestFocus() ;
	    } else {
	        mListView.setVisibility(View.GONE);
	        mGridView.setVisibility(View.VISIBLE);
	        mGridView.setAdapter(mListAdapter);
	        mListAdapter.updateContent(menu.getData());
	        
	        mGridView.requestFocus() ;
	    }
    	
    	if (selectedItem<3 || selectedBackgroud>3) {
        	selectedBackgroud-- ;
        	if (selectedBackgroud<1)
        		selectedBackgroud = 1 ;
        	switch (selectedBackgroud) {
    			case 1:
    	    		pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg1) ;
    				break;
    			case 2:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg2) ;
    				break;
    			case 3:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg3) ;
    				break;
    			case 4:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg4) ;
    				break;
    			case 5:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg5) ;
    				break;
    	
    			default:
    				break;
    		}
    		return false ;
    	} else {
    		mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT,null); 
	    	return true ;
    	}
    	
    	
    }
    
    @Override
    public boolean onKeyRight() {
    	
    	int listSize = mMenuAdapter.getCount() ;
    	int selectedItem = mGallery.getSelectedItemPosition() ;
//    	Log.i("shine", "onKeyRight selectedItem = " + selectedItem);
		if ((listSize-1)>menuSelectedPosition) {
			menuSelectedPosition++ ;
		} else {
			return false ;
		}
		// 改变中间ListView值
        HotelintroductionMenu menu = menuList.getLists().get(menuSelectedPosition);
        if (menu == null)
            return false ;

        if (menu.getType() == HotelintroductionMenu.TYPE_LIST) {
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mListView.setAdapter(mListAdapter);
            mListAdapter.updateContent(menu.getData());
            
            mListView.requestFocus() ;
        } else {
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(mListAdapter);
            mListAdapter.updateContent(menu.getData());
            
            mGridView.requestFocus() ;
        }
    	
    	if ((listSize-selectedItem)<=3 || selectedBackgroud<3) {
    		// 顶到头的情况
        	selectedBackgroud++ ;
        	if (selectedBackgroud>5)
        		selectedBackgroud = 5 ;
        	
    		switch (selectedBackgroud) {
    			case 1:
    	    		pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg1) ;
    				break;
    			case 2:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg2) ;
    				break;
    			case 3:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg3) ;
    				break;
    			case 4:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg4) ;
    				break;
    			case 5:
    				pinpaiRelativeLayout.setBackgroundResource(R.drawable.jdjs_bg5) ;
    				break;

    			default:
    				break;
    		}
    		return false ;
    	} else {
    		mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT,null); 
    		
        	return true ;
    	}
    }
    
}
