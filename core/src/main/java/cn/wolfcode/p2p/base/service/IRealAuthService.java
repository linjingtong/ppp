package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/23.
 */
public interface IRealAuthService {
    int save(RealAuth record);

    RealAuth get(Long id);

    List<RealAuth> selectAll();

    int update(RealAuth record);

    /**
     * 后台实名认证审核页面
     * @param queryObject
     * @return
     */
    PageInfo query(RealAuthQueryObject queryObject);


    void audit(Long id, int state, String remark);
}
