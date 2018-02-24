package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.SystemAccount;
import cn.wolfcode.p2p.bussiness.domain.SystemAccountFlow;
import cn.wolfcode.p2p.bussiness.mapper.SystemAccountFlowMapper;
import cn.wolfcode.p2p.bussiness.service.ISystemAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {
    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;

    @Override
    public int insert(SystemAccountFlow record) {
        return systemAccountFlowMapper.insert(record);
    }


    @Override
    public void createAccountManageChargeFlow(SystemAccount systemAccount, BigDecimal amount) {
        createFlow(systemAccount, amount, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE, "收取借款管理费" + amount + "元");
    }

    @Override
    public void createIntrestManageChargeflow(SystemAccount systemAccount, BigDecimal amount) {
        createFlow(systemAccount, amount, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE, "收取利息管理费" + amount + "元");
    }


    public void createFlow(SystemAccount systemAccount, BigDecimal amount, int actionType, String remark) {
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setRemark(remark);
        flow.setActionTime(new Date());
        flow.setAmount(amount);
        flow.setActionType(actionType);
        flow.setUsableAmount(systemAccount.getUsableAmount());
        flow.setFreezedAmount(systemAccount.getFreezedAmount());
        this.insert(flow);
    }
}
