package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.auth.ALoginParam;
import com.alibaba.wukong.auth.AuthInfo;
import com.alibaba.wukong.auth.AuthService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.entity.User;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.share.DemoUtil;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;
import cn.sdu.online.findteam.util.LoginUtils;

public class OriginActivity extends Activity {

    private Intent intent;
    private String loginName;
    private String loginPassword;
    User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.originactivity_layout);

        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE);
        loginName = preferences.getString("loginName", "");
        loginPassword = preferences.getString("loginPassword", "");
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (loginName.equals("") || loginPassword.equals("")) {
                    intent = new Intent();
                    intent.setClass(OriginActivity.this, StartActivity.class);
                    startActivity(intent);
                    OriginActivity.this.finish();
                } else {
                    if (!AuthService.getInstance().isLogin()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("fail", "未初始化聊天，请选择帐号登录");
                        Message message = new Message();
                        message.setData(bundle);
                        loginHandler.sendMessage(message);
                        intent = new Intent();
                        intent.setClass(OriginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        OriginActivity.this.finish();
                    } else {
                        if (AndTools.isNetworkAvailable(MyApplication.getInstance())) {
                            new Thread(new LoginThread(loginName, loginPassword)).start();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("netError", "网络错误！");
                            Message message = new Message();
                            message.setData(bundle);
                            loginHandler.sendMessage(message);
                            intent = new Intent();
                            intent.setClass(OriginActivity.this, StartActivity.class);
                            startActivity(intent);
                            OriginActivity.this.finish();
                        }
                    }
                }
            }
        };
        timer.schedule(timerTask, 800);
    }

    Handler loginHandler = new Handler() {
        public void handleMessage(Message message) {
            final Bundle bundle = message.getData();
            if (bundle.getString("fail") != null) {
                Toast.makeText(OriginActivity.this, bundle.getString("fail"), Toast.LENGTH_SHORT).show();
            } else if (bundle.getString("netError") != null) {
                Toast.makeText(OriginActivity.this, bundle.getString("netError"), Toast.LENGTH_SHORT).show();
            } else if (bundle.getInt("code") == NetCore.LOGIN_ERROR) {
                // 登录失败
                Toast.makeText(OriginActivity.this,
                        bundle.getString("msg"), Toast.LENGTH_SHORT)
                        .show();
            } else if (bundle.getInt("code") > NetCore.LOGIN_ERROR) {
                // 登录成功
                if (bundle.getString("msg").trim().length() == 0) {
                    Toast.makeText(OriginActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                DemoUtil.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        ALoginParam params = LoginUtils.loginRequest(myUser.getName(), myUser.getPassword());
                        loginWukong(params, myUser.getName());
                    }
                });
            }
        }
    };

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
        myUser = new User();
        myUser.setName(name);
        myUser.setPassword(password);
        String jsonResult = new NetCore().Login(myUser);

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

    private void loginWukong(ALoginParam param, final String nickname) {
        AuthService.getInstance().login(param, new Callback<AuthInfo>() {
            @Override
            public void onSuccess(AuthInfo authInfo) {
                AuthService.getInstance().setNickname(nickname);

                Intent intent = new Intent();
                intent.setClass(OriginActivity.this, MainActivity.class);
/*                intent.putExtra("loginIdentity", "<##用户##>" + myUser.getName());
                intent.putExtra("loginID", preferences.getLong("loginID", 0));*/
                MyApplication.USER_OR_NOT = 1;
                AndTools.showToast(OriginActivity.this, "登陆成功");
                startActivity(intent);
                OriginActivity.this.finish();
            }

            @Override
            public void onException(String code, String reason) {
                AndTools.showToast(OriginActivity.this, "登录失败" + "  " + reason);
                Intent intent = new Intent();
                intent.setClass(OriginActivity.this, StartActivity.class);
                OriginActivity.this.finish();
            }

            @Override
            public void onProgress(AuthInfo authInfo, int i) {
            }
        });
    }
}
