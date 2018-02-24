package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.query.PlatformBankinfoQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/27.
 */
public interface IPlatformBankinfoService {

    int save(PlatformBankinfo record);

    PlatformBankinfo get(Long id);

    List<PlatformBankinfo> selectAll();

    int update(PlatformBankinfo record);

    PageInfo query(PlatformBankinfoQueryObject qo);

    void saveOrUpdate(PlatformBankinfo platformBankinfo);
}
