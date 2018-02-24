package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.SystemAccount;

/**
 * Created by seemygo on 2018/1/28.
 */
public interface ISystemAccountService {


    int insert(SystemAccount record);

    SystemAccount selectSystemAccount();

    int updateByPrimaryKey(SystemAccount record);

    /**
     * 启动tomcat时查找数据库中是否存在账户
     * @return
     */
    SystemAccount initSystemAccount();
}
