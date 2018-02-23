package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class MailVerify extends BaseDomain {

    private Long userId;

    private Date sendTime;

    private String email;

    private String uuid; //邮件验证唯一识别
}
