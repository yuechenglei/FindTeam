package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.MyTeamListAdapter;
import cn.sdu.online.findteam.mob.MyTeamListItem;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

/**
 * Created by wn on 2015/8/29.
 */
public class MyTeamActivity extends BaseActivity {

    private List<List<MyTeamListItem>> list;
    private static MyTeamActivity instance;

    Dialog dialog;
    View contentView;
    List<MyTeamListItem> hasEnteredList;
    List<MyTeamListItem> hasOfferedList;
    List<MyTeamListItem> hasRefusedList;
    MyTeamListAdapter adapter;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.myteam_actionbar);
        Button back = (Button) findViewById(R.id.myteam_return_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        instance = this;

        dialog = DialogDefine.createLoadingDialog(this,
                "加载中...");
        dialog.show();

        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            dialog.dismiss();
            AndTools.showToast(this, "当前网络不可用！");
            return;
        }

        userID = MyApplication.getInstance().
                getSharedPreferences("loginmessage", Context.MODE_PRIVATE).
                getString("userID", "-1");
        if (userID.equals("-1")) {
            dialog.dismiss();
            AndTools.showToast(MyTeamActivity.this, "您还未登录！");
            MyTeamActivity.this.finish();
            return;
        }

        contentView = View.inflate(this, R.layout.myteam_list_layout, null);
        ExpandableListView myTeamlv = (ExpandableListView) contentView.findViewById(R.id.myteam_expand_listview);
        list = new ArrayList<>();
        hasEnteredList = new ArrayList<>();
        hasOfferedList = new ArrayList<>();
        hasRefusedList = new ArrayList<>();
        list.add(hasEnteredList);
        list.add(hasOfferedList);
        list.add(hasRefusedList);
        adapter = new MyTeamListAdapter(this, list);
        myTeamlv.setAdapter(adapter);

        new Thread(new loadMyTeam()).start();
    }

    public static MyTeamActivity getInstance() {
        return instance;
    }

    class loadMyTeam implements Runnable {

        @Override
        public void run() {
            try {
                String jsonData = new NetCore().getUserTeam(userID);
                JSONArray teamJsonList = new JSONArray(jsonData);
                for (int i = 0; i < teamJsonList.length(); i++) {
                    JSONObject teamJson = (JSONObject) teamJsonList.get(i);
                    String status = teamJson.getString("status");
                    String name = teamJson.getString("name");
                    String teamID = teamJson.getString("id");
                    String introduce = teamJson.getString("introduce");
                    String imgPath = teamJson.getString("imgPath");
                    JSONObject category = new JSONObject(teamJson.getString("category"));
                    String parentName = category.getString("name");
                    String teamUser = teamJson.getString("user");
                    JSONObject UserObj = new JSONObject(teamUser);
                    Long openId = UserObj.getLong("openId");
                    if (status.equals(NetCore.HAS_ENTERED)) {
                        hasEnteredList.add(new MyTeamListItem(openId, name, introduce, parentName, teamID, imgPath != null ? imgPath : ""));
                    } else if (status.equals(NetCore.HAS_OFFERED)) {
                        hasOfferedList.add(new MyTeamListItem(openId, name, introduce, parentName, teamID, imgPath != null ? imgPath : ""));
                    } else if (status.equals(NetCore.HAS_REFUSED)) {
                        hasRefusedList.add(new MyTeamListItem(openId, name, introduce, parentName, teamID, imgPath != null ? imgPath : ""));
                    }
                }
                loadTeamHandler.sendEmptyMessage(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    Handler loadTeamHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            if (dialog != null) {
                dialog.dismiss();
            }
            MyTeamActivity.this.setContentView(contentView);
        }
    };
}
