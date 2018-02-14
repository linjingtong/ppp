package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/23.
 */
public interface ISystemDictionaryService {

    int save(SystemDictionary record);

    SystemDictionary get(Long id);

    List<SystemDictionary> selectAll();

    int update(SystemDictionary record);

    PageInfo query(SystemDictionaryQueryObject queryObject);

    void saveOrUpdate(SystemDictionary systemDictionary);
}
