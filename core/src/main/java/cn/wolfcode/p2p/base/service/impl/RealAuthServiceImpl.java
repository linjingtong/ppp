package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.RealAuthMapper;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
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
public class RealAuthServiceImpl implements IRealAuthService {
    @Autowired
    private RealAuthMapper   realAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(RealAuth record) {


        RealAuth ra = new RealAuth();
        ra.setRealName(record.getRealName());
        ra.setSex(record.getSex());
        ra.setIdNumber(record.getIdNumber());
        ra.setBornDate(record.getBornDate());
        ra.setAddress(record.getAddress());
        ra.setState(RealAuth.STATE_NORMAL);
        ra.setApplyTime(new Date());
        ra.setApplier(UserContext.getCurrent());
        ra.setImage1(record.getImage1());
        ra.setImage2(record.getImage2());
        realAuthMapper.insert(ra);
        Userinfo current = userinfoService.getCurrent();
        current.setRealAuthId(ra.getId());
        userinfoService.updateByPrimaryKey(current);
        return 0;
    }

    @Override
    public RealAuth get(Long id) {
        return realAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<RealAuth> selectAll() {
        return realAuthMapper.selectAll();
    }

    @Override
    public int update(RealAuth record) {
        return realAuthMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo query(RealAuthQueryObject queryObject) {
        PageHelper.startPage(queryObject.getCurrentPage(), queryObject.getPageSize());
        List<RealAuth> list = realAuthMapper.queryForList(queryObject);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        RealAuth realAuth = realAuthMapper.selectByPrimaryKey(id);
        if (realAuth != null && realAuth.getState() == RealAuth.STATE_NORMAL) {
            RealAuth ra = realAuthMapper.selectByPrimaryKey(realAuth.getId());
            Userinfo userinfo = userinfoService.selectByPrimaryKey(ra.getApplier().getId());
            //审核通过
            if(state== RealAuth.STATE_PASS){
                ra.setState(RealAuth.STATE_PASS);
                //更新userinfo
                userinfo.setBitState(BitStatesUtils.addState(userinfo.getBitState(), BitStatesUtils.OP_REAL_AUTH));
                userinfo.setRealName(ra.getRealName());
                userinfo.setIdNumber(ra.getIdNumber());
            }else{
                ra.setState(RealAuth.STATE_REJECT);
                //更新userinfo
                userinfo.setRealAuthId(null);
            }
            ra.setRemark(remark);
            ra.setAuditor(UserContext.getCurrent());
            ra.setAuditTime(new Date());
            realAuthMapper.updateByPrimaryKey(ra);
            userinfoService.updateByPrimaryKey(userinfo);
        } else {
            throw new RuntimeException("非法参数");
        }
    }
}
