package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;

    @RequestMapping("/realAuth")
    public String realAuth(Model model, @ModelAttribute("qo") RealAuthQueryObject queryObject) {
        model.addAttribute("pageResult", realAuthService.query(queryObject));
        return "realAuth/list";
    }

    @RequestMapping("/realAuth_audit")
    @ResponseBody
    public AjaxResult realAuth_audit(Long id, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            realAuthService.audit(id, state, remark);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
