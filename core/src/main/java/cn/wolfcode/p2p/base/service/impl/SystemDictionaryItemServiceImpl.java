package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService{
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public int save(SystemDictionaryItem record) {
        return systemDictionaryItemMapper.insert(record);
    }

    @Override
    public SystemDictionaryItem get(Long id) {
        return systemDictionaryItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SystemDictionaryItem> selectAll() {
        return systemDictionaryItemMapper.selectAll();
    }

    @Override
    public int update(SystemDictionaryItem record) {
        return systemDictionaryItemMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(SystemDictionaryItemQueryObject queryObject) {
        PageHelper.startPage(queryObject.getCurrentPage(),queryObject.getPageSize());
        List<SystemDictionaryItem> list = systemDictionaryItemMapper.queryForList(queryObject);
        return new PageInfo(list);
    }

    @Override
    public void saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        if(systemDictionaryItem.getId()!=null){
            systemDictionaryItemMapper.updateByPrimaryKey(systemDictionaryItem);
        }else{
            systemDictionaryItemMapper.insert(systemDictionaryItem);
        }
    }

    @Override
    public List<SystemDictionaryItem> queryListBySn(String sn) {
        return systemDictionaryItemMapper.queryListBySn(sn);
    }
}
