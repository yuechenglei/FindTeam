package cn.sdu.online.findteam.resource;

/**
 * 这个类是 SingCompetitionListAdapter 中的数据类
 */
public class SingleCompetitionListItem {
    public int imageView;
    public String teamname;
    public String personnum;
    public int line1;
    public String content;
    public int line2;
    public int look;
    public int join;

    public SingleCompetitionListItem(int imageView, String teamname,
                                     String personnum, int line1,
                                     String content, int line2,
                                     int look, int join){
        this.imageView = imageView;
        this.teamname = teamname;
        this.personnum = personnum;
        this.line1 = line1;
        this.content = content;
        this.line2 = line2;
        this.look = look;
        this.join = join;
    }
}
