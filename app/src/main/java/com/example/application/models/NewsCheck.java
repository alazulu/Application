package com.example.application.models;

public class NewsCheck {

    private String fkey;
    private Boolean isSwitchOn;


    public void setFkey(String fkey){this.fkey=fkey;}
    public String getFkey(){return fkey;}

    public void setIsSwitchOn(boolean isSwitchOn){this.isSwitchOn=isSwitchOn;}
    public Boolean getIsSwitchOn(){return isSwitchOn;}

}
