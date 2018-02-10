package cn.wolfcode.p2p.base.service;


import cn.wolfcode.p2p.base.domain.Userinfo;

/**
 * Created by seemygo on 2018/1/19.
 */
public interface IUserinfoService {

    int insert(Userinfo record);

    Userinfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Userinfo record);

    Userinfo getCurrent();

   /* void bindPhone(String phoneNumber, String verifyCode);

    void bindEmail(String uuid);*/

    int queryEmailOnly(String email);

    /**
     * 修改个人资料
     * @param userinfo
     */
    void saveBasicInfo(Userinfo userinfo);

    /**
     * 判断当前用户是否满足借款条件
     * @param userinfo
     * @return
     */
    boolean canBorrow(Userinfo userinfo);
}
