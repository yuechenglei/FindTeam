package cn.sdu.online.findteam.adapter;

import android.content.Context;
import android.graphics.Typeface;
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
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item, null);
            holder.type = (TextView) convertView.findViewById(R.id.item_type_tv);
            holder.title = (TextView) convertView.findViewById(R.id.item_title_tv);
            holder.content = (TextView)convertView.findViewById(R.id.item_content_tv);
            holder.view = convertView.findViewById(R.id.spacing_view);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.type.setText("热门赛事");
        holder.title.setText("优衣库");
        holder.content.setText("　冠军虽然只有一个" +
                "，但通过本次比赛收获良多的，却不只是盛大众信" +
                "5v5聚力纵、崂山一中" +
                "，以及蓝村队，凭借严明的纪律，获得" +
                "了赛事组委会颁发的公平竞赛奖。而许多球" +
                "队虽然排名靠后，但通过比赛得到的锻炼，让他们收益" +
                "颇多。在观看完决赛后，蓝村队的姜曙光和" +
                "记者聊了起来，“决赛确实精彩，我们还有" +
                "许多东西向他们学习，来年" +
                "希望能打出更好成绩。");
        return convertView;
    }

    public class Holder {
        TextView type;
        TextView title;
        TextView content;
        View view;
    }
}
