package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.UserBankinfo;

import java.util.List;

/**
 * Created by seemygo on 2018/2/25.
 */
public interface IUserBankinfoService {

    int deleteByPrimaryKey(Long id);

    int insert(UserBankinfo record);

    UserBankinfo selectByPrimaryKey(Long id);

    List<UserBankinfo> selectAll();

    UserBankinfo getBankinfoByUserId(Long userId);

    void bind(UserBankinfo bankInfo);
}
