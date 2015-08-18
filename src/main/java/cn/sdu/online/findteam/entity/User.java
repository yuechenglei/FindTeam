package cn.sdu.online.findteam.entity;

import java.io.Serializable;

public class User implements Serializable{
    private String name, password, email, confirm;
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
}
