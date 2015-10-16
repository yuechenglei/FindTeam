package cn.sdu.online.findteam.fragment;

import android.app.Dialog;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.InfoOtherActivity;
import cn.sdu.online.findteam.activity.InfoPersonActivity;
import cn.sdu.online.findteam.activity.InviteNewMemActivity;
import cn.sdu.online.findteam.activity.MySingleTeamActivity;
import cn.sdu.online.findteam.adapter.TeamMemberListViewAdapter;
import cn.sdu.online.findteam.mob.TeamMemberListItem;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.DemoUtil;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

public class TeamMemberFragment extends Fragment {

    private ListView listView;
    private List<TeamMemberListItem> listItems;
    private Button invitemem;
    private TeamMemberListViewAdapter teamMemberListViewAdapter;
    private View view;
    Dialog dialog;
    private PopupWindow popupWindow;
    String teamID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        dialog = DialogDefine.createLoadingDialog(TeamMemberFragment.this.getActivity(),
                "加载中...");

        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            AndTools.showToast(TeamMemberFragment.this.getActivity(), "当前网络不可用！");
            if (dialog != null) {
                dialog.dismiss();
            }
            view = inflater.inflate(R.layout.other_teaminformation_layout, null);
            return view;
        } else {
            if (MyApplication.IDENTITY.equals("队长") || MyApplication.IDENTITY.equals("队员")) {
                view = inflater.inflate(R.layout.myteam_member_layout, container, false);
                myTeamMemInit();
            } else {
                view = inflater.inflate(R.layout.other_teammem_layout, container, false);
                otherTeamMemInit();
            }

            teamID = TeamMemberFragment.this.getActivity().getIntent().
                    getExtras().getString("teamID");
            new Thread() {
                @Override
                public void run() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("team.id", teamID));
                    try {
                        String jsonData = new NetCore().getResultWithCookies(NetCore.getOneTeamAddr,
                                params);
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray memList = new JSONArray(jsonObject.getString("member"));
                        for (int i = 0; i < memList.length(); i++) {
                            JSONObject jsonObject1 = memList.getJSONObject(i);
                            String name = jsonObject1.getString("userName");
                            String introduce = jsonObject1.getString("introduce");
                            String imgPath = jsonObject1.getString("imgPath");
                            String openID = jsonObject1.getString("openId");
                            String id = jsonObject1.getString("id");
                            listItems.add(new TeamMemberListItem(name, introduce, R.id.teammem_listview_headbmp, imgPath, openID, id));
                        }
                        loadMem.sendEmptyMessage(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return view;
    }

    Handler loadMem = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            teamMemberListViewAdapter.notifyDataSetChanged();
            if (dialog != null) {
                dialog.dismiss();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    if (listItems.get(position).openID.equals(
                            MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getLong("loginID", 0) + ""
                    )) {
                        intent = new Intent(TeamMemberFragment.this.getActivity(),
                                InfoPersonActivity.class);
                        TeamMemberFragment.this.getActivity().startActivity(intent);
                    } else {
                        intent = new Intent(TeamMemberFragment.this.getActivity(),
                                InfoOtherActivity.class);
                        intent.putExtra("openId", listItems.get(position).openID);
                        TeamMemberFragment.this.getActivity().startActivity(intent);
                    }
                }
            });
        }
    };

    private void myTeamMemInit() {
        listView = (ListView) view.findViewById(R.id.teammem_listview);
        listItems = new ArrayList<>();
        teamMemberListViewAdapter = new TeamMemberListViewAdapter(TeamMemberFragment.this.getActivity().getApplicationContext(), listItems);
        listView.setAdapter(teamMemberListViewAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow(position);
                return false;
            }
        });

        invitemem = (Button) view.findViewById(R.id.invite_new_member);
        invitemem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamMemberFragment.this.getActivity(),
                        InviteNewMemActivity.class);
                TeamMemberFragment.this.getActivity().startActivity(intent);
            }
        });
    }

    private void otherTeamMemInit() {
        listView = (ListView) view.findViewById(R.id.other_teammem_listview);
        listItems = new ArrayList<>();
        teamMemberListViewAdapter = new TeamMemberListViewAdapter(TeamMemberFragment.this.getActivity().getApplicationContext(), listItems);

        listView.setAdapter(teamMemberListViewAdapter);
    }

    private void showPopupWindow(final int position) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(TeamMemberFragment.this.getActivity()).inflate(
                R.layout.delete_pop_window, null);
        // 设置按钮的点击事件
        Button delete = (Button) contentView.findViewById(R.id.pop_delete_btn);
        delete.setText("从队伍中移除此人");

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setWidth(500);
        popupWindow.setHeight(180);
        popupWindow.setAnimationStyle(R.style.popwindow_anim);
        popupWindow.setTouchable(true);
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new poponDismissListener());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                DemoUtil.showAlertDialog(TeamMemberFragment.this.getActivity(), "确定要从队伍中移除该成员吗？",
                        new DemoUtil.DialogCallback() {
                            @Override
                            public void onPositive() {
                                dialog = DialogDefine.createLoadingDialog(TeamMemberFragment.this.getActivity(),
                                        "");
                                dialog.show();
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("team.id", teamID));
                                        params.add(new BasicNameValuePair("user.id", listItems.get(position).id));
                                        Log.v("denglusihbai", teamID + "           " + listItems.get(position).id);
                                        try {
                                            String data = new NetCore().getResultWithCookies(NetCore.refuseJoinAddr,
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
        popupWindow.showAtLocation(TeamMemberFragment.this.getView(), Gravity.CENTER, 0, 0);
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
                    AndTools.showToast(TeamMemberFragment.this.getActivity(), msg);
                    int position = bundle.getInt("position");
                    listItems.remove(position);
                    teamMemberListViewAdapter.notifyDataSetChanged();

                    break;

                default:
                    AndTools.showToast(TeamMemberFragment.this.getActivity(), msg);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = TeamMemberFragment.this.getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        TeamMemberFragment.this.getActivity().getWindow().setAttributes(lp);
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
}
