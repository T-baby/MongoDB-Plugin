package com.cybermkd;

import com.cybermkd.constraints.Exist;
import com.cybermkd.constraints.Inside;
import com.cybermkd.constraints.Type;
import com.cybermkd.kit.MongoValidate;
import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Size;

/**
 * 创建人:T-baby
 * 创建日期: 16/7/5
 * 文件描述:
 */
public class AccountBean extends MongoValidate{

    @Type(value = "int")
    private String id;

    @NotNull
    @Inside(value = {"aaa","xxx"})
    @Size(min = 3,max = 18)
    @Exist(value = false,collectionName = "item",key = "username")
    private String username;

    @SafeHtml
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
