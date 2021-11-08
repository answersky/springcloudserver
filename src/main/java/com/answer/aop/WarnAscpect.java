package com.answer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * created by liufeng
 * 2020/9/10
 */
@Aspect
@Component
public class WarnAscpect {

    private Logger logger = LoggerFactory.getLogger(WarnAscpect.class);

    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.answer.service.*.*(..))")
    public void pointCut(){

    }

    /**
     * 定义前置通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        logger.info("【注解：Before】------------------切面  before");
        logger.info("【注解：Before】执行的业务方法名=CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("【注解：Before】业务方法获得的参数=ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterThrowing("pointCut()")
    public void afterThrowing(JoinPoint jp){
        logger.info("【注解：AfterThrowing】方法异常时执行.....");
    }

   /* @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) {
        logger.info("【注解：Around . 环绕前】方法环绕start.....");
        try {
            logger.info("【注解：Before】业务方法获得的参数=ARGS : " + Arrays.toString(pjp.getArgs()));
            //如果不执行这句，会不执行切面的Before方法及业务方法
            Object o =  pjp.proceed();
            logger.info("【注解：Around. 环绕后】方法环绕proceed，结果是 :" + o);
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }*/

}
