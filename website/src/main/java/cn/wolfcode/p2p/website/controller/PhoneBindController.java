package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PhoneBindController {
    @Autowired
    private IVerifyCodeService sendVerifyCodeService;

    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    public AjaxResult sendVerifyCode(String phoneNumber) {
        AjaxResult ajaxResult = null;
        try {
            //发送验证码
            sendVerifyCodeService.sendVerifyCode(phoneNumber);
            ajaxResult = new AjaxResult("验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }

    //模拟美联短信接口,处理请求
    @RequestMapping("/sendSm")
    @ResponseBody
    public String sendSm(String username,String password) {

        return "success";
    }
}
