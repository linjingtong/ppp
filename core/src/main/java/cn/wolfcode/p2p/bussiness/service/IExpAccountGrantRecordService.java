package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.ExpAccountGrantRecord;

/**
 * Created by seemygo on 2018/2/26.
 */
public interface IExpAccountGrantRecordService {

    int insert(ExpAccountGrantRecord record);

    ExpAccountGrantRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ExpAccountGrantRecord record);
}
