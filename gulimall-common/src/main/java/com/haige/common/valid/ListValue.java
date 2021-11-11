package com.haige.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @className: com.haige.common.valid-> ListValue
 * @description: 校验注解
 * @author: cqh
 * @createDate: 2021-11-11 15:29
 * @version: 1.0
 * @todo:
 */
@Documented
// 可以使用不同的校验注解
@Constraint(validatedBy = { ListValueConstrainValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {

    String message() default "{com.haige.common.valid.ListValue.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int[] vals() default { };

}
