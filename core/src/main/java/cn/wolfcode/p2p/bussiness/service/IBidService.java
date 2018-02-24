package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.Bid;

import java.util.List;

/**
 * Created by seemygo on 2018/1/27.
 */
public interface IBidService {

    int insert(Bid record);

    List<Bid> selectAll();

    int updateByPrimaryKey(Bid record);

    Bid selectBidRequestId(Long bidRequestId);

    /**
     * 满标时改变当前标下所有投标对象的状态
     * @param bidRequestId
     */
    void changeStateByBidRequestId(Long bidRequestId, int state);
}
