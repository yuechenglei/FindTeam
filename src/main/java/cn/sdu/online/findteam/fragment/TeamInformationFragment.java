package cn.sdu.online.findteam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.MyTeamActivity;
import cn.sdu.online.findteam.activity.WriteActivity;

public class TeamInformationFragment extends Fragment implements View.OnClickListener{

    private Button changeinfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teaminformation_layout, null);
        if (TeamInformationFragment.this.getActivity().equals(MyTeamActivity.getContext())){
            changeinfo = (Button) view.findViewById(R.id.change_team_info);
            changeinfo.setVisibility(View.VISIBLE);
            changeinfo.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(TeamInformationFragment.this.getActivity().getApplicationContext(), WriteActivity.class);
        intent.putExtra("sign", "编辑队伍信息");
        TeamInformationFragment.this.getActivity().startActivity(intent);
    }
}
