package cn.sdu.online.findteam.mob;

/**
 * 这个类是 SingCompetitionListAdapter 中的数据类
 */
public class SingleCompetitionListItem {
    public int imageView;
    public String teamname;
    public int maxNum;
    public int line1;
    public String content;
    public int line2;
    public int look;
    public int join;
    public String teamID;
    public long userOpenID;
    public int currentNum;
    public String imgPath;

    public SingleCompetitionListItem(int imageView, String teamname,
                                     int maxNum, int line1,
                                     String content, int line2,
                                     int look, int join,
                                     String teamID, long userOpenID,
                                     int currentNum, String imgPath){
        this.imageView = imageView;
        this.teamname = teamname;
        this.maxNum = maxNum;
        this.line1 = line1;
        this.content = content;
        this.line2 = line2;
        this.look = look;
        this.join = join;
        this.teamID = teamID;
        this.userOpenID = userOpenID;
        this.currentNum = currentNum;
        this.imgPath = imgPath;
    }
}
