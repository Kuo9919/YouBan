package com.example.poemzj.user;
/**
 * Created by littlecurl 2018/6/24
 */
public class User {
    private String name;            //用户名
    private String facename;        //昵称
    private String password;        //密码
    private String sex;
    private String sign;            //签名
    private String date;            //出生日期
    
    
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setSign(String sign){
        this.sign=sign;
    }
    public void setSex(String sex){
        this.sex=sex;
    }
    public void setFacename(String facename){
        this.facename=facename;
    }
    public void setDate(String date){
        this.date=date;
    }
    
    public String getDate(){
        return date;
    }
    public String getFacename(){
        return facename;
    }
    public String getSex(){
        return sex;
    }
    public String getSign(){
        return sign;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

