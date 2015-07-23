package cn.sdu.online.findteam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.OtherTeamActivity;
import cn.sdu.online.findteam.activity.TeamLogActivity;
import cn.sdu.online.findteam.adapter.TeamLogListViewAdapter;
import cn.sdu.online.findteam.entity.TeamLogListViewItem;
import cn.sdu.online.findteam.view.SingleCompetitionListView;

public class TeamLogFragment extends Fragment {

    List<TeamLogListViewItem> listViewItems;
    SingleCompetitionListView listView;
    String name[];
    String time[];
    String content;
    TeamLogListViewAdapter teamLogListViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teamlog_layout, null);

        listViewItems = new ArrayList<TeamLogListViewItem>();
        listView = (SingleCompetitionListView) view.findViewById(R.id.teamlog_listview);
        name = new String[]{"大师兄", "二师弟", "沙师弟"};
        time = new String[]{"2015年7月23日 10:45", "2015年7月23日 11:45", "2015年7月23日 12:45"};
        content = "我是严肃的日志~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

        for (int i = 0;i<name.length;i++){
            listViewItems.add(new TeamLogListViewItem(R.id.teamlog_headbmp,
                    name[i], time[i], content));
        }

        teamLogListViewAdapter= new TeamLogListViewAdapter(TeamLogFragment.this.getActivity().getApplicationContext(), listViewItems);
        listView.setAdapter(teamLogListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(OtherTeamActivity.getContext(), TeamLogActivity.class);
                OtherTeamActivity.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
