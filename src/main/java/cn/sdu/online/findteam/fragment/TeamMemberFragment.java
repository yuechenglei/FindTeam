package cn.sdu.online.findteam.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.InviteNewMemActivity;
import cn.sdu.online.findteam.activity.MySingleTeamActivity;
import cn.sdu.online.findteam.adapter.TeamMemberListViewAdapter;
import cn.sdu.online.findteam.mob.TeamMemberListItem;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

public class TeamMemberFragment extends Fragment {

    private ListView listView;
    private String[] name = new String[]{"大师兄", "二师弟", "沙师弟"};
    private List<TeamMemberListItem> listItems;
    private Button invitemem;
    private TeamMemberListViewAdapter teamMemberListViewAdapter;
    private View view;
    Dialog dialog;

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
            if (MyApplication.IDENTITY.equals("游客")) {
                view = inflater.inflate(R.layout.other_teammem_layout, container, false);
                otherTeanMemInit();
            } else {
                view = inflater.inflate(R.layout.myteam_member_layout, container, false);
                myTeamMemInit();
            }

            final String teamID = TeamMemberFragment.this.getActivity().getIntent().
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
                            listItems.add(new TeamMemberListItem(name, introduce, R.id.teammem_listview_headbmp));
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
        }
    };

    private void myTeamMemInit() {
        listView = (ListView) view.findViewById(R.id.teammem_listview);
        listItems = new ArrayList<>();
        teamMemberListViewAdapter = new TeamMemberListViewAdapter(TeamMemberFragment.this.getActivity().getApplicationContext(), listItems);
        listView.setAdapter(teamMemberListViewAdapter);

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

    private void otherTeanMemInit() {
        listView = (ListView) view.findViewById(R.id.other_teammem_listview);
        listItems = new ArrayList<>();
        teamMemberListViewAdapter = new TeamMemberListViewAdapter(TeamMemberFragment.this.getActivity().getApplicationContext(), listItems);

        listView.setAdapter(teamMemberListViewAdapter);
    }
}
