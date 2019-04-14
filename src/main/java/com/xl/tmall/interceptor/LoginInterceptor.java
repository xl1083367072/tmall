package com.xl.tmall.interceptor;

import com.xl.tmall.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//登录拦截器
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        需要登录的url
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",
                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };
        HttpSession session = request.getSession();
//        根路径
        String contextPath = session.getServletContext().getContextPath();
//        资源路径
        String url = request.getRequestURI();
        url = StringUtils.remove(url,contextPath+"/");
//        比对请求的url和需要登录的url数组有没有匹配的
        if(compare(url,requireAuthPages)){
            User user = (User) session.getAttribute("user");
//            若没有登录,则跳转到登录页面
            if(user==null){
                response.sendRedirect("login");
                return false;
            }
        }

        return true;
    }

    public boolean compare(String url,String[] requireAuthPages){
        boolean result = false;
        for (String s:requireAuthPages){
            if(StringUtils.startsWith(url,s)){
                result = true;
                break;
            }
        }
        return result;
    }

}
