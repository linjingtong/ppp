package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.bussiness.mapper.PaymentScheduleDetailMapper;
import cn.wolfcode.p2p.bussiness.service.IPaymentScheduleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

    @Override
    public int save(PaymentScheduleDetail record) {
        return paymentScheduleDetailMapper.insert(record);
    }

    @Override
    public PaymentScheduleDetail selectByPaymentScheduleId(Long paymentScheduleId) {
        return paymentScheduleDetailMapper.selectByPaymentScheduleId(paymentScheduleId);
    }

    @Override
    public void updatePayDate(Long psId, Date payDate) {
        paymentScheduleDetailMapper.updatePayDate(psId,payDate);
    }
}
