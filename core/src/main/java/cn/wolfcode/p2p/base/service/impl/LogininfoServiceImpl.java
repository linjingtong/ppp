package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.LogininfoMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.MD5;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LogininfoServiceImpl implements ILogininfoService {
    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private IIpLogService   ipLogService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(Logininfo record) {
        return logininfoMapper.insert(record);
    }

    @Override
    public Logininfo get(Long id) {
        return logininfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Logininfo record) {
        return logininfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public void userRegister(String username, String password) {
        if(logininfoMapper.checkUsername(username)!=null){
            throw new RuntimeException("对不起,该账户已存在");
        }
        Logininfo logininfo = new Logininfo();
        logininfo.setPassword(MD5.encode(password));
        logininfo.setUsername(username);
        logininfo.setState(Logininfo.STATE_NOMAL);
        logininfo.setUserType(Logininfo.USERTYPE_USER);
        logininfoMapper.insert(logininfo);
        //保存用户实体
        Account account = new Account();
        account.setId(logininfo.getId());
        accountService.insert(account);
        //保存用户信息
        Userinfo userinfo = new Userinfo();
        userinfo.setId(logininfo.getId());
        userinfoService.insert(userinfo);

    }

    @Override
    public boolean checkUsername(String username) {
        return logininfoMapper.checkUsername(username) == null;
    }

    @Override
    public Logininfo login(String username, String password, int userType) {
        Logininfo info = logininfoMapper.loginCheck(username, MD5.encode(password), userType);
        //保存登录信息
        IpLog ipLog = new IpLog();
        ipLog.setUsername(username);
        ipLog.setLoginTime(new Date());
        ipLog.setIp(UserContext.getIP());
        if (info == null) {
            ipLog.setState(IpLog.LOGIN_FAILED);
        } else {
            if (info.getUserType() == Logininfo.USERTYPE_MANAGER) {
                ipLog.setUserType(Logininfo.USERTYPE_MANAGER);
            }
            ipLog.setState(IpLog.LOGIN_SUCCESS);
        }
        ipLogService.insert(ipLog);
        //存在用户,保存到session中
        UserContext.setCurrent(info);
        return info;
    }

    @Override
    public void initAdmin() {
        int count = logininfoMapper.selectByUserType(Logininfo.USERTYPE_MANAGER);
        if (count <= 0) {
            Logininfo logininfo = new Logininfo();
            logininfo.setUserType(Logininfo.USERTYPE_MANAGER);
            logininfo.setUsername(BidConst.DEFAULT_ADMIN_NAME);
            logininfo.setPassword(MD5.encode(BidConst.DEFAULT_ADMIN_PASSWORD));
            logininfo.setState(Logininfo.STATE_NOMAL);
            logininfoMapper.insert(logininfo);
        }
    }

    @Override
    public List<Map<String, Object>> videoAuth_autoSearch(String keyword) {
        return logininfoMapper.videoAuth_autoSearch(keyword,Logininfo.USERTYPE_USER);
    }
}
