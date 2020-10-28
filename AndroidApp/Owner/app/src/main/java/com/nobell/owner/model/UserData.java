package com.nobell.owner.model;

public class UserData {
    public String UserEmail;
    public String UserPwd;
    public String UserName;
    public String UserPhone;
    public String UserPin;
    public int UserRsid = -1;
    public Boolean UserLogined;
    public Boolean UserAuto;

    public void clear_user(){
        this.UserEmail = "";
        this.UserPwd = "";
        this.UserName = "";
        this.UserPhone = "";
        this.UserPin = "";
        this.UserRsid = -1;
        this.UserLogined = false;
        this.UserAuto = false;
    }
}
