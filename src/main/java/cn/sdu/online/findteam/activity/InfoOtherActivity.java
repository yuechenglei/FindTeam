package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.MessageBuilder;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.aliwukong.imkit.chat.controller.SingleChatActivity;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.Session;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.share.DemoUtil;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;
import cn.sdu.online.findteam.view.RoundImageView;

/**
 * Created by wn on 2015/9/23.
 */
public class InfoOtherActivity extends Activity implements View.OnClickListener {

    Dialog dialog;
    public Button bt_return, add_Friend_Btn, send_msg_Btn;
    public TextView text_tag1, text_tag2, text_tag3, text_tag4, text_realname,
            text_address, text_school, text_phonenumber, text_introduction;
    public TextView text_nickname, text_gender, text_email, text_openID, text_ID;
    private RoundImageView head;
    String openId;
    View contentView;
    private RelativeLayout relativeLayout;
    JSONObject person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog = DialogDefine.createLoadingDialog(this,
                "加载中...");
        dialog.show();

        if (getIntent().getExtras() != null) {
            openId = getIntent().getExtras().getString("openId");
        } else {
            openId = "";
        }

        if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
            dialog.dismiss();
            AndTools.showToast(InfoOtherActivity.this, "当前网络不可用！");
            return;
        }

        findview();
        Thread loadUserInfo = new Thread(new loadUserInfo());
        loadUserInfo.start();
    }

    private void findview() {
        contentView = View.inflate(InfoOtherActivity.this, R.layout.activity_info_other, null);
        bt_return = (Button) contentView.findViewById(R.id.bt_return_other);
        text_nickname = (TextView) contentView.findViewById(R.id.text_nickname_other);
        text_introduction = (TextView) contentView.findViewById(R.id.text_introduction_other);
        text_tag1 = (TextView) contentView.findViewById(R.id.text_tag1_other);
        text_tag2 = (TextView) contentView.findViewById(R.id.text_tag2_other);
        text_tag3 = (TextView) contentView.findViewById(R.id.text_tag3_other);
        text_realname = (TextView) contentView.findViewById(R.id.text_realname_other);
        text_gender = (TextView) contentView.findViewById(R.id.text_gender_other);
        text_address = (TextView) contentView.findViewById(R.id.text_address_other);
        text_school = (TextView) contentView.findViewById(R.id.text_school_other);
        text_phonenumber = (TextView) contentView.findViewById(R.id.text_phonenumber_other);
        text_email = (TextView) contentView.findViewById(R.id.text_email_other);
        add_Friend_Btn = (Button) contentView.findViewById(R.id.add_friend_btn);
        send_msg_Btn = (Button) contentView.findViewById(R.id.send_msg_btn);
        head = (RoundImageView) contentView.findViewById(R.id.head_other);
        text_openID = (TextView) contentView.findViewById(R.id.text_openID_other);
        text_ID = (TextView) contentView.findViewById(R.id.text_userID_other);
        relativeLayout = (RelativeLayout) contentView.findViewById(R.id.info_other_headlayout);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                getWindowManager().getDefaultDisplay().getWidth()));

        add_Friend_Btn.setOnClickListener(InfoOtherActivity.this);
        bt_return.setOnClickListener(InfoOtherActivity.this);
        head.setOnClickListener(InfoOtherActivity.this);
        send_msg_Btn.setOnClickListener(InfoOtherActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_return_other:
                InfoOtherActivity.this.finish();
                break;

            case R.id.head_other:
                Intent intent = new Intent();
                intent.setClass(InfoOtherActivity.this, ImgShowerActivity.class);
                intent.putExtra("bitmap", getBytes(head.getBmp()));
                InfoOtherActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.zoomin, 0);
                break;

            case R.id.add_friend_btn:
                if (AndTools.isNetworkAvailable(MyApplication.getInstance())) {
                    dialog = DialogDefine.createLoadingDialog(InfoOtherActivity.this, "");
                    dialog.show();

                    new Thread() {
                        @Override
                        public void run() {
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("uid", text_ID.getText().toString()));
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
                                addFriendHandler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                } else {
                    AndTools.showToast(InfoOtherActivity.this, "网络错误！");
                    break;
                }

            case R.id.send_msg_btn:
                createSingleConversation();
                break;
        }
    }

    Handler addFriendHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            int code = bundle.getInt("code");

            switch (code) {
                case 1:
                    String sysMsg = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("loginName", "");
                    Long myOpenId = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getLong("loginID", 0);
                    final com.alibaba.wukong.im.Message imMessage = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(sysMsg);
                    String myId = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("userID", "-1");
                    final Long openId = Long.parseLong(text_openID.getText().toString());
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
                    }, "<#$_*/ + addFriend + /*_$#>" + myId, myOpenId + "", imMessage, Conversation.ConversationType.GROUP, openId);
                    break;

                default:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    AndTools.showToast(InfoOtherActivity.this, bundle.getString("msg"));
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
            AndTools.showToast(InfoOtherActivity.this, "您的请求已申请");
        }

        @Override
        public void onException(String s, String s1) {
            if (dialog != null) {
                dialog.dismiss();
            }
            AndTools.showToast(InfoOtherActivity.this, "code=" + s + " reason=" + s1);
        }

        @Override
        public void onProgress(com.alibaba.wukong.im.Message message, int i) {

        }
    };

    public byte[] getBytes(Bitmap bitmap) {
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }

    class loadUserInfo implements Runnable {
        @Override
        public void run() {
            try {
                String info = new NetCore().getUserInfo(openId);
                if (info.trim().length() != 0) {
                    person = new JSONObject(info);
                    handler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class IsFriend implements Runnable {

        String id;

        public IsFriend(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("type", "friends"));
            try {
                String jsonData = new NetCore().getResultWithCookies(NetCore.getFriendListAddr, params);
                if (jsonData.trim().length() != 0) {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String thisId = jsonObject.getString("id");
                        if (id.equals(thisId)) {
                            handler.sendEmptyMessage(2);
                            return;
                        }
                    }
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String imgPath = "";
                    try {
                        text_phonenumber.setText(person.getString("contact"));
                        text_nickname.setText(person.getString("username"));
                        text_introduction.setText(person.getString("introduce"));
                        text_email.setText(person.getString("mail"));
                        text_realname.setText(person.getString("realName"));
                        text_gender.setText(person.getString("sex"));
                        text_address.setText(person.getString("address"));
                        text_school.setText(person.getString("college"));
                        text_ID.setText(person.getString("id"));
                        text_openID.setText(person.getString("openId"));
                        imgPath = person.getString("imgPath");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loadBitmap(imgPath);
                    relativeLayout.setBackground(head.getDrawable());
                    new Thread(new IsFriend(text_ID.getText().toString())).start();
                    break;

                case 1:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    InfoOtherActivity.this.setContentView(contentView);
                    break;

                case 2:
                    add_Friend_Btn.setVisibility(View.GONE);
                    send_msg_Btn.setVisibility(View.VISIBLE);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    InfoOtherActivity.this.setContentView(contentView);
                    break;
            }

        }
    };

    private void loadBitmap(String imgPath) {
        ImageRequest request = new ImageRequest(imgPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                head.setImageBitmap(bitmap);
                relativeLayout.setBackground(new BitmapDrawable(bitmap));
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getQueues().add(request);
    }

    /**
     * 创建单聊会话
     */
    public void createSingleConversation() {
        Long userOpenId = Long.parseLong(openId);
        String myOpenId = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getLong("loginID", 0) + "";
        DemoUtil.showProgressDialog(InfoOtherActivity.this, "正在创建会话...");
        IMEngine.getIMService(ConversationService.class).createConversation(new Callback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                DemoUtil.dismissProgressDialog();
                conversation.resetUnreadCount();
                Intent intent = new Intent(InfoOtherActivity.this, SingleChatActivity.class);
                intent.putExtra(Session.SESSION_INTENT_KEY, conversation);
                startActivity(intent);
            }

            @Override
            public void onException(String code, String reason) {
                DemoUtil.dismissProgressDialog();
                AndTools.showToast(InfoOtherActivity.this, R.string.chat_create_fail);
                AndTools.showToast(InfoOtherActivity.this,
                        R.string.chat_create_fail + ".code=" + code + " reason=" + reason);
            }

            @Override
            public void onProgress(Conversation data, int progress) {
            }
        }, myOpenId, myOpenId, null, Conversation.ConversationType.CHAT, userOpenId);
    }
}
