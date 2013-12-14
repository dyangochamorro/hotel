package com.shine.hotels.ui.appreciatetv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.shine.hotel.service.ChannelDesk;
import cn.com.shine.hotel.service.TvDeskProvider;
import cn.com.shine.hotel.tv.FullTVActivity;
import cn.com.shine.hotel.tv.HotelTVActivity;
import cn.com.shine.hotel.tv.TVChannel;

import com.mstar.tv.service.aidl.EN_INPUT_SOURCE_TYPE;
import com.mstar.tv.service.aidl.EN_MEMBER_SERVICE_TYPE;
import com.mstar.tv.service.interfaces.ITvServiceServer;
import com.mstar.tv.service.interfaces.ITvServiceServerCommon;
import com.mstar.tv.service.skin.AudioSkin;
import com.shine.hotels.R;
import com.shine.hotels.controller.AppreciatetvController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetTvEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Appreciatetv;
import com.shine.hotels.io.model.AppreciatetvList;
import com.shine.hotels.io.model.CategoryData;
import com.shine.hotels.ui.BaseFragment;
import com.squareup.picasso.Picasso;
import com.tvos.atv.AtvScanManager.EnumAtvManualTuneMode;
import com.tvos.common.TvManager;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.TvOsType.EnumScalerWindow;
import com.tvos.common.vo.VideoWindowType;

import de.greenrobot.event.EventBus;

/**
 * 电视欣赏
 * @author guoliang
 *
 */
public class FragmentAppreciatetv extends BaseFragment implements OnItemSelectedListener,
        OnItemClickListener, SurfaceHolder.Callback/*, OnPlayerEventListener*/ {
    
    AppreciatetvController mController ;
    
    AppreciatetvList aList ;
    
//    private int mSelectedPos;
    private ListView mListView;
    private Gallery mGallery;
    private ListViewAdapter mListViewAdapter;
    private GalleryAdapter mGalleryAdapter;
    private int mGalleySelectedPosition = 0;
    
    private Handler mHandler = new Handler();
    ChannelDesk cd = null;//
    TvDeskProvider serviceProvider;
    private AudioSkin audioSkin = null;
    ITvServiceServer mTvService;
    ITvServiceServerCommon commonService;
    private List<TVChannel> list2;
    private TVChannel channel;
    
    private ProgressBar mProgressBar;
    private View mListViewLayout;
    private View mGalleryLayout;
    private View mListCover;
    private View mTvLeft;
    private View mTvRight;
    
    private int mListSelected;
    private List<String> mFrequencys = new ArrayList<String>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tv_1, container, false) ;
        mListViewLayout = v.findViewById(R.id.list_layout);
        mGalleryLayout = v.findViewById(R.id.tv_gallery_layout);
        mListCover = v.findViewById(R.id.list_cover);
        mTvLeft = v.findViewById(R.id.tv_left);
        mTvRight = v.findViewById(R.id.tv_right);
        
        mListView = (ListView)v.findViewById(R.id.tv_list);
        mListViewAdapter = new ListViewAdapter(getActivity());
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemSelectedListener(this);
        mListView.setOnItemClickListener(this);

        mGallery = (Gallery)v.findViewById(R.id.tv_gallery);
        mGalleryAdapter = new GalleryAdapter(getActivity());
        mGallery.setAdapter(mGalleryAdapter);
        
        mProgressBar = (ProgressBar)v.findViewById(R.id.loading);
        
        setViewsVisiable(View.GONE);
        
        init();
        
        return v;
        
    }
    
    private void setViewsVisiable(int visibility) {
        mGallery.setVisibility(visibility);
        mListViewLayout.setVisibility(visibility);
        mGalleryLayout.setVisibility(visibility);
        mTvLeft.setVisibility(visibility);
        mTvRight.setVisibility(visibility);
        mListCover.setVisibility(visibility);
    }
    
    @Override
    public void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
    @Override
    public boolean onKeyLeft() {
        if (mGallery == null) return false;
        
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT);
        mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, event);
        
        return false;
    }
    
    @Override
    public boolean onKeyRight() {
        if (mGallery == null) return false;
        
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
        mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, event);
        
        return false;
    }
    
    @Override
    public boolean onKeyUp() {
        mListView.requestFocus();
        return false;
    };
    
    @Override
    public boolean onKeyDown() {
        mListView.requestFocus();
        return false;
    }
    
    @Override
    public boolean onKeyCenter() {
//        Log.d("tv", "onKeyCenter selected=" + mGallery.getSelectedItemPosition());
        CategoryData data = (CategoryData)mGallery.getSelectedItem();
        if (data != null) {
            Intent intent = new Intent(getActivity(), FullTVActivity.class);
            String url = data.getCategoryurl();
            
            intent.putStringArrayListExtra(FullTVActivity.INTENT_KEY_DATA, (ArrayList<String>)mFrequencys);
            intent.putExtra(FullTVActivity.INTENT_KEY_SELECTED, url);
            getActivity().startActivity(intent);
            
            return true;
        }
        return false;
    }

    private void init() {
        EventBus.getDefault().register(this);

        mController = (AppreciatetvController)ControllerManager.newController(getActivity(),
                AppreciatetvController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_APPRECIATE_TV).getResult();
        mController.handle(request);

    }
    
    private void switchChannelTV(int frequey){
//        Log.d("shine", "tv  frequey=" + frequey);
//        for (int i = 0; i < list2.size(); i++) {
//            int intfrequey=list2.get(i).getChannelfrequey();
//            if (intfrequey==frequey) {
//                Log.d("shine", "frequey=" + frequey);
//                 cd.programSel(i, MEMBER_SERVICETYPE.E_SERVICETYPE_ATV);
//            }
//        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }
    
    public void onEvent(GetTvEvent<AppreciatetvList> event) {
        aList = event.result;
        
        // 处理List返回内容
        if (aList == null) return;
        
        // init frequency
        List<Appreciatetv> list = aList.getLists();
        List<CategoryData> datas = list.get(list.size() - 1).getData();
        mFrequencys.clear();
        for (CategoryData data : datas) {
            mFrequencys.add(data.getCategoryurl());
        }
        
        setViewsVisiable(View.VISIBLE);
        
        mProgressBar.setVisibility(View.GONE);
        
        mListViewAdapter.updateData(aList.getLists());
        Appreciatetv tv = (Appreciatetv)mListViewAdapter.getItem(0);
        if (tv != null) {
            mGalleryAdapter.updateData(tv.getData());
            mGallery.setSelection(Integer.MAX_VALUE / 2 - 1) ;
        }
        
        mListView.requestFocus();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mListView) {
        } else {
            final int pos = position % mGalleryAdapter.getList().size();
            CategoryData data = mGalleryAdapter.getItem(pos);
            final int channel =Integer.valueOf(data.getCategoryurl().trim());
            Intent intent = new Intent(getActivity(), HotelTVActivity.class);
            getActivity().startActivity(intent);
        }

    }

    // TODO
    //int[] channels = {58437, 77937, 110500, 112937, 58437, 77937, 110500, 58437, 77937, 110500};
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mListView) {
//            Log.d("shine", "list onItemSelected pos=" + position);
            Appreciatetv tv = (Appreciatetv)mListViewAdapter.getItem(position);
            if (tv != null) {
                mGallery.removeAllViewsInLayout();
                mGalleryAdapter.updateData(tv.getData());
                mGalleySelectedPosition = Integer.MAX_VALUE / 2 - 1;
                mGallery.setSelection(mGalleySelectedPosition);
                
                mListSelected = position;
                mListViewAdapter.notifyDataSetChanged();
            }
            
        } else {
            CategoryData data = mGalleryAdapter.getItem(position);
            mGalleySelectedPosition = position;
            mGalleryAdapter.notifyDataSetChanged();

        }
        
    }
    
    private void changeChannel(int frequency) {
        Log.d("shine", "change channel :" + frequency);
        audioSkin.setMuteFlag(true);
        changeByFrequency(frequency);
        audioSkin.setMuteFlag(false);
    }
    
    private void initChannel() {
        
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        parent.setSelection(0);
        
    }
    
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v("shine", "===surfaceChanged===");
    }
    
    private void setPipscale() {

        try {

            VideoWindowType videoWindowType = new VideoWindowType();
            videoWindowType.x = getResources().getDimensionPixelSize(R.dimen.tv_x);
            videoWindowType.y = getResources().getDimensionPixelSize(R.dimen.tv_y);
            videoWindowType.width = getResources().getDimensionPixelSize(R.dimen.tv_width);
            videoWindowType.height = getResources().getDimensionPixelSize(R.dimen.tv_height);
            Log.d("shine", "x=" + videoWindowType.x);
            Log.d("shine", "y=" + videoWindowType.y);
            Log.d("shine", "w=" + videoWindowType.width);
            Log.d("shine", "h=" + videoWindowType.height);

            TvManager.getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
            TvManager.getPictureManager().setDisplayWindow(videoWindowType);
            TvManager.getPictureManager().scaleWindow();

        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }
    
    public void BackHomeSource() {
        ITvServiceServer tvService = ITvServiceServer.Stub
                .asInterface(ServiceManager.checkService(Context.TV_SERVICE));
        if (tvService == null) {
            // Log.w(TAG, "Unable to find ITvService interface.");
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
                        videoWindowType.x = getResources().getDimensionPixelSize(R.dimen.tv_x);
                        videoWindowType.y = getResources().getDimensionPixelSize(R.dimen.tv_y);
                        videoWindowType.width = getResources().getDimensionPixelSize(R.dimen.tv_width);
                        videoWindowType.height = getResources().getDimensionPixelSize(R.dimen.tv_height);
                        Log.d("shine", "x=" + videoWindowType.x);
                        Log.d("shine", "y=" + videoWindowType.y);
                        Log.d("shine", "w=" + videoWindowType.width);
                        Log.d("shine", "h=" + videoWindowType.height);
                        TvManager.getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
                        TvManager.getPictureManager().setDisplayWindow(videoWindowType);

                    } catch (TvCommonException e) {
                        e.printStackTrace();

                    }
                    int channel = mTvService.getChannelManager().getCurrentChannelNumber();
                    Log.w("shine", "channel=" + channel);
                    if ((channel < 0) || (channel > 255)) {
                        channel = 0;
                    }
                    mTvService.getChannelManager().programSel(channel,
                            EN_MEMBER_SERVICE_TYPE.E_SERVICETYPE_ATV);
                } else {
                    setPipscale();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    
 // 换台
    short s=0;
    private int changeByFrequency(int targetFrequency) {

        cd.setChannelChangeFreezeMode(false);
        cd.atvSetManualTuningStart(1000, targetFrequency,
                EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_UP);
        cd.setChannelChangeFreezeMode(true);
        cd.saveAtvProgram(0);
        cd.atvSetManualTuningEnd();

        int nCurrentFrequency = cd.atvGetCurrentFrequency();
        if (targetFrequency != nCurrentFrequency) {
            Log.d("shine", "nCurrentFrequency: " + nCurrentFrequency);
            changeByFrequency(targetFrequency);
        }

        return nCurrentFrequency;
    }
    
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		if (surfaceView!=null) {
//			wm.removeViewImmediate(surfaceView);
//		}
	}
    
    class GalleryAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context context ;
        private List<CategoryData> mCategoryList;
        
        public GalleryAdapter(Context context) {
            this.context = context ;
            mInflater = LayoutInflater.from(context);
        }
        
        public void updateData(List<CategoryData> list) {
            mCategoryList = list;
            
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mCategoryList != null) {
                return Integer.MAX_VALUE;
//                return mCategoryList.size();
            }
            return 0;
        }

        @Override
        public CategoryData getItem(int position) {
            if (mCategoryList == null) return null;
            
            int pos = position % mCategoryList.size();
            if (pos < mCategoryList.size() && pos > -1) {
                return mCategoryList.get(pos);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mCategoryList != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LinearLayout layout = (LinearLayout)mInflater.inflate(R.layout.tv_gallery_item, null);
                convertView = layout;
            }
            
            GridItemHold hold = (GridItemHold)convertView.getTag();
            if (hold == null) {
                hold = new GridItemHold();
                hold.img = (ImageView)convertView.findViewById(R.id.tv_gallery_item);
                hold.name = (TextView)convertView.findViewById(R.id.tv_gallery_txt);
                
                convertView.setTag(hold);
            }

            final int pos = position % mCategoryList.size();
            CategoryData data = mCategoryList.get(pos);
            hold.name.setText(data.getCategoryname());
            
            String url = data.getCategorypic();
            hold.position = pos;
            Picasso.with(context).load(url).placeholder(R.drawable.tv_default).into(hold.img);
            
            return convertView;
        }
        
        public List<CategoryData> getList() {
            if (mCategoryList == null) {
                return new ArrayList<CategoryData>();
            } else {
                return mCategoryList;
            }
        }
        
    }
    
    class ListViewAdapter extends BaseAdapter {
        private List<Appreciatetv> mData;
        private Context mContext;
        private LayoutInflater mInflater;
        
        public ListViewAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }
        
        public void updateData(List<Appreciatetv> data) {
            if (data == null) return;
            
            mData = data;
            notifyDataSetChanged();
//            Log.d("shine", "list updatedata!!");
            mListView.setSelected(true);
            mListView.setSelection(0);
            mListView.setItemChecked(0, true);
        }

        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size() - 1;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mData != null) {
                return mData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mData != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tv_list_item, null);
            }
            
            Appreciatetv tv = (Appreciatetv)getItem(position);
            TextView text = (TextView)convertView.findViewById(R.id.tv_list_item_txt);
            text.setText(tv.getCategorytypename());
            if (mListSelected == position) {
                text.setBackgroundResource(R.drawable.tv_btn_down);
            } else {
                text.setBackgroundResource(R.drawable.tv_btn_up);
            }
            
            return convertView;
        }
        
    }
    
    private static class GridItemHold {
        int position;
        ImageView img;
        TextView name;
    }
    
    private class ChangeChannleTask implements Runnable {
        private int mChannel;
        
         public ChangeChannleTask(int channel) {
            mChannel = channel;
        }

        @Override
        public void run() {
            if (mGalleryAdapter == null || mGalleySelectedPosition >= mGalleryAdapter.getCount()) return;
            // FIXME
            String s = mGalleryAdapter.getItem(mGalleySelectedPosition % mGalleryAdapter.getList().size()).getCategoryurl().trim();
            final int curr = Integer.valueOf(s);
//            int curr = channels[mGalleySelectedPosition];
            if (mChannel != curr) return;
            
            Log.v("shine", "ChangeChannleTask curr=" + mChannel);
            switchChannelTV(mChannel);
        }
        
    }
    
}