package cn.sdu.online.findteam.aliwukong.imkit.session.model;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.User;
import com.alibaba.wukong.im.utils.Utils;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.aliwukong.avatar.AvatarMagicianImpl;
import cn.sdu.online.findteam.aliwukong.imkit.base.ItemClick;
import cn.sdu.online.findteam.aliwukong.imkit.base.ViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.chat.controller.SingleChatActivity;
import cn.sdu.online.findteam.aliwukong.imkit.route.Router;
import cn.sdu.online.findteam.aliwukong.imkit.session.SessionViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.Session;
import cn.sdu.online.findteam.aliwukong.imkit.widget.CustomGridView;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.share.MyApplication;

/**
 * Created by wn on 2015/8/14.
 */

@Router({SessionViewHolder.class})
public class SingleSession extends Session implements ItemClick.OnItemClickListener {

    public SingleSession(Conversation conversation) {
        super(conversation);
    }

    @Override
    public void showAvatar(Context context, String mediaIds, View view, ListView itemParent) {
        List<Long> openId = new ArrayList<Long>(1);
        try {
            openId.add(Long.parseLong(mediaIds));
        } catch (NumberFormatException e) {
            Log.e("SingleSession", "NumberFormatException");
        }
        AvatarMagicianImpl.getInstance().setConversationAvatar((CustomGridView) view, openId, itemParent);
    }

    @Override
    public void onClick(Context sender, View view, int position) {
        resetUnreadCount();
        Intent intent = new Intent(sender, SingleChatActivity.class);
        intent.putExtra(SESSION_INTENT_KEY, this);
        sender.startActivity(intent);
    }

    protected void refreshTitle(final SessionViewHolder viewHolder) {
        mServiceFacade.getUserByOpenId(new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                if (TextUtils.isEmpty(user.nickname())) {
                    viewHolder.sessionTitleTxt.setText(title());
                } else {
                    viewHolder.sessionTitleTxt.setText(user.nickname());
                }
            }

            @Override
            public void onException(String code, String reason) {
                Log.d("SingleSession", "Get user error.code=" + code + " reason=" + reason);
            }

            @Override
            public void onProgress(User user, int progress) {
            }
        }, Utils.toLong(mConversation.title()));
    }

    public void setSessionContent(final TextView contentView) {
        if (latestMessage() == null) {
            contentView.setText("");
        } else {
            String text = mServiceFacade.getSessionContent(this);
            contentView.setText(text);
        }
    }
}
