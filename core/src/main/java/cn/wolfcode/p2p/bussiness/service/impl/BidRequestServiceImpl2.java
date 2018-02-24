package cn.wolfcode.p2p.bussiness.service.impl;/*
package cn.wolfcode.p2p.bussiness.service.impl;


import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.*;
import cn.wolfcode.p2p.bussiness.mapper.BidRequestMapper;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import cn.wolfcode.p2p.bussiness.service.*;
import cn.wolfcode.p2p.bussiness.util.CalculatetUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class BidRequestServiceImpl implements IBidRequestService {
   @Autowired
    private BidRequestMapper               bidRequestMapper;
    @Autowired
    private IUserinfoService               userinfoService;
    @Autowired
    private IAccountService                accountService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IBidService                    bidService;
    @Autowired
    private IAccountFlowService            accountFlowService;
    @Autowired
    private ISystemAccountService          systemAccountService;
    @Autowired
    private ISystemAccountFlowService      systemAccountFlowService;
    @Autowired
    private IPaymentScheduleService        paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService  paymentScheduleDetailService;

    @Override
    public int save(BidRequest record) {
        return bidRequestMapper.insert(record);
    }

    @Override
    public BidRequest get(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BidRequest> selectAll() {
        return bidRequestMapper.selectAll();
    }

    @Override
    public int update(BidRequest record) {

        int count = bidRequestMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁异常");
        }
        return count;
    }

    @Override
    public void borrow_apply(BidRequest bidRequest) {
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        //判断当前用户是否具备借款的资格
        if (userinfoService.canBorrow(userinfo) &&
                !userinfo.getHasBidrequestProcess() &&//当前用户是否有一个借款正在处理流程当中
                bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 && //借款金额在系统最小借款金额和用户剩余授信额度之间
                bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) <= 0 &&
                bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE) >= 0 &&//借款利率在最小年利率和最大年利率之间
                bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE) <= 0 &&
                bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0) {  //系统最小投标金额<=最小投标金额
            BidRequest bq = new BidRequest();
            bq.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);//还款方式(按月分期)
            bq.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);
            bq.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//待发布状态
            bq.setBidRequestAmount(bidRequest.getBidRequestAmount());
            bq.setCurrentRate(bidRequest.getCurrentRate());
            bq.setMinBidAmount(bidRequest.getMinBidAmount());
            bq.setMonthes2Return(bidRequest.getMonthes2Return());
            bq.setTotalRewardAmount(CalculatetUtil.calTotalInterest(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL, bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), bidRequest.getMonthes2Return()));
            bq.setTitle(bidRequest.getTitle());
            bq.setDescription(bidRequest.getDescription());
            bq.setDisableDays(bidRequest.getDisableDays());
            bq.setCreateUser(UserContext.getCurrent());
            bq.setApplyTime(new Date());
            bidRequestMapper.insert(bq);
            //给userinfo添加状态
            userinfo.setBitState(BitStatesUtils.addState(userinfo.getBitState(), BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS));
            userinfoService.updateByPrimaryKey(userinfo);
        }
    }

    @Override
    public PageInfo query(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List<BidRequest> list = bidRequestMapper.queryListIndex(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        BidRequest bidRequest = this.get(id);
        //是否处于待审核状态
        if (bidRequest != null && bidRequest.getBidRequestState() == BaseAuditDomain.STATE_NORMAL) {
            //创建审核历史
            CreateBidRequestAuditHistory(BidRequestAuditHistory.PUBLISH_AUDIT, state, remark, bidRequest);
            //审核通过,招标截止时间,发布时间,状态,风控意见
            if (state == BaseAuditDomain.STATE_PASS) {
                bidRequest.setDisableDate(DateUtils.addDays(new Date(), bidRequest.getDisableDays()));//计算招标截止日期
                bidRequest.setPublishTime(new Date());
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                bidRequest.setNote(remark);
            } else {
                //审核拒绝,修改标的状态,userinfo去掉状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                userinfo.setBitState(BitStatesUtils.removeState(userinfo.getBitState(), BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS));
                userinfoService.updateByPrimaryKey(userinfo);
            }

            bidRequestMapper.updateByPrimaryKey(bidRequest);
        }
    }

    //创建审核历史
    private void CreateBidRequestAuditHistory(int AuditType, int state, String remark, BidRequest bidRequest) {
        //创建审核历史对象
        BidRequestAuditHistory auditHistory = new BidRequestAuditHistory();
        auditHistory.setApplyTime(bidRequest.getApplyTime());
        auditHistory.setApplier(bidRequest.getCreateUser());
        auditHistory.setRemark(remark);
        auditHistory.setAuditTime(new Date());
        auditHistory.setAuditor(UserContext.getCurrent());
        auditHistory.setAuditType(AuditType);
        auditHistory.setState(state);
        auditHistory.setBidRequestId(bidRequest.getId());
        if (state == BaseAuditDomain.STATE_PASS) {
            auditHistory.setState(BaseAuditDomain.STATE_PASS);
        } else {
            auditHistory.setState(BaseAuditDomain.STATE_REJECT);
        }
        bidRequestAuditHistoryService.save(auditHistory);
    }

    @Override
    public List<BidRequest> queryListIndex(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        //进行list查询时,当前面存在startPage()方法,自动封装分页;
        //可以直接返回list,不返回PageInfo
        List<BidRequest> list = bidRequestMapper.queryListIndex(qo);
        return list;
    }

    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {
        //获取当前标,是否处于招标状态
        BidRequest bidRequest = this.get(bidRequestId);
        Account account = accountService.getCurrent();
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING) {
            if (amount.compareTo(bidRequest.getMinBidAmount()) >= 0 && //投标金额>=标最小投标金额
                    amount.compareTo(account.getUsableAmount().min(bidRequest.getRemainAmount())) <= 0 && //投标金额<=min(可用金额,当前标可投金额)
                    !bidRequest.getCreateUser().getId().equals(UserContext.getCurrent().getId())) {//当前用户是否是借款人
                //标变化,标的已投金额增加,投标次数
                bidRequest.setBidCount(bidRequest.getBidCount() + 1);
                bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
                //account信息变化
                account.setUsableAmount(account.getUsableAmount().subtract(amount));
                account.setFreezedAmount(account.getFreezedAmount().add(amount));
                accountService.updateByPrimaryKey(account);
                //生成一条投标数据Bid
                Bid bid = new Bid();
                bid.setBidUser(UserContext.getCurrent());
                bid.setBidTime(new Date());
                bid.setBidRequestTitle(bidRequest.getTitle());
                bid.setBidRequestId(bidRequestId);
                bid.setAvailableAmount(amount);
                bid.setActualRate(bidRequest.getCurrentRate());
                bid.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                bidService.insert(bid);
                //生成投标流水
                accountFlowService.createBidFlow(account, amount);
                //判断当前标是否满
                if (bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount()) == 0) {
                    //如果已经投满,当前标状态变化
                    bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                    // 当前标下的所有投标数据状态全部更改
                    bidService.changeStateByBidRequestId(bidRequestId, BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                }
                this.update(bidRequest);
            }
        }
    }

    private void auditReject(BidRequest bidRequest) {
        // 投标对象状态
        bidService.changeStateByBidRequestId(bidRequest.getId(), BidConst.BIDREQUEST_STATE_REJECTED);
        //标状态,借款人去除一种状态
        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
        Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
        userinfo.setBitState(BitStatesUtils.removeState(userinfo.getBitState(), BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS));
        userinfoService.updateByPrimaryKey(userinfo);
        //当前标id的所有投标对象的投资人,可用金额,冻结金额变化
        Long bidUserId;
        Account bidUserAccount;
        Map<Long, Account> map = new HashMap<Long, Account>();
        for (Bid bid : bidRequest.getBids()) {
            bidUserId = bid.getBidUser().getId();
            bidUserAccount = map.get(bidUserId);
            //是否在map中存在,不存在put一条
            if (bidUserAccount == null) {
                bidUserAccount = accountService.selectByPrimaryKey(bidUserId);
                map.put(bidUserId, bidUserAccount);
            }
            //冻结金额,可用金额变化
            bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
            bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(bid.getAvailableAmount()));
            //生成投标失败的流水
            accountFlowService.createBidFailedFlow(bidUserAccount, bid.getAvailableAmount());
        }
        //对账户进行统一修改
        for (Account account : map.values()) {
            accountService.updateByPrimaryKey(account);
        }
    }

    @Override
    public void audit_one(Long id, int state, String remark) {
        //判断是否处于满标一审状态,(投标次数是否超过上限)
        BidRequest bidRequest = this.get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) {
            //保存一条审核历史数据
            this.CreateBidRequestAuditHistory(BidRequestAuditHistory.FULL_AUDIT_1, state, remark, bidRequest);
            //审核通过,标状态和投标对象状态
            if (state == BaseAuditDomain.STATE_PASS) {
                bidService.changeStateByBidRequestId(id, BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            } else {
                //审核拒绝
                this.auditReject(bidRequest);

            }
            this.update(bidRequest);
        }
    }

    @Override
    public void audit_2(Long id, int state, String remark) {
        //是否处于满标二审
        BidRequest bidRequest = this.get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            //保存一条审核历史记录
            this.CreateBidRequestAuditHistory(BidRequestAuditHistory.FULL_AUDIT_2, state, remark, bidRequest);
            if (state == BaseAuditDomain.STATE_REJECT) {
                //审核拒绝
                this.auditReject(bidRequest);
            } else {
                //审核通过,标状态,投标对象状态改变
                bidService.changeStateByBidRequestId(id, BidConst.BIDREQUEST_STATE_PAYING_BACK);//还款中
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //=========借款人=========
                 //借款人userInfo去除正在发标处理的状态
                Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                userinfo.setBitState(BitStatesUtils.removeState(userinfo.getBitState(), BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS));
                userinfoService.updateByPrimaryKey(userinfo);
                Account requestAccount = accountService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                //可用金额(扣除手续费),待还金额,剩余授信额度
                requestAccount.setUsableAmount(requestAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
                requestAccount.setRemainBorrowLimit(requestAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                requestAccount.setUnReturnAmount(requestAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount())));
                //生成借款成功流水
                accountFlowService.createBorrowSuccessFlow(requestAccount,bidRequest.getBidRequestAmount());
                //==========系统账户=========
                SystemAccount systemAccount = systemAccountService.selectSystemAccount();
                //借款管理费,生成流水SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE
                BigDecimal bidRequest_manage_fee = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(bidRequest_manage_fee));
                systemAccountService.updateByPrimaryKey(systemAccount);
                systemAccountFlowService.createAccountManageChargeFlow(systemAccount, bidRequest_manage_fee);
                // 借款人对象扣除手续费,生成扣费流水,更新
                requestAccount.setUsableAmount(requestAccount.getUsableAmount().subtract(bidRequest_manage_fee));
                accountService.updateByPrimaryKey(requestAccount);
                accountFlowService.createBidRequestManageChargeFlow(requestAccount, bidRequest_manage_fee);
                //=============投标人===========
                Long bidUserId;
                Account bidUserAccount;
                Map<Long, Account> map = new HashMap<Long, Account>();
                for (Bid bid : bidRequest.getBids()) {
                    bidUserId = bid.getBidUser().getId();
                    bidUserAccount = map.get(bidUserId);
                    if (bidUserAccount == null) {
                        bidUserAccount = accountService.selectByPrimaryKey(bidUserId);
                        map.put(bidUserId, bidUserAccount);
                    }
                    //冻结金额减少
                    bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    //生成投标成功流水
                    accountFlowService.createBidSuccessFlow(bidUserAccount, bid.getAvailableAmount());
                }
                //=============创建还款对象,还款对象明细===========
                List<PaymentSchedule> paymentSchedules = this.createPaymentSchedule(bidRequest);
                //投资人待收利息和待收本金
                for (PaymentSchedule ps : paymentSchedules) {
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        bidUserAccount = map.get(psd.getInvestorId());
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().add(psd.getInterest()));
                    }
                }
                for (Account bidUser : map.values()) {
                    accountService.updateByPrimaryKey(bidUser);
                }
            }
            this.update(bidRequest);
        }
    }

    private List<PaymentSchedule> createPaymentSchedule(BidRequest bidRequest) {
        List<PaymentSchedule> paymentSchedules = new ArrayList<PaymentSchedule>();
        BigDecimal totalInterest = BidConst.ZERO;
        BigDecimal totalPrincipal = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
            PaymentSchedule ps = new PaymentSchedule();
            ps.setBidRequestId(bidRequest.getId());
            ps.setBidRequestTitle(bidRequest.getTitle());
            ps.setBidRequestType(bidRequest.getBidRequestType());
            ps.setBorrowUser(bidRequest.getCreateUser());
            ps.setReturnType(bidRequest.getReturnType());
            ps.setMonthIndex(i + 1);  //设置已还期数
            ps.setDeadLine(DateUtils.addMonths(new Date(), i + 1));//=====本期还款截止日期:下一个月的今天=======
            //最后一期为投标总金额减去前面所有之和
            //判断是否为最后一期
            if (i < bidRequest.getMonthes2Return() - 1) {
                //注意运算精度和存储精度;中间变量:保存8位;  结果变量:保存4位
                ////计算投资比例,中间变量,保留8位
                BigDecimal bidRate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP);
                psd.setPrincipal(bidRate.multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
                psd.setInterest(bidRate.multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
                //本期还款本金
                ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
                //=====计算当前总本金和总利息,为了得到最后一期的利息和本金======
                totalInterest = totalInterest.add(ps.getInterest());
                totalPrincipal = totalPrincipal.add(ps.getPrincipal());
            } else {
                //最后一期利息
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(totalInterest));
                //最后一期本金
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(totalPrincipal));
                //最后一期还款总金额
                ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
            }

            paymentScheduleService.save(ps);
            //===============生成还款明细===============
            this.createPaymentScheduleDetail(ps, bidRequest);
            paymentSchedules.add(ps);
        }
        return paymentSchedules;
    }

    //每一个投资明细相当于一个投资人
    private void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {
        BigDecimal totalInterest = BidConst.ZERO;
        BigDecimal totalPrincipal = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getBids().size(); i++) {
            Bid bid = bidRequest.getBids().get(i);
            PaymentScheduleDetail psd = new PaymentScheduleDetail();
            //每一个投标创建一个还款明细
            psd.setReturnType(bidRequest.getReturnType());
            psd.setPaymentScheduleId(ps.getId());
            psd.setBidAmount(bid.getAvailableAmount());
            psd.setBidId(bid.getId());
            psd.setBidRequestId(bidRequest.getId());
            psd.setBorrowUser(bidRequest.getCreateUser());
            psd.setInvestorId(bid.getBidUser().getId());
            psd.setDeadLine(DateUtils.addMonths(new Date(), i + 1)); //=====本期还款截止日期:下一个月的今天=======
            psd.setMonthIndex(i + 1);
            //判断是否为最后一个明细,最后一个明细的总金额等于总金额减前面所有之和
            if (i < bidRequest.getBids().size() - 1) {
                //本期还款本金=(本次投标/总借款金额)*本期还款本金
                //CAL_SCALE 计算精度
                psd.setPrincipal(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP).multiply(ps.getPrincipal()));
                //本期还款利息=(本次投标/总借款金额)*本期还款利息
                //STORE_SCALE 存储精度
                psd.setInterest(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.STORE_SCALE, RoundingMode.HALF_UP).multiply(ps.getInterest()));
                //本期还款总金额
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));

                //=====计算当前总本金和总利息,为了得到最后一期的利息和本金======
                totalInterest = totalInterest.add(psd.getInterest());
                totalPrincipal = totalPrincipal.add(psd.getPrincipal());
            } else {
                //最后一期利息
                psd.setInterest(ps.getInterest().subtract(totalInterest));
                //最后一期本金
                psd.setPrincipal(ps.getPrincipal().subtract(totalPrincipal));
                //最后一期还款总金额
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
            }
            paymentScheduleDetailService.save(psd);
            //保存到ps中的details集合中
            ps.getDetails().add(psd);
        }
    }
}
*/
