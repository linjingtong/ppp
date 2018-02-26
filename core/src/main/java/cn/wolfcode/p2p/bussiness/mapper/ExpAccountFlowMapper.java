package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.ExpAccountFlow;

import java.util.List;

public interface ExpAccountFlowMapper {
    int insert(ExpAccountFlow record);

    List<ExpAccountFlow> selectAll();

}