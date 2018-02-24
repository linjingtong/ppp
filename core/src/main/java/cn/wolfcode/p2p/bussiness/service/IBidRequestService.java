package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by seemygo on 2018/1/26.
 */
public interface IBidRequestService {

    int save(BidRequest record);

    BidRequest get(Long id);

    List<BidRequest> selectAll();

    int update(BidRequest record);

    /**
     * 保存借款申请
     * @param bidRequest
     */
    void borrow_apply(BidRequest bidRequest);

    PageInfo query(BidRequestQueryObject qo);

    /**
     * 发标前审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);

    /**
     * 前台页面按顺序显示的5条数据,不需要分页条
     * 查询三种状态下的标--招标,还款,已还清
     * @param qo
     * @return
     */
    List<BidRequest> queryListIndex(BidRequestQueryObject qo);


    void bid(Long bidRequestId, BigDecimal amount);

    /**
     * 满标一审
     * @param id
     * @param state
     * @param remark
     */
    void audit_one(Long id, int state, String remark);

    /**
     * 满标二审
     * @param id
     * @param state
     * @param remark
     */
    void audit_2(Long id, int state, String remark);

}
