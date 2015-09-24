package cn.sdu.online.findteam.entity;

import java.io.Serializable;

public class User implements Serializable{
    private String
            // 用户名，密码
            name, password,
            // 邮箱，确认密码
            email, confirm,
            // 联系方式, 个性签名
            contact, introduce,
            // 学校学院，家庭住址
            school, address,
            // 性别, 队伍名
            sex, teamName,
            // 团队最大人数， 团队截至时间
            teamNum, teamEndTime,
            // 团队介绍， 团队分类ID
            teamIntroduce, teamCategoryID,
            // 日志是否可见， 是否需要审核
            logVisible, teamVerify,
            // 头像路径
            imgPath;
    private Long ID;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getConfirm(){
        return confirm;
    }

    public void setConfirm(String confirm){
        this.confirm = confirm;
    }

    public void setID(Long ID){
        this.ID = ID;
    }

    public Long getID(){
        return ID;
    }

    public String getContact(){
        return contact;
    }

    public void setContact(String contact){
        this.contact = contact;
    }

    public String getIntroduce(){
        return introduce;
    }

    public void setIntroduce(String introduce){
        this.introduce = introduce;
    }

    public void setSchool(String school){
        this.school = school;
    }

    public String getSchool(){
        return school;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public String getSex(){
        return sex;
    }

    public String getTeamName(){return teamName;}

    public void setTeamName(String teamName){
        this.teamName = teamName;
    }

    public void setTeamNum(String teamNum){
        this.teamNum = teamNum;
    }

    public String getTeamNum(){return teamNum;}

    public String getTeamEndTime(){return teamEndTime;}

    public void setTeamEndTime(String teamEndTime){
        this.teamEndTime = teamEndTime;
    }

    public String getTeamIntroduce(){return teamIntroduce;}

    public void setTeamIntroduce(String teamIntroduce){
        this.teamIntroduce = teamIntroduce;
    }

    public String getTeamCategoryID(){return teamCategoryID;}

    public void setTeamCategoryID(String teamCategoryID){
        this.teamCategoryID = teamCategoryID;
    }

    public String getLogVisible(){return logVisible;}

    public void setLogVisible(String logVisible){
        this.logVisible = logVisible;
    }

    public String getTeamVerify(){return teamVerify;}

    public void setTeamVerify(String teamVerify){
        this.teamVerify = teamVerify;
    }

    public void setImgPath(String imgPath){
        this.imgPath = imgPath;
    }

    public String getImgPath(){
        return imgPath;
    }
}
