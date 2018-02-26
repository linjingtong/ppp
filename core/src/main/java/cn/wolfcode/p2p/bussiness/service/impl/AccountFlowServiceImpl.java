package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.AccountFlow;
import cn.wolfcode.p2p.bussiness.mapper.AccountFlowMapper;
import cn.wolfcode.p2p.bussiness.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccountFlowServiceImpl implements IAccountFlowService {
    @Autowired
    private AccountFlowMapper accountFlowMapper;

    @Override
    public int insert(AccountFlow record) {
        return accountFlowMapper.insert(record);
    }


    @Override
    public List<AccountFlow> selectAll() {
        return accountFlowMapper.selectAll();
    }

    @Override
    public void createRechargeOfflineFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE, "线下充值,可用余额增加" + amount + "元");
    }

    @Override
    public void createBidFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL, "投标冻结金额" + amount + "元");
    }

    @Override
    public void createBidFailedFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED, "投标失败,取消冻结金额" + amount + "元");
    }

    @Override
    public void createBorrowSuccessFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL, "借款成功,可用金额增加" + amount + "元");
    }

    @Override
    public void createBidRequestManageChargeFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_CHARGE, "借款成功,支付平台管理费" + amount + "元");
    }

    @Override
    public void createBidSuccessFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL, "投资成功,冻结金额减少" + amount + "元");
    }

    @Override
    public void createReturnMoneyFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY, "还款成功,可用金额减少" + amount + "元");
    }

    @Override
    public void createCallbackMoneyFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY, "回款成功,可用金额增加" + amount + "元");
    }

    @Override
    public void createMoneyWithDrawApplyFlow(Account account, BigDecimal amount) {
        this.createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED, "提现申请,可用金额减少" + amount + "元");
    }


    public void createFlow(Account account, BigDecimal amount, int actionType, String remark) {
        AccountFlow flow = new AccountFlow();
        flow.setRemark(remark);
        flow.setUsableAmount(account.getUsableAmount());
        flow.setAccountId(account.getId());
        flow.setAmount(amount);
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setTradeTime(new Date());
        flow.setActionType(actionType);
        this.insert(flow);
    }
}
