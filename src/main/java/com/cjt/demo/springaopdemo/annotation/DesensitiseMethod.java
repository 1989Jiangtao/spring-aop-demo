package com.cjt.demo.springaopdemo.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

import java.lang.annotation.*;

/*************************
 * @Project spring-aop-demo
 * @PackageName com.cjt.demo.springaopdemo.annotation
 * @DateTime 2024/7/2 0002 15:04
 * @Author Cao Jiangtao
 * @Describe 对方法进行拦截的注解
 *************************/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@ConditionalOnWebApplication
public @interface DesensitiseMethod {
}
