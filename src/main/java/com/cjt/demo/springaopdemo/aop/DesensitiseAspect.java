package com.cjt.demo.springaopdemo.aop;

import com.cjt.demo.springaopdemo.annotation.DesensitiseField;
import com.cjt.demo.springaopdemo.annotation.DesensitiseMethod;
import com.cjt.demo.springaopdemo.entity.Employee;
import com.cjt.demo.springaopdemo.enums.FieldTypeEnums;
import com.cjt.demo.springaopdemo.utils.MaskUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


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
    public void cut() {
    }

    @AfterReturning(value = "cut()", returning = "result")
    public void DesensitiseResponse(JoinPoint joinPoint, Object result) throws IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        /***********************************************************************************
         * 1. 如果返回的是一个集合，则需要看集合中的元素是否包含需要脱敏的字段，如有则需要逐个脱敏
         * 2. 如果返回的直接是一个对象，则判断是否包含有需要脱敏的字段，如有则直接脱敏处理
         ***********************************************************************************/
        if (method.getReturnType().getTypeName().equals("com.github.pagehelper.PageInfo")) { // 确认是分页返回，需要逐个处理
            // 获取集合中数据，并确认集合中的数据是否有需要脱敏的注解
            PageInfo pageInfo = (PageInfo) result;
            List pageInfoList = pageInfo.getList();
            List targetList = new ArrayList(pageInfoList.size());
            Annotation[] annotations = method.getDeclaredAnnotations();
            boolean present = Arrays.stream(annotations).
                    anyMatch(x -> x.annotationType().getName().equals("com.cjt.demo.springaopdemo.annotation.DesensitiseMethod"));
            // 如果集合中没有需要脱敏的字段，直接跳出
            if (present) {
                // 返回的集合中按照实体类需脱敏字段逐个遍历处理
                for (Object pageItem : pageInfoList) {
                    Class<?> itemClass = pageItem.getClass();
                    // 获取单个元素的所有字段属性
                    Field[] fields = itemClass.getDeclaredFields();
                    // 对象转换为map进行处理
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map itemMap = objectMapper.convertValue(pageItem, Map.class);
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(DesensitiseField.class)) {
                            field.setAccessible(true);
                            Object obj = itemMap.get(field.getName()); // 获取需要脱敏的值，如身份证，姓名等
                            FieldTypeEnums desensitiseType = field.getAnnotation(DesensitiseField.class).value();
                            if (obj instanceof String) {
                                String desVal = desensitise(String.valueOf(obj), desensitiseType);
                                itemMap.replace(field.getName(), desVal); // 重写脱敏后的值
                            }
                        }
                    }
                    targetList.add(objectMapper.convertValue(itemMap, itemClass)); // 当前的一条记录转换后添加到新集合中
                }
                pageInfo.setList(targetList); // 设置新的集合
            }
        } else {
            Annotation[] annotations = method.getDeclaredAnnotations();
            // 返回的实体类是否存在需要脱敏的注解类，如果存在就继续进行后续处理逻辑
            boolean present = Arrays.stream(annotations).
                    anyMatch(x -> x.annotationType().getName().equals("com.cjt.demo.springaopdemo.annotation.DesensitiseMethod"));
            if (present) {
                // 标记有脱敏的方法才进行脱敏处理
                // 利用反射获取当前实体中需要脱敏的字段
                Field[] declaredFields = method.getReturnType().getDeclaredFields();
                // 遍历实体类集合，对标注脱敏的字段进行脱敏处理
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(DesensitiseField.class)) {
                        field.setAccessible(true);
                        Object obj = field.get(result); // 获取需要脱敏的值，如身份证，姓名等
                        FieldTypeEnums desensitiseType = field.getAnnotation(DesensitiseField.class).value();
                        if (obj instanceof String) {
                            String desVal = desensitise(String.valueOf(obj), desensitiseType);
                            field.set(result, desVal); // 重写脱敏后的值
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据类型对传入的对象进行脱敏处理
     *
     * @param val  待脱敏的字符串
     * @param type 字符串对应的类型，按照类型进行脱敏
     * @return 返回脱敏后的字符串
     */
    private String desensitise(String val, FieldTypeEnums type) {
        switch (type) {
            case ID_NUMBER:
                return MaskUtils.maskIdCardNo(val); // 按照身份证进行脱敏处理
            case EMAIL:
                return MaskUtils.maskEmail(val);  // 按照邮箱进行脱敏处理
            case FIXED_PHONE:
            case MOBILE_PHONE:
                return MaskUtils.maskMobile(val); // 按照电话进行脱敏处理
            case ZH_NAME:
                return MaskUtils.maskName(val);  // 按照姓名进行脱敏处理
            default:
                return val;
        }
    }


}
