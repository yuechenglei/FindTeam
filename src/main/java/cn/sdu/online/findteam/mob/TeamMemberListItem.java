package cn.sdu.online.findteam.mob;

public class TeamMemberListItem {
    public String name;
    public String introduction;
    public int headerbmp;
    public String imgPath;
    public String openID;
    public String id;

    public TeamMemberListItem(String name, String introduction, int headerbmp, String imgPath, String openID, String id){
        this.name = name;
        this.introduction = introduction;
        this.headerbmp = headerbmp;
        this.imgPath = imgPath;
        this.openID = openID;
        this.id = id;
    }
}
