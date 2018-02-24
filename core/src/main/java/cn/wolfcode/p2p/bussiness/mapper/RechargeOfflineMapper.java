package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import cn.wolfcode.p2p.bussiness.query.RechargeOfflineQueryObject;

import java.util.List;

public interface RechargeOfflineMapper {

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    List<RechargeOffline> selectAll();

    int updateByPrimaryKey(RechargeOffline record);

    List<RechargeOffline> queryForList(RechargeOfflineQueryObject qo);
}