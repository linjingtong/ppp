package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserFileMapper;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static cn.wolfcode.p2p.base.domain.BaseAuditDomain.STATE_PASS;

@Service
@Transactional
public class UserFileServiceImpl implements IUserFileService {
    @Autowired
    private UserFileMapper   userFileMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(UserFile record) {
        return userFileMapper.insert(record);
    }

    @Override
    public UserFile get(Long id) {
        return userFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserFile> selectAll() {
        return userFileMapper.selectAll();
    }

    @Override
    public int update(UserFile record) {
        return userFileMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(UserFileQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List<UserFile> list = userFileMapper.queryForList(qo);
        return new PageInfo(list);
    }

    @Override
    public void saveFirst(String fileName) {
        UserFile userFile = new UserFile();
        userFile.setImage(fileName);
        userFile.setApplyTime(new Date());
        userFile.setApplier(UserContext.getCurrent());
        userFileMapper.insert(userFile);
    }


    @Override
    public List<UserFile> getHasNullFileType() {
        return userFileMapper.getHasNullFileType();
    }

    @Override
    public void addFileType(Long[] fileTypes, Long[] ids) {
        if (ids != null && fileTypes != null && ids.length == fileTypes.length) {
            UserFile userFile;
            SystemDictionaryItem sysItem;
            for (int i = 0; i < ids.length; i++) {
                userFile = this.get(ids[i]);
                if (UserContext.getCurrent().getId().equals(userFile.getApplier().getId())) {
                    sysItem = new SystemDictionaryItem();
                    sysItem.setId(fileTypes[i]);
                    userFile.setFileType(sysItem);
                    this.update(userFile);
                } else {
                    throw new RuntimeException("非法操作");
                }
            }
        }
    }

    @Override
    public void audit(Long id, int state, int score, String remark) {
        UserFile userFile = userFileMapper.selectByPrimaryKey(id);
        if (userFile != null && userFile.getState() == UserFile.STATE_NORMAL) {
            if (state == STATE_PASS) {
                userFile.setRemark(remark);
                userFile.setScore(score);
                userFile.setAuditTime(new Date());
                userFile.setAuditor(UserContext.getCurrent());
                userFile.setState(STATE_PASS);
                userFileMapper.updateByPrimaryKey(userFile);
                Userinfo userinfo = userinfoService.selectByPrimaryKey(userFile.getApplier().getId());
                userinfo.setScore(userinfo.getScore() + score);
                userinfoService.updateByPrimaryKey(userinfo);

            } else {
                userFile.setRemark(remark);
                userFile.setScore(score);
                userFile.setAuditTime(new Date());
                userFile.setAuditor(UserContext.getCurrent());
                userFile.setState(UserFile.STATE_REJECT);
                userFileMapper.updateByPrimaryKey(userFile);
            }
        } else {
            throw new RuntimeException("非法参数");
        }
    }

    @Override
    public List<UserFile> queryListByApplierId(Long applierId) {
        return userFileMapper.queryListByApplierId(applierId, UserFile.STATE_PASS);
    }
}
