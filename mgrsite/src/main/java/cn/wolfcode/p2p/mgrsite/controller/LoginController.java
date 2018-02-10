package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private ILogininfoService loginInfoService;

    @RequestMapping("/managerLogin")
    @ResponseBody
    public AjaxResult managerLogin(String username, String password) {
        AjaxResult ajaxResult = null;
        Logininfo loginInfo = loginInfoService.login(username, password, Logininfo.USERTYPE_MANAGER);
        if (loginInfo != null) {
            ajaxResult = new AjaxResult("登录成功");
        } else {
            ajaxResult = new AjaxResult(false, "登录失败");
        }
        return ajaxResult;
    }
}
