package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.PaymentScheduleDetail;

import java.util.Date;

/**
 * Created by seemygo on 2018/1/28.
 */
public interface IPaymentScheduleDetailService {
    int save(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPaymentScheduleId(Long paymentScheduleId);

    /**
     * 统一更改当前还款对象中的明细对象的还款日期
     * @param psId
     * @param payDate
     */
    void updatePayDate(Long psId, Date payDate);
}
