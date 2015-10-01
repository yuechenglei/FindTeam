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
import cn.sdu.online.findteam.mob.TeamMemberListItem;
import cn.sdu.online.findteam.share.MyApplication;

public class TeamMemberListViewAdapter extends BaseAdapter{

    LayoutInflater inflater;
    List<TeamMemberListItem> listItems;

    public TeamMemberListViewAdapter(Context mContext, List<TeamMemberListItem> listItems){
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
    public View getView(int position,
                        View convertView, ViewGroup parent) {
        Viewholder viewholder;
        if (convertView == null){
            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.teammember_listitem_layout,null);
            viewholder.headerbmp = (ImageView) convertView.findViewById(R.id.teammem_listview_headbmp);
            viewholder.nametv = (TextView) convertView.findViewById(R.id.teammember_name);
            viewholder.introductiontv = (TextView) convertView.findViewById(R.id.teammember_introduction);

            convertView.setTag(viewholder);
        }
        else {
            viewholder = (Viewholder) convertView.getTag();
        }
        viewholder.nametv.setText(listItems.get(position).name);
        viewholder.introductiontv.setText(listItems.get(position).introduction);
        loadBitmap(listItems.get(position).imgPath, viewholder.headerbmp);

        return convertView;
    }

    private void loadBitmap(String imgPath, final ImageView imageView) {
/*        ImageRequest request = new ImageRequest(imgPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getQueues().add(request);*/
        ImageLoader imageLoader = new ImageLoader(MyApplication.getQueues(), MyApplication.bitmapCache);
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imageView, R.drawable.head_moren,
                R.drawable.head_moren);
        imageLoader.get(imgPath, imageListener);
    }

    public class Viewholder{
        TextView nametv;
        TextView introductiontv;
        ImageView headerbmp;
    }
}
