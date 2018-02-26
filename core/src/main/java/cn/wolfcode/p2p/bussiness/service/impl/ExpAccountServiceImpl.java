package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.ExpAccount;
import cn.wolfcode.p2p.bussiness.mapper.ExpAccountMapper;
import cn.wolfcode.p2p.bussiness.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional@Service
public class ExpAccountServiceImpl implements IExpAccountService {
    @Autowired
    private ExpAccountMapper expAccountMapper;

    @Override
    public int insert(ExpAccount record) {
        return expAccountMapper.insert(record);
    }

    @Override
    public ExpAccount selectByPrimaryKey(Long id) {
        return expAccountMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(ExpAccount record) {
        int ret = expAccountMapper.updateByPrimaryKey(record);
        if(ret <= 0){
            throw new RuntimeException("乐观锁异常,体验金账户:" + record.getId());
        }
        return 0;
    }

    @Override
    public ExpAccount getByUserId(Long userId) {
        return expAccountMapper.getByUserId(userId);
    }
}
