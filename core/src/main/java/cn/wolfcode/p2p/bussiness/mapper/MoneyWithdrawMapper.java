package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.domain.MoneyWithdraw;
import cn.wolfcode.p2p.bussiness.query.MoneyWithdrawQueryObject;

import java.util.List;

public interface MoneyWithdrawMapper {

    int insert(MoneyWithdraw record);

    MoneyWithdraw selectByPrimaryKey(Long id);

    List<MoneyWithdraw> selectAll();

    int updateByPrimaryKey(MoneyWithdraw record);

    List<BidRequest> queryForList(MoneyWithdrawQueryObject qo);
}