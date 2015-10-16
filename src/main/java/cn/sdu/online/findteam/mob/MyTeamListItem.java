package cn.sdu.online.findteam.mob;


/**
 * Created by wn on 2015/8/29.
 */
public class MyTeamListItem {
    public Long openId;
    public String teamName;
    public String introduce;
    public String parent;
    public String teamID;
    public String imgPath;

    public MyTeamListItem(Long openId, String teamName, String introduce, String parent, String teamID, String imgPath){
        this.openId = openId;
        this.teamName = teamName;
        this.introduce = introduce;
        this.parent = parent;
        this.teamID = teamID;
        this.imgPath = imgPath;
    }
}
