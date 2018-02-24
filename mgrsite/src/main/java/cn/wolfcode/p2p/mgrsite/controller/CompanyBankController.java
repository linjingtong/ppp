package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.query.PlatformBankinfoQueryObject;
import cn.wolfcode.p2p.bussiness.service.IPlatformBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CompanyBankController {
    @Autowired
    private IPlatformBankinfoService platformBankinfoService;

    @RequestMapping("/companyBank_list")
    public String companyBank_list(Model model, @ModelAttribute("qo") PlatformBankinfoQueryObject qo){
        model.addAttribute("pageResult",platformBankinfoService.query(qo));
        return "platformbankinfo/list";
    }

    @RequestMapping("/companyBank_update")
    public String saveOrUpdate(PlatformBankinfo platformBankinfo){
        platformBankinfoService.saveOrUpdate(platformBankinfo);
        return "redirect:/companyBank_list";
    }
}
