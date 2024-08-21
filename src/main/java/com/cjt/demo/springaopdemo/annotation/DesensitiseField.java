package com.cjt.demo.springaopdemo.annotation;

import com.cjt.demo.springaopdemo.enums.FieldTypeEnums;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

import java.lang.annotation.*;

/*************************
 * @Project spring-aop-demo
 * @PackageName com.cjt.demo.springaopdemo.annotation
 * @DateTime 2024/6/26 0026 15:55
 * @Author Cao Jiangtao
 * @Describe 字段脱敏注解
 *************************/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@ConditionalOnWebApplication
public @interface DesensitiseField {

    FieldTypeEnums value() default FieldTypeEnums.NULL;

}
