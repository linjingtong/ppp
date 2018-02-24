package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.bussiness.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.bussiness.service.IPlatformBankinfoService;
import cn.wolfcode.p2p.bussiness.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RechargeOfflineController {
    @Autowired
    private IRechargeOfflineService  rechargeOfflineService;
    @Autowired
    private IPlatformBankinfoService platformBankinfoService;

    @RequestMapping("/rechargeOffline")
    public String rechargeOffline_audit(Model model, @ModelAttribute("qo") RechargeOfflineQueryObject qo) {
        model.addAttribute("banks", platformBankinfoService.selectAll());
        model.addAttribute("pageResult", rechargeOfflineService.query(qo));
        return "rechargeoffline/list";
    }

    @RequestMapping("/rechargeOffline_audit")
    @ResponseBody
    public AjaxResult audit(Long id, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            rechargeOfflineService.audit(id,state,remark);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
