package com.imgbucket.xyztk.util.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorAspect {

    // 全てのコントローラ、サービス、リポジトリを対象
    @AfterThrowing(value = "execution(* *..*..*(..))"
            + "&&(bean(*Contorller) || bean(*Service) || bean(*Mapper))", throwing = "ex")
    public void throwingNull(DataAccessException ex) {
        // 例外処理の内容
        System.out.println("==========================================");
        System.out.println("DataAccessExceptionが発生しました。 :" + ex);
        System.out.println("==========================================");

    }
}
