package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.Bid;
import cn.wolfcode.p2p.bussiness.mapper.BidMapper;
import cn.wolfcode.p2p.bussiness.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class BidServiceImpl implements IBidService {
    @Autowired
    private BidMapper bidMapper;

    @Override
    public int insert(Bid record) {
        return bidMapper.insert(record);
    }

    @Override
    public Bid selectBidRequestId(Long bidRequestId) {
        return bidMapper.selectBidRequestId(bidRequestId);
    }

    @Override
    public List<Bid> selectAll() {
        return bidMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Bid record) {
        return bidMapper.updateByPrimaryKey(record);
    }

    @Override
    public void changeStateByBidRequestId(Long bidRequestId,int state) {
        bidMapper.changeStateByBidRequestId(bidRequestId,state);
    }
}
