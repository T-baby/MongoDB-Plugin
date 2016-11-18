package com.cybermkd.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 创建人:T-baby
 * 创建日期: 16/7/25
 * 文件描述:
 */
public class MongoValidate {


    String validateErrorMessage = "";

    public boolean validation() {
        validateErrorMessage = MongoKit.INSTANCE.validation(this);
        return validateErrorMessage.isEmpty();
    }

    public boolean validation(String... keys) {
        validateErrorMessage = MongoKit.INSTANCE.validation(this, keys);
        return validateErrorMessage.isEmpty();
    }

    public String errorMessage() {
        return validateErrorMessage;
    }


    public String toString(){
        return JSON.toJSONString(this);
    }

    public Map toMap() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return MongoKit.INSTANCE.toMap(this);
    }

    public JSONObject toJSON(){
        return (JSONObject) JSON.toJSON(this);
    }

}
