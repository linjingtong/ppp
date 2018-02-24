package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    List<UserFile> selectAll();

    int updateByPrimaryKey(UserFile record);

    List<UserFile> queryForList(UserFileQueryObject qo);

    List<UserFile> getHasNullFileType();

    List<UserFile> queryListByApplierId(@Param("applierId") Long applierId, @Param("state") int state);
}