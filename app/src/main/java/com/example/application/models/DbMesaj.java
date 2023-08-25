package com.example.application.models;

import java.io.Serializable;

public class DbMesaj implements Serializable {
    private String gonderen, mesaj;
    private Long timestamp;
    private Boolean okundu;


    public void setGonderen(String gonderen) {this.gonderen = gonderen;}
    public String getGonderen() {
        return gonderen;
    }

    public void setMesaj(String mesaj){this.mesaj=mesaj;}
    public String getMesaj(){return mesaj;}

    public void setTimestamp(Long timestamp){this.timestamp=timestamp;}
    public Long getTimestamp(){return timestamp;}

    public void setOkundu(Boolean okundu){this.okundu=okundu;}
    public Boolean getOkundu(){return okundu;}


}
