package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.MoneyWithdraw;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by seemygo on 2018/2/25.
 */
public interface IMoneyWithdrawService {

    int save(MoneyWithdraw record);

    MoneyWithdraw get(Long id);

    List<MoneyWithdraw> list();

    void moneyWithdraw_apply(BigDecimal moneyAmount);
}
