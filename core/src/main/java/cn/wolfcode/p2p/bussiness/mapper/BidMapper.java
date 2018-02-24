package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.Bid;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BidMapper {

    int insert(Bid record);

    List<Bid> selectAll();

    int updateByPrimaryKey(Bid record);

    Bid selectBidRequestId(Long bidRequestId);

    void changeStateByBidRequestId(@Param("bidRequestId") Long bidRequestId, @Param("state") int state);
}