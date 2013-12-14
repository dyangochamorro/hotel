
package cn.com.shine.hotel.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.shine.hotel.bean.ProgramBean;
import cn.com.shine.hotel.bean.ProgramsListBean;
import cn.com.shine.hotel.util.BitmapUtil;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramListAdapter extends BaseAdapter {

    // private ArrayList<ProgramsListBean> list;

    private Context context;

    private List<ProgramBean> list;

    public ProgramListAdapter() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ProgramListAdapter(List<ProgramBean> list, Context context) {
        super();
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private int select = 0;

    public void notifyDataSetChanged(int all) {
        select = all;
        super.notifyDataSetChanged();
    }
    
    public void setSelectedPosition(int pos) {
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ImageView in = new ImageView(context);
        if (select == position) {
            // ProgramsListBean pro=list.get(position%list.size());
            ProgramBean pro = list.get(position % list.size());
            in.setImageBitmap(BitmapUtil.createReflectedImage(BitmapUtil.createTxtImage(
                    pro.getChannel_name(), 32)));
            // in.setImageBitmap(BitmapUtil.createReflectedImage(BitmapUtil.createTxtImage(pro.getProgramname(),
            // 32)));

        } else {
            // ProgramsListBean pro=list.get(position%list.size());
            // in.setImageBitmap(BitmapUtil.createTxtImage(pro.getProgramname(),
            // 24));
            ProgramBean pro = list.get(position % list.size());
            in.setImageBitmap(BitmapUtil.createTxtImage(pro.getChannel_name(), 24));
        }

        return in;

    }

}
