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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.MessageBuilder;
import com.alibaba.wukong.im.MessageContent;
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

public class OtherTeamActivity extends FragmentActivity implements View.OnClickListener {
    public static Context mContext;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private OtherTeamFragmentAdapter mFragmentAdapter;

    private ViewPager mPageVp;
    /**
     * Tab显示内容TextView
     */
    private TextView mTeamInfoTv, mTeamLogTv, mTeamMemTv, mTeamName, mTeamIntroduce;
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
    private Button join_btn;
    View contentView;
    Dialog dialog;
    String imgPath, name;
    String teamID;
    long userOpenID;

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
            if (dialog != null) {
                dialog.dismiss();
            }
            return;
        }
        contentView = View.inflate(this, R.layout.otherteam_layout, null);
/*        setContentView(R.layout.otherteam_layout);*/
        mContext = OtherTeamActivity.this;
        teamID = OtherTeamActivity.this.getIntent().
                getExtras().getString("teamID");
        userOpenID = OtherTeamActivity.this.getIntent().
                getExtras().getLong("userOpenId");
        findById();
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("team.id", teamID));
                try {
                    String jsonData = new NetCore().getResultWithCookies(NetCore.getOneTeamAddr,
                            params);
                    Log.v("UploadUtil122", jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    name = jsonObject.getString("name");
                    imgPath = jsonObject.getString("imgPath");
/*                    int maxNum = jsonObject.getInt("maxNum");
                    int currentNum = jsonObject.getInt("currentNum");*/
                    if (name.length() != 0) {
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
        if (MyApplication.ohterTeam_CurrentPage == 0) {
            mPageVp.setCurrentItem(0);
        } else if (MyApplication.ohterTeam_CurrentPage == 1) {
            mPageVp.setCurrentItem(1);
        } else {
            mPageVp.setCurrentItem(2);
        }
    }

    Handler loadTeam = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTeamName.setText(name);
            loadBitmap(imgPath);
/*            mTeamIntroduce.setText();*/
            if (dialog != null) {
                dialog.dismiss();
            }
            OtherTeamActivity.this.setContentView(contentView);

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
        mTeamName = (TextView) contentView.findViewById(R.id.team_name);
        mTeamIntroduce = (TextView) contentView.findViewById(R.id.team_introduce);
        mTeamLogTv = (TextView) contentView.findViewById(R.id.id_teamlog_tv);
        mTeamInfoTv = (TextView) contentView.findViewById(R.id.id_teaminfo_tv);
        mTeamMemTv = (TextView) contentView.findViewById(R.id.id_teammem_tv);
        mTabLineIv = (ImageView) contentView.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) contentView.findViewById(R.id.id_page_vp);
        imageView = (RoundImageView) contentView.findViewById(R.id.imageView);
        relativeLayout = (RelativeLayout) contentView.findViewById(R.id.otherteam_head_background);
        backimg = (ImageView) contentView.findViewById(R.id.otherteam_back_img);
        backimg.setOnClickListener(OtherTeamActivity.this);
        join_btn = (Button) contentView.findViewById(R.id.join_otherteam);
        join_btn.setOnClickListener(OtherTeamActivity.this);
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
                        MyApplication.ohterTeam_CurrentPage = 0;
                        break;
                    case 1:
                        mTeamMemTv.setTextColor(Color.rgb(80, 154, 255));
                        MyApplication.ohterTeam_CurrentPage = 1;
                        break;
                    case 2:
                        mTeamLogTv.setTextColor(Color.rgb(80, 154, 255));
                        MyApplication.ohterTeam_CurrentPage = 2;
                        break;
                }
                currentIndex = position;
            }
        });
        mPageVp.setOffscreenPageLimit(2);
        setTabListener();
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

        if (MyApplication.ohterTeam_CurrentPage == 1) {
            lp.setMargins(screenWidth / 3, 0, 0, 0);
        } else if (MyApplication.ohterTeam_CurrentPage == 2) {
            mPageVp.setCurrentItem(2);
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
        teaminfo_ll.setOnClickListener(OtherTeamActivity.this);

        teammem_ll.setOnClickListener(OtherTeamActivity.this);

        teamlog_ll.setOnClickListener(OtherTeamActivity.this);
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otherteam_back_img:
                OtherTeamActivity.this.finish();
                break;

            case R.id.join_otherteam:
                dialog = DialogDefine.createLoadingDialog(OtherTeamActivity.this, "");
                dialog.show();

                new Thread(){
                    @Override
                    public void run() {
                        String jsonData = null;
                        try {
                            jsonData = new NetCore().joinTeam(teamID);
                            JSONObject jsonObject = new JSONObject(jsonData);
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", jsonObject.getString("msg"));
                            Message message = new Message();
                            message.setData(bundle);
                            jointeamHander.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
                break;

            case R.id.id_teaminfo_ll:
                mPageVp.setCurrentItem(0);
                MyApplication.ohterTeam_CurrentPage = 0;
                break;

            case R.id.id_teammem_ll:
                mPageVp.setCurrentItem(1);
                MyApplication.ohterTeam_CurrentPage = 1;
                break;

            case R.id.id_tab_teamlog_ll:
                mPageVp.setCurrentItem(2);
                MyApplication.ohterTeam_CurrentPage = 3;
                break;
        }
    }

    Handler jointeamHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String jsonMsg = bundle.getString("msg", "出现异常错误！");
            if (jsonMsg.equals("您的请求已通过")) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                AndTools.showToast(OtherTeamActivity.this, jsonMsg);
                Intent intent = new Intent(OtherTeamActivity.this, MyTeamActivity.class);
                intent.putExtra("teamID", teamID);
                MyApplication.IDENTITY = "队员";
                OtherTeamActivity.this.startActivity(intent);
                overridePendingTransition(0, 0);
                OtherTeamActivity.this.finish();
            } else if (jsonMsg.equals("您的请求已申请")) {
                String sysMsg = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("loginName", "");
                String joinMsg = sysMsg + "  " + "申请加入你的队伍" + "  " + name;

                final com.alibaba.wukong.im.Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(joinMsg);

                IMEngine.getIMService(ConversationService.class).createConversation(new com.alibaba.wukong.Callback<Conversation>() {

                    @Override
                    public void onSuccess(Conversation conversation) {
                        //ToDo 在这处理创建成功的会话： conversation
                        message.sendTo(conversation, backMsg);
                        conversation.removeAndClearMessage();
                    }

                    @Override
                    public void onException(String code, String reason) {
                        //会话创建失败异常处理
                    }

                    @Override
                    public void onProgress(Conversation data, int progress) {
                        // Do Nothing
                    }
                }, "<#$_*/ + join + /*_$#>"+teamID, null, message, Conversation.ConversationType.GROUP, userOpenID);
            }
            else {
                if (dialog != null) {
                    dialog.dismiss();
                }
                AndTools.showToast(OtherTeamActivity.this, jsonMsg);
            }
        }
    };

    Callback<com.alibaba.wukong.im.Message> backMsg = new Callback<com.alibaba.wukong.im.Message>() {
        @Override
        public void onSuccess(com.alibaba.wukong.im.Message message) {
            MessageContent msgContent = message.messageContent();
            Log.v("TAG12313212313", "消息内容：" + msgContent.toString());
            if (dialog != null) {
                dialog.dismiss();
            }
            AndTools.showToast(OtherTeamActivity.this, "您的请求已申请");
        }

        @Override
        public void onException(String s, String s1) {
            Log.v("TAG12313212313", "code=" + s + " reason=" + s1);
        }

        @Override
        public void onProgress(com.alibaba.wukong.im.Message message, int i) {

        }
    };
}
