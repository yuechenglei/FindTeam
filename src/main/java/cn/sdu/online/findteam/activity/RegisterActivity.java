package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText registername, registerpassword, registerconfirm, registeremail;
    private Button registerbtn;

    private Dialog dialog;

    boolean isemail;

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
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(RegisterActivity.this,
                            bundle.getString("msg"), Toast.LENGTH_SHORT).show();

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            if (MainActivity.mainActivity != null){
                                MainActivity.mainActivity.finish();
                            }
                            intent.setClass(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("loginIdentity", "<##用户##>" + registername.getText().toString());
                            intent.putExtra("loginID", bundle.getInt("code"));
                            startActivity(intent);
                            RegisterActivity.this.finish();
                            if (StartActivity.startActivity != null
                                    && LoginActivity.loginActivity != null) {
                                LoginActivity.loginActivity.finish();
                                StartActivity.startActivity.finish();
                            }
                        }
                    };
                    timer.schedule(timerTask, 200);
                    break;

                default:
                    break;
            }
        }
    };
}
