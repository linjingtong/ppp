package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.PaymentSchedule;
import cn.wolfcode.p2p.bussiness.query.PaymentScheduleQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2018/1/28.
 */
public interface IPaymentScheduleService {
    int save(PaymentSchedule record);

    PaymentSchedule get(Long id);

    int update(PaymentSchedule record);

    PageInfo query(PaymentScheduleQueryObject qo);

    void borrowBidReturn(Long id);
}
