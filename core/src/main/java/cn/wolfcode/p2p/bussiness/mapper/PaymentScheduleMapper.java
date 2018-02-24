package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.PaymentSchedule;
import cn.wolfcode.p2p.bussiness.query.PaymentScheduleQueryObject;

import java.util.List;

public interface PaymentScheduleMapper {

    int insert(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PaymentSchedule record);

    List<PaymentSchedule> queryForList(PaymentScheduleQueryObject qo);

    List<PaymentSchedule> queryByBidRequestId(Long bidRequestId);
}