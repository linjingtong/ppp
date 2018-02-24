package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.*;
import cn.wolfcode.p2p.bussiness.mapper.PaymentScheduleMapper;
import cn.wolfcode.p2p.bussiness.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.bussiness.service.*;
import cn.wolfcode.p2p.bussiness.util.CalculatetUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {
    @Autowired
    private PaymentScheduleMapper         paymentScheduleMapper;
    @Autowired
    private IAccountService               accountService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private ISystemAccountFlowService     systemAccountFlowService;
    @Autowired
    private ISystemAccountService         systemAccountService;
    @Autowired
    private IAccountFlowService           accountFlowService;
    @Autowired
    private IBidRequestService            bidRequestService;
    @Autowired
    private IBidService                   bidService;

    @Override
    public int save(PaymentSchedule record) {
        return paymentScheduleMapper.insert(record);
    }

    @Override
    public PaymentSchedule get(Long id) {
        return paymentScheduleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(PaymentSchedule record) {
        return paymentScheduleMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(PaymentScheduleQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List<PaymentSchedule> list = paymentScheduleMapper.queryForList(qo);
        return new PageInfo(list);
    }

    @Override
    public void borrowBidReturn(Long id) {
        PaymentSchedule ps = this.get(id);
        Account account = accountService.getCurrent();
        //还款对象是否处于待还款状
        if (ps.getState() == BidConst.PAYMENT_STATE_NORMAL) {
            //当用用户是否是还款用户
            if (ps.getBorrowUser().getId().equals(UserContext.getCurrent().getId())) {
                //用户可用金额大于等于还款金额
                if (account.getUsableAmount().compareTo(ps.getTotalAmount()) >= 0) {
                    //还款对象状态,还款日期
                    ps.setState(BidConst.PAYMENT_STATE_DONE);
                    ps.setPayDate(new Date());
                    this.update(ps);
                    //还款明细还款日期
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        paymentScheduleDetailService.updatePayDate(ps.getId(), ps.getPayDate());
                    }
                    //借款人可用金额,待还金额减少,剩余授信额度增加(还款的本金,不是总金额)
                    account.setUsableAmount(account.getUsableAmount().subtract(ps.getTotalAmount()));
                    account.setUnReturnAmount(account.getUnReturnAmount().subtract(ps.getTotalAmount()));
                    account.setRemainBorrowLimit(account.getRemainBorrowLimit().add(ps.getPrincipal()));
                    accountService.updateByPrimaryKey(account);
                    //生成还款流水
                    accountFlowService.createReturnMoneyFlow(account, ps.getTotalAmount());
                    //投资人可用金额增加,生成流水;待收利息,待收本金减少,支付利息手续费,生成流水
                    Long bidUserId;
                    Account bidUserAccount;
                    Map<Long, Account> map = new HashMap<Long, Account>();
                    SystemAccount systemAccount = systemAccountService.selectSystemAccount();
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        bidUserAccount = map.get(psd.getInvestorId());
                        if (bidUserAccount == null) {
                            bidUserAccount = accountService.selectByPrimaryKey(psd.getInvestorId());
                            map.put(psd.getInvestorId(), bidUserAccount);
                        }
                        bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(psd.getTotalAmount()));
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().subtract(psd.getInterest()));
                        //生成回款成功流水
                        accountFlowService.createCallbackMoneyFlow(bidUserAccount, psd.getTotalAmount());
                        //系统收取利息手续费,
                        BigDecimal interestManagerCharge = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
                        systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(interestManagerCharge));
                        //生成收取利息管理费流水
                        systemAccountFlowService.createIntrestManageChargeflow(systemAccount, interestManagerCharge);
                        bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().subtract(interestManagerCharge));
                    }
                    systemAccountService.updateByPrimaryKey(systemAccount);
                    for (Account bidAccount : map.values()) {
                        accountService.updateByPrimaryKey(bidAccount);
                    }
                    //判断是否已经还清(通过bidRequestId查看旗下还款对象的状态)
                   List<PaymentSchedule> pslist = paymentScheduleMapper.queryByBidRequestId(ps.getBidRequestId());
                    for (PaymentSchedule paymentSchedule : pslist) {
                        if(paymentSchedule.getState()== BidConst.PAYMENT_STATE_NORMAL){
                            return;
                        }
                    }
                    //如果全部已还,设置标状态,投资对象状态
                    BidRequest bidRequest = bidRequestService.get(ps.getBidRequestId());
                    bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                    bidRequestService.update(bidRequest);
                    for (Bid bid : bidRequest.getBids()) {
                        bid.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                        bidService.updateByPrimaryKey(bid);
                    }

                }
            }
        }
    }
}
