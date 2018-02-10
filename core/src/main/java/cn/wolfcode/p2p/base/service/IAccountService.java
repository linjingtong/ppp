package cn.wolfcode.p2p.base.service;


import cn.wolfcode.p2p.base.domain.Account;

/**
 * Created by seemygo on 2018/1/19.
 */
public interface IAccountService {

    int insert(Account record);

    Account selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Account record);

    Account getCurrent();
}
