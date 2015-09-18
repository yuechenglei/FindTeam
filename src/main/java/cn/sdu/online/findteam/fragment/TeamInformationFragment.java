package cn.sdu.online.findteam.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.MySingleTeamActivity;
import cn.sdu.online.findteam.activity.SingleCompetitionActivity;
import cn.sdu.online.findteam.activity.WriteActivity;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

public class TeamInformationFragment extends Fragment implements View.OnClickListener {

    private Button changeinfo;
    private TextView inforTv;
    View view;

    Dialog dialog;
    String introduce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = DialogDefine.createLoadingDialog(TeamInformationFragment.this.getActivity(),
                "加载中...");
        dialog.show();

        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            AndTools.showToast(TeamInformationFragment.this.getActivity(), "当前网络不可用！");
            if (dialog != null){
                dialog.dismiss();
            }
            view = inflater.inflate(R.layout.other_teaminformation_layout,null);
            return view;
        }
        else {
            if (MyApplication.IDENTITY.equals("队长")) {
                view = inflater.inflate(R.layout.myteam_information_layout, container, false);
                changeinfo = (Button) view.findViewById(R.id.change_team_info);
                inforTv = (TextView) view.findViewById(R.id.team_infor_tv);
                changeinfo.setOnClickListener(this);
            } else if (MyApplication.IDENTITY.equals("队员")) {
                view = inflater.inflate(R.layout.myteam_information_layout, container, false);
                changeinfo = (Button) view.findViewById(R.id.change_team_info);
                inforTv = (TextView) view.findViewById(R.id.team_infor_tv);
                changeinfo.setVisibility(View.GONE);
            } else {
                view = inflater.inflate(R.layout.other_teaminformation_layout, null);
                inforTv = (TextView) view.findViewById(R.id.otherteam_info_tv);
            }

            final String teamID = TeamInformationFragment.this.getActivity().getIntent().
                    getExtras().getString("teamID");
            new Thread(){
                @Override
                public void run() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("team.id", teamID));
                    try {
                        String jsonData = new NetCore().getResultWithCookies(NetCore.getOneTeamAddr,
                                params);
                        JSONObject jsonObject = new JSONObject(jsonData);
                        introduce = jsonObject.getString("introduce");
                        if (introduce.length() != 0) {
                            Bundle bundle = new Bundle();
                            Message message = new Message();
                            message.setData(bundle);
                            loadInfo.sendEmptyMessage(0);
                        }
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

    Handler loadInfo = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog != null){
                dialog.dismiss();
            }
            setInfor(introduce);
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(TeamInformationFragment.this.getActivity(), WriteActivity.class);
        intent.putExtra("sign", "编辑队伍信息");
        TeamInformationFragment.this.getActivity().startActivityForResult(intent, 1);
    }


    public void setInfor(String infor){
        inforTv.setText(infor);
    }
}
