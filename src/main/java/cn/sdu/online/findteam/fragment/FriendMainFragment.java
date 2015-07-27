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
import android.widget.ListView;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.MyMessageListViewAdapter;

public class FriendMainFragment extends ListFragment {

    private ListView list ;
    //private SimpleAdapter adapter;
    private List<Map<String, Object>> data;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.mymessage_listview, container,false);

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //list = (ListView)findViewById(android.R.id.list);
        data = getData();
        MyMessageListViewAdapter adapter = new MyMessageListViewAdapter(getActivity(),data);
        setListAdapter(adapter);

    }
    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<10;i++)
        {
            map = new HashMap<String, Object>();
            map.put("title", "同学甲");
            map.put("info","我是个人介绍我是个人我是个人我是个人");
            map.put("img", R.drawable.frienda);
            list.add(map);
            list.add(map);
        }
        return list;
    }
}
