package cn.wolfcode.p2p.base.util;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.vo.VerifyCodeVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserContext {
    public static final String USER_IN_SESSION = "logininfo";
    public static final String VERIFYCODEVO_IN_SESSION = "verifyCodeVo";

    private static HttpServletRequest getRequest() {
        //MyRequestContextHolder.getHttpRequest().getSession();
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static void setCurrent(Logininfo current) {
        getRequest().getSession().setAttribute(USER_IN_SESSION, current);
    }

    public static Logininfo getCurrent() {
        return (Logininfo) getRequest().getSession().getAttribute(USER_IN_SESSION);
    }

    //获取登录的IP
    public static String getIP() {
        return getRequest().getRemoteAddr();
    }

    //设值验证码信息对象
    public static void setVerifyCodeVo(VerifyCodeVo vo) {
        getRequest().getSession().setAttribute(VERIFYCODEVO_IN_SESSION, vo);
    }

    public static VerifyCodeVo getVerifyCodeVo() {
        return (VerifyCodeVo) getRequest().getSession().getAttribute(VERIFYCODEVO_IN_SESSION);
    }
}
