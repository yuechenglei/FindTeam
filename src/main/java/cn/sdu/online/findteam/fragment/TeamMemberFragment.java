package cn.sdu.online.findteam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.TeamMemberListViewAdapter;
import cn.sdu.online.findteam.resource.TeamMemberListItem;

public class TeamMemberFragment extends Fragment {

    private ListView listView;
    private String[] name = new String[]{"大师兄", "二师弟", "沙师弟"};
    private List<TeamMemberListItem> listItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teammember_layout,null);
        listView = (ListView) view.findViewById(R.id.teammem_listview);
        listItems = new ArrayList<>();
        String introduction = "孙悟空是中国最著名的神话角色之一，出自四大古典名著之《西游记》。相传他由开天辟地以来的仙石孕育而生，因带领群猴进入水帘洞而成为众猴之王，号称为 “美猴王”。后来在西牛贺洲拜菩提祖师为师学艺，得名孙悟空，学会地煞七十二变、筋斗云等高超的法术。";

        for (int i = 0;i<name.length;i++){
            listItems.add(new TeamMemberListItem(name[i], introduction, R.id.teammem_listview_headbmp));
        }

        TeamMemberListViewAdapter teamMemberListViewAdapter = new TeamMemberListViewAdapter(TeamMemberFragment.this.getActivity().getApplicationContext(), listItems);

        listView.setAdapter(teamMemberListViewAdapter);
        return view;
    }
}
