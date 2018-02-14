package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserinfoMapper;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserinfoServiceImpl implements IUserinfoService {
    @Autowired
    private UserinfoMapper     userinfoMapper;
    @Autowired
    private IVerifyCodeService verifyCodeService;
    /* @Autowired
    private IMailVerifyService mailVerifyService;*/

    @Override
    public int insert(Userinfo record) {
        return userinfoMapper.insert(record);
    }

    @Override
    public Userinfo selectByPrimaryKey(Long id) {
        return userinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Userinfo record) {
        int count = userinfoMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁异常");
        }
        return count;
    }

    @Override
    public Userinfo getCurrent() {
        return this.selectByPrimaryKey(UserContext.getCurrent().getId());
    }

    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        //验证码,手机号是否是之前发送的,是否已经超时
        boolean result = verifyCodeService.check(phoneNumber, verifyCode);
        if (!result) {
            throw new RuntimeException("验证码错误或超过有效期,请重新发送");
        }
        //验证用户是否绑定手机
        Userinfo userinfo = this.getCurrent();
        if (userinfo.getHasBindPhone()) {
            throw new RuntimeException("您已绑定手机号,请不要重复绑定");
        }
        //满足条件,更改用户状态
        userinfo.setBitState(BitStatesUtils.addState(userinfo.getBitState(), BitStatesUtils.OP_BIND_PHONE));
        userinfo.setPhoneNumber(phoneNumber);
        this.updateByPrimaryKey(userinfo);
    }

    /*
    @Override
    public void bindEmail(String uuid) {
        //根据uuid去数据库查询
        MailVerify mailVerify = mailVerifyService.selectByUUID(uuid);
        if (mailVerify == null) {
            throw new RuntimeException("邮箱地址有误,请重新发送");
        }
        if (DateUtil.getBetweenTime(new Date(), mailVerify.getSendTime()) > BidConst.VERIFYEMAIL_VAILDATE_DAY * 24 * 60 * 60) {
            throw new RuntimeException("验证邮件已过有效期,请重新发送");
        }
        //如果当前用户已经绑定邮箱
        //=====注意:不能通过getCurrent()获取,可能用户未登录或在不同设备登录,session无用户信息======
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(mailVerify.getUserId());
        if (userinfo.getHasBindEmail()) {
            throw new RuntimeException("您已经绑定过邮箱,请不要重复绑定");
        }
        //给用户添加邮箱的状态码
        userinfo.setBitState(BitStatesUtils.addState(userinfo.getBitState(), BitStatesUtils.OP_BIND_EMAIL));
        userinfo.setEmail(mailVerify.getEmail());
        userinfoMapper.updateByPrimaryKey(userinfo);
    }*/

    @Override
    public int queryEmailOnly(String email) {
        return userinfoMapper.queryEmailOnly(email);
    }

    @Override
    public void saveBasicInfo(Userinfo userinfo) {
        Userinfo current = this.getCurrent();
        current.setEducationBackground(userinfo.getEducationBackground());
        current.setHouseCondition(userinfo.getHouseCondition());
        current.setIncomeGrade(userinfo.getIncomeGrade());
        current.setKidCount(userinfo.getKidCount());
        current.setMarriage(userinfo.getMarriage());
        current.setBitState(BitStatesUtils.addState(current.getBitState(), BitStatesUtils.OP_BASIC_INFO));
        userinfoMapper.updateByPrimaryKey(current);
    }

    @Override
    public boolean canBorrow(Userinfo userinfo) {
        if (userinfo.getIsRealAuth() && //实名认证
                userinfo.getIsVedioAuth() && //视频认证
                userinfo.getIsBasicInfo() && //基本资料
                userinfo.getScore() >= BidConst.BASE_BORROW_SCORE) {
            return true;
        }
        return false;
    }
}
