package com.example.application.models;

public class iletisimItem {
    private String title;
    private String deger;
    private int drawable;

    public iletisimItem(String title,String deger,int drawable){
        this.title=title;
        this.deger=deger;
        this.drawable=drawable;
    }
    public int getilDrawable(){
        return drawable;
    }
    public String getilTitle(){
        return title;
    }
    public String getilDeger(){
        return deger;
    }




}
