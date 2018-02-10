package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class IpLog extends BaseDomain {
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED  = 1;
    private String ip;
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date   loginTime;
    private int    state;
    private int userType;  //区分前台用户和后台管理员

    public String getStateDisplay() {
        return state == LOGIN_SUCCESS ? "登录成功" : "登录失败";
    }
    public String getUserTypeDisplay() {
        return userType == Logininfo.USERTYPE_MANAGER ? "后台管理员" : "前台用户";
    }
}
