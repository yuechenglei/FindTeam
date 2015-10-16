package cn.sdu.online.findteam.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class WriteActivity extends BaseActivity implements View.OnClickListener {

    private String getsign;
    private TextView titleTv;
    private EditText teamMsgEt;
    private Button back;
    private LinearLayout finishBtn;
    private Intent intent;

    Dialog dialog;

    String verify;
    String allowComment;
    String logVisible;
    String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setActionBarLayout(R.layout.writeactivity_actionbar);
        getsign = WriteActivity.this.getIntent().getExtras().getString("sign");
        titleTv = (TextView) findViewById(R.id.writeactivity_ab_text);
        handle();
        setContentView(R.layout.writeactivity_layout);

        initView();
    }

    private void initView() {
        back = (Button) findViewById(R.id.writeactivity_backbt);
        finishBtn = (LinearLayout) findViewById(R.id.write_finish_bt);
        teamMsgEt = (EditText) findViewById(R.id.writeat_edit_text);

        back.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
    }

    private void handle() {
        if (getsign.equals("编辑队伍信息")) {
            titleTv.setText("编辑队伍信息");
        } else if (getsign.equals("写日志")) {
            titleTv.setText("写日志");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeactivity_backbt:
                WriteActivity.this.finish();
                break;
            case R.id.write_finish_bt:
                if (getsign.equals("编辑队伍信息")) {
                    editMsgFinish();
                } else if (getsign.equals("写日志")) {
                    writeLogFinish();
                }
                break;
        }
    }

    private void editMsgFinish() {
        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            AndTools.showToast(WriteActivity.this, "当前网络不可用！");
            return;
        }
        dialog = DialogDefine.createLoadingDialog(WriteActivity.this, "");
        dialog.show();

        if (teamMsgEt.getText().toString().length() != 0) {
            intent = WriteActivity.this.getIntent();
            verify = intent.getExtras().getString("verify");
            allowComment = intent.getExtras().getString("allowComment");
            logVisible = intent.getExtras().getString("logVisible");
            verify = verify.equals("true") ? "1" : "0";
            allowComment = allowComment.equals("true") ? "1" : "0";
            logVisible = logVisible.equals("true") ? "1" : "0";
            teamId = intent.getExtras().getString("teamId");

            new Thread(runnable).start();
        } else {
            Toast.makeText(WriteActivity.this, "。您还未填写队伍信息！", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("teamInfo.id", teamId));
            params.add(new BasicNameValuePair("teamInfo.verify", verify));
            params.add(new BasicNameValuePair("teamInfo.logVisible", logVisible));
            params.add(new BasicNameValuePair("teamInfo.allowComment", allowComment));
            params.add(new BasicNameValuePair("teamInfo.introduce", teamMsgEt.getText().toString()));

            try {
                String jsonData = new NetCore().getResultWithCookies(NetCore.modifyTeamAddr, params);
                JSONObject jsonObject = new JSONObject(jsonData);
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                Bundle bundle = new Bundle();
                bundle.putString("msg", msg);
                bundle.putInt("code", code);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int code = bundle.getInt("code");
            String message = bundle.getString("msg");
            switch (code) {
                case 1:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    AndTools.showToast(WriteActivity.this, message);
                    intent.putExtra("teaminfor", teamMsgEt.getText().toString());
                    WriteActivity.this.setResult(1, intent);
                    WriteActivity.this.finish();
                    break;

                default:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    AndTools.showToast(WriteActivity.this, message);
                    break;
            }
        }
    };

    private void writeLogFinish() {
        if (teamMsgEt.getText().toString().trim().length() != 0) {
            intent = WriteActivity.this.getIntent();
            teamId = intent.getExtras().getString("teamId");
            dialog = DialogDefine.createLoadingDialog(WriteActivity.this, "");
            dialog.show();
            new Thread(logRun).start();

        } else {
            Toast.makeText(WriteActivity.this, "您还未填写日志！", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable logRun = new Runnable() {
        @Override
        public void run() {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("log.team.id", teamId));
            params.add(new BasicNameValuePair("log.content", teamMsgEt.getText().toString()));
            try {
                String jsonData = new NetCore().getResultWithCookies(NetCore.createTeamLogAddr, params);
                if (jsonData.trim().length() != 0) {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", code);
                    bundle.putString("msg", msg);
                    Message message = new Message();
                    message.setData(bundle);
                    createLog.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Handler createLog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int code = bundle.getInt("code");
            String message = bundle.getString("msg");
            if (code > 0) {
                if (dialog != null){
                    dialog.dismiss();
                }
                AndTools.showToast(WriteActivity.this, message);
                intent = WriteActivity.this.getIntent();
                intent.putExtra("content", teamMsgEt.getText().toString());
                WriteActivity.this.setResult(2, intent);
                WriteActivity.this.finish();
            } else {
                if (dialog != null){
                    dialog.dismiss();
                }
                AndTools.showToast(WriteActivity.this, message);
            }
        }
    };
}
