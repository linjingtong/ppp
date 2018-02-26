package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.PaymentScheduleDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//还款计划:记录借款每月的还款信息,针对于借款用户的
@Getter
@Setter
public class PaymentSchedule extends BaseDomain {
    private Date payDate;//还款日期
    private Date deadLine;//本期还款截止日期
    private BigDecimal totalAmount = BidConst.ZERO;//本期还款金额
    private BigDecimal principal   = BidConst.ZERO;//本期还款本金
    private BigDecimal interest    = BidConst.ZERO;//本期还款利息
    private int monthIndex;//第几期还款(第几个月)
    private int state = BidConst.PAYMENT_STATE_NORMAL;//本期还款状态

    private List<PaymentScheduleDetail> details = new ArrayList<PaymentScheduleDetail>();//包含的还款计划明细

    private Long      bidRequestId;//借款对象
    private Logininfo borrowUser;//借款人/还款人
    private String    bidRequestTitle;//借款标题
    private int       bidRequestType;//借款类型
    private int       returnType;//还款方式

    public String getStateDisplay(){

       switch (state) {
                   case BidConst.PAYMENT_STATE_NORMAL:
                       return "还款中";
                   case BidConst.PAYMENT_STATE_DONE:
                       return "已还";
                   case BidConst.PAYMENT_STATE_OVERDUE:
                       return "逾期";
                   default:
                       return "";
               }
    }
}