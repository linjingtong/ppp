package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.MailVerify;
import cn.wolfcode.p2p.base.mapper.MailVerifyMapper;
import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class MailVerifyServiceImpl implements IMailVerifyService {
    @Autowired
    private MailVerifyMapper mailVerifyMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Override
    public int insert(MailVerify record) {
        return mailVerifyMapper.insert(record);
    }

    @Override
    public MailVerify selectByUUID(String uuid) {
        return mailVerifyMapper.selectByUUID(uuid);
    }

    @Value("${email.verifyURL}")
    private String verifyURL;

    @Override
    public void sendEmailVerify(String email) {
        //校验该邮箱是否已经被绑定了
        int count = userinfoService.queryEmailOnly(email);
        if(count>0){
            throw new RuntimeException("该邮箱已经被其他用户绑定");
        }
        //创建uuid
        String uuid = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder(100);
        sb.append("这是一封验证邮件,请点击<a href='").append(verifyURL).append("/bindEmail?uuid=").append(uuid).
                append("'>这里</a>完成验证,有效期为").append(BidConst.VERIFYEMAIL_VAILDATE_DAY).append("天,请尽快完成验证");
        try {
            MailVerify mailVerify = new MailVerify();
            mailVerify.setEmail(email);
            mailVerify.setSendTime(new Date());
            mailVerify.setUserId(UserContext.getCurrent().getId());
            mailVerify.setUuid(uuid);
            mailVerifyMapper.insert(mailVerify);
            System.out.println(sb.toString());
            //调用发送邮件
            this.sendEmailMessage(email,sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String         fromMail;

    public void sendEmailMessage(String email,String content) throws Exception {
        //建立邮件消息
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        //设置收件人,寄件人
        helper.setTo(email);
        helper.setFrom(fromMail);
        helper.setSubject("这是一封验证邮件");
        helper.setText(content);
        sender.send(message);
    }
}
