package cn.sdu.online.findteam.aliwukong.imkit.chat.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.android.volley.toolbox.ImageLoader;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.aliwukong.avatar.AvatarMagicianImpl;
import cn.sdu.online.findteam.aliwukong.imkit.base.ViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.chat.controller.SingleChatActivity;
import cn.sdu.online.findteam.aliwukong.imkit.chat.viewholder.ChatViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.chat.viewholder.ReceiveViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.JoinViewHolder;
import cn.sdu.online.findteam.aliwukong.user.UserProfileActivity;
import cn.sdu.online.findteam.share.MyApplication;

/**
 * Created by wn on 2015/8/14.
 */
public class ReceiveMessage extends ChatMessage {

    @Override
    public void showChatMessage(Context context, ViewHolder holder) {
        //显示头像
        showAvatar(context, (ReceiveViewHolder) holder);

        //置未读消息为读状态
        if (!mMessage.iHaveRead()) {
            readMessage();
        }
    }

    /**
     * 显示消息发送者头像
     *
     * @param context
     * @param holder
     */
    public void showAvatar(final Context context, ReceiveViewHolder holder) {
        String url = SingleChatActivity.getImg_Url();
        if (url.equals("-1")) {
            holder.chatting_avatar.setImageResource(R.drawable.head_moren);
        } else {
            ImageLoader imageLoader = new ImageLoader(MyApplication.getQueues(), MyApplication.bitmapCache);
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(holder.chatting_avatar,
                    R.drawable.head_moren, R.drawable.head_moren);
            imageLoader.get(SingleChatActivity.getImg_Url(), imageListener);
        }
/*        AvatarMagicianImpl.getInstance().setUserAvatar(holder.chatting_avatar,mMessage.senderId(),(ListView)(holder.parentView));*/
        holder.chatting_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user_open_id", mMessage.senderId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onJoinShow(Context context, ChatViewHolder viewHolder, String tag) {

    }

    @Override
    public void onAddShow(Context context, ChatViewHolder viewHolder, String tag) {

    }
}
