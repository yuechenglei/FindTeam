package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.InviteMemListAdapter;
import cn.sdu.online.findteam.mob.InviteMemListItem;

public class InviteNewMemActivity extends Activity{

    private ListView inviteMemList;
    private Button button;
    private List<InviteMemListItem> list;
    private InviteMemListAdapter inviteMemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitemem_activity);
        init();
        initList();
    }

    private void init(){
        inviteMemList = (ListView) findViewById(R.id.invite_activity_listview);
        button = (Button) findViewById(R.id.invite_finish_btn);
    }

    private void initList(){
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new InviteMemListItem("同学甲", "我是个人介绍我是个人介绍我是个人介绍",
                    R.drawable.frienda, false));
        }
        inviteMemListAdapter = new InviteMemListAdapter(list, InviteNewMemActivity.this);
        inviteMemList.setAdapter(inviteMemListAdapter);
        inviteMemListAdapter.setTextCallback(new InviteMemListAdapter.TextCallback() {
            public void onListenr(int count) {
                button.setText("完成" + "(" + count + ")");
            }
        });
        inviteMemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inviteMemListAdapter.notifyDataSetChanged();
            }
        });
    }
}
