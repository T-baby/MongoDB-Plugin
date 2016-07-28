package com.cybermkd.kit;

/**
 * 创建人:T-baby
 * 创建日期: 16/7/25
 * 文件描述:
 */
public class MongoValidate {


    String validateErrorMessage = "";

    public boolean validation() {
        validateErrorMessage = MongoKit.validation(this);
        return validateErrorMessage.isEmpty();
    }

    public String getErrorMessage() {
        return validateErrorMessage;
    }
}
