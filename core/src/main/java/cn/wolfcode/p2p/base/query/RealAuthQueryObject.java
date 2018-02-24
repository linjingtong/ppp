package cn.wolfcode.p2p.base.query;

import cn.wolfcode.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter@Setter
public class RealAuthQueryObject extends QueryObject {
    private int state = -1;
    //申请时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public Date getEndDate() {
        if (endDate != null) {
            return DateUtil.getEndDate(endDate);
        }
        return null;
    }

}
