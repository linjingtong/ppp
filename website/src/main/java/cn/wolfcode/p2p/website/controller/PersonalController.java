package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.impl.IpLogServiceImpl;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonalController {
    @Autowired
    private IAccountService  accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IpLogServiceImpl ipLogService;

    @RequestMapping("/personal")
    @RequireLogin("登录")
    public String personalPage(Model model) {
        model.addAttribute("account", accountService.getCurrent());
        model.addAttribute("userinfo", userinfoService.getCurrent());
        //最后登录时间
        model.addAttribute("endLoginTime",ipLogService.getEndLogin());
        return "personal";
    }

    @RequestMapping("/bindPhone")
    @ResponseBody
    public AjaxResult bindPhone(String phoneNumber, String verifyCode) {

        AjaxResult ajaxResult = null;
        try {
            userinfoService.bindPhone(phoneNumber,verifyCode);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
   /* @RequestMapping("/bindEmail")
    public String bindEmail(String uuid,Model model) {
        try {
            userinfoService.bindEmail(uuid);
            model.addAttribute("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
        }
        return "checkmail_result";

    }*/
}
