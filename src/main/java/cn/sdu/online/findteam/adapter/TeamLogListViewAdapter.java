package cn.sdu.online.findteam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.mob.TeamLogListViewItem;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.view.RoundImageView;

/**
 * Created by wn on 2015/7/23.
 */
public class TeamLogListViewAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<TeamLogListViewItem> listViewItems;

    public TeamLogListViewAdapter(Context context, List<TeamLogListViewItem> listViewItems) {
        layoutInflater = LayoutInflater.from(context);
        this.listViewItems = listViewItems;
    }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.teamlog_list_item, null);

            viewHolder.headbmp = (ImageView) convertView.findViewById(R.id.teamlog_headbmp);
            viewHolder.name = (TextView) convertView.findViewById(R.id.teamlog_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.teamlog_time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.teamlog_content);
            viewHolder.view = convertView.findViewById(R.id.teamlog_spacing_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.headbmp.setImageResource(R.drawable.head_moren);
        loadBitmap(viewHolder.headbmp, listViewItems.get(position).imgPath);
        viewHolder.name.setText(listViewItems.get(position).name);
        viewHolder.time.setText(listViewItems.get(position).time);
        viewHolder.content.setText(listViewItems.get(position).content);

        return convertView;
    }

    private void loadBitmap(final ImageView imageView, String imgPath) {
        ImageLoader imageLoader = new ImageLoader(MyApplication.getQueues(), MyApplication.bitmapCache);
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imageView, R.drawable.head_moren, R.drawable.head_moren);
        imageLoader.get(imgPath, imageListener);
    }

    public class ViewHolder {
        public ImageView headbmp;
        public TextView name;
        public TextView time;
        public TextView content;
        public View view;
    }
}
