package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.AccountFlow;

import java.util.List;

public interface AccountFlowMapper {
    int insert(AccountFlow record);

    AccountFlow selectByPrimaryKey(Long id);

    List<AccountFlow> selectAll();

    int updateByPrimaryKey(AccountFlow record);
}