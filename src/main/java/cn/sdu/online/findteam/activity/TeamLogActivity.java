package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.sdu.online.findteam.R;

public class TeamLogActivity extends Activity implements View.OnClickListener{

    private Button back/*addview,*/;
    private LinearLayout discuss_item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.teamlog_actionbar);
        setContentView(R.layout.teamlog_content_layout);

        /*addview = (Button) findViewById(R.id.add_view);*/
        back = (Button) findViewById(R.id.teamlog_back_bt);
        discuss_item = (LinearLayout) findViewById(R.id.discuss_item_linearlayout);

       /* addview.setOnClickListener(this);*/
        back.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
  /*          case R.id.add_view:
                LayoutInflater inflater = LayoutInflater.from(TeamLogActivity.this);
                View view = inflater.inflate(R.layout.teamlog_content_discuss,null);
                discuss_item.addView(view);
              *//*  TeamLogActivity.this.setContentView(R.layout.teamlog_content_layout);*//*
                break;*/

            case R.id.teamlog_back_bt:
                TeamLogActivity.this.finish();
                break;
            default:
                break;
        }

    }
}
