package com.cybermkd;

import com.alibaba.fastjson.annotation.JSONField;
import com.cybermkd.constraints.Exist;
import com.cybermkd.constraints.Inside;
import com.cybermkd.constraints.Type;
import com.cybermkd.kit.MongoValidate;
import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Size;

public class AccountBean extends MongoValidate{

    @Type(value = "int")
    private String id;

    @NotNull
    @Inside(value = {"mongo","db"})
    @Size(min = 3,max = 18)
    @Exist(value = false,collectionName = "item",key = "username")
    private String username;

    @SafeHtml
    private String password;

    private Integer age;

    @JSONField(name = "create_at")
    private Long createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
}
