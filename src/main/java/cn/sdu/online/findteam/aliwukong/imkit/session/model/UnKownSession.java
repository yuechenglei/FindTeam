package cn.sdu.online.findteam.aliwukong.imkit.session.model;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.Message;

import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.MyMessageActivity;
import cn.sdu.online.findteam.aliwukong.avatar.AvatarMagicianImpl;
import cn.sdu.online.findteam.aliwukong.imkit.base.ItemClick;
import cn.sdu.online.findteam.aliwukong.imkit.route.Router;
import cn.sdu.online.findteam.aliwukong.imkit.widget.CustomGridView;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;

/**
 * Created by wn on 2015/9/19.
 */

@Router({JoinViewHolder.class})
public class UnKownSession extends Session implements ItemClick.OnItemClickListener {

    public UnKownSession(Conversation conversation) {
        super(conversation);
    }

    @Override
    public void onClick(Context sender, View view, int position) {
    }

    @Override
    public void setSessionContent(TextView contentView) {
        Message message = latestMessage();

        if (message == null) {
            contentView.setText("");
        } else {
            String text = mServiceFacade.getSessionContent(this);
            contentView.setText(text);
        }
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
}
