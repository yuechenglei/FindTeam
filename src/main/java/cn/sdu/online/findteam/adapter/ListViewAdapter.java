package cn.sdu.online.findteam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.sdu.online.findteam.R;

public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    private List<HashMap<String, String>> mlist;

    public ListViewAdapter(Context context, List<HashMap<String, String>> list) {
        mContext = context;
        mlist = list;
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO 自动生成的方法存根
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.textView.setText(mlist.get(position).get("name"));
        return convertView;
    }

    public class Holder {
        TextView textView;
    }
}
