package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.query.PlatformBankinfoQueryObject;

import java.util.List;

public interface PlatformBankinfoMapper {

    int insert(PlatformBankinfo record);

    PlatformBankinfo selectByPrimaryKey(Long id);

    List<PlatformBankinfo> selectAll();

    int updateByPrimaryKey(PlatformBankinfo record);

    List<PlatformBankinfo> queryForList(PlatformBankinfoQueryObject qo);
}