package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.w3c.dom.Text;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.share.MyApplication;

public class TeamLogActivity extends BaseActivity implements View.OnClickListener {

    private Button back, push;
    private LinearLayout discuss_item;
    private EditText editText;
    private ScrollView scrollView;
    private ImageView header;
    private TextView name, time, content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.teamlog_actionbar);
        setContentView(R.layout.teamlog_content_layout);

        findById();
        setData();
    }

    private void findById(){
        back = (Button) findViewById(R.id.teamlog_back_bt);
        discuss_item = (LinearLayout) findViewById(R.id.discuss_item_linearlayout);
        editText = (EditText) findViewById(R.id.write_discuss);
        push = (Button) findViewById(R.id.push_discuss);
        scrollView = (ScrollView) findViewById(R.id.teamlog_scrollview);
        header = (ImageView) findViewById(R.id.teamlog_userhead);
        name = (TextView) findViewById(R.id.teamlog_username_tv);
        content = (TextView) findViewById(R.id.teamlog_content_tv);
        time = (TextView) findViewById(R.id.teamlog_time_tv);

        back.setOnClickListener(this);
        push.setOnClickListener(this);
    }

    private void setData(){
        String imgPath = getIntent().getStringExtra("imgPath");
        String nameStr = getIntent().getStringExtra("name");
        String timeStr = getIntent().getStringExtra("time");
        String contentStr = getIntent().getStringExtra("content");

        ImageLoader.ImageListener imageListener = MyApplication.imageLoader.getImageListener(
                header, R.drawable.head_moren, R.drawable.head_moren
        );
        MyApplication.imageLoader.get(imgPath, imageListener);
        name.setText(nameStr);
        content.setText(contentStr);
        time.setText(timeStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.push_discuss:
                if (editText.getText().toString().trim().length() != 0) {
                    String disc = editText.getText().toString();

                    LayoutInflater inflater = LayoutInflater.from(TeamLogActivity.this);
                    View view = inflater.inflate(R.layout.teamlog_content_discuss, null);
                    TextView textView = (TextView) view.findViewById(R.id.log_discuss_tv);
                    textView.setText(disc);

                    discuss_item.addView(view);
                  /*  TeamLogActivity.this.setContentView(R.layout.teamlog_content_layout);*/
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                editText.setText("");
                break;

            case R.id.teamlog_back_bt:
                TeamLogActivity.this.finish();
                break;
     /*       case R.id.write_discuss:
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                break;*/
            default:
                break;
        }

    }
}
