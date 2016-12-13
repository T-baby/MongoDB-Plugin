package com.cybermkd;

import com.cybermkd.mongo.kit.MongoValidate;

import java.util.Date;

public class Time extends MongoValidate{

    private Date date=new Date();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
