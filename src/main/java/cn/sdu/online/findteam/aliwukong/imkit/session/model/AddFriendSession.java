package cn.sdu.online.findteam.aliwukong.imkit.session.model;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.Message;

import cn.sdu.online.findteam.aliwukong.avatar.AvatarMagicianImpl;
import cn.sdu.online.findteam.aliwukong.imkit.base.ItemClick;
import cn.sdu.online.findteam.aliwukong.imkit.route.Router;
import cn.sdu.online.findteam.aliwukong.imkit.widget.CustomGridView;

/**
 * Created by wn on 2015/10/13.
 */
@Router({AddFriendViewHolder.class})
public class AddFriendSession extends Session implements ItemClick.OnItemClickListener {
    public AddFriendSession(Conversation conversation) {
        super(conversation);
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
    public void onClick(Context sender, View view, int position) {
    }

    @Override
    public void setSessionContent(TextView name, TextView con, TextView team) {
        Message message = latestMessage();

        if (message == null) {
            con.setText("");
        } else {
            String text = mServiceFacade.getSessionContent(this);
            String a[] = text.split("  ");
            name.setText(a[0]);
            con.setText("  " + a[1] + "  ");
            team.setText(a[2]);
        }
    }

    @Override
    public void showAvatar(Context context, String mediaIds, View view, ListView itemParent) {
        AvatarMagicianImpl.getInstance().setConversationAvatar((CustomGridView) view, mediaIds, itemParent);
    }
}
