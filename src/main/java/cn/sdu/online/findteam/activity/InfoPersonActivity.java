package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.sdu.online.findteam.R;


public class InfoPersonActivity extends Activity {
    public Button bt_return;
    public EditText text_nickname, text_introduction, text_tag1, text_tag2, text_tag3, text_realname,
            text_gender, text_dress, text_school, text_phonenumber, text_email;
    public TextView text_edit;
    public Boolean bl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info_person);

        bl = true;
        bt_return = (Button) findViewById(R.id.bt_return);
        text_nickname = (EditText) findViewById(R.id.text_nickname);
        text_introduction = (EditText) findViewById(R.id.text_introduction);
        text_tag1 = (EditText) findViewById(R.id.text_tag1);
        text_tag2 = (EditText) findViewById(R.id.text_tag2);
        text_tag3 = (EditText) findViewById(R.id.text_tag3);
        text_realname = (EditText) findViewById(R.id.text_realname);
        text_gender = (EditText) findViewById(R.id.text_gender);
        text_dress = (EditText) findViewById(R.id.text_dress);
        text_school = (EditText) findViewById(R.id.text_school);
        text_phonenumber = (EditText) findViewById(R.id.text_phonenumber);
        text_email = (EditText) findViewById(R.id.text_email);
        text_edit = (TextView) findViewById(R.id.text_edit);

        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bl) {
                    text_nickname.setFocusableInTouchMode(true);
                    text_nickname.requestFocus();
                    text_introduction.setFocusableInTouchMode(true);
                    text_introduction.requestFocus();
                    text_tag1.setFocusableInTouchMode(true);
                    text_tag1.requestFocus();
                    text_tag2.setFocusableInTouchMode(true);
                    text_tag2.requestFocus();
                    text_tag3.setFocusableInTouchMode(true);
                    text_tag3.requestFocus();
                    text_realname.setFocusableInTouchMode(true);
                    text_realname.requestFocus();
                    text_gender.setFocusableInTouchMode(true);
                    text_gender.requestFocus();
                    text_dress.setFocusableInTouchMode(true);
                    text_dress.requestFocus();
                    text_school.setFocusableInTouchMode(true);
                    text_school.requestFocus();
                    text_phonenumber.setFocusableInTouchMode(true);
                    text_phonenumber.requestFocus();
                    text_email.setFocusableInTouchMode(true);
                    text_email.requestFocus();
                    text_edit.setBackgroundColor(0xff519aff);
                    text_edit.setText("保存资料");
                    text_edit.setTextColor(Color.WHITE);
                    bl = false;
                } else {
                    text_nickname.setFocusableInTouchMode(false);
                    text_nickname.requestFocus();
                    text_nickname.clearFocus();
                    text_introduction.setFocusableInTouchMode(false);
                    text_introduction.requestFocus();
                    text_introduction.clearFocus();
                    text_tag1.setFocusableInTouchMode(false);
                    text_tag1.requestFocus();
                    text_tag1.clearFocus();
                    text_tag2.setFocusableInTouchMode(false);
                    text_tag2.requestFocus();
                    text_tag2.clearFocus();
                    text_tag3.setFocusableInTouchMode(false);
                    text_tag3.requestFocus();
                    text_tag3.clearFocus();
                    text_realname.setFocusableInTouchMode(false);
                    text_realname.requestFocus();
                    text_realname.clearFocus();
                    text_gender.setFocusableInTouchMode(false);
                    text_gender.requestFocus();
                    text_gender.clearFocus();
                    text_dress.setFocusableInTouchMode(false);
                    text_dress.requestFocus();
                    text_dress.clearFocus();
                    text_school.setFocusableInTouchMode(false);
                    text_school.requestFocus();
                    text_school.clearFocus();
                    text_phonenumber.setFocusableInTouchMode(false);
                    text_phonenumber.requestFocus();
                    text_phonenumber.clearFocus();
                    text_email.setFocusableInTouchMode(false);
                    text_email.requestFocus();
                    text_email.clearFocus();
                    text_edit.setBackgroundColor(Color.WHITE);
                    text_edit.setText("编辑资料");
                    text_edit.setTextColor(Color.rgb(80,154,255));
                    bl = true;
                }
            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}