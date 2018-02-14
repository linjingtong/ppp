package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.Logininfo;
import org.apache.ibatis.annotations.Param;

public interface LogininfoMapper {

    int insert(Logininfo record);

    Logininfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Logininfo record);

    Logininfo checkUsername(String username);

    Logininfo loginCheck(@Param("username") String username, @Param("password") String password, @Param("userType") int userType);

    int selectByUserType(int usertype);
}