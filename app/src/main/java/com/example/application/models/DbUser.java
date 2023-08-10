package com.example.application.models;

import java.io.Serializable;

public class DbUser implements Serializable {
    private String isim;
    private String soyisim;
    private String tel;
    private String mail;
    private String userId;
    private String imageurl;
    private int istek;
    private long zaman;


    public void setUserIsim(String isim) {
        this.isim = isim;
    }
    public String getUserIsim() {
        return isim;
    }

    public void setUserSoyisim(String soyisim){this.soyisim=soyisim;}
    public String getUserSoyisim(){return soyisim;}

    public void setUserTel(String tel){this.tel=tel;}
    public String getUserTel(){return tel;}

    public void setUserMail(String mail){this.mail=mail;}
    public String getUserMail(){return mail;}

    public void setUseruserId(String userId){this.userId=userId;}
    public String getUseruserId(){return userId;}

    public void setUserImageurl(String imageurl){this.imageurl=imageurl;}
    public String getUserImageurl(){return imageurl;}

    public void setUserIstek(int istek){this.istek=istek;}
    public int getUserIstek(){return istek;}

    public void setUserZaman(long zaman){this.zaman=zaman;}
    public long getUserZaman(){return zaman;}




}
