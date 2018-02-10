package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class QueryObject {
    private Integer currentPage = 1;
    private Integer pageSize = 10;
}
