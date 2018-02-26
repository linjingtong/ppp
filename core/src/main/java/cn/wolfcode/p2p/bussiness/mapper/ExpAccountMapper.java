package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.ExpAccount;

public interface ExpAccountMapper {

    int insert(ExpAccount record);

    ExpAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ExpAccount record);

    ExpAccount getByUserId(Long userId);

}