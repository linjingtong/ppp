package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.SystemAccount;
import cn.wolfcode.p2p.bussiness.mapper.SystemAccountMapper;
import cn.wolfcode.p2p.bussiness.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemAccountServiceImpl implements ISystemAccountService {
    @Autowired
    private SystemAccountMapper systemAccountMapper;

    @Override
    public int insert(SystemAccount record) {
        return systemAccountMapper.insert(record);
    }

    @Override
    public SystemAccount selectSystemAccount() {
        return systemAccountMapper.selectSystemAccount();
    }


    @Override
    public int updateByPrimaryKey(SystemAccount record) {
        return systemAccountMapper.updateByPrimaryKey(record);
    }

    @Override
    public SystemAccount initSystemAccount() {
        SystemAccount systemAccount = systemAccountMapper.selectSystemAccount();
        if (systemAccount == null) {
            systemAccount = new SystemAccount();
            this.insert(systemAccount);
        }
        return null;
    }
}
