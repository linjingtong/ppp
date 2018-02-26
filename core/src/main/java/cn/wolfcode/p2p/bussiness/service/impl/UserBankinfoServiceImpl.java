package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.UserBankinfo;
import cn.wolfcode.p2p.bussiness.mapper.UserBankinfoMapper;
import cn.wolfcode.p2p.bussiness.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserBankinfoServiceImpl implements IUserBankinfoService {
    @Autowired
    private UserBankinfoMapper userBankinfoMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userBankinfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserBankinfo record) {
        return userBankinfoMapper.insert(record);
    }

    @Override
    public UserBankinfo selectByPrimaryKey(Long id) {
        return userBankinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserBankinfo> selectAll() {
        return userBankinfoMapper.selectAll();
    }

    @Override
    public UserBankinfo getBankinfoByUserId(Long userId) {
        return this.userBankinfoMapper.getBankinfoByUserId(userId);
    }

    @Override
    public void bind(UserBankinfo bankInfo) {
        // 判断当前用户没有绑定;
        Userinfo current = this.userinfoService.getCurrent();
        if (!current.getHasBindBankinfo() && current.getIsRealAuth()) {
            // 创建一个UserBankinfo,设置相关属性;
            UserBankinfo b = new UserBankinfo();
            b.setAccountName(current.getRealName());
            b.setBankNumber(bankInfo.getBankNumber());
            b.setBankForName(bankInfo.getBankForName());
            b.setBankName(bankInfo.getBankName());
            b.setUserinfoId(UserContext.getCurrent().getId());
            this.userBankinfoMapper.insert(b);
            // 修改用户状态码
            current.setBitState(BitStatesUtils.addState(current.getBitState(),BitStatesUtils.OP_BIND_BANKINFO));
            this.userinfoService.updateByPrimaryKey(current);
        }
    }

}
