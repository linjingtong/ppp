package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2018/1/20.
 */
public interface IIpLogService {
    int insert(IpLog record);

    /**
     * pagehelper依赖中的类
     * @param qo
     * @return
     */
    PageInfo query(IpLogQueryObject qo);
}
