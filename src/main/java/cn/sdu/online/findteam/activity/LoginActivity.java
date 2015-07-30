package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.entity.User;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;


public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText loginname;
    private EditText loginpassword;
    private Button login;
    private Button register;
    private Dialog dialog;

    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_layout);

        initView();
    }

    private void initView() {
        loginname = (EditText) findViewById(R.id.login_name);
        loginpassword = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.loginac_login_btn);
        register = (Button) findViewById(R.id.loginac_register_btn);

        loginActivity = this;

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginac_login_btn:
                String name = loginname.getText().toString();
                String password = loginpassword.getText().toString();

                // 打开progressDialog
                dialog = DialogDefine.createLoadingDialog(LoginActivity.this,
                        "登陆中......");
                dialog.show();
                Thread loginThread = new Thread(new LoginThread(name, password));
                loginThread.start();
                break;

            case R.id.loginac_register_btn:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    class LoginThread implements Runnable {
        String name, password;

        public LoginThread(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Bundle result = startLogin(name, password);
            Message message = new Message();
            message.setData(result);
            loginHandler.sendMessage(message);
        }
    }

    private Bundle startLogin(String name, String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        String jsonResult = new NetCore().Login(user);

        int codeResult = 404;
        String messageResult = "";
        try {
            codeResult = new JSONObject(jsonResult).getInt("code");
            messageResult = new JSONObject(jsonResult).getString("msg");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("code", codeResult);
        bundle.putString("msg", messageResult);
        return bundle;
    }

    Handler loginHandler = new Handler() {
        public void handleMessage(Message message) {
            final Bundle bundle = message.getData();
            if (bundle.getInt("code") == NetCore.LOGIN_ERROR) {
                // 登录失败
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(LoginActivity.this,
                        bundle.getString("msg"), Toast.LENGTH_SHORT)
                        .show();
            } else if (bundle.getInt("code") > NetCore.LOGIN_ERROR) {
                // 登录成功
                if (dialog != null) {
                    dialog.dismiss();
                }

                Toast.makeText(LoginActivity.this,
                        bundle.getString("msg"), Toast.LENGTH_SHORT).show();

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        MainActivity.mainActivity.finish();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        intent.putExtra("loginIdentity", "<##用户##>" + loginname.getText().toString());
                        intent.putExtra("loginID", bundle.getInt("code"));
                        startActivity(intent);
                        LoginActivity.this.finish();
                        StartActivity.startActivity.finish();
                    }
                };
                timer.schedule(timerTask, 100);
            } else {
            }

        }
    };
}
