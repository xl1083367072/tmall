package com.xl.tmall.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//自定义异常处理类
@RestController
@ControllerAdvice
public class DefaultExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public String defaultHandle(Exception e, HttpServletRequest request) throws Exception{
        e.printStackTrace();
        Class clazz = Class.forName("org.hibernate.exception.ConstraintViolationException");
        if(e.getCause()!=null&&e.getCause().getClass()==clazz){
            return "存在约束,不能删除";
        }
        return e.getMessage();
    }

}
