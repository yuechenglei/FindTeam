package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.wukong.auth.AuthService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

/**
 * Created by wn on 2015/10/2.
 */
public class ChangePassActivity extends BaseActivity implements View.OnClickListener {

    EditText oldPass_Edit, newPass_Edit, surePass_Edit;
    Button change_Btn;
    Dialog dialog;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.singlecompetition_actionbar_layout);
        TextView title = (TextView) findViewById(R.id.singleGame_actionbar_tv);
        title.setText("修改密码");
        setContentView(R.layout.change_pass_layout);

        initView();
    }

    private void initView() {
        oldPass_Edit = (EditText) findViewById(R.id.oldPass_edit);
        newPass_Edit = (EditText) findViewById(R.id.newPass_edit);
        change_Btn = (Button) findViewById(R.id.changePass_btn);
        surePass_Edit = (EditText) findViewById(R.id.surePass_edit);
        change_Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dialog = DialogDefine.createLoadingDialog(ChangePassActivity.this, "");
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!AndTools.isNetworkAvailable(ChangePassActivity.this)) {
                handler.sendEmptyMessage(-3);
            } else {
                if (isEditValue()) {
                    String oldPass = oldPass_Edit.getText().toString();
                    String newPass = newPass_Edit.getText().toString();
                    String confirmPass = surePass_Edit.getText().toString();

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("usr.password", oldPass));
                    params.add(new BasicNameValuePair("usr.newPassword", newPass));
                    params.add(new BasicNameValuePair("usr.confirm", confirmPass));
                    try {
                        String data = new NetCore().getResultWithCookies(NetCore.modifyUserInfoAddr, params);
                        JSONObject jsonObject = new JSONObject(data);
                        int code = jsonObject.getInt("code");
                        msg = jsonObject.getString("msg");
                        handler.sendEmptyMessage(code);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private boolean isEditValue() {
        if (oldPass_Edit.getText().toString().trim().length() == 0) {
            handler.sendEmptyMessage(-2);
            return false;
        }
        if (newPass_Edit.getText().toString().trim().length() == 0) {
            handler.sendEmptyMessage(-1);
            return false;
        }
        if (surePass_Edit.getText().toString().trim().length() == 0) {
            handler.sendEmptyMessage(-4);
            return false;
        }
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (dialog != null) {
                dialog.dismiss();
            }

            switch (message.what) {
                case -3:
                    AndTools.showToast(ChangePassActivity.this, "当前网络不可用！");
                    break;

                case -2:
                    AndTools.showToast(ChangePassActivity.this, "请输入原密码！");
                    break;

                case -1:
                    AndTools.showToast(ChangePassActivity.this, "请输入新密码！");
                    break;

                case -4:
                    AndTools.showToast(ChangePassActivity.this, "请确认密码！");
                    break;

                case 0:
                    AndTools.showToast(ChangePassActivity.this, msg);
                    exitSuccess();
                    Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ChangePassActivity.this.startActivity(intent);
                    break;

                default:
                    AndTools.showToast(ChangePassActivity.this, msg);
                    break;
            }
        }
    };

    protected void exitSuccess() {
        AuthService.getInstance().logout();
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
