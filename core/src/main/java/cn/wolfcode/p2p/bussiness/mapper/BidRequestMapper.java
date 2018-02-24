package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;

import java.util.List;

public interface BidRequestMapper {

    int insert(BidRequest record);

    BidRequest selectByPrimaryKey(Long id);

    List<BidRequest> selectAll();

    int updateByPrimaryKey(BidRequest record);

    List<BidRequest> queryForList(BidRequestQueryObject qo);

    /**
     * 前台页面按顺序显示的5条数据
     * @param
     * @param qo
     * @return
     */
    List<BidRequest> queryListIndex(BidRequestQueryObject qo);
}