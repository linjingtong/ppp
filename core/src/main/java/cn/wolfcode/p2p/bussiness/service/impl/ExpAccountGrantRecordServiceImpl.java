package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.ExpAccountGrantRecord;
import cn.wolfcode.p2p.bussiness.mapper.ExpAccountGrantRecordMapper;
import cn.wolfcode.p2p.bussiness.service.IExpAccountGrantRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service@Transactional
public class ExpAccountGrantRecordServiceImpl implements IExpAccountGrantRecordService{
    @Autowired
    private ExpAccountGrantRecordMapper expAccountGrantRecordMapper;

    @Override
    public int insert(ExpAccountGrantRecord record) {
        return expAccountGrantRecordMapper.insert(record);
    }

    @Override
    public ExpAccountGrantRecord selectByPrimaryKey(Long id) {
        return expAccountGrantRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(ExpAccountGrantRecord record) {
        return expAccountGrantRecordMapper.updateByPrimaryKey(record);
    }
}
