
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
        List<BidRequest> list = bidRequestMapper.queryForList(qo);
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
                bidRequest.setDisableDate(DateUtils.addDays(new Date(),bidRequest.getDisableDays()));
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
                    amount.compareTo(

                            account.getUsableAmount().min(bidRequest.getRemainAmount())

                    ) <= 0 && //投标金额<=min(可用金额,当前标可投金额)
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
                //审核通过
                //标的状态,投资对象的状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
                bidService.changeStateByBidRequestId(bidRequest.getId(), BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //更新系统账户信息,生成系统流水
                BigDecimal accountManagementCharge = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                SystemAccount systemAccount = systemAccountService.selectSystemAccount();
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(accountManagementCharge));
                systemAccountService.updateByPrimaryKey(systemAccount);
                systemAccountFlowService.createAccountManageChargeFlow(systemAccount, accountManagementCharge);
                //借款人可用金额增加,冻结金额减少,扣除系统借款手续费,待还金额,剩余授信额度减少
                Account requestAccount = accountService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                requestAccount.setUsableAmount(requestAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()).subtract(accountManagementCharge));
                requestAccount.setRemainBorrowLimit(requestAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                requestAccount.setUnReturnAmount(requestAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount()).add(bidRequest.getTotalRewardAmount()));
                accountService.updateByPrimaryKey(requestAccount);
                //借款人userInfo去除正在发标处理的状态
                Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                userinfo.setBitState(BitStatesUtils.removeState(userinfo.getBitState(), BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS));
                userinfoService.updateByPrimaryKey(userinfo);
                //生成借款成功,支付借款手续费两条流水
                accountFlowService.createBorrowSuccessFlow(requestAccount, bidRequest.getBidRequestAmount());
                accountFlowService.createBidRequestManageChargeFlow(               requestAccount, accountManagementCharge);
                //投资人---冻结金额减少,待收利息,待收本金
                Long bidUserId;
                Account bidUserAccount;
                Map<Long, Account> map = new HashMap<Long, Account>();
                for (Bid bid : bidRequest.getBids()) {
                    bidUserAccount = map.get(bid.getBidUser().getId());
                    if (bidUserAccount == null) {
                        bidUserAccount = accountService.selectByPrimaryKey(bid.getBidUser().getId());
                        map.put(bidUserAccount.getId(), bidUserAccount);
                    }
                    bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    //生成投标成功,解除冻结金额流水
                    accountFlowService.createBidSuccessFlow(bidUserAccount, bid.getAvailableAmount());
                }
                //创建还款对象(List集合),还款明细,得出每一期还款的利息,本金,总金额
                List<PaymentSchedule> psList = createPaymentSchedule(bidRequest);
                //遍历还款对象和还款明细,叠加算出投资人的待收利息和待收本金
                for (PaymentSchedule ps : psList) {
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        //注意,获取的是map结合中的account
                        bidUserAccount = map.get(psd.getInvestorId());
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().add(psd.getInterest()));
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                    }
                }
                for (Account account : map.values()) {
                    accountService.updateByPrimaryKey(account);
                }
            }
            this.update(bidRequest);
        }
    }

    //每一期相当于一个还款对象
    private List<PaymentSchedule> createPaymentSchedule(BidRequest bidRequest) {
        List<PaymentSchedule> psList = new ArrayList<PaymentSchedule>();
        PaymentSchedule ps;
        BigDecimal totalPrincipal = BidConst.ZERO;
        BigDecimal totalInterest = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
            ps = new PaymentSchedule();
            ps.setBidRequestTitle(bidRequest.getTitle());
            ps.setReturnType(bidRequest.getReturnType());
            ps.setMonthIndex(i + 1);
            ps.setBidRequestId(bidRequest.getId());
            ps.setBidRequestType(bidRequest.getBidRequestType());
            ps.setBorrowUser(bidRequest.getCreateUser());
            ps.setState(bidRequest.getBidRequestState());
            ps.setDeadLine(DateUtils.addMonths(new Date(), i + 1));
            //计算每期还款利息,本金,总金额
            //判断是否为最后一期
            if (i < bidRequest.getMonthes2Return() - 1) {
                //本期还款总金额
                ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                //总利息
                ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                //本金
                ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
                totalInterest = totalInterest.add(ps.getInterest());
                totalPrincipal = totalPrincipal.add(ps.getPrincipal());
            }else{
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(totalPrincipal));
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(totalInterest));
                ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
            }
            //创建还款对象明细,添加到detailList中
            //注意要先保存还款对象,在创建明细,否则拿不到还款对象id
            paymentScheduleService.save(ps);
            createPaymentScheduleDetail(ps, bidRequest);
            psList.add(ps);
        }
        return psList;
    }

    private void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {
        PaymentScheduleDetail psd;
        BigDecimal totalPrincipal = BidConst.ZERO;
        BigDecimal totalInterest = BidConst.ZERO;
        //还款明细对象个数:投资人数/投标次数
        for (int i = 0; i < bidRequest.getBids().size();i++){
            //获取当前bid对象
            Bid bid = bidRequest.getBids().get(i);
            psd = new PaymentScheduleDetail();
            psd.setPaymentScheduleId(ps.getId());
            psd.setBidRequestId(bidRequest.getId());
            psd.setBidRequestId(bidRequest.getId());
            psd.setReturnType(bidRequest.getReturnType());
            psd.setMonthIndex(i+1);
            psd.setInvestorId(bid.getBidUser().getId());
            psd.setDeadLine(DateUtils.addMonths(new Date(),i+1));
            psd.setBorrowUser(bidRequest.getCreateUser());
            psd.setBidId(bid.getId());
            psd.setBidAmount(bid.getAvailableAmount());
            if(i<bidRequest.getBids().size()-1){
                //注意运算精度和存储精度;中间变量:保存8位;  结果变量:保存4位
                ////计算投资比例,中间变量,保留8位
                BigDecimal bidRate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP);
                psd.setPrincipal(bidRate.multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
                psd.setInterest(bidRate.multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
                totalInterest = totalInterest.add(psd.getInterest());
                totalPrincipal = totalPrincipal.add(psd.getPrincipal());
            }else{
                psd.setInterest(ps.getInterest().subtract(totalInterest));
                psd.setPrincipal(ps.getPrincipal().subtract(totalPrincipal));
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
            }
            paymentScheduleDetailService.save(psd);
            ps.getDetails().add(psd);
        }
    }


}

