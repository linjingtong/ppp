package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.PaymentScheduleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface PaymentScheduleDetailMapper {

    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPaymentScheduleId(Long paymentScheduleId);

    void updatePayDate(@Param("psId") Long psId, @Param("payDate") Date payDate);
}