package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.mapper.IpLogMapper;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class IpLogServiceImpl implements IIpLogService
{
    @Autowired
    private IpLogMapper ipLogMapper;

    @Override
    public int insert(IpLog record) {
        return ipLogMapper.insert(record);
    }

    @Override
    public PageInfo query(IpLogQueryObject qo) {
        //注意顺序
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<IpLog> list = ipLogMapper.queryList(qo);
        return new PageInfo(list);
    }

    public List<IpLog> getEndLogin() {
        return ipLogMapper.getEndLogin();
    }
}
