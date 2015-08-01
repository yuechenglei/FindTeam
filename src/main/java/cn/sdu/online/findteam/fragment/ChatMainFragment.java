package cn.sdu.online.findteam.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.MyMessageListViewAdapter;
import cn.sdu.online.findteam.mob.ChatListItem;

import static android.widget.AdapterView.*;

public class ChatMainFragment extends ListFragment {
    //private SimpleAdapter adapter1;
    private List<ChatListItem> data;
    private MyMessageListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.mymessage_listview, container, false);
        //list1 = (ListView) view.findViewById(android.R.id.list);
        data = getData();
        adapter = new MyMessageListViewAdapter(getActivity(), data);
        setListAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<ChatListItem> getData() {
        List<ChatListItem> list = new ArrayList<ChatListItem>();
        for (int i = 0; i < 10; i++) {
            list.add(new ChatListItem("同学甲", "我是私信我是私信我是私信我是私信",
                    R.drawable.frienda, false, 0));
        }
        return list;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ChatListItem chatListItem = data.get(position);
        chatListItem.setSeeornot(true);
        chatListItem.setNum(chatListItem.getNum() + 1);
        adapter.notifyDataSetChanged();
    }
}
