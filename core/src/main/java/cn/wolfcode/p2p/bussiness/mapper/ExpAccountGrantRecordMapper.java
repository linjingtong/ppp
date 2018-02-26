package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.ExpAccountGrantRecord;

public interface ExpAccountGrantRecordMapper {

    int insert(ExpAccountGrantRecord record);

    ExpAccountGrantRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ExpAccountGrantRecord record);
}