package cn.sdu.online.findteam.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.SingleCompetitionActivity;
import cn.sdu.online.findteam.adapter.ListViewAdapter;
import cn.sdu.online.findteam.util.Time;
import cn.sdu.online.findteam.view.XListView;

public class AllGamesFragment extends Fragment implements
        XListView.IXListViewListener {

    XListView listView;
    private List<HashMap<String, String>> list;
    private ListViewAdapter adapter;
    private View view;
    private OnFragmentInteractionListener mListener;

    private LinearLayout mButton;
    private PopupWindow mPopupWindow;
    private RelativeLayout relativeLayout;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.allgame_layout, null);

        initView();

        list = getListDate();
        adapter = new ListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getActivity(), SingleCompetitionActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void initView() {
        listView = (XListView) view.findViewById(R.id.allgame_listview);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);

        View popupView = AllGamesFragment.this.getActivity().
                getLayoutInflater().inflate(R.layout.allgame_popup_layout, null);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.allgame_relayout);

        textView = (TextView) view.findViewById(R.id.allgame_class);

        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listView.setAlpha(1);
                textView.setText("分类");
            }
        });

        mButton = (LinearLayout) view.findViewById(R.id.allgame_down_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAlpha(0.4f);
                mPopupWindow.showAsDropDown(relativeLayout);
                textView.setText("选择分类");
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = AllGamesFragment.this.getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        AllGamesFragment.this.getActivity().getWindow().setAttributes(lp);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    listView.setRefreshTime(Time.getDate());

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", "刷新得到的item");
//				list.add(map);
                    list.add(0, map);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    listView.setRefreshTime(Time.getDate());

                    HashMap<String, String> map1 = new HashMap<String, String>();
                    map1.put("name", "加载更多得到的item");
                    list.add(map1);

                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        ;
    };

    /**
     * 下拉刷新实现
     *
     * @return
     */
    private List<HashMap<String, String>> getListDate() {
        list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 15; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", "第" + i + "项");
            list.add(map);
        }
        return list;

    }


    @Override
    public void onRefresh() {
        myThread(0);
    }

    @Override
    public void onLoadMore() {
        myThread(1);
    }

    /**
     * @param msg 0为下拉刷新 1为加载更多
     */
    private void myThread(final int msg) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(msg);
                } catch (InterruptedException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
