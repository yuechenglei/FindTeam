package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.BuildTeamFragment;
import cn.sdu.online.findteam.resource.RoundImageView;
import cn.sdu.online.findteam.util.ChangeHeader;


public class InfoPersonActivity extends Activity implements View.OnClickListener {
    public Button bt_return;
    public EditText text_nickname, text_introduction, text_tag1, text_tag2, text_tag3, text_realname,
            text_gender, text_dress, text_school, text_phonenumber, text_email;
    public TextView text_edit;
    public Boolean isEdited;

    private RelativeLayout relativeLayout;
    private RoundImageView head;

    private ChangeHeader changeHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info_person);

        isEdited = false;
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
        head = (RoundImageView) findViewById(R.id.head);
        relativeLayout = (RelativeLayout) findViewById(R.id.info_person_headlayout);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                getWindowManager().getDefaultDisplay().getWidth()));

        text_edit.setOnClickListener(this);
        bt_return.setOnClickListener(this);
        head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_edit:
                isEdited = !isEdited;
                if (isEdited) {
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
                    text_edit.setTextColor(Color.rgb(80, 154, 255));
                }
                break;

            case R.id.bt_return:
                InfoPersonActivity.this.finish();
                break;

            case R.id.head:
                if (isEdited) {
                    final String[] itemsfirst = {"修改个人头像", "查看大图"};// 第一级条目列表
                    new AlertDialog.Builder(InfoPersonActivity.this, R.style.AlertDialogCustom)// 建立对话框
                            .setItems(itemsfirst, new DialogInterface.OnClickListener() {// 以下为监听
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (which == 0) {
                                        final String[] itemssecond = {"相册", "相机"}; // 第二级条目列表
                                        new AlertDialog.Builder(InfoPersonActivity.this, R.style.AlertDialogCustom)// 建立对话框
                                                .setTitle("请选择方式")// 标题
                                                .setItems(itemssecond, new DialogInterface.OnClickListener() {// 以下为监听

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        if (which == 0) {
                                                            // 相册
                                                            changeHeader =
                                                                    new ChangeHeader(InfoPersonActivity.this,
                                                                            head, ChangeHeader.PERSONHEADER);
                                                            changeHeader.chooseAlbum();
                                                        }
                                                        if (which == 1) {
                                                            // 相机
                                                            changeHeader =
                                                                    new ChangeHeader(InfoPersonActivity.this,
                                                                            head, ChangeHeader.PERSONHEADER);
                                                            changeHeader.chooseCamera();
                                                        }
                                                    }
                                                }).show();
                                    }
                                    if (which == 1) {
                                        Intent intent = new Intent();
                                        intent.setClass(InfoPersonActivity.this, ImgShowerActivity.class);
                                        intent.putExtra("bitmap", getBytes(head.getBmp()));
                                        InfoPersonActivity.this.startActivity(intent);
                                        overridePendingTransition(R.anim.zoomin, 0);
                                    }
                                }
                            }).show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(InfoPersonActivity.this, ImgShowerActivity.class);
                    intent.putExtra("bitmap", getBytes(head.getBmp()));
                    InfoPersonActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, 0);
                }
                break;
        }
    }

    public byte[] getBytes(Bitmap bitmap) {
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BuildTeamFragment.PHOTO_REQUEST:// 相册返回
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        changeHeader.setHeaderImgAlbum();
                        break;
                }
                break;

            case BuildTeamFragment.CAMERA_REQUEST:// 照相返回

                switch (resultCode) {
                    case Activity.RESULT_OK:// 照相完成点击确定
                        changeHeader.getHeaderImgCamera();
                        break;

                    case Activity.RESULT_CANCELED:// 取消
                        break;
                }
                break;

            case BuildTeamFragment.CAMERA_CUT_REQUEST:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        changeHeader.setHeaderImgCamera(data);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}