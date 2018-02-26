package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.util.BidConst;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 体验金账户
 * 
 * @author stef
 *
 */
@Setter
@Getter
public class ExpAccount extends BaseDomain {

	private int version;
	private BigDecimal usableAmount = BidConst.ZERO;// 体验金账户余额
	private BigDecimal freezedAmount = BidConst.ZERO;// 体验金冻结金额
	private BigDecimal unReturnExpAmount = BidConst.ZERO;// 临时垫收体验金

}
