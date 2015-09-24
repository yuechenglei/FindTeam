package cn.sdu.online.findteam.mob;


/**
 * Created by wn on 2015/8/29.
 */
public class MyTeamListItem {
    public int header;
    public String teamName;
    public String introduce;
    public String parent;
    public String teamID;
    public String imgPath;

    public MyTeamListItem(int header, String teamName, String introduce, String parent, String teamID, String imgPath){
        this.header = header;
        this.teamName = teamName;
        this.introduce = introduce;
        this.parent = parent;
        this.teamID = teamID;
        this.imgPath = imgPath;
    }
}
