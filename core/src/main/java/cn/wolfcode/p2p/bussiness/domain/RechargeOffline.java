package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//线下充值单
@Getter
@Setter
public class RechargeOffline extends BaseAuditDomain {
    private PlatformBankinfo bankInfo;//
    private String           tradeCode;//交易号
    private BigDecimal       amount;//充值金额
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date             tradeTime;//充值时间
    private String           note;//充值说明

    public String getJsonString() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", id);
        json.put("username", this.applier.getUsername());
        json.put("tradeCode", tradeCode);
        json.put("amount", amount);
        json.put("tradeTime", tradeTime);
        return JSONObject.toJSONString(json);
    }

}