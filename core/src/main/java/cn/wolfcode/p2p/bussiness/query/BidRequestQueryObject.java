package cn.wolfcode.p2p.bussiness.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidRequestQueryObject extends QueryObject {

    private int bidRequestState = -1;

    private String order;  //列表顺序

    private int[] states;
}
