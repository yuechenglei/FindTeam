package cn.sdu.online.findteam.aliwukong.imkit.session.model;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.wukong.im.Conversation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.MyMessageActivity;
import cn.sdu.online.findteam.aliwukong.imkit.base.ViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.widget.CustomGridView;
import cn.sdu.online.findteam.aliwukong.imkit.widget.DateUtil;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.util.AndTools;

/**
 * Created by wn on 2015/9/19.
 */
public class JoinViewHolder extends ViewHolder {

    public CustomGridView sessionIconView;
    public TextView sessionUnreadTxt, sessionGmtTxt, sessionContentTxt;
    public ImageView sessionSilenceImgView, mMessageStatus;
    public Button agree, refuse;

    @Override
    protected void initView(View view) {
        sessionContentTxt = (TextView) view.findViewById(R.id.session_join_content);
        sessionGmtTxt = (TextView) view.findViewById(R.id.session_join_gmt);
        sessionIconView = (CustomGridView) view.findViewById(R.id.session_join_icon);
        sessionSilenceImgView = (ImageView) view.findViewById(R.id.session_join_silence);
        sessionUnreadTxt = (TextView) view.findViewById(R.id.session_join_unread);
        mMessageStatus = (ImageView) view.findViewById(R.id.chat_join_notsuccess_iv);
        agree = (Button) view.findViewById(R.id.agree_join);
        refuse = (Button) view.findViewById(R.id.refuse_join);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.session_item_join;
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
            sessionGmtTxt.setText(DateUtil.formatRimetShowTime(sessionContentTxt.getContext(), time, false));
        }
    }
}

