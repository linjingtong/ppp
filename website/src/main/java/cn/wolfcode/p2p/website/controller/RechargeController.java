package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import cn.wolfcode.p2p.bussiness.service.IPlatformBankinfoService;
import cn.wolfcode.p2p.bussiness.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RechargeController {
    @Autowired
    private IPlatformBankinfoService platformBankinfoService;
    @Autowired
    private IRechargeOfflineService  rechargeOfflineService;

    //线下充值
    @RequestMapping("/recharge")
    public String recharge(Model model) {
        model.addAttribute("banks", platformBankinfoService.selectAll());
        return "recharge";
    }

    @RequestMapping("/recharge_save")
    @ResponseBody
    public AjaxResult save(RechargeOffline rechargeOffline) {
        AjaxResult ajaxResult = null;
        try {
            rechargeOfflineService.save(rechargeOffline);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }

}
