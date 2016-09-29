package com.cybermkd;

import com.alibaba.fastjson.annotation.JSONField;
import com.cybermkd.constraints.Chinese;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Size;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/25
 * 文件描述:
 */
public class Teacher {

    /**

     * Teacher是一个英语培训机构的老师类,因为MongoDB本身没有约束,故使用class来约束

     * @param id Objectid
     * @param name 老师姓名
     */


    @JSONField(name = "_id")
    private String id;

    @Chinese(value = true)
    @Size(min = 1,max = 4)
    @SafeHtml
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
