package com.cjt.demo.springaopdemo.aop;

import com.cjt.demo.springaopdemo.annotation.DesensitiseField;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;


/*************************
 * @Project spring-aop-demo
 * @PackageName com.cjt.demo.springaopdemo.aop
 * @DateTime 2024/6/26 0026 15:48
 * @Author Cao Jiangtao
 * @Describe 脱敏处理注解类
 *************************/
@Component
@Aspect
@Slf4j
public class DesensitiseAspect {

    @Pointcut(value = "@annotation(com.cjt.demo.springaopdemo.annotation.DesensitiseMethod)")
    public void cut(){
    }

    @AfterReturning(value = "cut()",returning = "result")
    public void DesensitiseResponse(JoinPoint joinPoint , Object result){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName()
                    .equals("com.cjt.demo.springaopdemo.annotation.DesensitiseMethod")) {
                // 标记有脱敏的方法才进行脱敏处理
                Class<?> returnType = method.getReturnType();
                ObjectMapper objectMapper = new ObjectMapper();
                Map map = objectMapper.convertValue(result, Map.class);
                Field[] declaredFields = returnType.getDeclaredFields();
                for (Field field : declaredFields) {
                    String fieldName = field.getName();
                    field.getGenericType().getTypeName();
                    Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                    for (Annotation declaredAnnotation : declaredAnnotations) {
                        if (declaredAnnotation.annotationType().getName()
                                .equals("com.cjt.demo.springaopdemo.annotation.DesensitiseField")) {

                            Object o = map.get(fieldName);


                        }
                    }


                }

            }

        }

//        log.info("AfterReturning --->> {}",result);

    }


}
