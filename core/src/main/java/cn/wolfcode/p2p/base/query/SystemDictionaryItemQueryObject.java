package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SystemDictionaryItemQueryObject extends QueryObject {

    private String keyword;

    private Long parentId;
}
