package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class SystemDictionaryServiceImpl implements ISystemDictionaryService{
    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;

    @Override
    public int save(SystemDictionary record) {
        return systemDictionaryMapper.insert(record);
    }

    @Override
    public SystemDictionary get(Long id) {
        return systemDictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SystemDictionary> selectAll() {
        return systemDictionaryMapper.selectAll();
    }

    @Override
    public int update(SystemDictionary record) {
        return systemDictionaryMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(SystemDictionaryQueryObject queryObject) {
        PageHelper.startPage(queryObject.getCurrentPage(),queryObject.getPageSize());
        List<SystemDictionary> list = systemDictionaryMapper.queryForList(queryObject);
        return new PageInfo(list);
    }

    @Override
    public void saveOrUpdate(SystemDictionary systemDictionary) {
        if(systemDictionary.getId()!=null){
            systemDictionaryMapper.updateByPrimaryKey(systemDictionary);
        }else{
            systemDictionaryMapper.insert(systemDictionary);
        }
    }
}
