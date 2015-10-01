package cn.sdu.online.findteam.net;

import android.content.Context;
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
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdu.online.findteam.entity.User;
import cn.sdu.online.findteam.share.MyApplication;

public class NetCore {
    private static final String ServerAddr = "http://202.194.14.195:8080/findteam/";

    // 登录的url
    private final String LoginAddr = ServerAddr + "user/login";
    // 注册的url
    private final static String RegisterAddr = ServerAddr + "user/register";
    // 登出的url
    public final static String LogingOutAddr = ServerAddr + "user/loginout";
    // 获取个人信息的url
    public final static String getUserInfoAddr = ServerAddr + "user/userInfo";
    // 修改个人信息的url
    public final static String modifyUserInfoAddr = ServerAddr + "user/modify";
    // 获取比赛信息
    public final static String getGamesAddr = ServerAddr + "category/listGame";
    // 获取某个比赛下的队伍
    public final static String getGamesTeamAddr = ServerAddr + "team/listByCategory";
    // 列出某用户的队伍
    public final static String getUserTeamAddr = ServerAddr + "team/listByUser";
    // 加入队伍
    public final static String joinTeamAddr = ServerAddr + "team/join";
    // 创建队伍
    public final static String buildTeamAddr = ServerAddr + "team/create";
    // 获取单只队伍
    public final static String getOneTeamAddr = ServerAddr + "team/getOneTeam";
    // 通过openID获取个人信息
    public final static String getopenIDInfoAddr = ServerAddr + "user/userInfoByOpenId";
    // 上传文件
    public final static String upLoadInfoAddr = ServerAddr + "upload/upload";
    // 加载图片
    public final static String downloadAddr = ServerAddr + "download/imageDownload";
    // 用户允许加入队伍
    public final static String allowJoinAddr = ServerAddr + "team/allow";
    // 用户拒绝加入队伍
    public final static String refuseJoinAddr = ServerAddr + "team/deny";
    // 修改队伍信息
    public final static String modifyTeamAddr = ServerAddr + "team/modify";
    // 增加队伍日志
    public final static String createTeamLogAddr = ServerAddr + "teamlog/create";
    // 获取队伍日志
    public final static String getTeamLogAddr = ServerAddr + "teamlog/listTeam";

    // 获取到的有用cookie
    public static String jsessionid;

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

    // 修改个人信息参数
    public static final int MODIFY_SUCCESS = 0; // 修改成功
    public static final int MODIFY_ERROR = 1; // 修改失败

    // 列出某用户的队伍申请状态参数
    public static final String HAS_ENTERED = "1";
    public static final String HAS_REFUSED = "2";
    public static final String HAS_OFFERED = "3";

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
    }

    /**
     * 登录
     */
    public String loginAndGetCookies(String url, List<NameValuePair> params)
            throws IOException {
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
                    MyApplication.getInstance().getSharedPreferences("jsessionid", Context.MODE_PRIVATE).
                            edit().putString("jsessionid", jsessionid).apply();
                    break;
                }
            }

            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
            Log.v("LoginActivity", jsonData + "");
        }
        return jsonData;
    }

    /**
     * 注册 必填字段// reg.name // reg.email // reg.password // reg.confirm //
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
        return result;
    }

    /**
     * 从网络获取结果数据
     */
    public String getResultFromNet(String url, List<NameValuePair> params)
            throws IOException {
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

    public String getResultWithCookies(String url, List<NameValuePair> params)
            throws IOException {
        HttpPost httpRequest = new HttpPost(url);
        jsessionid = MyApplication.getInstance().getSharedPreferences("jsessionid", Context.MODE_PRIVATE).getString("jsessionid", "");
        Cookie cookie = new BasicClientCookie("JSESSIONID", jsessionid);
        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        List<Cookie> cookies = new ArrayList<Cookie>();
        cookies.add(cookie);
        cookieSpecBase.formatCookies(cookies);
        httpRequest.setHeader(cookieSpecBase.formatCookies(cookies).get(0));

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

    /**
     * 通过openID获取用户信息
     */
    public String getUserInfo(String userId) throws IOException {
/*        HttpPost httpPost = new HttpPost(getUserInfoAddr);*/
        HttpPost httpPost = new HttpPost(getopenIDInfoAddr);
        jsessionid = MyApplication.getInstance().getSharedPreferences("jsessionid", Context.MODE_PRIVATE).getString("jsessionid", "");
        Cookie cookie = new BasicClientCookie("JSESSIONID", jsessionid);
        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        List<Cookie> cookies = new ArrayList<Cookie>();
        cookies.add(cookie);
        cookieSpecBase.formatCookies(cookies);
        httpPost.setHeader(cookieSpecBase.formatCookies(cookies).get(0));

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String jsonData = "";

        if (userId.equals("")) {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                jsonData = EntityUtils.toString(entity, HTTP.UTF_8);
            }
        } else {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // 必填
            params.add(new BasicNameValuePair("usr.openId", userId));
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                jsonData = EntityUtils.toString(entity, HTTP.UTF_8);
            }
        }
        return jsonData;
    }

    /**
     * 修改个人信息
     * <p/>
     * 可填字段// usr.realName(用户真实姓名) // usr.cid(学号) // usr.introduce(个人介绍)
     * // usr.contact(联系方式) // usr.imgPath(用户头像) // usr.newPassword(用户新密码)
     * <p/>
     * 必填字段// usr.password(用户原密码) // jessionid(登录返回的cookie)
     *
     * @param user 用户
     */
    public String modifyUserInfo(User user)
            throws ClientProtocolException, IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 必填
        params.add(new BasicNameValuePair("usr.password", user.getPassword()));

        params.add(new BasicNameValuePair("usr.realName", user.getName()));
        params.add(new BasicNameValuePair("usr.contact", user.getContact()));
        params.add(new BasicNameValuePair("usr.introduce", user.getIntroduce()));
        params.add(new BasicNameValuePair("usr.address", user.getAddress()));
        params.add(new BasicNameValuePair("usr.college", user.getSchool()));
        params.add(new BasicNameValuePair("usr.sex", user.getSex()));
        params.add(new BasicNameValuePair("usr.imgPath", user.getImgPath()));

        HttpPost httpRequest = new HttpPost(modifyUserInfoAddr);
        jsessionid = MyApplication.getInstance().getSharedPreferences("jsessionid", Context.MODE_PRIVATE).getString("jsessionid", "");
        Cookie cookie = new BasicClientCookie("JSESSIONID", jsessionid);
        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        List<Cookie> cookies = new ArrayList<Cookie>();
        cookies.add(cookie);
        cookieSpecBase.formatCookies(cookies);
        httpRequest.setHeader(cookieSpecBase.formatCookies(cookies).get(0));

        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
        }
        return jsonData;
    }

    /**
     * 下拉刷新比赛信息
     *
     * @param pageNum     获取第几页的信息
     * @param pageListNum 每页获取的记录数
     */
    public String pullRefreshGamesData(String url, int pageNum, int pageListNum) throws IOException {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("page", pageNum + ""));
        params.add(new BasicNameValuePair("pagelistnum", pageListNum + ""));

        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
        }
        return jsonData;
    }

    /**
     * 加载某个比赛下的队伍
     *
     * @param gameID 分类id
     */

    public String getGamesTeam(String gameID) throws IOException {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("category.id", gameID + ""));

        HttpPost httpRequest = new HttpPost(getGamesTeamAddr);
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
        }
        return jsonData;
    }

    public String getUserTeam(String userID) throws IOException {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("user.id", userID + ""));

        HttpPost httpRequest = new HttpPost(getUserTeamAddr);
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
        }
        return jsonData;
    }

    public String joinTeam(String teamID) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("team.id", teamID));
        HttpPost httpRequest = new HttpPost(joinTeamAddr);
        jsessionid = MyApplication.getInstance().getSharedPreferences("jsessionid", Context.MODE_PRIVATE).getString("jsessionid", "");
        Cookie cookie = new BasicClientCookie("JSESSIONID", jsessionid);
        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        List<Cookie> cookies = new ArrayList<Cookie>();
        cookies.add(cookie);
        cookieSpecBase.formatCookies(cookies);
        httpRequest.setHeader(cookieSpecBase.formatCookies(cookies).get(0));

        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
        }
        return jsonData;
    }

    public String buildTeam(User user) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("teamInfo.name", user.getTeamName()));
        params.add(new BasicNameValuePair("teamInfo.num", user.getTeamNum()));
        params.add(new BasicNameValuePair("teamInfo.endTime", user.getTeamEndTime()));
        params.add(new BasicNameValuePair("teamInfo.introduce", user.getTeamIntroduce()));
        params.add(new BasicNameValuePair("teamInfo.categoryId", user.getTeamCategoryID()));
        params.add(new BasicNameValuePair("teamInfo.logVisible", user.getLogVisible()));
        params.add(new BasicNameValuePair("teamInfo.verify", user.getTeamVerify()));
        params.add(new BasicNameValuePair("teamInfo.imgPath", user.getImgPath()));

        HttpPost httpRequest = new HttpPost(buildTeamAddr);
        jsessionid = MyApplication.getInstance().getSharedPreferences("jsessionid", Context.MODE_PRIVATE).getString("jsessionid", "");
        Cookie cookie = new BasicClientCookie("JSESSIONID", jsessionid);
        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        List<Cookie> cookies = new ArrayList<Cookie>();
        cookies.add(cookie);
        cookieSpecBase.formatCookies(cookies);
        httpRequest.setHeader(cookieSpecBase.formatCookies(cookies).get(0));

        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        String jsonData = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                jsonData += line + "\r\n";
            }
        }
        return jsonData;
    }
}
