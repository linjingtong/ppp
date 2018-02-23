package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailBindController {
    @Autowired
    private IMailVerifyService mailVerifyService;

    @RequestMapping("/sendEmail")
    @ResponseBody
    public AjaxResult sendEmail(String email) {
        AjaxResult ajaxResult = null;
        try {
            mailVerifyService.sendEmailVerify(email);
            ajaxResult = new AjaxResult("已发送验证邮件");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
