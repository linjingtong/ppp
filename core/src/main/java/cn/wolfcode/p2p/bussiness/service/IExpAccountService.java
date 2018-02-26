package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.ExpAccount;

/**
 * 体验金账户
 * Created by seemygo on 2018/2/25.
 */
public interface IExpAccountService {

    int insert(ExpAccount record);

    ExpAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ExpAccount record);

    ExpAccount getByUserId(Long userId);
}
