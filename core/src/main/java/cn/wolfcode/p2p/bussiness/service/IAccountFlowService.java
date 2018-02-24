package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.bussiness.domain.AccountFlow;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by seemygo on 2018/1/27.
 */
public interface IAccountFlowService {

    int insert(AccountFlow record);

    List<AccountFlow> selectAll();

    /**
     * 创建线下充值流水
     * @param account
     * @param amount
     */
    void createRechargeOfflineFlow(Account account, BigDecimal amount);
    /**
     * 创建投标流水
     * @param account
     * @param amount
     */
    void createBidFlow(Account account, BigDecimal amount);

    /**
     * 创建投标失败流水
     * @param account
     * @param amount
     */
    void createBidFailedFlow(Account account, BigDecimal amount);

    /**
     * 借款成功,可用金额增加
     * @param account
     * @param amount
     */
    void createBorrowSuccessFlow(Account account, BigDecimal amount);
    /**
     * 支付平台管理手续费流水
     * @param account
     * @param amount
     */
    void createBidRequestManageChargeFlow(Account account, BigDecimal amount);

    /**
     * 投资成功流水
     * @param account
     * @param amount
     */
    void createBidSuccessFlow(Account account, BigDecimal amount);
    /**
     * 还款成功流水
     * @param account
     * @param amount
     */
    void createReturnMoneyFlow(Account account, BigDecimal amount);
    /**
     * 回款成功流水
     * @param account
     * @param amount
     */
    void createCallbackMoneyFlow(Account account, BigDecimal amount);
}
