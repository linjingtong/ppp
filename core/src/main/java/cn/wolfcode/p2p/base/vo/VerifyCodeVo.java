package cn.wolfcode.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter@Setter
public class VerifyCodeVo implements Serializable{

    private String phoneNumber;

    private String verifyCode;

    private Date lastSendTime;//最后发送时间
}
