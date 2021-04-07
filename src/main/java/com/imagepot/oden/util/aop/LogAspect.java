package com.imagepot.oden.util.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    // 全てのパッケージの中で語尾にControllerと付くファイルの全てのメソッドが対象
    @Around("execution(* *..*.*Controller.*(..))")
    public Object startLog(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("メソッド開始：" + jp.getSignature());
        try {
            Object result = jp.proceed();
            System.out.println("メソッド終了" + jp.getSignature());
            return result;
        } catch (Exception e) {
            System.out.println("メソッド異常終了" + jp.getSignature());
            e.printStackTrace();
            throw e;
        }
    }

    // UserDaoのメソッドのみ対象
    @Around("execution(* *..*.*UserDao*.*(..))")
    public Object daoLog(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("メソッド開始：" + jp.getSignature());
        try {
            Object result = jp.proceed();
            System.out.println("メソッド終了" + jp.getSignature());
            return result;
        } catch (Exception e) {
            System.out.println("メソッド異常終了" + jp.getSignature());
            e.printStackTrace();
            throw e;
        }
    }
}
