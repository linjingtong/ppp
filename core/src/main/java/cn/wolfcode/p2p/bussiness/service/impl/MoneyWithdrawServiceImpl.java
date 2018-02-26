package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.domain.MoneyWithdraw;
import cn.wolfcode.p2p.bussiness.domain.UserBankinfo;
import cn.wolfcode.p2p.bussiness.mapper.MoneyWithdrawMapper;
import cn.wolfcode.p2p.bussiness.query.MoneyWithdrawQueryObject;
import cn.wolfcode.p2p.bussiness.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service@Transactional
public class MoneyWithdrawServiceImpl implements IMoneyWithdrawService{
    @Autowired
    private MoneyWithdrawMapper moneyWithdrawMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserBankinfoService userBankinfoService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;

    @Override
    public int save(MoneyWithdraw record) {
        return moneyWithdrawMapper.insert(record);
    }

    @Override
    public MoneyWithdraw get(Long id) {
        return moneyWithdrawMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MoneyWithdraw> list() {
        return moneyWithdrawMapper.selectAll();
    }

    @Override
    public void moneyWithdraw_apply(BigDecimal moneyAmount) {
        // 判断当前用户是否有一个提现申请,已经绑定银行卡
        // 判断提现金额<=用户可用余额 && 提现金额>=系统最小提现金额
        Userinfo current = this.userinfoService.getCurrent();
        Account account = this.accountService.getCurrent();
        if (current.getHasBindBankinfo() && !current.getHasMoneyWithDraw_Process()
                && moneyAmount.compareTo(account.getUsableAmount()) <= 0
                && moneyAmount.compareTo(BidConst.MIN_WITHDRAW_AMOUNT) >= 0) {
            // 得到用户绑定的银行卡信息
            UserBankinfo ub = this.userBankinfoService.getBankinfoByUserId(current.getId());

            // 创建一个提现申请对象,设置相关属性;
            MoneyWithdraw m = new MoneyWithdraw();
            m.setAccountName(ub.getAccountName());
            m.setAccountNumber(ub.getBankNumber());
            m.setAmount(moneyAmount);
            m.setApplier(UserContext.getCurrent());
            m.setApplyTime(new Date());
            m.setBankForkName(ub.getBankForName());
            m.setBankName(ub.getBankName());
            m.setCharge(BidConst.MONEY_WITHDRAW_CHARGEFEE);
            m.setState(MoneyWithdraw.STATE_NORMAL);
            this.moneyWithdrawMapper.insert(m);

            // 对于账户:冻结金额增加,提现申请冻结流水;
            account.setUsableAmount(account.getUsableAmount().subtract(moneyAmount));
            account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));
            this.accountFlowService.createMoneyWithDrawApplyFlow(account, moneyAmount);
            this.accountService.updateByPrimaryKey(account);

            // 用户添加状态码
            current.setBitState(BitStatesUtils.addState(current.getBitState(),BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS));
            this.userinfoService.updateByPrimaryKey(current);
        }
    }

    @Override
    public PageInfo query(MoneyWithdrawQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<BidRequest> list = moneyWithdrawMapper.queryForList(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, String remark, int state) {
        // 得到提现申请单
        MoneyWithdraw m = this.moneyWithdrawMapper.selectByPrimaryKey(id);
        // 1,判断:提现单状态
        if (m != null && m.getState() == MoneyWithdraw.STATE_NORMAL) {
            // 2,设置相关参数
            m.setAuditor(UserContext.getCurrent());
            m.setAuditTime(new Date());
            m.setRemark(remark);
            m.setState(state);

          Account account = this.accountService.selectByPrimaryKey(m.getApplier().getId());
            if (state == MoneyWithdraw.STATE_PASS) {
                // 3,如果审核通过
                // 1,冻结金额减少(减少手续费),增加提现支付手续费流水;
                account.setFreezedAmount(account.getFreezedAmount().subtract(
                        m.getCharge()));
                this.accountFlowService.createMoneyWithDrawApplyFlow(account, m.getAmount());
                // 2,系统账户增加可用余额,增加收取提现手续费流水;
                this.systemAccountFlowService.createAccountManageChargeFlow(systemAccountService.selectSystemAccount(),m.getAmount());

                // 3,冻结金额减少(减少提现金额);增加提现成功流水;
                BigDecimal realWithdrawFee = m.getAmount().subtract(
                        m.getCharge());
                account.setFreezedAmount(account.getFreezedAmount().subtract(
                        realWithdrawFee));
                this.accountFlowService.createDrawSuccessFlow(account, m.getAmount());

            } else {
                // 4,如果审核拒绝
                // 1,取消冻结金额,可用余额增加,增加去掉冻结流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(
                        m.getAmount()));
                account.setUsableAmount(account.getUsableAmount().add(
                        m.getAmount()));
                this.accountFlowService.createDrawFailedFlow(account, m.getAmount());
            }
            this.accountService.updateByPrimaryKey(account);
            this.moneyWithdrawMapper.updateByPrimaryKey(m);
            // 5,取消用户状态码
            Userinfo userinfo = this.userinfoService.selectByPrimaryKey(m.getApplier().getId());
            userinfo.setBitState(BitStatesUtils.removeState(userinfo.getBitState(),BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS));
            this.userinfoService.updateByPrimaryKey(userinfo);
        }
    }
}
