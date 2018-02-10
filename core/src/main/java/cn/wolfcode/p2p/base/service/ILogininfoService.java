package cn.wolfcode.p2p.base.service;


import cn.wolfcode.p2p.base.domain.Logininfo;

/**
 * Created by seemygo on 2018/2/9.
 */
public interface ILogininfoService {

    int save(Logininfo record);

    Logininfo get(Long id);

    int update(Logininfo record);

    void userRegister(String username, String password);

    boolean checkUsername(String username);

    Logininfo login(String username, String password, int usertypeUser);
}
