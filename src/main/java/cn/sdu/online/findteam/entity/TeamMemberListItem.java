package cn.sdu.online.findteam.entity;

public class TeamMemberListItem {
    public String name;
    public String introduction;
    public int headerbmp;

    public TeamMemberListItem(String name, String introduction, int headerbmp){
        this.name = name;
        this.introduction = introduction;
        this.headerbmp = headerbmp;
    }
}
