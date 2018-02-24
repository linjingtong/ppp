package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;

import java.util.List;

public interface VideoAuthMapper {

    int insert(VideoAuth record);

    VideoAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKey(VideoAuth record);

    List<VideoAuth> queryForList(VideoAuthQueryObject qo);
}