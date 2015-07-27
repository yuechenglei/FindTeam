package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.BuildTeamFragment;
import cn.sdu.online.findteam.fragment.FragmentSetting;
import cn.sdu.online.findteam.fragment.MainFragment;
import cn.sdu.online.findteam.view.ActionBarDrawerToggle;
import cn.sdu.online.findteam.view.DrawerArrowDrawable;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Button bt_dropdown;//下拉选择菜单按钮
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelative;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private List<Fragment> lists;//存放各个fragment
    //  public View contentView;
//    public PopupWindow popupWindow;//弹出下拉菜单
//    private RelativeLayout rela_drop;//下拉的选择按钮
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
        //contentView = this.getLayoutInflater().inflate(R.layout.classify_layout, null);
        /*popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        rela_drop = (RelativeLayout) this.findViewById(R.id.rela_top);*/
        fragmentManager = getSupportFragmentManager();

        /**
         *设置ActionBar的自定义布局。
         */
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view_actionbar = inflator.inflate(R.layout.actionbar_layout, null);
        setActionBarLayout(view_actionbar);
        init_button();
        initMDrawer();
        initFragment();

        /**
         * 初始化 acState 为true
         */
        acState = true;
        //  dropState = true;

        searchLayout = (LinearLayout) findViewById(R.id.search_layout);

        fragmentManager.beginTransaction()
                .add(R.id.container, new MainFragment()).commit();


    }

    /**
     * 初始化各个fragment
     */
    private void initFragment() {
//        lists = new ArrayList<Fragment>();
//        Fragment fg3 = new FragmentSetting();
//        lists.add(fg1);
//        lists.add(fg2);
//        lists.add(fg3);
    }

    void initMDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelative = (RelativeLayout) findViewById(R.id.navdrawer);


        final DrawerArrowDrawable drawerArrow = new DrawerArrowDrawable(this) {
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
                //             listView.setEnabled(true);
                actionsearch.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);
                View view = frameLayout.getChildAt(0);
                view.setClickable(false);

//                listView.setEnabled(false);//设置不可点击
                actionsearch.setVisibility(View.INVISIBLE);//搜索按钮消失
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


    //初始化侧滑的按钮
    void init_button() {
        Button bt_game = (Button) this.findViewById(R.id.bt_games);
        Button bt_set = (Button) this.findViewById(R.id.bt_set);
        Button bt_news = (Button) this.findViewById(R.id.bt_news);
        Button bt_my = (Button) this.findViewById(R.id.bt_my);
        Button bt_make = (Button) this.findViewById(R.id.bt_make);
        Button bt_hot = (Button) this.findViewById(R.id.bt_hot);
        Button bt_head = (Button) this.findViewById(R.id.bt_head);
        searchButton = (Button) findViewById(R.id.search_button);
        /**
         * 定义ActionBar上的搜索按钮,并设置监听
         */
        actionsearch = (Button) view_actionbar.findViewById(R.id.action_search);
        actionsearch.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        /*bt_dropdown = (Button) this.findViewById(R.id.arrow);*/
        bt_game.setOnClickListener(this);
        bt_set.setOnClickListener(this);
        bt_news.setOnClickListener(this);
        bt_my.setOnClickListener(this);
        bt_make.setOnClickListener(this);
        bt_hot.setOnClickListener(this);
        bt_head.setOnClickListener(this);
        /*bt_dropdown.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {
        int tag = v.getId();
        switch (tag) {

            case R.id.bt_head:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                Timer timer3 = new Timer(true);
                TimerTask timerTask3 = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, InfoPersonActivity.class);
                        startActivity(intent);
                    }
                };
                timer3.schedule(timerTask3,200);
                break;
            case R.id.bt_set:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("设置");
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentSetting()).commit();
                break;
            case R.id.bt_make:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("创建队伍");
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new BuildTeamFragment()).commit();
                break;
            case R.id.bt_my:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                Timer timer = new Timer(true);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent();
                        intent1.setClass(MainActivity.this,MyTeamActivity.class);
                        intent1.putExtra("identity","队长");
                        startActivity(intent1);
                    }
                };
                timer.schedule(timerTask,200);
                /*setActionBarTest("我的队伍");*/

                break;
            case R.id.bt_news:
                mDrawerLayout.closeDrawer(mDrawerRelative);
               /* setActionBarTest("我的消息");*/
                //延迟800毫秒，让侧边栏完全收回时再开新的Activity
                Timer timer1 = new Timer(true);
                TimerTask timerTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent2 = new Intent();
                        intent2.setClass(MainActivity.this,MyMessageActivity.class);
                        startActivity(intent2);
                    }
                };
                timer1.schedule(timerTask1,200);
                break;
            case R.id.bt_games:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("所有比赛");
                break;
            case R.id.bt_hot:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                setActionBarTest("热门赛事");
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MainFragment()).commit();
                break;
            case R.id.action_search:
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


