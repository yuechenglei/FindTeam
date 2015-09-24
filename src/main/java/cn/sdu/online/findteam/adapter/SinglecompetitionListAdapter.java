package cn.sdu.online.findteam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.MessageBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.activity.MySingleTeamActivity;
import cn.sdu.online.findteam.activity.OtherTeamActivity;
import cn.sdu.online.findteam.activity.SingleCompetitionActivity;
import cn.sdu.online.findteam.mob.SingleCompetitionListItem;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.resource.DialogDefine;
import cn.sdu.online.findteam.view.RoundImageView;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.MessageContent;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class SingleCompetitionListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<SingleCompetitionListItem> listItems;
    long currentID;

    public SingleCompetitionListAdapter(Context mContext, List<SingleCompetitionListItem> listItems) {
        inflater = LayoutInflater.from(mContext);
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.singlecompetition_item_layout, null);
            viewHolder.imageView = (RoundImageView) convertView.findViewById(R.id.singlecp_item_img);
            viewHolder.teamname = (TextView) convertView.findViewById(R.id.singlecp_item_teamname);
            viewHolder.personnum = (TextView) convertView.findViewById(R.id.singlecp_item_personnum);
            viewHolder.line1 = convertView.findViewById(R.id.singlecp_item_line1);
            viewHolder.content = (TextView) convertView.findViewById(R.id.singlecp_item_content);
            viewHolder.line2 = convertView.findViewById(R.id.singlecp_item_line2);
            viewHolder.look = (Button) convertView.findViewById(R.id.singlecp_item_look);
            viewHolder.join = (Button) convertView.findViewById(R.id.singlecp_item_join);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.teamname.setText(listItems.get(position).teamname);
        viewHolder.personnum.setText("缺" + (listItems.get(position).maxNum - listItems.get(position).currentNum) + "人");
        viewHolder.content.setText(listItems.get(position).content);
        viewHolder.imageView.setImageResource(R.drawable.head_moren);
        if (listItems.get(position).imgPath.trim().length() != 0) {
            loadBitmap(viewHolder.imageView, listItems.get(position).imgPath);
        }
        viewHolder.look.setTag(position);
        viewHolder.join.setTag(position);

        currentID = listItems.get(position).userOpenID;
        viewHolder.look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleCompetitionActivity.dialog = DialogDefine.createLoadingDialog(SingleCompetitionActivity.getContext(),
                        "");
                SingleCompetitionActivity.dialog.show();

                if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
                    AndTools.showToast(SingleCompetitionActivity.getContext(), "当前网络不可用！");
                    if (SingleCompetitionActivity.dialog != null) {
                        SingleCompetitionActivity.dialog.dismiss();
                    }
                    return;
                }

                if (MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE)
                        .getLong("loginID", 0) == currentID) {
                    Log.v("UploadUtil", currentID + "");
                    Intent intent = new Intent();
                    intent.setClass(SingleCompetitionActivity.getContext(), MySingleTeamActivity.class);
                    intent.putExtra("teamID", listItems.get(position).teamID);
                    MyApplication.IDENTITY = "队长";
                    if (SingleCompetitionActivity.dialog != null) {
                        SingleCompetitionActivity.dialog.dismiss();
                    }
                    SingleCompetitionActivity.getContext().startActivity(intent);
                    return;
                }

                new Thread() {
                    @Override
                    public void run() {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("team.id", listItems.get(position).teamID));
                        try {
                            String jsonData = new NetCore().getResultWithCookies(NetCore.getOneTeamAddr,
                                    params);
                            JSONObject jsonObject = new JSONObject(jsonData);
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("member"));
                            Bundle bundle = new Bundle();
                            Message message = new Message();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject member = jsonArray.getJSONObject(i);
                                if (member.getString("userName").
                                        equals(MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("loginName", ""))) {
                                    bundle.putString("msg", "队员");
                                    bundle.putString("teamID", listItems.get(position).teamID);
                                    message.setData(bundle);
                                    loadteamHander.sendMessage(message);
                                    return;
                                }
                            }
                            bundle.putString("msg", "游客");
                            bundle.putString("teamID", listItems.get(position).teamID);
                            message.setData(bundle);
                            loadteamHander.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });

        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleCompetitionActivity.dialog = DialogDefine.createLoadingDialog(SingleCompetitionActivity.getContext(),
                        "");
                SingleCompetitionActivity.dialog.show();

                if (!AndTools.isNetworkAvailable(MyApplication.getInstance())) {
                    AndTools.showToast(SingleCompetitionActivity.getContext(), "当前网络不可用！");
                    if (SingleCompetitionActivity.dialog != null) {
                        SingleCompetitionActivity.dialog.dismiss();
                    }
                    return;
                }

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String jsonData = new NetCore().joinTeam(listItems.get(position).teamID);
                            JSONObject jsonObject = new JSONObject(jsonData);
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", jsonObject.getString("msg"));
                            bundle.putString("name", listItems.get(position).teamname);
                            bundle.putString("teamID", listItems.get(position).teamID);
                            Message message = new Message();
                            message.setData(bundle);
                            loadteamHander.sendMessage(message);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        return convertView;
    }

    public class ViewHolder {
        RoundImageView imageView;
        TextView teamname;
        TextView personnum;
        View line1;
        TextView content;
        View line2;
        Button look;
        Button join;
    }

    private void loadBitmap(final RoundImageView imageView, String imgPath) {
        ImageRequest request = new ImageRequest(imgPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getQueues().add(request);
    }

    Handler loadteamHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String jsonMsg = bundle.getString("msg", "出现异常错误！");
            if (jsonMsg.equals("您的请求已通过")) {
                AndTools.showToast(SingleCompetitionActivity.getContext(), jsonMsg);
                if (SingleCompetitionActivity.dialog != null) {
                    SingleCompetitionActivity.dialog.dismiss();
                }
            } else if (jsonMsg.equals("您的请求已申请")) {
                String name = bundle.getString("name");
                String teamID = bundle.getString("teamID");
                String sysMsg = MyApplication.getInstance().getSharedPreferences("loginmessage", Context.MODE_PRIVATE).getString("loginName", "");
                String joinMsg = sysMsg + "  " + "申请加入你的队伍" + "  " + name;

                final com.alibaba.wukong.im.Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(joinMsg);

                IMEngine.getIMService(ConversationService.class).createConversation(new com.alibaba.wukong.Callback<Conversation>() {

                    @Override
                    public void onSuccess(Conversation conversation) {
                        //ToDo 在这处理创建成功的会话： conversation
                        message.sendTo(conversation, backMsg);
                        conversation.removeAndClearMessage();
                    }

                    @Override
                    public void onException(String code, String reason) {
                        //会话创建失败异常处理
                    }

                    @Override
                    public void onProgress(Conversation data, int progress) {
                        // Do Nothing
                    }
                }, "<#$_*/ + join + /*_$#>" + teamID, null, message, Conversation.ConversationType.GROUP, currentID);

            } else if (jsonMsg.equals("队员")) {
                String teamID = bundle.getString("teamID");
                Intent intent = new Intent();
                intent.setClass(SingleCompetitionActivity.getContext(), MySingleTeamActivity.class);
                intent.putExtra("teamID", teamID);
                MyApplication.IDENTITY = "队员";
                if (SingleCompetitionActivity.dialog != null) {
                    SingleCompetitionActivity.dialog.dismiss();
                }
                SingleCompetitionActivity.getContext().startActivity(intent);
            } else if (jsonMsg.equals("游客")) {
                String teamID = bundle.getString("teamID");
                Intent intent = new Intent();
                intent.setClass(SingleCompetitionActivity.getContext(), OtherTeamActivity.class);
                intent.putExtra("teamID", teamID);
                intent.putExtra("userOpenId", currentID);
                MyApplication.IDENTITY = "游客";
                if (SingleCompetitionActivity.dialog != null) {
                    SingleCompetitionActivity.dialog.dismiss();
                }
                SingleCompetitionActivity.getContext().startActivity(intent);
            }
        }
    };

    Callback<com.alibaba.wukong.im.Message> backMsg = new Callback<com.alibaba.wukong.im.Message>() {
        @Override
        public void onSuccess(com.alibaba.wukong.im.Message message) {
            MessageContent msgContent = message.messageContent();
            Log.v("TAG12313212313", "消息内容：" + msgContent.toString());
            if (SingleCompetitionActivity.dialog != null) {
                SingleCompetitionActivity.dialog.dismiss();
            }
            AndTools.showToast(SingleCompetitionActivity.getContext(), "您的请求已申请");
        }

        @Override
        public void onException(String s, String s1) {
            Log.v("TAG12313212313", "code=" + s + " reason=" + s1);
        }

        @Override
        public void onProgress(com.alibaba.wukong.im.Message message, int i) {

        }
    };
}