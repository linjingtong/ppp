package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import cn.wolfcode.p2p.bussiness.query.RechargeOfflineQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/27.
 */
public interface IRechargeOfflineService {

    int save(RechargeOffline record);

    RechargeOffline get(Long id);

    List<RechargeOffline> selectAll();

    int update(RechargeOffline record);

    PageInfo query(RechargeOfflineQueryObject qo);

    void audit(Long id, int state, String remark);
}
