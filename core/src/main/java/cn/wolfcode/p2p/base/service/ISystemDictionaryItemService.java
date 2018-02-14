package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/23.
 */
public interface ISystemDictionaryItemService {

    int save(SystemDictionaryItem record);

    SystemDictionaryItem get(Long id);

    List<SystemDictionaryItem> selectAll();

    int update(SystemDictionaryItem record);

    PageInfo query(SystemDictionaryItemQueryObject queryObject);

    void saveOrUpdate(SystemDictionaryItem systemDictionaryItem);

    List<SystemDictionaryItem> queryListBySn(String sn);
}
