package cn.sdu.online.findteam.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.TeamLogActivity;
import cn.sdu.online.findteam.activity.WriteActivity;
import cn.sdu.online.findteam.adapter.TeamLogListViewAdapter;
import cn.sdu.online.findteam.mob.TeamLogListViewItem;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

public class TeamLogFragment extends Fragment implements View.OnClickListener {

    private View view;
    List<TeamLogListViewItem> listViewItems;
    ListView listView;
    TeamLogListViewAdapter teamLogListViewAdapter;
    private Button writeLog;
    private PopupWindow popupWindow;
    Dialog dialog;

    String teamID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = DialogDefine.createLoadingDialog(TeamLogFragment.this.getActivity(),
                "加载中...");

        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            AndTools.showToast(TeamLogFragment.this.getActivity(), "当前网络不可用！");
            if (dialog != null) {
                dialog.dismiss();
            }
            view = inflater.inflate(R.layout.other_teamlog_layout, null);
            return view;
        } else {
            if (MyApplication.IDENTITY.equals("队长")) {
                view = inflater.inflate(R.layout.myteam_teamlog_layout, null);
                teamHeaderLog();
            } else if (MyApplication.IDENTITY.equals("队员")) {
                view = inflater.inflate(R.layout.myteam_teamlog_layout, null);
                teamMemLog();
            } else {
                view = inflater.inflate(R.layout.other_teamlog_layout, null);
                teamOtherLog();
            }

            teamID = TeamLogFragment.this.getActivity().getIntent().
                    getExtras().getString("teamID");
            Thread thread = new Thread() {
                @Override
                public void run() {
                    loadLog();
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public void loadLog() {
        if (listViewItems == null) {
            listViewItems = new ArrayList<>();
        } else {
            listViewItems.clear();
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("log.team.id", teamID));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("pagelistnum", "0"));
        try {
            String jsonData = new NetCore().getResultWithCookies(NetCore.getTeamLogAddr,
                    params);
            if (jsonData.trim().length() != 0) {
                JSONArray jsonArray = new JSONArray(jsonData);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String userStr = jsonObject.getString("user");
                    JSONObject userObj = new JSONObject(userStr);
                    String name = userObj.getString("userName");
                    String imgPath = userObj.getString("imgPath");
                    String content = jsonObject.getString("content");
                    String id = jsonObject.getString("id");
                    String time = jsonObject.getString("time");
                    JSONObject timeObj = new JSONObject(time);
                    long totalTime = timeObj.getLong("time");
                    Date date = new Date(totalTime);
                    time = date.toLocaleString();
                    addListItem(name, time, content, imgPath, id);
                }
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    teamLogListViewAdapter.notifyDataSetChanged();
                    break;

                case 1:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    AndTools.showToast(TeamLogFragment.this.getActivity(), "获取队伍日志失败");
            }
        }
    };

    private void initListView(ListView listView) {
        listViewItems = new ArrayList<TeamLogListViewItem>();
        teamLogListViewAdapter = new TeamLogListViewAdapter(TeamLogFragment.this.getActivity().getApplicationContext(), listViewItems);
        listView.setAdapter(teamLogListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("imgPath", listViewItems.get(position).imgPath);
                intent.putExtra("name", listViewItems.get(position).name);
                intent.putExtra("content", listViewItems.get(position).content);
                intent.putExtra("time", listViewItems.get(position).time);
                intent.setClass(TeamLogFragment.this.getActivity(), TeamLogActivity.class);
                TeamLogFragment.this.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teammem_writelog:
                Intent intent = new Intent();
                intent.setClass(TeamLogFragment.this.getActivity(), WriteActivity.class);
                intent.putExtra("sign", "写日志");
                intent.putExtra("teamId", teamID);
                TeamLogFragment.this.getActivity().startActivityForResult(intent, 2);
                break;
        }
    }

    /**
     * 别人来的时候调用
     */
    private void teamOtherLog() {
        listView = (ListView) view.findViewById(R.id.teamlog_listview);
        initListView(listView);
    }

    /**
     * 队员来的时候调用
     */
    private void teamMemLog() {
        writeLog = (Button) view.findViewById(R.id.teammem_writelog);
        writeLog.setOnClickListener(TeamLogFragment.this);
        listView = (ListView) view.findViewById(R.id.teammem_log_list);
        initListView(listView);
    }

    /**
     * 队长来的时候调用
     */
    private void teamHeaderLog() {
        writeLog = (Button) view.findViewById(R.id.teammem_writelog);
        writeLog.setOnClickListener(TeamLogFragment.this);
        listView = (ListView) view.findViewById(R.id.teammem_log_list);
        initListView(listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow(position);
                return false;
            }
        });
    }

    private void showPopupWindow(final int position) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(TeamLogFragment.this.getActivity()).inflate(
                R.layout.delete_pop_window, null);
        // 设置按钮的点击事件
        Button delete = (Button) contentView.findViewById(R.id.pop_delete_btn);

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setWidth(400);
        popupWindow.setHeight(150);
        popupWindow.setAnimationStyle(R.style.popwindow_anim);
        popupWindow.setTouchable(true);
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new poponDismissListener());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                dialog = DialogDefine.createLoadingDialog(TeamLogFragment.this.getActivity(),
                        "");
                dialog.show();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("log.id", listViewItems.get(position).id));

                        try {
                            String data = new NetCore().getResultWithCookies(NetCore.deleteTeamLogAddr,
                                    params);
                            JSONObject jsonObject = new JSONObject(data);
                            String msg = jsonObject.getString("msg");
                            int code = jsonObject.getInt("code");
                            Bundle bundle = new Bundle();
                            Message message = new Message();
                            bundle.putInt("code", code);
                            if (code == 1) {
                                bundle.putInt("position", position);
                            }
                            bundle.putString("msg", msg);
                            message.setData(bundle);
                            deleteHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.color.textcolor_gray));

        // 设置好参数之后再show
        popupWindow.showAtLocation(TeamLogFragment.this.getView(), Gravity.CENTER, 0, 0);
    }

    Handler deleteHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            int code = bundle.getInt("code");
            String msg = bundle.getString("msg");
            if (dialog != null) {
                dialog.dismiss();
            }
            switch (code) {
                case 1:
                    AndTools.showToast(TeamLogFragment.this.getActivity(), msg);
                    int position = bundle.getInt("position");
                    listViewItems.remove(position);
                    teamLogListViewAdapter.notifyDataSetChanged();

                    break;

                default:
                    AndTools.showToast(TeamLogFragment.this.getActivity(), msg);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = TeamLogFragment.this.getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        TeamLogFragment.this.getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    public void addListItem(String name, String time, String content, String imgPath, String id) {
        listViewItems.add(new TeamLogListViewItem(name, time, content, imgPath, id));
    }
}


