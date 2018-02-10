package cn.wolfcode.p2p.base.mapper;


import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;

import java.util.List;

public interface IpLogMapper {
    int insert(IpLog record);

    List<IpLog> queryList(IpLogQueryObject qo);

    List<IpLog> getEndLogin();

}