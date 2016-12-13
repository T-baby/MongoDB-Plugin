package com.cybermkd.mongo.kit;

/**
 * 创建人:T-baby
 * 创建日期: 2016/12/12
 * 文件描述:
 */
public class MongoValidate extends MongoBean{

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


}
