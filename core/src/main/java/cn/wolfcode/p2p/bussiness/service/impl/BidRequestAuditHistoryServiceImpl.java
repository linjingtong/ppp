package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.bussiness.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.bussiness.service.IBidRequestAuditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller@Transactional
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
    @Override
    public int save(BidRequestAuditHistory record) {
        return bidRequestAuditHistoryMapper.insert(record);
    }

    @Override
    public List<BidRequest> queryByBidRequestId(Long bidRequestId) {
        return bidRequestAuditHistoryMapper.queryByBidRequestId(bidRequestId);
    }
}
