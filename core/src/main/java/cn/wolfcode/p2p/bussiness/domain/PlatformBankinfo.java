package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


//平台的账户信息,供线下充值转账使用
@Getter
@Setter
public class PlatformBankinfo extends BaseDomain {
    private String bankName;    //银行名称
    private String bankNumber;  //银行账号
    private String bankForkName;    //支行名称
    private String accountName; //开户人姓名

    public String getJsonString() {
        Map<String, Object> json = new HashMap<String,Object>();
        json.put("id", id);
        json.put("bankName", this.bankName);
        json.put("accountName", accountName);
        json.put("bankNumber", bankNumber);
        json.put("bankForkName", bankForkName);
        return JSONObject.toJSONString(json);
    }

}