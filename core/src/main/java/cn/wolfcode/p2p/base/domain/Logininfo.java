package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Logininfo extends BaseDomain{
    public static final int STATE_NOMAL      = 0;
    public static final int STATE_LOCK       = 1;  //锁定状态
    public static final int USERTYPE_USER    = 0;
    public static final int USERTYPE_MANAGER = 1;  //后台管理员

    private String username;

    private String password;

    private int state;

    private int userType;

}