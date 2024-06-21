package com.cjt.demo.springaopdemo.aop;

import com.cjt.demo.springaopdemo.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.UUID;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.aop
 * @class WebParamAspect
 * @author caojiantao
 * @datetime 2024/6/21 14:53
 * @describe WEB请求参数打印切面类
 *           https://docs.spring.io/spring-framework/reference/core/aop.html
 ****************************************************/
@Aspect
@Component
@ConditionalOnClass(value = {ObjectMapper.class})
public class WebParamAspect {

    /**
     * 全局日志记录
     */
    private static final Logger PARAM_LOG = LoggerFactory.getLogger(WebParamAspect.class);


    /**
     * 定义切点： execution ,with,target等，可以参考官方文档
     * https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/pointcuts.html
     */
    @Pointcut(value = "execution(public * com.cjt.demo.springaopdemo.controller..*(..))")
    public void cut() {
    }

    /**
     * 使用环绕通知：可以拿到请求前和请求后的参数，同时打印
     *
     * @param joinPoint 连接点
     * @return
     */
    @Around(value = "cut()")
    public Object printWebParam(ProceedingJoinPoint joinPoint) throws Throwable {
        // 可以加入接口记录时间，方法进入的时间
        long start = System.currentTimeMillis();

        // 获取连接点方法签名，可以从中解析得到关于该方法的许多信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 方法所在类名称
        String declaringTypeName = methodSignature.getDeclaringTypeName();
        // 方法名
        String methodName = methodSignature.getMethod().getName();

        // 生成唯一的交易编码，便于观察出参和入参
        String uuid = UUID.randomUUID().toString();

        // 获取参数
        Object[] args = joinPoint.getArgs();
        String jsonString = JsonUtil.toJSONString(args);
        // 判断是否开启打印，打印请求信息
        if (PARAM_LOG.isInfoEnabled()) {
            PARAM_LOG.info("TRACE:{} , 请求服务{} - 请求方法{}, 请求参数： {}", uuid, declaringTypeName, methodName, jsonString);
        }


        Object result = null;
        try {
            // 执行代理类的实现逻辑
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            if (PARAM_LOG.isErrorEnabled()) {
                PARAM_LOG.error("TRACE:{} , 请求服务{} - 请求方法{}, 发生异常了，原因是： {}", uuid, declaringTypeName, methodName, throwable.getMessage(), throwable);
            }
            throw throwable;
        }
        // 方法结束的时间
        long end = System.currentTimeMillis();
        // 判断是否开启打印，打印结果信息
        if (PARAM_LOG.isInfoEnabled()) {
            PARAM_LOG.info("TRACE:{} , 请求服务{} - 请求方法{} , 请求耗时 ： {} ms , 请求结果： {}", uuid, declaringTypeName, methodName, (end - start), jsonString);
        }

        return result;
    }

}
