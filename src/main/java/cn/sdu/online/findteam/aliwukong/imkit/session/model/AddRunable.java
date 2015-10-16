package cn.sdu.online.findteam.aliwukong.imkit.session.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.aliwukong.imkit.base.ListAdapter;
import cn.sdu.online.findteam.net.NetCore;

/**
 * Created by wn on 2015/10/13.
 */
public class AddRunable implements Runnable {

    String id;
    String url;
    Handler handler;

    public AddRunable(String id, String url, Handler handler) {
        this.id = id;
        this.handler = handler;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            NetCore netCore = new NetCore();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // 必填
            params.add(new BasicNameValuePair("uid", id));
            String result = netCore.getResultWithCookies(url, params);
            Bundle bundle = new Bundle();
            Message message = new Message();
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                int resultCode = jsonObject.getInt("code");
                String resultMsg = jsonObject.getString("msg");
                bundle.putInt("code", ListAdapter.SUCCESS);
                bundle.putInt("resultCode", resultCode);
                bundle.putString("message", resultMsg);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                bundle.putInt("code", ListAdapter.ERROR);
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
