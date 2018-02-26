package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class RealAuth extends BaseAuditDomain {
    public static final int SEX_MALE     = 0;//性别男
    public static final int SEX_FEMALE   = 1;//性别女



    private String    realName;//真实姓名
    private int       sex;//性别
    private String    idNumber;//身份证
    private String    bornDate;//出生日期
    private String    address;//身份证地址
    private String    image1;//身份证正面
    private String    image2;//身份证反面


    public String getSexDisplay() {
        return sex == RealAuth.SEX_MALE ? "男" : "女";
    }


    public String getJsonMap(){
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("id",id);
        map.put("state",state);
        map.put("username",applier.getUsername());
        map.put("realName",realName);
        map.put("sex",getSexDisplay());
        map.put("idNumber",idNumber);
        map.put("bornDate",bornDate);
        map.put("address",address);
        map.put("image1",image1);
        map.put("image2",image2);
        return JSON.toJSONString(map);
    }


    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     * @return
     */
    public String getAnonymousIdNumber() {
        if (StringUtils.hasLength(idNumber)) {
            int len = idNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += idNumber.charAt(i);
                }
            }
            return replace;
        }
        return idNumber;
    }

    /**
     * 获取用户住址的隐藏字符串
     * @return
     */
    public String getAnonymousAddress() {
        if (StringUtils.hasLength(address) && address.length() > 4) {
            String last = address.substring(address.length() - 4,
                    address.length());
            String stars = "";
            for (int i = 0; i < address.length() - 4; i++) {
                stars += "*";
            }
            return stars + last;
        }
        return address;
    }

}