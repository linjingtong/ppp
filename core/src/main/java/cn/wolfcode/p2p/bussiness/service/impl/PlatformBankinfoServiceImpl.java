package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.mapper.PlatformBankinfoMapper;
import cn.wolfcode.p2p.bussiness.query.PlatformBankinfoQueryObject;
import cn.wolfcode.p2p.bussiness.service.IPlatformBankinfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlatformBankinfoServiceImpl implements IPlatformBankinfoService {
    @Autowired
    private PlatformBankinfoMapper platformBankinfoMapper;

    @Override
    public int save(PlatformBankinfo record) {
        return platformBankinfoMapper.insert(record);
    }

    @Override
    public PlatformBankinfo get(Long id) {
        return platformBankinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PlatformBankinfo> selectAll() {
        return platformBankinfoMapper.selectAll();
    }

    @Override
    public int update(PlatformBankinfo record) {
        return platformBankinfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(PlatformBankinfoQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List<PlatformBankinfo> list = platformBankinfoMapper.queryForList(qo);
        return new PageInfo(list);
    }

    @Override
    public void saveOrUpdate(PlatformBankinfo platformBankinfo) {
        if (platformBankinfo.getId() != null) {
            platformBankinfoMapper.updateByPrimaryKey(platformBankinfo);
        } else {
            platformBankinfoMapper.insert(platformBankinfo);
        }
    }
}
