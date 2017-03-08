package com.cybermkd;

import com.alibaba.fastjson.annotation.JSONField;
import com.cybermkd.mongo.kit.MongoValidate;

import java.util.Date;

public class Time extends MongoValidate{

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date date=new Date();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
