package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.Bid;
import cn.wolfcode.p2p.bussiness.domain.ExpAccount;
import cn.wolfcode.p2p.bussiness.domain.ExpAccountFlow;
import cn.wolfcode.p2p.bussiness.mapper.ExpAccountFlowMapper;
import cn.wolfcode.p2p.bussiness.service.IExpAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service@Transactional
public class ExpAccountFlowServiceImpl implements IExpAccountFlowService{
    @Autowired
    private ExpAccountFlowMapper expAccountFlowMapper;

    @Override
    public int insert(ExpAccountFlow record) {
        return expAccountFlowMapper.insert(record);
    }

    @Override
    public List<ExpAccountFlow> selectAll() {
        return expAccountFlowMapper.selectAll();
    }

    @Override
    public void createFlow(Bid bid, ExpAccount expAccount, BigDecimal amount, int actionType, String note) {
        ExpAccountFlow flow = new ExpAccountFlow();
        if(actionType==BidConst.EXPMONEY_TYPE_REGISTER){
            flow.setUsableAmount(expAccount.getUsableAmount());
            flow.setFreezedAmount(new BigDecimal(0));
            flow.setAmount(new BigDecimal(0));
        }else {
            flow.setUsableAmount(expAccount.getUsableAmount());
            flow.setFreezedAmount(expAccount.getFreezedAmount());
            flow.setAmount(bid.getAvailableAmount());
        }
        flow.setNote(note);
        flow.setActionTime(new Date());
        flow.setActionType(actionType);
        flow.setExpAccountId(expAccount.getId());
        this.insert(flow);
    }

    @Override
    public void createRegister_GrantExpMoney_flow(Bid bid , ExpAccount expAccount, BigDecimal amount) {
        createFlow(bid,expAccount, amount, BidConst.EXPMONEY_TYPE_REGISTER, "发放体验金,可用余额增加" + amount + "元");
    }

    @Override
    public void createBidFlow(Bid bid ,ExpAccount expAccount, BigDecimal amount) {
        createFlow(bid,expAccount, amount, BidConst.EXPMONEY_BID, "体验金投资,体验金可用余额减少" + amount + "元");
    }

    @Override
    public void createBidFailedFlow(Bid bid, ExpAccount expAccount, BigDecimal amount) {
        createFlow(bid,expAccount, amount, BidConst.EXPMONEY_BID_FAILED, "体验标投标失败,增加可用体验金金额" + amount + "元");
    }

    @Override
    public void createBidSuccessFlow(Bid bid, ExpAccount expAccount, BigDecimal amount) {
        createFlow(bid,expAccount, amount, BidConst.EXPMONEY_BID_SUCCESS, "体验标投标成功,解除冻结体验金金额" + amount + "元");
    }
}
