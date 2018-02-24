package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import cn.wolfcode.p2p.bussiness.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.bussiness.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.bussiness.service.IAccountFlowService;
import cn.wolfcode.p2p.bussiness.service.IRechargeOfflineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountService       accountService;
    @Autowired
    private IAccountFlowService   accountFlowService;

    @Override
    public int save(RechargeOffline record) {
        RechargeOffline rechargeOffline = new RechargeOffline();
        rechargeOffline.setState(BaseAuditDomain.STATE_NORMAL);
        rechargeOffline.setNote(record.getNote());
        rechargeOffline.setAmount(record.getAmount());
        rechargeOffline.setBankInfo(record.getBankInfo());
        rechargeOffline.setTradeCode(record.getTradeCode());
        rechargeOffline.setTradeTime(record.getTradeTime());
        rechargeOffline.setApplyTime(new Date());
        rechargeOffline.setApplier(UserContext.getCurrent());
        rechargeOfflineMapper.insert(rechargeOffline);
        return 0;
    }

    @Override
    public RechargeOffline get(Long id) {
        return rechargeOfflineMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<RechargeOffline> selectAll() {
        return rechargeOfflineMapper.selectAll();
    }

    @Override
    public int update(RechargeOffline record) {
        return rechargeOfflineMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(RechargeOfflineQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List<RechargeOffline> list = rechargeOfflineMapper.queryForList(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        RechargeOffline rechargeOffline = rechargeOfflineMapper.selectByPrimaryKey(id);
        if (rechargeOffline != null && rechargeOffline.getState() == BaseAuditDomain.STATE_NORMAL) {
            if(state== BaseAuditDomain.STATE_PASS){
                rechargeOffline.setState(BaseAuditDomain.STATE_PASS);
                //用户可用金额增加
                Account account = accountService.selectByPrimaryKey(rechargeOffline.getApplier().getId());
                account.setUsableAmount(account.getUsableAmount().add(rechargeOffline.getAmount()));
                accountService.updateByPrimaryKey(account);
                //生成一条流水
                accountFlowService.createRechargeOfflineFlow(account,rechargeOffline.getAmount());
            }else{
                rechargeOffline.setState(BaseAuditDomain.STATE_REJECT);
            }
            rechargeOffline.setRemark(remark);
            rechargeOffline.setAuditor(UserContext.getCurrent());
            rechargeOffline.setAuditTime(new Date());
            this.update(rechargeOffline);
        }
    }
}
