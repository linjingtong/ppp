package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.SystemAccount;

public interface SystemAccountMapper {

    int insert(SystemAccount record);

    SystemAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKey(SystemAccount record);

    SystemAccount selectSystemAccount();
}