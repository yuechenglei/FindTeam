package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.wukong.auth.ALoginParam;
import com.alibaba.wukong.auth.AuthInfo;
import com.alibaba.wukong.auth.AuthService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.entity.User;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.DemoUtil;
import cn.sdu.online.findteam.util.AndTools;
import cn.sdu.online.findteam.util.LoginUtils;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText registername, registerpassword, registerconfirm, registeremail;
    private Button registerbtn;

    private Dialog dialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeractivity_layout);
        initView();
    }

    private void initView() {
        registername = (EditText) findViewById(R.id.register_name);
        registerpassword = (EditText) findViewById(R.id.register_password);
        registerconfirm = (EditText) findViewById(R.id.register_confirm);
        registeremail = (EditText) findViewById(R.id.register_email);
        registerbtn = (Button) findViewById(R.id.registerac_register_btn);
        registerbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.v("输入格式", registeremail.getText().toString());
        String name = registername.getText().toString();
        String email = registeremail.getText().toString();
        String password = registerpassword.getText().toString();
        String confirm = registerconfirm.getText().toString();

        dialog = DialogDefine.createLoadingDialog(RegisterActivity.this,
                "注册中.......");
        dialog.show();
        new Thread(new RegisterThread(name, email, password, confirm)).start();
    }

    class RegisterThread implements Runnable {

        String name, email, password, confirm;

        public RegisterThread(String name, String email, String password, String confirm) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.confirm = confirm;
        }

        @Override
        public void run() {
            Bundle result = startRegister(name, email, password, confirm);
            Message message = new Message();
            message.setData(result);
            registerHandler.sendMessage(message);
        }

    }

    private Bundle startRegister(String name, String email, String password, String confirm) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirm(confirm);
        String jsonResult = new NetCore().Register(user);

        int codeResult = 404;
        String messageResult = "";
        try {
            codeResult = new JSONObject(jsonResult).getInt("code");
            Log.v("后台的数据", codeResult + "");
            messageResult = new JSONObject(jsonResult).getString("msg");
            Log.v("后台的数据", messageResult);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("code", codeResult);
        bundle.putString("msg", messageResult);
        return bundle;
    }

    Handler registerHandler = new Handler() {
        public void handleMessage(Message message) {
            final Bundle bundle = message.getData();
            switch (bundle.getInt("code")) {
                case NetCore.REGISTER_EMAIL_EXISTED:
                    // 邮箱存在
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(RegisterActivity.this,
                            bundle.getString("msg"), Toast.LENGTH_SHORT).show();
                    break;

                case NetCore.REGISTER_ERROR:
                    //数据库错误
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(RegisterActivity.this,
                            bundle.getString("msg"), Toast.LENGTH_SHORT).show();
                    break;

                case NetCore.REGISTER_NAME_EXISTED:
                    //用户名存在
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(RegisterActivity.this,
                            bundle.getString("msg"), Toast.LENGTH_SHORT).show();
                    break;

                case NetCore.REGISTER_SUCCESS:
                    //注册成功
                    if (bundle.getString("msg").trim().length() == 0) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DemoUtil.getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            String username = registername.getText().toString();
                            ALoginParam params = LoginUtils.loginRequest(username, registerpassword.getText().toString());
                            registerWuKong(params, username);
                        }
                    });
                    break;

                default:
                    break;
            }
        }
    };

    private void registerWuKong(ALoginParam param, final String nickname){
        AuthService.getInstance().login(param, new com.alibaba.wukong.Callback<AuthInfo>() {
            @Override
            public void onSuccess(AuthInfo data) {
                AuthService.getInstance().setNickname(nickname);
                preferences = getSharedPreferences("loginmessage", Activity.MODE_PRIVATE);
                editor = preferences.edit();
                editor.remove("loginName").apply();
                editor.remove("loginPassword").apply();
                editor.putString("loginName", registername.getText().toString()).apply();
                editor.putString("loginPassword", registerpassword.getText().toString()).apply();

                Intent intent = new Intent();
                if (MainActivity.mainActivity != null) {
                    MainActivity.mainActivity.finish();
                }
                intent.setClass(RegisterActivity.this, MainActivity.class);
                intent.putExtra("loginIdentity", "<##用户##>" + registername.getText().toString());
                intent.putExtra("loginID", preferences.getLong("loginID", 0));
                if (dialog != null) {
                    dialog.dismiss();
                }
                AndTools.showToast(RegisterActivity.this, "注册成功");
                startActivity(intent);
                RegisterActivity.this.finish();
                if (StartActivity.startActivity != null) {
                    StartActivity.startActivity.finish();
                }
            }

            @Override
            public void onException(String code, String reason) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                AndTools.showToast(RegisterActivity.this, R.string.signup_failed + " " + reason);
            }

            @Override
            public void onProgress(AuthInfo s, int i) {
            }
        });
    }
}
