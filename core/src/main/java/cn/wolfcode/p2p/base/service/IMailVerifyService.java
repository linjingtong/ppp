package cn.wolfcode.p2p.base.service;


import cn.wolfcode.p2p.base.domain.MailVerify;

/**
 * Created by seemygo on 2018/1/21.
 */
public interface IMailVerifyService {

    int insert(MailVerify record);

    MailVerify selectByUUID(String uuid);

    /**
     * 发送邮件
     * @param email
     */
    void sendEmailVerify(String email);

    /**
     * 模拟局域网设置邮件内容
     * @param email
     * @param content
     */
    void sendEmailMessage(String email, String content) throws  Exception;
}
