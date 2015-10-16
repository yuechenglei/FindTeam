package cn.sdu.online.findteam.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.MessageBuilder;
import com.alibaba.wukong.im.MessageContent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.SessionFragment;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;
import cn.sdu.online.findteam.view.ClearableEditText;

public class AddFriendActivity extends BaseActivity implements View.OnClickListener {

    ClearableEditText edt;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.singlecompetition_actionbar_layout);
        TextView title_tv = (TextView) findViewById(R.id.singleGame_actionbar_tv);
        title_tv.setText("添加好友");

        setContentView(R.layout.newchat_layout);
        initView();
    }

    private void initView() {
        Button add_Btn = (Button) findViewById(R.id.btn_create_chat);
        add_Btn.setOnClickListener(this);
        Button back_Btn = (Button) findViewById(R.id.singlecp_return_bt);
        back_Btn.setOnClickListener(this);
        edt = (ClearableEditText) findViewById(R.id.et_create_chat);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singlecp_return_bt:
                AddFriendActivity.this.finish();
                break;

            case R.id.btn_create_chat:
                if (AndTools.isNetworkAvailable(MyApplication.getInstance())) {
                    dialog = DialogDefine.createLoadingDialog(AddFriendActivity.this, "");
                    dialog.show();

                    new Thread() {
                        @Override
                        public void run() {
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("uid", edt.getText().toString()));
                            try {
                                String jsonData = new NetCore().getResultWithCookies(NetCore.addFriendAddr, params);
                                JSONObject jsonObject = new JSONObject(jsonData);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                Bundle bundle = new Bundle();
                                bundle.putInt("code", code);
                                bundle.putString("msg", msg);
                                Message message = new Message();
                                message.setData(bundle);
                                handler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                } else {
                    AndTools.showToast(AddFriendActivity.this, "网络错误！");
                    break;
                }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            int code = bundle.getInt("code");

            switch (code) {
                case 1:
                    String sysMsg = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("loginName", "");
                    Long myOpenId = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getLong("loginID", 0);
                    final String userId = edt.getText().toString();
                    final com.alibaba.wukong.im.Message imMessage = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(sysMsg);
                    String myId = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("userID", "-1");
                    final Long[] openId = {null};
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            openId[0] = getOpenId(userId);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (openId[0] != null) {
                        IMEngine.getIMService(ConversationService.class).createConversation(new com.alibaba.wukong.Callback<Conversation>() {

                            @Override
                            public void onSuccess(Conversation conversation) {
                                //ToDo 在这处理创建成功的会话： conversation
                                imMessage.sendTo(conversation, backMsg);
                            }

                            @Override
                            public void onException(String code, String reason) {
                                //会话创建失败异常处理
                            }

                            @Override
                            public void onProgress(Conversation data, int progress) {
                                // Do Nothing
                            }
                        }, "<#$_*/ + addFriend + /*_$#>" + myId, myOpenId + "", imMessage, Conversation.ConversationType.GROUP, openId[0]);
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        AndTools.showToast(AddFriendActivity.this, "申请发送失败");
                    }
                    break;

                default:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    AndTools.showToast(AddFriendActivity.this, bundle.getString("msg"));
                    break;
            }
        }
    };

    Callback<com.alibaba.wukong.im.Message> backMsg = new Callback<com.alibaba.wukong.im.Message>() {
        @Override
        public void onSuccess(com.alibaba.wukong.im.Message message) {
            message.conversation().removeAndClearMessage();
            if (dialog != null) {
                dialog.dismiss();
            }
            AndTools.showToast(AddFriendActivity.this, "您的请求已申请");
        }

        @Override
        public void onException(String s, String s1) {
            if (dialog != null) {
                dialog.dismiss();
            }
            Log.v("TAG12313212313", "code=" + s + " reason=" + s1);
        }

        @Override
        public void onProgress(com.alibaba.wukong.im.Message message, int i) {

        }
    };

    private Long getOpenId(String id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("usr.id", id));
        Long openId = null;
        try {
            String jsonData = new NetCore().getResultWithCookies(NetCore.getUserInfoAddr, params);
            JSONObject jsonObject = new JSONObject(jsonData);
            openId = jsonObject.getLong("openId");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return openId;
    }
}
