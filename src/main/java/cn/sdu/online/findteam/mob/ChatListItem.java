package cn.sdu.online.findteam.mob;

public class ChatListItem {
    public String name;
    public String info;
    public String imgPath;
    public String openId;

    public ChatListItem(String title, String info, String imgPath, String openId) {
        this.name = title;
        this.info = info;
        this.imgPath = imgPath;
        this.openId = openId;
    }
}
