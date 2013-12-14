
package com.shine.hotels.ui.appreciatemusic;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mstar.tv.service.skin.AudioSkin;
import com.shine.hotels.HotelsApplication;
import com.shine.hotels.R;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.AppreciatemusicController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.BackgroundMusicEvent;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Appreciatemusic;
import com.shine.hotels.io.model.AppreciatemusicList;
import com.shine.hotels.io.model.Appreciatetv;
import com.shine.hotels.io.model.MusicData;
import com.shine.hotels.service.MusicService;
import com.shine.hotels.service.MusicService.MusicBinder;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.HostActivity;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 音乐欣赏
 * 
 * @author guoliang
 */
public class FragmentAppreciatemusic extends BaseFragment implements OnItemSelectedListener, OnItemClickListener {

    AppreciatemusicController mController;

    AppreciatemusicList aList;

    private Gallery mGallery;
    private GalleryAdapter mGalleryAdapter;
    private ListView mListView;
    private ListAdapter mListAdapter;
    private ImageView mPoster;
    private TextView musicTitle ;
    
    private MusicService mService;
    
    private int mGalleySelectedPosition = 0;
    private int mListSelected;
    private MusicPlayReceiver mPlayReceiver;
    
    private AudioSkin mAudioSkin;
    private ProgressBar mProgressBar;
    private ImageView mVolIconIv;
    private int mVol;
    private static final int VOL_ADDON = 5;
    
    @Override
    public boolean onKeyLeft() {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT);
        mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, event);
        
        return false;
    }
    
    @Override
    public boolean onKeyRight() {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
        mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, event);
        
        return false;
    }
    
    @Override
    public boolean onKeyUp() {
        mListView.requestFocus();
        return true;
    }
    
    @Override
    public boolean onKeyDown() {
        mListView.requestFocus();
        return true;
    }
    
    @Override
    public boolean onKeyVolumeUp() {
        volumeUp();
        return true;
    }
    
    @Override
    public boolean onKeyVolumeDown() {
        volumeDown();
        return true;
    }
    
    @Override
    public boolean onKeyVolumeMute() {
        volumeMute();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        HostActivity activity = (HostActivity)getActivity();
        mService = activity.getMusicService();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.AUDIO_PLAYBACKGROUND);
        mPlayReceiver = new MusicPlayReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mPlayReceiver, filter);
        
        mAudioSkin = new AudioSkin(getActivity());
        mAudioSkin.connect(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_music, container, false);
        mGallery = (Gallery)v.findViewById(R.id.music_category);
        mGallery.setOnItemSelectedListener(this);
        mGalleryAdapter = new GalleryAdapter(getActivity());
        mGallery.setAdapter(mGalleryAdapter);
        mGallery.requestFocus();
        
        musicTitle = (TextView)v.findViewById(R.id.music_titel) ;
        
        mListView = (ListView)v.findViewById(R.id.music_list);
        mListView.setOnItemClickListener(this);
        mListAdapter = new ListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);
        
        mListView.setDividerHeight(0) ;
        
        mPoster = (ImageView)v.findViewById(R.id.music_cover);
        
        mProgressBar = (ProgressBar)v.findViewById(R.id.vol_progress);
        mProgressBar.setProgress(mAudioSkin.getVolume());
        
        mVolIconIv = (ImageView)v.findViewById(R.id.vol_icon);

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        mController = (AppreciatemusicController)ControllerManager.newController(getActivity(),
                AppreciatemusicController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_APPRECIATE_MUSIC).getResult();
        mController.handle(request);
        
        int vol = HotelsApplication.sMusicVol;
        mVol = vol > 0 ? vol : 20;
        mAudioSkin.setVolume(mVol);
        mProgressBar.setProgress(mVol);
        if (mAudioSkin.GetMuteFlag() && mService != null && mService.isPlaying()) {
            mVolIconIv.setImageResource(R.drawable.vol_mute);
        } else {
            mVolIconIv.setImageResource(R.drawable.vol_sound);
        }
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        HotelsApplication.sMusicVol = mVol;
        
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mGallery && mGalleryAdapter.getList() != null && mGalleryAdapter.getList().size() > 0) {
            int pos = position % mGalleryAdapter.getList().size();
//            Log.d("shine", "gallery on selected pos=" + pos);
            Appreciatemusic music = (Appreciatemusic)mGalleryAdapter.getItem(pos);
            musicTitle.setText(music.getMusicspecialname()) ;
            Picasso.with(getActivity()).load(music.getSpecialposter()).placeholder(R.drawable.music_pic).into(mPoster);
            List<MusicData> list = music.getData();
            mListAdapter.updateContent(list);
            mListView.setSelection(0);
            mListView.requestFocus();

        } else if (parent == mListView) {
        }

    }
    

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mGallery) {
//            Appreciatemusic music = (Appreciatemusic)mGalleryAdapter.getItem(position);
//            musicTitle.setText(music.getMusicspecialname()) ;
//            Picasso.with(getActivity()).load(music.getSpecialposter()).placeholder(R.drawable.music_default).into(mPoster);
//            List<MusicData> list = music.getData();
//            mListAdapter.updateContent(list);
            
        } else if (parent == mListView) {
            MusicData data = (MusicData)mListAdapter.getItem(position);

            Builder builder = new Builder();
            Request request = builder.obtain(Request.Action.PLAY_APPRECIATE_MUSIC).
                    putObject(data).getResult();
            if (mController != null)
                mController.handle(request);
            
            if (mService != null) mService.playList(mListAdapter.getMusicList(), position);
        }
        
    }
    
    private void volumeUp() {
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
            mVolIconIv.setImageResource(R.drawable.vol_sound);
        }

        mVol += VOL_ADDON;
        mVol = mVol < 100 ? mVol : 100;

        mAudioSkin.setVolume(mVol);

        mProgressBar.setProgress(mVol);
    }
    
    private void volumeDown() {
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
            mVolIconIv.setImageResource(R.drawable.vol_sound);
        }

        mVol -= VOL_ADDON;
        mVol = mVol > 0 ? mVol : 0;
        
        mAudioSkin.setVolume(mVol);
        
        mProgressBar.setProgress(mVol);
        
    }
    
    private void volumeMute() {
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
            mVolIconIv.setImageResource(R.drawable.vol_sound);
        } else {
            mAudioSkin.setMuteFlag(true);
            mVolIconIv.setImageResource(R.drawable.vol_mute);
        }
    }
    
    public void onEvent(GetResultEvent<AppreciatemusicList> event) {
        aList = event.result;
        
        if (aList == null) return;

        // 处理List返回内容
        mGalleryAdapter.updateContent(aList.getLists());
        mGallery.setSelection(Integer.MAX_VALUE / 2 - 1) ;
    }
    
    public class MusicPlayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.AUDIO_PLAYBACKGROUND)) {
                int pos = intent.getIntExtra(CenterManager.CENTER_BROADCAST_MUSIC_POSITION, 0);
                
                Log.d("order", "onReceive pos=" + pos);
                mListView.setSelection(pos);
            }
        }
        
    }

    private static class GalleryAdapter extends BaseAdapter {
        private List<Appreciatemusic> mList;

        private Context mContext;
        private LayoutInflater mInflater;

        public GalleryAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void updateContent(List<Appreciatemusic> list) {
            if (list == null)
                return;

            mList = list;

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mList != null) {
                return Integer.MAX_VALUE;
//                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList != null && mList.size() > 0) {
                return mList.get(position % mList.size());
//                return mList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mList != null && mList.size() > 0) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LinearLayout layout = (LinearLayout)mInflater.inflate(R.layout.music_grid_item, null);
                convertView = layout;
            }
            
            ImageView img = (ImageView)convertView.findViewById(R.id.music_img);
            if (mList != null && mList.size() > 0) {
                Appreciatemusic music = mList.get(position % mList.size());
                Picasso.with(mContext).load(music.getMusictypepic())
                        .placeholder(R.drawable.music_default).into(img);
            }
            
            return convertView;
        }
        
        public List<Appreciatemusic> getList() {
            if (mList == null) {
                return new ArrayList<Appreciatemusic>();
            }
            return mList;
        }

    }

    private static class ListAdapter extends BaseAdapter {
        private List<MusicData> mList;

        //private Context mContext;
        private LayoutInflater mInflater;

        public ListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void updateContent(List<MusicData> list) {
            if (list == null)
                return;

            mList = list;

            notifyDataSetChanged();
        }
        
        public List<MusicData> getMusicList() {
            return mList;
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
                TextView tv = (TextView)mInflater.inflate(R.layout.music_list_item, null);

                convertView = tv;
            }

            MusicData data = mList.get(position);
            String name  = data.getMusicname();
//            Log.v("shine", "music name=" + name);
            ((TextView)convertView).setText(name);
            return convertView;
        }

    }

}
