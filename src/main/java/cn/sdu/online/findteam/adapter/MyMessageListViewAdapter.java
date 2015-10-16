package cn.sdu.online.findteam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;
import java.util.Map;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.mob.ChatListItem;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.view.BadgeView;

public class MyMessageListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<ChatListItem> data;

    public MyMessageListViewAdapter(Context context, List<ChatListItem> data) {
        //根据context上下文加载布局，这里的是Demo17Activity本身，即this
        this.data = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //How many items are in the data set represented by this Adapter.
        //在此适配器中所代表的数据集中的条目数
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        //获取数据集中与指定索引对应的数据项
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.friend_icon_img);
            holder.title = (TextView) convertView.findViewById(R.id.friend_name_tv);
            holder.info = (TextView) convertView.findViewById(R.id.friend_info_tv);

            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageResource(R.drawable.head_moren);
        holder.title.setText(data.get(position).name);
        holder.info.setText(data.get(position).info);
        String imgPath = data.get(position).imgPath;
        ImageLoader.ImageListener imageListener = MyApplication.imageLoader.getImageListener(
                holder.img, R.drawable.head_moren, R.drawable.head_moren
        );
        MyApplication.imageLoader.get(imgPath, imageListener);

        return convertView;
    }

    public class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView info;
    }
}
