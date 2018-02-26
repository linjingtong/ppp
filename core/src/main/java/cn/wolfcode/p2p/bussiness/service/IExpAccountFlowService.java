package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.Bid;
import cn.wolfcode.p2p.bussiness.domain.ExpAccount;
import cn.wolfcode.p2p.bussiness.domain.ExpAccountFlow;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by seemygo on 2018/2/26.
 */
public interface IExpAccountFlowService {
    int insert(ExpAccountFlow record);

    List<ExpAccountFlow> selectAll();

    /**
     * 发放体验金流水
     * @param expAccount
     * @param amount
     */
    void createRegister_GrantExpMoney_flow(Bid bid ,ExpAccount expAccount, BigDecimal amount);

    void createFlow(Bid bid, ExpAccount expAccount, BigDecimal amount, int actionType, String note);

    /**
     * 投资流水
     * @param expAccount
     * @param amount
     */
    void createBidFlow(Bid bid ,ExpAccount expAccount, BigDecimal amount);

    /**
     * 体验标投标失败流水
     * @param bid
     * @param expAccount
     * @param amount
     */
    void createBidFailedFlow(Bid bid, ExpAccount expAccount, BigDecimal amount);
    /**
     * 体验标投标失败流水
     * @param bid
     * @param expAccount
     * @param amount
     */
    void createBidSuccessFlow(Bid bid, ExpAccount expAccount, BigDecimal amount);
}
