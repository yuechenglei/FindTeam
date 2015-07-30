package cn.sdu.online.findteam.net;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.entity.User;

public class NetCore {
    private static final String ServerAddr = "http://202.194.14.195:8080/findteam/";

    // 登录的url
    private final String LoginAddr = ServerAddr + "user/login";

    public static String jsessionid;

    // 注册的url
    private final String RegisterAddr = ServerAddr + "user/register";
    // 登出的url
    public final static String LogingOutAddr = ServerAddr + "user/loginout";

    /**
     * 后台的返回参数
     */
    // 登录的返回参数
    public static final int LOGIN_ERROR = 1; // 登录错误

    // 注册的返回参数
    public static final int REGISTER_NAME_EXISTED = -1;// 用户名存在
    public static final int REGISTER_EMAIL_EXISTED = -2;// 邮箱存在
    public static final int REGISTER_SUCCESS = 0;// 注册成功
    public static final int REGISTER_ERROR = 1; // 数据库异常

    // 登出的返回参数
    public static final int LOGINOUT_SUCCESS = 0; // 登出成功
    /**
     * 登陆
     * <p/>
     * 必填字段// user.email // user.password
     *
     * @param user 用户
     */
    public String Login(User user) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login.username", user.getName()));
        params.add(new BasicNameValuePair("login.password", user.getPassword()));
        String jsonData = "";
        try {
            jsonData = loginAndGetCookies(LoginAddr, params);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // int result = getJsonResult(jsonData);
        // JsonObject jo=new JsonObject();
        // jo.get
        return jsonData;
        // gson.
    }

    /**
     * 登录
     */
    public String loginAndGetCookies(String url, List<NameValuePair> params)
            throws ClientProtocolException, IOException {
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            CookieStore store = httpClient.getCookieStore();
            List<Cookie> cookies = store.getCookies();
            // Log.v("LoginActivity", cookies.toString());

            for (int i = 0; i < cookies.size(); i++) {
                if ("JSESSIONID".equals(cookies.get(i).getName())) {
                    jsessionid = cookies.get(i).getValue();
                    Log.v("LoginActivity", jsessionid);
                    break;
                }
            }

            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
            Log.v("LoginActivity",jsonData+"");
        }
        return jsonData;
    }

    /**
     * 注册 必填字段// reg.name // reg.email // reg.password // reg.confirm //
     *
     */
    public String Register(User user) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 必填
        params.add(new BasicNameValuePair("reg.username", user.getName()));
        params.add(new BasicNameValuePair("reg.password", user.getPassword()));
        params.add(new BasicNameValuePair("reg.mail", user.getEmail()));
        params.add(new BasicNameValuePair("reg.confirm", user.getConfirm()));

        String result = "";
        try {
            result = getResultFromNet(RegisterAddr, params);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Gson gson = new Gson();
        // Map<String, Integer> map = new HashMap<String, Integer>();
        // map = gson.fromJson(result, Map.class);
        // double resultNum = map.get("result");
        // System.out.println(resultNum);
        return result;
    }

    /**
     * 从网络获取结果数据
     */
    public String getResultFromNet(String url, List<NameValuePair> params)
            throws ClientProtocolException, IOException {
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            jsonData = EntityUtils.toString(entity, HTTP.UTF_8);
        }
        return jsonData;
    }

    /**
     * 用户登出
     */
    public String loginOut(String url) throws IOException {
        HttpPost httpRequest = new HttpPost(url);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            jsonData = EntityUtils.toString(entity, HTTP.UTF_8);
        }
        return jsonData;
    }
}
