package com.cybermkd.validation;

import com.cybermkd.constraints.Exist;
import com.cybermkd.kit.MongoQuery;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/3
 * 文件描述:
 */
public class ExistValidator implements ConstraintValidator<Exist, String> {

    private boolean value = false;

    private String collectionName = "";

    private String key = "";

    @Override
    public void initialize(Exist exist) {
        value = exist.value();
        collectionName = exist.collectionName();
        key = exist.key();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (value) {
            return exist(s);
        }
        return !exist(s);
    }

    private boolean exist(String s) {
        MongoQuery query = new MongoQuery().use(collectionName);
        if (key.equals("id") || key.equals("_id")) {
            query.byId(s);
        } else {
            query.eq(key, s);
        }
        return query.exist();
    }
}
