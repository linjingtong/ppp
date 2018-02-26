package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.MoneyWithdraw;

import java.util.List;

public interface MoneyWithdrawMapper {

    int insert(MoneyWithdraw record);

    MoneyWithdraw selectByPrimaryKey(Long id);

    List<MoneyWithdraw> selectAll();

}