package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sdu.online.findteam.R;

public class WriteActivity extends Activity implements View.OnClickListener{

    private String getsign;
    private TextView textView;
    private Button back;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setActionBarLayout(R.layout.writeactivity_actionbar);
        textView = (TextView) findViewById(R.id.writeactivity_ab_text);
        handle();
        setContentView(R.layout.writeactivity_layout);

        back = (Button) findViewById(R.id.writeactivity_backbt);
        imageView = (ImageView) findViewById(R.id.write_finish_bt);

        back.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

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

    private void handle(){
        getsign = WriteActivity.this.getIntent().getExtras().getString("sign");
        if (getsign.equals("编辑队伍信息")){
            textView.setText("编辑队伍信息");
        }
        else if (getsign.equals("写日志")){
            textView.setText("写日志");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.writeactivity_backbt:
                WriteActivity.this.finish();
                break;
            case R.id.write_finish_bt:
                break;
        }
    }
}
