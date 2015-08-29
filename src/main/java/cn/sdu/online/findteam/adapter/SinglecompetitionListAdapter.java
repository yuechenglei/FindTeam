package cn.sdu.online.findteam.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.OtherTeamActivity;
import cn.sdu.online.findteam.activity.SingleCompetitionActivity;
import cn.sdu.online.findteam.mob.SingleCompetitionListItem;

public class SingleCompetitionListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<SingleCompetitionListItem> listItems;

    public SingleCompetitionListAdapter(Context mContext, List<SingleCompetitionListItem> listItems) {
        inflater = LayoutInflater.from(mContext);
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.singlecompetition_item_layout, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.singlecp_item_img);
            viewHolder.teamname = (TextView) convertView.findViewById(R.id.singlecp_item_teamname);
            viewHolder.personnum = (TextView) convertView.findViewById(R.id.singlecp_item_personnum);
            viewHolder.line1 = convertView.findViewById(R.id.singlecp_item_line1);
            viewHolder.content = (TextView) convertView.findViewById(R.id.singlecp_item_content);
            viewHolder.line2 = convertView.findViewById(R.id.singlecp_item_line2);
            viewHolder.look = (Button) convertView.findViewById(R.id.singlecp_item_look);
            viewHolder.join = (Button) convertView.findViewById(R.id.singlecp_item_join);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.teamname.setText(listItems.get(position).teamname);
        viewHolder.personnum.setText("缺" + listItems.get(position).personnum + "人");
        viewHolder.content.setText(listItems.get(position).content);
        viewHolder.look.setTag(position);
        viewHolder.join.setTag(position);

        viewHolder.look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SingleCompetitionActivity.getContext(), OtherTeamActivity.class);
                SingleCompetitionActivity.getContext().startActivity(intent);
            }
        });

        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView teamname;
        TextView personnum;
        View line1;
        TextView content;
        View line2;
        Button look;
        Button join;
    }
}