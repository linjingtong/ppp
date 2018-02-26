package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 提现申请
 * 
 * @author Administrator
 * 
 */
@Getter
@Setter
public class MoneyWithdraw extends BaseAuditDomain {

	private BigDecimal amount;// 提现金额
	private BigDecimal charge;// 提现手续费
	private String bankName;// 银行名称
	private String accountName;// 开户人姓名
	private String accountNumber;// 银行账号
	private String bankForkName;// 开户支行

	public String getJsonString() {
		Map<String, Object> json = new HashMap<String,Object>();
		json.put("id", id);
		json.put("username", this.applier.getUsername());
		json.put("realName", accountName);
		json.put("applyTime", DateFormat.getDateTimeInstance()
				.format(applyTime));
		json.put("bankName", bankName);
		json.put("accountNumber", accountNumber);
		json.put("bankforkname", bankForkName);
		json.put("moneyAmount", amount);
		return JSONObject.toJSONString(json);
	}
}
