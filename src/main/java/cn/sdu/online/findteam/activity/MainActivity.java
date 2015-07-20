package cn.sdu.online.findteam.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.ListViewAdapter;
import cn.sdu.online.findteam.view.ActionBarDrawerToggle;
import cn.sdu.online.findteam.view.DrawerArrowDrawable;
import cn.sdu.online.findteam.view.XListView;

public class MainActivity extends Activity implements XListView.IXListViewListener, View.OnClickListener {
    XListView listView;
    private List<HashMap<String, String>> list;
    private ListViewAdapter adapter;
    private Button bt_game, bt_set, bt_news, bt_my, bt_make, bt_person, bt_head;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelative;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        listView = (XListView) findViewById(R.id.listview);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);
        list = getListDate();
        adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.v("listview", "我被长安点击");

            }
        });
        init_button();
        /**
         * 侧边栏的初始化
         */
        ActionBar ab = this.getActionBar();

        /**
         *左上角设置一个按钮，并设置可点击
         */
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelative = (RelativeLayout) findViewById(R.id.navdrawer);


        DrawerArrowDrawable drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    //初始化侧滑的按钮
    void init_button() {
        bt_game = (Button) this.findViewById(R.id.bt_games);
        bt_set = (Button) this.findViewById(R.id.bt_set);
        bt_news = (Button) this.findViewById(R.id.bt_news);
        bt_my = (Button) this.findViewById(R.id.bt_my);
        bt_make = (Button) this.findViewById(R.id.bt_make);
        bt_person = (Button) this.findViewById(R.id.bt_person);
        bt_head = (Button) this.findViewById(R.id.bt_head);
        bt_game.setOnClickListener(this);
        bt_set.setOnClickListener(this);
        bt_news.setOnClickListener(this);
        bt_my.setOnClickListener(this);
        bt_make.setOnClickListener(this);
        bt_person.setOnClickListener(this);
        bt_head.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
//        MenuItem searchItem = menu.findItem(R.id.ab_search);
//        SearchView searchView = (SearchView) MenuItemCompat
//                .getActionView(searchItem);
//        if(searchView==null)
//            return true;
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String arg0) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String arg0) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//        });
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerRelative)) {
                mDrawerLayout.closeDrawer(mDrawerRelative);
            } else {
                mDrawerLayout.openDrawer(mDrawerRelative);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * 下拉刷新实现
     *
     * @return
     */
    private List<HashMap<String, String>> getListDate() {
        list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 15; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", "第" + i + "item");
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

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    listView.setRefreshTime(getDate());

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", "刷新得到的item");
//				list.add(map);
                    list.add(0, map);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    listView.setRefreshTime(getDate());

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
     * 得到刷新时间
     *
     * @return
     */
    public static String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins);

        return sbBuffer.toString();
    }


    @Override
    public void onClick(View v) {
        int tag = v.getId();
        switch (tag) {
            case R.id.bt_head:
                break;
            case R.id.bt_set:
                break;
            case R.id.bt_make:
                break;
            case R.id.bt_my:
                break;
            case R.id.bt_news:
                break;
            case R.id.bt_games:
                break;
            case R.id.bt_person:
                break;


        }

    }
}


