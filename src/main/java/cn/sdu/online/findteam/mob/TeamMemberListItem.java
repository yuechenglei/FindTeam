package cn.sdu.online.findteam.mob;

public class TeamMemberListItem {
    public String name;
    public String introduction;
    public int headerbmp;
    public String imgPath;

    public TeamMemberListItem(String name, String introduction, int headerbmp, String imgPath){
        this.name = name;
        this.introduction = introduction;
        this.headerbmp = headerbmp;
        this.imgPath = imgPath;
    }
}
