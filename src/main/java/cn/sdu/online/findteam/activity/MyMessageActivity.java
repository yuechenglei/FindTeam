package cn.sdu.online.findteam.activity;


import java.util.ArrayList;
import java.util.List;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import cn.sdu.online.findteam.view.BadgeView;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.ChatMainFragment;
import cn.sdu.online.findteam.fragment.FriendMainFragment;


public class MyMessageActivity extends FragmentActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    private int mScreen1_2;

    private int mCurrentPageIndex;
    private ImageView mTabline;
    private TextView mChatTextView;
    private TextView mFriendTextView;

    private LinearLayout friend;
    private LinearLayout chat;
    private LinearLayout searchlayout;

    private Button back;
    private Button actionsearch;

    private boolean state;

    public static BadgeView badgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setActionBarLayout(R.layout.mymessage_actionbar);
        setContentView(R.layout.mymessageactivity_layout);
        state = false;
        initTabLine();
        initView();
    }

    private void initTabLine() {
        mTabline = (ImageView) findViewById(R.id.id_invite_tabline);
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        mScreen1_2 = dpMetrics.widthPixels / 2;
        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mTabline
                .getLayoutParams();
        lp1.width = mScreen1_2;
        mTabline.setLayoutParams(lp1);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mChatTextView = (TextView) findViewById(R.id.id_tv_chat);
        mFriendTextView = (TextView) findViewById(R.id.id_tv_friend);
        friend = (LinearLayout) findViewById(R.id.mymessage_friend_tab);
        chat = (LinearLayout) findViewById(R.id.mymessage_chat_tab);
        back = (Button) findViewById(R.id.mymessage_back_btn);
        actionsearch = (Button) findViewById(R.id.mymessage_actionsearch);
        searchlayout = (LinearLayout) findViewById(R.id.mymessage_search_layout);

        friend.setOnClickListener(this);
        chat.setOnClickListener(this);
        back.setOnClickListener(this);
        actionsearch.setOnClickListener(this);

        mDatas = new ArrayList<Fragment>();
        ChatMainFragment tab02 = new ChatMainFragment();
        FriendMainFragment tab01 = new FriendMainFragment();
        mDatas.add(tab01);
        mDatas.add(tab02);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mDatas.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                resetTextView();
                switch (position) {
                    case 1:
                        mChatTextView.setTextColor(Color.parseColor("#509aff"));
                        break;
                    case 0:
                        mFriendTextView.setTextColor(Color.parseColor("#509aff"));
                        break;

                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPx) {
                // TODO Auto-generated method stub
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline
                        .getLayoutParams();

                if (mCurrentPageIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * mScreen1_2 + mCurrentPageIndex
                            * mScreen1_2);
                } else if (mCurrentPageIndex == 1 && position == 0)// 1->0
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_2 + (positionOffset - 1)
                            * mScreen1_2);
                }
                mTabline.setLayoutParams(lp);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        badgeView = new BadgeView(this);
        badgeView.setBadgeCount(0);
        badgeView.setBackgroundResource(R.drawable.badgeview_bg);
        chat.addView(badgeView);
        badgeView.setVisibility(View.GONE);
    }

    public static int getCount(){
        return badgeView.getBadgeCount();
    }

    protected void resetTextView() {
        mChatTextView.setTextColor(Color.BLACK);
        mFriendTextView.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mymessage_friend_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.mymessage_chat_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.mymessage_back_btn:
                MyMessageActivity.this.finish();
                break;
            case R.id.mymessage_actionsearch:
                if (state == false) {
                    searchlayout.setVisibility(View.VISIBLE);
                    state = true;
                }
                else {
                    searchlayout.setVisibility(View.GONE);
                    state = false;
                }

        }
    }

    /**
     * @param layoutId 布局Id
     */
    public void setActionBarLayout(int layoutId) {
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(layoutId, null);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionMenuView.LayoutParams.FILL_PARENT, ActionMenuView.LayoutParams.FILL_PARENT);
            actionBar.setCustomView(v, layout);
        }
    }
}
