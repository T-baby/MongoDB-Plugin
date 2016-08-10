package com.cybermkd.constraints;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/3
 * 文件描述:
 */

import com.cybermkd.validation.ChineseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ChineseValidator.class)
@Documented
public @interface Chinese {

    String message() default "{com.cybermkd.constraints.Chinese.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean value();

}

