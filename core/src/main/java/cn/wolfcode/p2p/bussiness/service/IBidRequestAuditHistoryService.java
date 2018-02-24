package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.domain.BidRequestAuditHistory;

import java.util.List;

/**
 * Created by seemygo on 2018/1/26.
 */
public interface IBidRequestAuditHistoryService {

    int save(BidRequestAuditHistory record);

    List<BidRequest> queryByBidRequestId(Long bidRequestId);
}
