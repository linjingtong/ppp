package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class UserBankinfo extends BaseDomain {

    private Long userinfoId;
    private String bankName;    //银行名称
    private String bankNumber;  //银行账号
    private String bankForName;    //支行名称
    private String accountName; //账户名

    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.accountName)) {
            int len = accountName.length();
            String replace = "";
            replace += accountName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return accountName;
    }

    /**
     * 获取银行卡号的隐藏字符串
     * @return
     */
    public String getAnonymousAccountNumber() {
        if (StringUtils.hasLength(bankNumber)) {
            int len = bankNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += bankNumber.charAt(i);
                }
            }
            return replace;
        }
        return bankNumber;
    }
}