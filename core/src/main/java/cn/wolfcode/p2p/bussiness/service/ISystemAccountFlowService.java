package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.SystemAccount;
import cn.wolfcode.p2p.bussiness.domain.SystemAccountFlow;

import java.math.BigDecimal;

/**
 * Created by seemygo on 2018/1/28.
 */
public interface ISystemAccountFlowService {

    int insert(SystemAccountFlow record);

    /**
     * 收取账户管理费流水
     * @param systemAccount
     * @param amount
     */
    void createAccountManageChargeFlow(SystemAccount systemAccount, BigDecimal amount);
    /**
     * 收到利息管理费流水
     * @param systemAccount
     * @param amount
     */
    void createIntrestManageChargeflow(SystemAccount systemAccount, BigDecimal amount);
    /**
     * 收到提现手续费流水
     * @param systemAccount
     * @param amount
     */
    void createDrawChargeFeeFlow(SystemAccount systemAccount, BigDecimal amount);
}
