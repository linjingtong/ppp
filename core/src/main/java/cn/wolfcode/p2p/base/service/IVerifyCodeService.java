package cn.wolfcode.p2p.base.service;

/**
 * Created by seemygo on 2018/1/21.
 */
public interface IVerifyCodeService {
    /**
     * 发送验证码(无发送验证码操作)
     *
     * @param phoneNumber
     */
    void sendVerifyCode(String phoneNumber);


    /**
     * 发送真实验证码
     *
     * @param phoneNumber
     */
    void sendRealMessage(String phoneNumber, String content) throws Exception;

    /**
     * 校验验证码
     *
     * @param phoneNumber
     * @param verifyCode
     * @return
     */
    boolean check(String phoneNumber, String verifyCode);
}
