package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UserFile extends BaseAuditDomain {

    private int                  score;//审核得分
    private String               image;//图片
    private SystemDictionaryItem fileType;//风控资料类型

    public String getJsonMap(){
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("id",id);
        map.put("state",state);
        map.put("applier",applier.getUsername());
        map.put("image",image);
        map.put("fileType",fileType.getTitle());
        return JSON.toJSONString(map);
    }
}