package cn.sdu.online.findteam.aliwukong.imkit.session.model;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.aliwukong.imkit.base.ViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.widget.CustomGridView;
import cn.sdu.online.findteam.aliwukong.imkit.widget.DateUtil;

/**
 * Created by wn on 2015/10/13.
 */
public class AddFriendViewHolder extends ViewHolder {

    public TextView sessionUnreadTxt, sessionGmtTxt, sessionTitleName;
    public ImageView sessionSilenceImgView, mMessageStatus;
    public Button agree, refuse;
    public CustomGridView mHeader;

    @Override
    protected void initView(View view) {
        sessionTitleName = (TextView) view.findViewById(R.id.session_title_name);
        sessionGmtTxt = (TextView) view.findViewById(R.id.session_gmt);
        sessionSilenceImgView = (ImageView) view.findViewById(R.id.session_silence);
        sessionUnreadTxt = (TextView) view.findViewById(R.id.session_unread);
        mMessageStatus = (ImageView) view.findViewById(R.id.chatting_notsuccess_iv);
        agree = (Button) view.findViewById(R.id.session_btn_agree);
        refuse = (Button) view.findViewById(R.id.session_btn_refuse);
        mHeader = (CustomGridView) view.findViewById(R.id.session_icon);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.session_item_add;
    }

    public void showSessionStatusFail() {
        mMessageStatus.setBackgroundResource(R.drawable.session_status_failed);
        mMessageStatus.setVisibility(View.VISIBLE);
    }

    public void showSessionStatusSending() {
        mMessageStatus.setBackgroundResource(R.drawable.session_status_sending);
        mMessageStatus.setVisibility(View.VISIBLE);
    }

    public void showSessionUnread(int count) {
        if (count > 0) {
            sessionUnreadTxt.setVisibility(View.VISIBLE);
            sessionUnreadTxt.setText(count + "");
        } else {
            sessionUnreadTxt.setVisibility(View.GONE);
        }
    }

    /**
     * 设置session的时间
     *
     * @param time
     */
    public void showTime(long time) {
        if (time == 0) {
            sessionGmtTxt.setVisibility(View.GONE);
        } else {
            sessionGmtTxt.setVisibility(View.VISIBLE);
            sessionGmtTxt.setText(DateUtil.formatRimetShowTime(sessionTitleName.getContext(), time, false));
        }
    }
}
