package cn.wolfcode.p2p.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by seemygo on 2018/1/18.
 */
public class MyRequestContextHolder{

    public static ThreadLocal<HttpServletRequest> local = new ThreadLocal<HttpServletRequest>();

    public static void setHttpRequest(HttpServletRequest request){
        local.set(request);
    }

    public static HttpServletRequest getHttpRequest(){
        return local.get();
    }
}
