package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.domain.BidRequestAuditHistory;

import java.util.List;

public interface BidRequestAuditHistoryMapper {

    int insert(BidRequestAuditHistory record);

    List<BidRequest> queryByBidRequestId(Long bidRequestId);
}