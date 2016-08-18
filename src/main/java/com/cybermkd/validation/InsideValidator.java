package com.cybermkd.validation;

import com.cybermkd.constraints.Inside;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/3
 * 文件描述:
 */
public class InsideValidator implements ConstraintValidator<Inside, String> {

    private String[] value;

    @Override
    public void initialize(Inside inside) {
        value = inside.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (String text : value) {
            if (text.equals(s)) {
                return true;
            }
        }
        return false;
    }


}
