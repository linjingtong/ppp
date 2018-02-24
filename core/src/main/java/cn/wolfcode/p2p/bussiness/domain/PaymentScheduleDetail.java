package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.util.BidConst;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

//还款计划明细,针对于投资人,表示投资人的回款明细
@Getter
@Setter
public class PaymentScheduleDetail extends BaseDomain {
    private BigDecimal bidAmount;// 该投标人总共投标金额,便于还款/垫付查询
    private Long       bidId;//投标对象
    private Long       paymentScheduleId;//所属还款计划
    private Logininfo  borrowUser;//发标人/还款人
    private Long       investorId;//投资人(收款人) investorId

    private Long bidRequestId;//借款对象
    private int  returnType;//还款方式
    private Date payDate;//还款日期
    private Date deadLine;//本期还款截止日期
    private int  monthIndex;//第几期还款(第几个月)
    private BigDecimal totalAmount = BidConst.ZERO;//本期还款金额(本金+利息)
    private BigDecimal principal   = BidConst.ZERO;//本期还款本金
    private BigDecimal interest    = BidConst.ZERO;//本期还款利息
}
