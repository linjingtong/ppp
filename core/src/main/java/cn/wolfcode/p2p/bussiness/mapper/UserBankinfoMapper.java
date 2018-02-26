package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.UserBankinfo;

import java.util.List;

public interface UserBankinfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBankinfo record);

    UserBankinfo selectByPrimaryKey(Long id);

    List<UserBankinfo> selectAll();

    UserBankinfo getBankinfoByUserId(Long userId);
}