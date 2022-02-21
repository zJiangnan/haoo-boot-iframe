package com.haoo.iframe.template.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 系统日志切面类
 * <p></p>
 *
 * @author pluto
 * @version 1.0
 * @createdate 2022/1/27 3:14 PM
 * @see SysLogAspect
 **/
@Aspect     //表示此类为一个 AOP 切面类
@Component
@Slf4j
public class SysLogAspect {

    /**
     * 1、实现切面第一步添加切入点
     */
    @Pointcut("@annotation(com.haoo.iframe.template.aspect.SysLog)")
    public void logPointCut() {

    }

    /**
     * 2、做通知处理
     * {@code @Around} 为环绕通知
     * 在方法的调用前后执行。
     * @param point 待执行实体
     * @return  返回执行方法实体
     * @throws Throwable    运行异常
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
//        获取当前时间
        long beginTime = System.currentTimeMillis();
        //调用执行目标方法 proceed()，获取返回参数，防止 NULL 异常
        Object result = point.proceed();
        //执行后的时间
        long time = System.currentTimeMillis() - beginTime;
        log.info("--------------调用结果：{}", result);
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        log.info("--------------调用耗时：" + time);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

//        获取注解上的描述
        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            log.info("--------------调用接口名：" + syslog.value());
        }

//        请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("----------------请求的类名：{}，请求的方法名：{}", className, methodName);
        log.info("----------------请求方法全路径：{}.{}", className, methodName);

//        请求的参数
        Object[] args = joinPoint.getArgs();
        String argsStr = JSON.toJSONString(args);
        log.info("------------------请求参数：{}", argsStr);
    }

}
