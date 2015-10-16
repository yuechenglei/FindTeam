package cn.sdu.online.findteam.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.InfoOtherActivity;
import cn.sdu.online.findteam.adapter.MyMessageListViewAdapter;
import cn.sdu.online.findteam.mob.ChatListItem;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

public class FriendMainFragment extends Fragment {

    View view;
    private ListView listView;
    private TextView empty_Tv;
    private MyMessageListViewAdapter adapter;
    private List<ChatListItem> list;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.mymessage_listview, container, false);
        initView();
        getData();
        return view;
    }

    private void getData() {
        if (AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            dialog = DialogDefine.createLoadingDialog(FriendMainFragment.this.getActivity(),
                    "");
            dialog.show();

            new Thread() {
                @Override
                public void run() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("type", "friends"));
                    try {
                        String jsonData = new NetCore().getResultWithCookies(NetCore.getFriendListAddr, params);
                        JSONArray jsonArray = new JSONArray(jsonData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String userName = jsonObject.getString("userName");
                            String introduce = jsonObject.getString("introduce");
                            String imgPath = jsonObject.getString("imgPath");
                            String openId = jsonObject.getLong("openId") + "";
                            list.add(new ChatListItem(userName, introduce, imgPath, openId));
                        }
                        handler.sendEmptyMessage(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            AndTools.showToast(FriendMainFragment.this.getActivity(), "网络错误！");
        }
    }

    private void initView() {
        empty_Tv = (TextView) view.findViewById(R.id.empty_tv);
        listView = (ListView) view.findViewById(R.id.friend_list);
        list = new ArrayList<>();
        adapter = new MyMessageListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setEmptyView(empty_Tv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendMainFragment.this.getActivity(), InfoOtherActivity.class);
                intent.putExtra("openId", list.get(position).openId);
                FriendMainFragment.this.getActivity().startActivity(intent);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog != null) {
                dialog.dismiss();
            }
            empty_Tv.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    };
}
