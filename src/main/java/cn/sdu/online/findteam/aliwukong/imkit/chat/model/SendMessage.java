package cn.sdu.online.findteam.aliwukong.imkit.chat.model;

import android.content.Context;
import android.view.View;

import com.alibaba.wukong.im.Conversation;
import com.android.volley.toolbox.ImageLoader;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.aliwukong.imkit.base.ViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.chat.controller.SingleChatActivity;
import cn.sdu.online.findteam.aliwukong.imkit.chat.viewholder.ChatViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.chat.viewholder.SendViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.JoinViewHolder;
import cn.sdu.online.findteam.share.DemoUtil;
import cn.sdu.online.findteam.share.MyApplication;

public class SendMessage extends ChatMessage {

    @Override
    public void showChatMessage(Context context, ViewHolder holder) {
        showForSendMessageStatus(context, (SendViewHolder) holder);
    }

    /**
     * 消息发送状态组件的显示
     *
     * @param context
     * @param viewHolder
     */
    public void showForSendMessageStatus(final Context context, SendViewHolder viewHolder) {
        String url = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("Header_Url", "-1");
        if (!url.equals("-1")) {
            ImageLoader imageLoader = new ImageLoader(MyApplication.getQueues(), MyApplication.bitmapCache);
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(viewHolder.sendChatting_avatar,
                    R.drawable.head_moren, R.drawable.head_moren);
            imageLoader.get(url, imageListener);
        }
        else {
            viewHolder.sendChatting_avatar.setImageResource(R.drawable.head_moren);
        }
/*        viewHolder.sendChatting_avatar.setImageResource(R.drawable.headphoto);*/
        switch (mMessage.status()) {
            case OFFLINE:
                sendAgain(context, viewHolder);
                viewHolder.chatting_unreadcount_tv.setVisibility(View.GONE);
                viewHolder.chatting_unread_icon_iv.setVisibility(View.GONE);
                viewHolder.chatting_notsuccess_iv.setVisibility(View.VISIBLE);
                viewHolder.chatting_status_progress.setVisibility(View.GONE);
                break;
            case SENDING:
                viewHolder.chatting_unreadcount_tv.setVisibility(View.GONE);
                viewHolder.chatting_unread_icon_iv.setVisibility(View.GONE);
                viewHolder.chatting_notsuccess_iv.setVisibility(View.GONE);// 占时不展示
                viewHolder.chatting_status_progress.setVisibility(View.VISIBLE);
                break;
            case SENT:
                setUnreadStatus(context, viewHolder);
                viewHolder.chatting_unread_icon_iv.setVisibility(View.GONE);
                viewHolder.chatting_notsuccess_iv.setVisibility(View.GONE);
                viewHolder.chatting_status_progress.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 显示消息的对方未读状态的情况
     *
     * @param context
     * @param viewHolder
     */
    public void setUnreadStatus(Context context, SendViewHolder viewHolder) {
        viewHolder.chatting_unread_icon_iv.setVisibility(View.GONE);
        viewHolder.chatting_unreadcount_tv.setVisibility(View.VISIBLE);
        String unReadtips = null;
        if (mMessage.allReceiversRead()) {    //已读
            unReadtips = context.getResources().getString(R.string.chat_item_read_tips);
//            viewHolder.chatting_unreadcount_tv.setTextColor(context.getResources().getColor(R.color.text_color_gray));
//            viewHolder.chatting_unread_icon_iv.setBackgroundResource(R.drawable.unread_icon_iv);
        } else {  //未读
            if (Conversation.ConversationType.CHAT == getConversationType()) {
                unReadtips = context.getResources().getString(R.string.chat_item_unread_tips);
            } else {
                unReadtips = context.getResources().getString(R.string.group_item_unread_tips, getUnreadCount());
            }
//            viewHolder.chatting_unreadcount_tv.setTextColor(context.getResources().getColor(R.color.unread_tv_color));
//            viewHolder.chatting_unread_icon_iv.setBackgroundResource(R.drawable.unread_icon_iv);
        }

        viewHolder.chatting_unreadcount_tv.setText(unReadtips);
        viewHolder.chatting_unreadcount_tv.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        viewHolder.chatting_unread_icon_iv.setBackgroundResource(R.drawable.unread_icon_iv);
    }

    /**
     * 离线消息点击重新发送
     *
     * @param context
     * @param viewHolder
     */
    private void sendAgain(final Context context, final SendViewHolder viewHolder) {
        viewHolder.chatting_notsuccess_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoUtil.showAlertDialog(
                        context, context.getString(R.string.send_again),
                        new DemoUtil.DialogCallback() {
                            @Override
                            public void onPositive() {
                                mMessage.sendTo(mMessage.conversation(), null);
                            }
                        }
                );
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

