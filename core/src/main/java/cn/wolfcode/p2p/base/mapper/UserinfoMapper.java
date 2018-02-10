package cn.wolfcode.p2p.base.mapper;


import cn.wolfcode.p2p.base.domain.Userinfo;

public interface UserinfoMapper {

    int insert(Userinfo record);

    Userinfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Userinfo record);

    /**
     * 校验邮箱之前是否被绑定
     * @param email
     * @return
     */
    int queryEmailOnly(String email);
}