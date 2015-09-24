package cn.sdu.online.findteam.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.OtherTeamFragmentAdapter;
import cn.sdu.online.findteam.fragment.TeamInformationFragment;
import cn.sdu.online.findteam.fragment.TeamLogFragment;
import cn.sdu.online.findteam.fragment.TeamMemberFragment;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DepthPageTransformer;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.view.RoundImageView;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;
import cn.sdu.online.findteam.view.TeamPopWindow;

public class MySingleTeamActivity extends FragmentActivity implements View.OnClickListener{
    public static Context mContext;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private OtherTeamFragmentAdapter mFragmentAdapter;

    private ViewPager mPageVp;
    /**
     * Tab显示内容TextView
     */
    private TextView mTeamInfoTv, mTeamLogTv, mTeamMemTv;
    /**
     * Fragment
     */
    private TeamInformationFragment mTeamInfoFg;
    private TeamMemberFragment mTeamMemFg;
    private TeamLogFragment mTeamLogFg;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;

    /**
     * 三个引导fragment的 Layout
     */
    private LinearLayout teaminfo_ll;
    private LinearLayout teammem_ll;
    private LinearLayout teamlog_ll;

    /**
     * 返回按钮
     */
    private ImageView backimg;
    /**
     * 参加别人队伍的按钮
     */
    private Button join;

    /**
     * 队伍设置按钮
     */
    private Button teamsetting;

    private TextView mTeamName;

    Dialog dialog;
    String name, imgPath;
    View contentView;

    RoundImageView imageView;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog = DialogDefine.createLoadingDialog(this,
                "加载中...");
        dialog.show();
        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            AndTools.showToast(this, "当前网络不可用！");
            if (dialog != null){
                dialog.dismiss();
            }
            return;
        }

        contentView = View.inflate(this, R.layout.otherteam_layout, null);

        mContext = MySingleTeamActivity.this;
        findById();

        final String teamID = MySingleTeamActivity.this.getIntent().
                getExtras().getString("teamID");
        new Thread(){
            @Override
            public void run() {
                super.run();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("team.id", teamID));
                try {
                    String jsonData = new NetCore().getResultWithCookies(NetCore.getOneTeamAddr,
                            params);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    name = jsonObject.getString("name");
                    imgPath = jsonObject.getString("imgPath");
/*                    int maxNum = jsonObject.getInt("maxNum");
                    int currentNum = jsonObject.getInt("currentNum");*/
                    if (name.length() != 0) {
                        Bundle bundle = new Bundle();
                        Message message = new Message();
                        message.setData(bundle);
                        loadTeam.sendEmptyMessage(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        init();
        initTabLineWidth();
        if (MyApplication.myTeam_CurrentPage == 0){
            mPageVp.setCurrentItem(0);
        }
        else if (MyApplication.myTeam_CurrentPage == 1){
            mPageVp.setCurrentItem(1);
        }
        else {
            mPageVp.setCurrentItem(2);
        }
    }

    Handler loadTeam = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTeamName.setText(name);
            loadBitmap(imgPath);
/*            mTeamIntroduce.setText();*/
            if (dialog != null){
                dialog.dismiss();
            }
            MySingleTeamActivity.this.setContentView(contentView);
        }
    };

    private void loadBitmap(String imgPath) {
        ImageRequest request = new ImageRequest(imgPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                relativeLayout.setBackground(new BitmapDrawable(bitmap));
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getQueues().add(request);
    }

    private void findById() {
        mTeamLogTv = (TextView) contentView.findViewById(R.id.id_teamlog_tv);
        mTeamInfoTv = (TextView) contentView.findViewById(R.id.id_teaminfo_tv);
        mTeamMemTv = (TextView) contentView.findViewById(R.id.id_teammem_tv);
        mTabLineIv = (ImageView) contentView.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) contentView.findViewById(R.id.id_page_vp);
        backimg = (ImageView) contentView.findViewById(R.id.otherteam_back_img);
        join = (Button) contentView.findViewById(R.id.join_otherteam);
        teamsetting = (Button) contentView.findViewById(R.id.team_setting_bt);
        mTeamName = (TextView) contentView.findViewById(R.id.team_name);
        imageView = (RoundImageView) contentView.findViewById(R.id.imageView);
        relativeLayout = (RelativeLayout) contentView.findViewById(R.id.otherteam_head_background);
    }

    private void init() {
        mTeamMemFg = new TeamMemberFragment();
        mTeamLogFg = new TeamLogFragment();
        mTeamInfoFg = new TeamInformationFragment();
        mFragmentList.add(mTeamInfoFg);
        mFragmentList.add(mTeamMemFg);
        mFragmentList.add(mTeamLogFg);

        mFragmentAdapter = new OtherTeamFragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);
        mPageVp.setPageTransformer(true, new DepthPageTransformer());

        teaminfo_ll = (LinearLayout) contentView.findViewById(R.id.id_teaminfo_ll);
        teammem_ll = (LinearLayout) contentView.findViewById(R.id.id_teammem_ll);
        teamlog_ll = (LinearLayout) contentView.findViewById(R.id.id_tab_teamlog_ll);

        setVisible();

        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

           /* *
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。*/

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             * **/
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0*/


                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTeamInfoTv.setTextColor(Color.rgb(80, 154, 255));
                        MyApplication.myTeam_CurrentPage = 0;
                        break;
                    case 1:
                        mTeamMemTv.setTextColor(Color.rgb(80, 154, 255));
                        MyApplication.myTeam_CurrentPage = 1;
                        break;
                    case 2:
                        mTeamLogTv.setTextColor(Color.rgb(80, 154, 255));
                        MyApplication.myTeam_CurrentPage = 2;
                        break;
                }
                currentIndex = position;
            }
        });
        mPageVp.setOffscreenPageLimit(2);
        setTabListener();
        backimg.setOnClickListener(this);
        teamsetting.setOnClickListener(this);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 3;
        if (MyApplication.myTeam_CurrentPage == 1){
            lp.setMargins(screenWidth / 3, 0, 0, 0);
        }
        else if (MyApplication.myTeam_CurrentPage == 2){
            lp.setMargins((screenWidth / 3) * 2, 0, 0, 0);
        }
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTeamInfoTv.setTextColor(Color.BLACK);
        mTeamMemTv.setTextColor(Color.BLACK);
        mTeamLogTv.setTextColor(Color.BLACK);
    }

    private void setTabListener() {
        teaminfo_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(0);
                MyApplication.myTeam_CurrentPage = 0;
            }
        });

        teammem_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(1);
                MyApplication.myTeam_CurrentPage = 1;
            }
        });

        teamlog_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(2);
                MyApplication.myTeam_CurrentPage = 2;
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    private void setVisible() {
        join.setVisibility(View.GONE);
        teamsetting.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                if (data.getExtras() != null) {
                    mTeamInfoFg.setInfor(data.getExtras().getString("teaminfor"));
                    Toast.makeText(MySingleTeamActivity.this, "队伍信息修改成功！", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                if (data.getExtras() != null){
                    mTeamLogFg.loadLog();
                    Toast.makeText(MySingleTeamActivity.this, "日志填写成功！", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.otherteam_back_img:
                MySingleTeamActivity.this.finish();
                break;

            case R.id.team_setting_bt:
                TeamPopWindow teamPopWindow = new TeamPopWindow(MySingleTeamActivity.this);
                teamPopWindow.showPopupWindow(teamsetting);
                break;
        }
    }
}