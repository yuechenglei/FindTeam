package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.AllGamesFragment;
import cn.sdu.online.findteam.fragment.BuildTeamFragment;
import cn.sdu.online.findteam.fragment.FragmentSetting;
import cn.sdu.online.findteam.fragment.MainFragment;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.Time;
import cn.sdu.online.findteam.view.ActionBarDrawerToggle;
import cn.sdu.online.findteam.view.DrawerArrowDrawable;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static MainActivity mainActivity;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelative;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    /**
     * 记录actionsearch按钮的点击状态，true为不搜索状态，false为搜索栏弹出状态
     */
    private boolean acState;
    /**
     * 主界面搜索按钮
     */
    private Button actionsearch;
    private Button searchButton;
    // 主界面的搜索框布局
    private LinearLayout searchLayout;
    // 导航栏
    private View view_actionbar;

    private LinearLayout mVisitorDrawerLayout;
    // 获取intent传入的字符串和Id号
    private String intentString;
    private int Id;
    // 侧边栏用户名
    private TextView tv_text;

    MainFragment mainFragment;
    BuildTeamFragment buildTeamFragment;
    FragmentSetting fragmentSetting;
    AllGamesFragment allGamesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;

        intentString = getIntent().getExtras().getString("loginIdentity");

        fragmentManager = getSupportFragmentManager();
        // 设置ActionBar的自定义布局。
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view_actionbar = inflator.inflate(R.layout.actionbar_layout, null);
        setActionBarLayout(view_actionbar);

        if (intentString.startsWith("<##用户##>")) {
            Log.v("用户", intentString);
            ViewStub viewStub = (ViewStub) findViewById(R.id.drawer_viewstub);
            viewStub.setLayoutResource(R.layout.drawer_layout);
            viewStub.inflate();
            mDrawerRelative = (RelativeLayout) findViewById(R.id.navdrawer);
            initMDrawer();
            init_button();
            tv_text = (TextView) findViewById(R.id.tv_name);
            tv_text.setText(intentString.substring(8));
        } else {
            Log.v("游客", intentString);
            ViewStub viewStub = (ViewStub) findViewById(R.id.drawer_viewstub);
            viewStub.setLayoutResource(R.layout.visitor_drawer_layout);
            viewStub.inflate();
            mVisitorDrawerLayout = (LinearLayout) findViewById(R.id.visitor_drawer_layout);
            initMDrawer();
            init_Visitor_Btn();
        }
        // 初始化 acState 为true
        acState = true;

        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        if (fragmentManager.findFragmentByTag("mainfragment") == null
                && fragmentManager.findFragmentByTag("fragmentsetting") == null
                && fragmentManager.findFragmentByTag("buildteamfragment") == null
                && fragmentManager.findFragmentByTag("allgamefragment") == null) {
            mainFragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, mainFragment, "mainfragment").commit();
        }

        MyApplication.IDENTITY = "队长";
    }

    void initMDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mVisitorDrawerLayout = (LinearLayout) findViewById(R.id.visitor_drawer_layout);

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
            if (mDrawerRelative != null) {
                if (mDrawerLayout.isDrawerOpen(mDrawerRelative)) {
                    mDrawerLayout.closeDrawer(mDrawerRelative);

                } else {
                    mDrawerLayout.openDrawer(mDrawerRelative);

                }
            } else {
                if (mDrawerLayout.isDrawerOpen(mVisitorDrawerLayout)) {
                    mDrawerLayout.closeDrawer(mVisitorDrawerLayout);
                } else {
                    mDrawerLayout.openDrawer(mVisitorDrawerLayout);
                }
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

    //初始化用户的侧滑的按钮
    void init_button() {
        Button bt_game = (Button) this.findViewById(R.id.bt_games);
        Button bt_set = (Button) this.findViewById(R.id.bt_set);
        Button bt_news = (Button) this.findViewById(R.id.bt_news);
        Button bt_my = (Button) this.findViewById(R.id.bt_my);
        Button bt_make = (Button) this.findViewById(R.id.bt_make);
        Button bt_hot = (Button) this.findViewById(R.id.bt_hot);
        Button bt_head = (Button) this.findViewById(R.id.bt_head);
        bt_head.setBackgroundResource(R.drawable.head_moren);

        searchButton = (Button) findViewById(R.id.search_button);
        actionsearch = (Button) view_actionbar.findViewById(R.id.action_search);
        actionsearch.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        bt_game.setOnClickListener(this);
        bt_set.setOnClickListener(this);
        bt_news.setOnClickListener(this);
        bt_my.setOnClickListener(this);
        bt_make.setOnClickListener(this);
        bt_hot.setOnClickListener(this);
        bt_head.setOnClickListener(this);
    }

    private void init_Visitor_Btn() {
        Button visitorLogin = (Button) findViewById(R.id.visitor_login_btn);
        LinearLayout visitorAllGame = (LinearLayout) findViewById(R.id.visitor_allgame_btn);
        LinearLayout visitorHotGame = (LinearLayout) findViewById(R.id.visitor_hotgame_btn);
        LinearLayout visitorSetting = (LinearLayout) findViewById(R.id.visitor_setting_btn);

        searchButton = (Button) findViewById(R.id.search_button);
        actionsearch = (Button) view_actionbar.findViewById(R.id.action_search);
        actionsearch.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        visitorAllGame.setOnClickListener(this);
        visitorHotGame.setOnClickListener(this);
        visitorLogin.setOnClickListener(this);
        visitorSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int tag = v.getId();
        switch (tag) {
            case R.id.bt_head:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                Timer timer1 = new Timer(true);
                TimerTask timerTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, InfoPersonActivity.class);
                        startActivity(intent);
                    }
                };
                timer1.schedule(timerTask1, 200);
                break;
            case R.id.bt_set:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                if (fragmentSetting == null) {
                    fragmentSetting = new FragmentSetting();
                }
                Timer timer2 = new Timer(true);
                TimerTask timerTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragmentSetting, "fragmentsetting").commit();
                    }
                };
                timer2.schedule(timerTask2, 200);
                setActionBarTest("设置");
                break;
            case R.id.bt_make:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                if (buildTeamFragment == null){
                    buildTeamFragment = new BuildTeamFragment();
                }
                Timer timer3 = new Timer(true);
                TimerTask timerTask3 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, buildTeamFragment, "buildteamfragment").commit();
                    }
                };
                timer3.schedule(timerTask3, 200);
                setActionBarTest("创建队伍");
                break;
            case R.id.bt_my:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                Timer timer4 = new Timer(true);
                TimerTask timerTask4 = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent();
                        intent1.setClass(MainActivity.this, MyTeamActivity.class);
                        startActivity(intent1);
                    }
                };
                timer4.schedule(timerTask4, 200);
                setActionBarTest("我的队伍");
                break;

            case R.id.bt_news:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                //延迟200毫秒，让侧边栏完全收回时再开新的Activity
                Timer timer5 = new Timer(true);
                TimerTask timerTask5 = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent2 = new Intent();
                        intent2.setClass(MainActivity.this, MyMessageActivity.class);
                        startActivity(intent2);
                    }
                };
                timer5.schedule(timerTask5, 200);
                break;
            case R.id.bt_games:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                if (allGamesFragment == null){
                    allGamesFragment = new AllGamesFragment();
                }
                Timer timer6 = new Timer(true);
                TimerTask timerTask6 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction().
                                replace(R.id.container, allGamesFragment,"allgamefragment").commit();
                    }
                };
                timer6.schedule(timerTask6, 200);
                setActionBarTest("所有比赛");
                break;
            case R.id.bt_hot:
                mDrawerLayout.closeDrawer(mDrawerRelative);
                if (mainFragment == null){
                    mainFragment = new MainFragment();
                }
                Timer timer9 = new Timer(true);
                TimerTask timerTask9 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mainFragment, "mainfragment").commit();
                    }
                };
                timer9.schedule(timerTask9, 200);
                setActionBarTest("热门赛事");
                break;
            case R.id.action_search:
                if (acState) {
                    searchLayout.setVisibility(View.VISIBLE);
                    acState = false;
                } else {
                    searchLayout.setVisibility(View.GONE);
                    acState = true;
                }
                break;

            case R.id.visitor_allgame_btn:
                mDrawerLayout.closeDrawer(mVisitorDrawerLayout);
                if (allGamesFragment == null){
                    allGamesFragment = new AllGamesFragment();
                }
                Timer timer8 = new Timer(true);
                TimerTask timerTask8 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction().
                                replace(R.id.container, allGamesFragment,"allgamefragment").commit();
                    }
                };
                timer8.schedule(timerTask8, 200);
                setActionBarTest("所有比赛");
                break;

            case R.id.visitor_hotgame_btn:
                mDrawerLayout.closeDrawer(mVisitorDrawerLayout);
                if (mainFragment == null){
                    mainFragment = new MainFragment();
                }
                Timer timer10 = new Timer(true);
                TimerTask timerTask10 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mainFragment, "mainfragment").commit();
                    }
                };
                timer10.schedule(timerTask10, 200);
                setActionBarTest("热门赛事");
                break;

            case R.id.visitor_setting_btn:
                mDrawerLayout.closeDrawer(mVisitorDrawerLayout);
                if (fragmentSetting == null){
                    fragmentSetting = new FragmentSetting();
                }
                Timer timer7 = new Timer(true);
                TimerTask timerTask7 = new TimerTask() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragmentSetting, "fragmentsetting").commit();
                    }
                };
                timer7.schedule(timerTask7, 200);
                setActionBarTest("设置");
                break;

            case R.id.visitor_login_btn:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            default:
                break;
        }

    }

    @Override
    public void finish() {
        super.finish();
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

            if (mDrawerRelative != null) {
                if (mDrawerLayout.isDrawerOpen(mDrawerRelative)) {
                    mDrawerLayout.closeDrawer(mDrawerRelative);
                } else {
                    if (fragmentManager.findFragmentByTag("mainfragment") == null) {
                        fragmentManager.beginTransaction().replace(R.id.container,
                                mainFragment, "mainfragment").commit();
                    } else {
                        onBackPressed(); // 调用双击退出函数
                    }
                }
            } else {
                if (mDrawerLayout.isDrawerOpen(mVisitorDrawerLayout)) {
                    mDrawerLayout.closeDrawer(mVisitorDrawerLayout);
                } else {
                    if (fragmentManager.findFragmentByTag("mainfragment") == null) {
                        fragmentManager.beginTransaction().replace(R.id.container,
                                mainFragment, "mainfragment").commit();
                    } else {
                        onBackPressed(); // 调用双击退出函数
                    }
                }
            }

        }
        return false;
    }

}


