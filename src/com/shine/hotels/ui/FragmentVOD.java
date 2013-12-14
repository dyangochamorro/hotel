
package com.shine.hotels.ui;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.controller.MenuController;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.io.model.Menu;
import com.shine.hotels.io.model.MenuList;
import com.shine.hotels.ui.appreciatemovie.FragmentAppreciatemovie;
import com.shine.hotels.ui.appreciatemusic.FragmentAppreciatemusic;
import com.shine.hotels.ui.widget.CoverFlow;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

public class FragmentVOD extends BaseFragment implements OnItemClickListener {

//    private HomepageAdapter adapter;
    private GridAdapter adapter;
    private FragmentTransaction transaction;
	private MenuController mController ;

    // 定义数组，即图片资源
    private String[] mImageUrls ;

    // 定义数组，即文字资源
    private String[] txts ;

	List<Menu> menus ;
	
	CoverFlow gridView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View v = inflater.inflate(R.layout.fragment_vod, container, false) ;
        
        adapter = new GridAdapter(getActivity(), mImageUrls, txts);
        gridView = (CoverFlow)v.findViewById(R.id.guidebook_gridview);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.requestFocus();
        
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mController = (MenuController)ControllerManager.newController(getActivity(), MenuController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_NAVIGATION_MENU).putStringParam("tag", "DBFW").getResult();
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
        
    	menus = result.getLists() ;
    	
    	if (null!=menus && menus.size()>0) {
    		mImageUrls = new String[menus.size()] ;
    		txts = new String[menus.size()] ;
    		
    		for (int i=0;i<menus.size();i++) {
    			Menu menu = menus.get(i) ;
    			mImageUrls[i] = menu.getMenupic() ;
    			txts[i] = menu.getMenuname() ;
    		}
    	}
    	
    	adapter.update(mImageUrls, txts) ;
    	
    	adapter.notifyDataSetChanged() ;
    	gridView.setSelection(0);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = position % mImageUrls.length;
        
        if (null!=menus && menus.size()>0 && null!=menus.get(pos)) {
        	String tag = menus.get(pos).getTag() ;
        	if ("DYXS".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment movie = new FragmentAppreciatemovie();
                transaction.replace(R.id.bottom_main_layout, movie, UIConfig.FRAGMENT_TAG_MOVIE_INDEX);
                transaction.addToBackStack(UIConfig.FRAGMENT_TAG_MOVIE_INDEX);
                transaction.commit();
        	} else if ("YYXS".equalsIgnoreCase(tag)) {
        		transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment music = new FragmentAppreciatemusic();
                transaction.replace(R.id.bottom_main_layout, music, UIConfig.FRAGMENT_TAG_MUSIC_INDEX);
                transaction.addToBackStack(UIConfig.FRAGMENT_TAG_MUSIC_INDEX);
                transaction.commit();
        	}
        }
    }
    
    public boolean onKeyLeft() {
        return false;
    }

    public boolean onKeyRight() {
        return false;
    }
    
    public static class GridAdapter extends BaseAdapter {
        private Context mContext;
        // 定义数组，即图片资源
        private String[] mImageUrls ;
        // 定义数组，即文字资源
        private String[] txts ;
        
        public GridAdapter(Context c, String[] mImageUrls, String[] txts) {
            this.mContext = c;
            this.mImageUrls = mImageUrls ;
            this.txts = txts ;
        }

        @Override
        public int getCount() {
            if (txts != null) {
                return txts.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (txts != null) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.cover_flow_item, null);

                Holder holder = new Holder();
                holder.image = (ImageView)convertView.findViewById(R.id.cover_flow_image);
                holder.text = (TextView)convertView.findViewById(R.id.cover_flow_text);

                convertView.setTag(holder);
            }
            
            if (null == mImageUrls || mImageUrls.length <= 0)
                return convertView;

            Holder holder = (Holder)convertView.getTag();
            String url = mImageUrls[position];

            Picasso.with(mContext).load(url).placeholder(R.drawable.icon_introduct).into(holder.image);

            holder.text.setText(txts[position]);

            return convertView;
        }
        
        public void update(String[] mImageUrls, String[] txts) {
            this.mImageUrls = mImageUrls ;
            this.txts = txts ;
        }
        
        class Holder {
            ImageView image;
            TextView text;
        }
        
    }

}
