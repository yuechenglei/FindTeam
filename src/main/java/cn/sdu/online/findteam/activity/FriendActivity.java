package cn.sdu.online.findteam.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.FriendFragment;
import cn.sdu.online.findteam.fragment.MessageFragment;

public class FriendActivity extends FragmentActivity implements OnGestureListener {

    private GestureDetector mDetector;
    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        layoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("首页").setIndicator("首页"), FriendFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("聊天").setIndicator("聊天"), MessageFragment.class, null);

        mDetector = new GestureDetector(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        int currentTab = this.mTabHost.getCurrentTab();
        if (currentTab < 3 && e1.getX() - e2.getX() > 120) {
            this.mTabHost.setCurrentTab(currentTab + 1);
        } else if (currentTab > 0 && e1.getX() - e2.getX() < -120) {
            this.mTabHost.setCurrentTab(currentTab - 1);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }
}
