package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2018/1/26.
 */
public interface IVideoAuthService {

    int save(VideoAuth record);

    VideoAuth get(Long id);

    int update(VideoAuth record);

    PageInfo query(VideoAuthQueryObject qo);

    void audit(Long id, int state, String remark);
}
