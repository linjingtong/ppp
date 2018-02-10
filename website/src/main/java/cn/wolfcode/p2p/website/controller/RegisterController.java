package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
    @Autowired
    ILogininfoService loginInfoService;

    /**
     * 用户注册
     */
    @RequestMapping("/userRegister")
    @ResponseBody
    public AjaxResult userRegister(String username, String password) {
        AjaxResult ajaxResult = null;
        try {
            loginInfoService.userRegister(username, password);
            ajaxResult = new AjaxResult("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
    /**
     * 表单输入时校验用户明是否重复
     */
    @RequestMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(String username) {
        return loginInfoService.checkUsername(username);
    }

    /**
     * 登录操作
     */
    @RequestMapping("/userLogin")
    @ResponseBody
    public AjaxResult userLogin(String username, String password) {
        AjaxResult ajaxResult = null;
        Logininfo loginInfo = loginInfoService.login(username, password, Logininfo.USERTYPE_USER);
        if (loginInfo != null) {
            ajaxResult = new AjaxResult("登陆成功");
        } else {
            ajaxResult = new AjaxResult(false, "账号或密码错误");
        }
        return ajaxResult;
    }

}
