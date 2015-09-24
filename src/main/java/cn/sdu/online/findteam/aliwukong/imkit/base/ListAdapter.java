package cn.sdu.online.findteam.aliwukong.imkit.base;

/**
 * Created by wn on 2015/8/14.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Member;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.MyMessageActivity;
import cn.sdu.online.findteam.aliwukong.imkit.route.RouteProcessor;
import cn.sdu.online.findteam.aliwukong.imkit.session.SessionViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.JoinViewHolder;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.Session;
import cn.sdu.online.findteam.aliwukong.imkit.session.model.UnKownSession;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.util.AndTools;

/**
 * 线程不安全
 *
 * @param <T>
 */
public abstract class ListAdapter<T extends DisplayListItem> extends BaseAdapter {

    protected List<T> mList;
    private Context mContext;
    private HashMap<String, ViewHolder> viewHolderMap;
    Dialog dialog;
    public String teamID;
    public Conversation mConversation;
    Map<Integer, String> map = new HashMap<>();
    String openID;

    public static final int ERROR = 0;
    public static final int SUCCESS = 1;

    public ListAdapter(Context context) {
        mContext = context;
        viewHolderMap = new HashMap<String, ViewHolder>();
        mList = new ArrayList<T>();
    }

    public void setList(List<T> list) {
        if (this.mList != null) {
            this.mList.clear();
        } else {
            this.mList = new ArrayList<T>();
        }
        if (list != null) {
            this.mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null || mList.size() == 0) {
            return null;
        } else {
            return mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        T item = (T) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = RouteProcessor.route(item, getDomainCategory());
            convertView = viewHolder.inflate(mContext, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (item.getClass() == UnKownSession.class) {
            final UnKownSession item1 = (UnKownSession) item;

            JoinViewHolder joinViewHolder = (JoinViewHolder) viewHolder;
            String conversationId = item1.mConversation.conversationId();
            IMEngine.getIMService(ConversationService.class).listMembers(new Callback<List<Member>>() {
                @Override
                public void onProgress(List<Member> data, int progress) {
                    //Nothing to do
                }

                @Override
                public void onSuccess(List<Member> data) {
                    //ToDo 获取群成员
/*                    openID = data.get(0).user().openId() + "";*/
                    map.put(position, data.get(0).user().openId() + "");
                }

                @Override
                public void onException(String code, String reason) {
                    //异常处理
                }
            }, conversationId, 0, 1);

            joinViewHolder.agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = DialogDefine.createLoadingDialog(MyMessageActivity.getInstance(),
                            "");
                    dialog.show();
                    mConversation = item1.mConversation;
                    openID = map.get(position);
                    teamID = mConversation.title().substring(22);

                    new Thread(new runnable(openID, NetCore.allowJoinAddr)).start();
                }
            });

            joinViewHolder.refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = DialogDefine.createLoadingDialog(MyMessageActivity.getInstance(),
                            "");
                    dialog.show();
                    mConversation = item1.mConversation;
                    openID = map.get(position);
                    teamID = mConversation.title().substring(22);

                    new Thread(new runnable(openID, NetCore.refuseJoinAddr)).start();
                }
            });

        }
        removeOldViewHolder(viewHolder);
        viewHolder.parentView = parent;
        addViewHolder(viewHolder, item.getId());
        onBindView(viewHolder, item, position);
        return convertView;
    }


    class runnable implements Runnable {

        String openID, url;

        public runnable(String openID, String url) {
            this.openID = openID;
            this.url = url;
        }

        @Override
        public void run() {
            try {
                NetCore netCore = new NetCore();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                // 必填
                params.add(new BasicNameValuePair("usr.openId", openID));
                String jsonData = netCore.getResultWithCookies(NetCore.getopenIDInfoAddr,
                        params);
                Message message = new Message();
                Bundle bundle = new Bundle();
                if (jsonData != null) {
                    String userId = new JSONObject(jsonData).getString("id");
                    params.clear();
                    params.add(new BasicNameValuePair("team.id", teamID));
                    params.add(new BasicNameValuePair("user.id", userId));
                    String result = netCore.getResultWithCookies(url, params);
                    if (result != null) {
                        Log.v("UploadUtil", result);
                        JSONObject jsonObject = new JSONObject(result);
                        int resultCode = jsonObject.getInt("code");
                        String resultMsg = jsonObject.getString("msg");
                        bundle.putInt("code", SUCCESS);
                        bundle.putInt("resultCode", resultCode);
                        bundle.putString("message", resultMsg);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } else {
                        bundle.putInt("code", ERROR);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }

                } else {
                    bundle.putInt("code", ERROR);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int code = bundle.getInt("code");

            switch (code) {
                case ERROR:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    AndTools.showToast(MyMessageActivity.getInstance(), "未知错误！");
                    break;

                case SUCCESS:
                    int resultCode = bundle.getInt("resultCode");
                    String resultMsg = bundle.getString("message");
                    if (resultCode == 1 || resultCode == 2) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        AndTools.showToast(MyMessageActivity.getInstance(), resultMsg);
                        mConversation.remove();
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        AndTools.showToast(MyMessageActivity.getInstance(), resultMsg);
                    }
                    break;
            }
        }
    };

    private void removeOldViewHolder(ViewHolder viewHolder) {
        if (viewHolderMap.containsKey(viewHolder.tag)
                && viewHolderMap.get(viewHolder.tag) == viewHolder) {
            viewHolderMap.remove(viewHolder.tag);
        }
    }

    private void addViewHolder(ViewHolder viewHolder, String key) {
        viewHolder.tag = key;
        viewHolderMap.put(viewHolder.tag, viewHolder);
    }

    protected void onBindView(ViewHolder viewHolder, T item, int position) {
        viewHolder.position = position;
        if (item.getClass() == UnKownSession.class) {
            item.onJoinShow(mContext, viewHolder, null);
        } else {
            item.onShow(mContext, viewHolder, null);
        }
    }

    /**
     * 更新某项
     */
    public void notifyDataSetChanged(T item, String tag) {
        if (item == null) {
            return;
        }
        ViewHolder viewHolder = viewHolderMap.get(item.getId());
        if (viewHolder != null && viewHolder.tag.equals(item.getId())) {
            if (item.getClass() == UnKownSession.class) {
                item.onJoinShow(mContext, viewHolder, tag);
            } else {
                item.onShow(mContext, viewHolder, tag);
            }
        }
    }

    public void notifyDataSetChanged(List<T> list, String tag) {
        for (T item : list) {
            notifyDataSetChanged(item, tag);
        }
    }

    @Override
    public int getItemViewType(int position) {
        DisplayListItem msg = (DisplayListItem) getItem(position);
        return RouteProcessor.getViewType(msg, getDomainCategory());
    }

    @Override
    public int getViewTypeCount() {
        int size = RouteProcessor.getViewCount(getDomainCategory());
        return size == 0 ? 1 : size;
    }

    protected abstract String getDomainCategory();

}

