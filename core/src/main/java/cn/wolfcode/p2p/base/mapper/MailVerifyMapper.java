package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.MailVerify;

public interface MailVerifyMapper {

    int insert(MailVerify record);

    MailVerify selectByUUID(String uuid);

    int updateByPrimaryKey(MailVerify record);
}