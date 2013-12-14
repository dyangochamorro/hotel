
package com.shine.hotels.ui.appreciatemovie;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.AppreciatemovieController;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.AppreciatemovieIdEvent;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Appreciatemovie;
import com.shine.hotels.io.model.AppreciatemovieList;
import com.shine.hotels.io.model.MovieData;
import com.shine.hotels.ui.BaseFragment;
import com.shine.hotels.ui.UIConfig;
import com.shine.hotels.ui.widget.MovieGridView;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * 电影欣赏-菜单加载
 * 
 * @author guoliang
 */
public class FragmentAppreciatemovie extends BaseFragment implements OnItemSelectedListener,
    OnItemClickListener {

    AppreciatemovieController mController;

    AppreciatemovieList movieList;

    private ListAdapter mListAdapter;
    private ListView mListView;
    private GridAdapter mGridAdapter;
    private MovieGridView mGridView;
    
    private int mGridViewSelected;
    private int mListViewSelected;
    
    private boolean mIsFirst = true;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie, container, false);

        mListView = (ListView)v.findViewById(R.id.movie_list);
        mListAdapter = new ListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemSelectedListener(this);

        mGridAdapter = new GridAdapter(getActivity());
        mGridView = (MovieGridView)v.findViewById(R.id.movie_grid);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemSelectedListener(this);
        mGridView.setOnItemClickListener(this);
        
        return v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        EventBus.getDefault().register(this);
        
        mController = (AppreciatemovieController)ControllerManager.newController(getActivity(),
                AppreciatemovieController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_APPRECIATE_MOVIE).getResult();
        mController.handle(request);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        
        super.onStop();
    }

    public void onBackToFont() {
        if (mIsFirst) {
            mIsFirst = false;
        } else {
            mListAdapter.notifyDataSetChanged();
            
            mGridView.requestFocus();
            mGridView.setSelection(mGridViewSelected);
            
        }
    }

    public void onEvent(GetResultEvent<AppreciatemovieList> event) {
        movieList = event.result;
        
        if (movieList == null) return;

        // 处理List返回内容
        mListAdapter.updateContent(movieList.getLists());
//        mListView.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mListView) {
            List<MovieData> list = (List<MovieData>)movieList.getLists().get(position).getData();
            
            updateGrid(list);
            
        } else {
            mGridViewSelected = position;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mListView) {
        } else {

            MovieData data = (MovieData)mGridAdapter.getItem(position);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            Fragment room = null;
            final int type = data.getMovietype();
            if (type == 2) {
                room = new FragmentAppreciatemovieShow2();
            } else {
                room = new FragmentAppreciatemovieShow();
            }
            transaction.add(R.id.bottom_main_layout, room, UIConfig.FRAGMENT_TAG_MOVIE_SHOW);
            transaction.addToBackStack(UIConfig.FRAGMENT_TAG_MOVIE_SHOW);
            transaction.commit();

            AppreciatemovieIdEvent event = new AppreciatemovieIdEvent();
            event.ids = data.getMovieIds();
            EventBus.getDefault().postSticky(event);
        }

    }
    
 /*   @Override
    public boolean onKeyDown() {
        if (mListView.isFocused()) {
//            int next = mListViewSelected;
//            if (mListViewSelected != mListAdapter.getCount() - 1) {
//                next = mListViewSelected + 1;
//            }
//            mListView.setSelection(next);
//            mListViewSelected = next;
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN);
            mListView.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, event);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onKeyUp() {
        if (mListView.isFocused()) {
//            int next = mListViewSelected;
//            if (mListViewSelected != 0) {
//                next = mListViewSelected - 1;
//            }
//            mListView.setSelection(next);
//            mListViewSelected = next;
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP);
            mListView.onKeyDown(KeyEvent.KEYCODE_DPAD_UP, event);
            return true;
        }
        return false;
    }*/
    
    private void updateGrid(List<MovieData> datas) {
        mGridAdapter.updateContent(datas);
    }

    private static class ListAdapter extends BaseAdapter {
        List<Appreciatemovie> mList;

        private Context mContext;
        private LayoutInflater mInflater;

        public ListAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void updateContent(List<Appreciatemovie> list) {
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
                View v = mInflater.inflate(R.layout.movie_list_item, null);

                convertView = v;
            }
            
            TextView tv = (TextView)convertView.findViewById(R.id.item_text);

            Appreciatemovie item = mList.get(position);
            if (item != null) {
                String text = item.getMovietypename();
                tv.setText(text);
            }
            
            return convertView;
        }

    }

    private class GridAdapter extends BaseAdapter {
        private static final int MODE = 3;
        
        private List<MovieData> mList;
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        
        public GridAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            
        }
        
        public void updateContent(List<MovieData> datas) {
          mList = datas;
          notifyDataSetChanged();
          mGridView.clearFocus();
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
                LinearLayout layout = (LinearLayout)mLayoutInflater.inflate(
                        R.layout.movie_grid_item, null);

                GridItemHold hold = new GridItemHold();
                hold.img = (ImageView)layout.findViewById(R.id.movie_grid_item_img);
                hold.img.setDrawingCacheEnabled(true);
                hold.name = (TextView)layout.findViewById(R.id.movie_grid_item_txt);
                layout.setTag(hold);

                convertView = layout;
            }

            MovieData data = mList.get(position);
            if (data != null) {
                GridItemHold hold = (GridItemHold)convertView.getTag();
                hold.name.setText(data.getMoviename());
                Picasso.with(mContext).load(data.getMoviepic())
                        .placeholder(R.drawable.movie_item_default).into(hold.img);
                hold.position = position;
            }

            return convertView;
        }

//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            GridItemHold holder = (GridItemHold)v.getTag();
//            int position = holder.position;
//            
//            Log.d("shine", "position=" + position + " " + hasFocus);
//            
//            if (hasFocus) {
//                v.startAnimation(mScaleUp);
//            } else {
//                v.startAnimation(mScaleDown);
//            }
//        }

    }
    
    private static class GridItemHold {
        int position;
        
        ImageView img;

        TextView name;
    }


}
