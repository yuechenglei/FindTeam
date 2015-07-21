package cn.sdu.online.findteam.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button bt_dropdown;//下拉选择菜单按钮
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelative;
    private ActionBarDrawerToggle mDrawerToggle;

    public View contentView;
    public PopupWindow popupWindow;//弹出下拉菜单
    private RelativeLayout rela_drop;//下拉的选择按钮
    /**
     * 记录actionsearch按钮的点击状态，true为不搜索状态，false为搜索栏弹出状态
     */
    private boolean acState;
    private boolean dropState;//下拉状态
    /**
     * 主界面搜索按钮
     */
    private Button actionsearch;
    private Button searchButton;
    /**
     * 主界面的搜索框布局
     */
    private LinearLayout searchLayout;
    /**
     * 导航栏
     */
    private View view_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        contentView = this.getLayoutInflater().inflate(R.layout.classify_layout, null);
        /*popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        rela_drop = (RelativeLayout) this.findViewById(R.id.rela_top);*/
        init_button();
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
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SingleCompetitionActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 初始化 acState 为true
         */
        acState = true;
        dropState = true;
        searchButton = (Button) findViewById(R.id.search_button);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        /**
         *设置ActionBar的自定义布局。
         */
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view_actionbar = inflator.inflate(R.layout.actionbar_layout, null);
        setActionBarLayout(view_actionbar);
        /**
         * 定义ActionBar上的搜索按钮,并设置监听
         */
        actionsearch = (Button) findViewById(R.id.action_search);
        actionsearch.setOnClickListener(new ActionSearchListener());

        /**
         * 初始化 acState 为true
         */
        acState = true;


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
                actionsearch.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionsearch.setVisibility(View.INVISIBLE);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    /**
     * 布局Id
     */
    public void setActionBarLayout(View v) {
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);

            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionMenuView.LayoutParams.FILL_PARENT, ActionMenuView.LayoutParams.FILL_PARENT);
            actionBar.setCustomView(v, layout);
        }
    }

    /**
     * 设置actionbar的标题
     */
    void setActionBarTest(String test) {
        TextView title = (TextView) view_actionbar.findViewById(R.id.title);
        title.setText(test);
    }

    private class ActionSearchListener implements View.OnClickListener {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        @Override
        public void onClick(View v) {
            if (acState) {
                searchLayout.setVisibility(View.VISIBLE);
                /*rela_drop.setLayoutParams(params);
                popupWindow.dismiss();*/
                acState = false;
            } else {
                searchLayout.setVisibility(View.GONE);
                /*rela_drop.setLayoutParams(params);*/
                acState = true;
            }
        }
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

    //初始化侧滑的按钮
    void init_button() {
        Button bt_game = (Button) this.findViewById(R.id.bt_games);
        Button bt_set = (Button) this.findViewById(R.id.bt_set);
        Button bt_news = (Button) this.findViewById(R.id.bt_news);
        Button bt_my = (Button) this.findViewById(R.id.bt_my);
        Button bt_make = (Button) this.findViewById(R.id.bt_make);
        Button bt_person = (Button) this.findViewById(R.id.bt_person);
        Button bt_head = (Button) this.findViewById(R.id.bt_head);
        /*bt_dropdown = (Button) this.findViewById(R.id.arrow);*/
        bt_game.setOnClickListener(this);
        bt_set.setOnClickListener(this);
        bt_news.setOnClickListener(this);
        bt_my.setOnClickListener(this);
        bt_make.setOnClickListener(this);
        bt_person.setOnClickListener(this);
        bt_head.setOnClickListener(this);
        /*bt_dropdown.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {
        int tag = v.getId();
        switch (tag) {

            case R.id.bt_head:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("修改头像");
                break;
            case R.id.bt_set:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("设置");
                break;
            case R.id.bt_make:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("创建队伍");
                break;
            case R.id.bt_my:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("我的队伍");
                break;
            case R.id.bt_news:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("我的消息");
                break;
            case R.id.bt_games:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("所有比赛");
                break;
            case R.id.bt_person:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("个人信息");
                break;

            default:
                break;


        }


    }

    /**
     * 双击退出函数
     */
    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, R.string.to_exit, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
        // super.onBackPressed();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mDrawerLayout.isDrawerOpen(mDrawerRelative)) {
                mDrawerLayout.closeDrawer(mDrawerRelative);
            } else {
                onBackPressed(); // 调用双击退出函数
            }

        }
        return false;
    }

}


