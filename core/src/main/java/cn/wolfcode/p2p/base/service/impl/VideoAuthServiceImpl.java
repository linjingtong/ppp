package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.mapper.VideoAuthMapper;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VideoAuthServiceImpl implements IVideoAuthService {
    @Autowired
    private VideoAuthMapper  videoAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(VideoAuth record) {
        return videoAuthMapper.insert(record);
    }

    @Override
    public VideoAuth get(Long id) {
        return videoAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(VideoAuth record) {
        return videoAuthMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(VideoAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List<VideoAuth> list = videoAuthMapper.queryForList(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        Userinfo userinfo = userinfoService.selectByPrimaryKey(id);
        if (userinfo != null && !userinfo.getIsVedioAuth()) {
            VideoAuth va = new VideoAuth();
            va.setRemark(remark);
            va.setAuditor(UserContext.getCurrent());
            va.setAuditTime(new Date());
            //只需要logininfo的id,没必要发sql查询
            Logininfo loginInfo = new Logininfo();
            loginInfo.setId(id);
            va.setApplier(loginInfo);
            va.setApplyTime(new Date());
            if (state == VideoAuth.STATE_PASS) {
                va.setState(VideoAuth.STATE_PASS);
                //更新userinfo的状态
                userinfo.setBitState(BitStatesUtils.addState(userinfo.getBitState(), BitStatesUtils.OP_VIDEO_AUTH));
                userinfoService.updateByPrimaryKey(userinfo);
            } else {
                va.setState(VideoAuth.STATE_REJECT);
            }
            videoAuthMapper.insert(va);
        }
    }
}
