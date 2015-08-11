package cn.sdu.online.findteam.share;

import android.app.Application;

public class MyApplication extends Application{
    public static String IDENTITY = "";
    public static int myMessage_CurrentPage  = 0;
    public static int myTeam_CurrentPage = 0;
    public static int ohterTeam_CurrentPage = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
