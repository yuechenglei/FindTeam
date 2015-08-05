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

public class TeamInformationFragment extends Fragment implements View.OnClickListener {

    private Button changeinfo;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (TeamInformationFragment.this.getActivity().equals(MyTeamActivity.mContext)) {
            view = inflater.inflate(R.layout.myteam_information_layout, container, false);
            changeinfo = (Button) view.findViewById(R.id.change_team_info);
            changeinfo.setOnClickListener(this);
        }
        else {
            view = inflater.inflate(R.layout.other_teaminformation_layout,null);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
