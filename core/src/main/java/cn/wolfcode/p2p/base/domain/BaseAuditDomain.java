package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseAuditDomain extends BaseDomain {
    public static final int STATE_NORMAL = 0;//待审核
    public static final int STATE_PASS   = 1;//审核通过
    public static final int STATE_REJECT = 2;//审核拒接

    protected Logininfo applier;//申请人
    protected int       state;//审核状态
    protected Logininfo auditor;//审核人呢
    protected Date      applyTime;//申请时间
    protected Date      auditTime;//审核时间
    protected String    remark;//备注


    public String getStateDisplay() {
        if(state== RealAuth.STATE_NORMAL){
            return "待审核";
        }else if (state== RealAuth.STATE_PASS){
            return "审核通过";
        }
        return "审核拒绝";
    }
}
