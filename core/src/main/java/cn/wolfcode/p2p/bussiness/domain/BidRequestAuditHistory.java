package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidRequestAuditHistory extends BaseAuditDomain {
    public static final int PUBLISH_AUDIT = 0;//发标前审核
    public static final int FULL_AUDIT_1 = 1;//满标一审
    public static final int FULL_AUDIT_2 = 2;//满标二审

    private Long bidRequestId;//所属哪一个借款对象
    private int auditType;//审核类型
}