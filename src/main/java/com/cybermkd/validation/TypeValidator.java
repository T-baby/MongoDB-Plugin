package com.cybermkd.validation;

import com.cybermkd.constraints.Type;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/3
 * 文件描述:
 */
public class TypeValidator implements ConstraintValidator<Type, String> {

    private String value="string";

    @Override
    public void initialize(Type type) {
        value=type.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        switch (value){
            case "int":return isInt(s);
            case "double":return isDouble(s);
            case "boolean":return isDouble(s);
            default:return true;
        }
    }

    private boolean isInt(String s){
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(s);
        return mer.find();
    }

    private boolean isDouble(String s){
        Matcher mer = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$").matcher(s);
        return mer.find();
    }

    private boolean isBoolean(String s){
        return s.equals("true")||s.equals("false");
    }





}
