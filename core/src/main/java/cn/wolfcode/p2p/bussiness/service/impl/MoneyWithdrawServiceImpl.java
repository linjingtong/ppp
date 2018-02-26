package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.MoneyWithdraw;
import cn.wolfcode.p2p.bussiness.domain.UserBankinfo;
import cn.wolfcode.p2p.bussiness.mapper.MoneyWithdrawMapper;
import cn.wolfcode.p2p.bussiness.service.IAccountFlowService;
import cn.wolfcode.p2p.bussiness.service.IMoneyWithdrawService;
import cn.wolfcode.p2p.bussiness.service.IUserBankinfoService;
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
}
