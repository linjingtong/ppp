package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.bussiness.domain.UserBankinfo;
import cn.wolfcode.p2p.bussiness.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BindBankinfoController {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IUserBankinfoService userBankinfoService;

    @RequestMapping("/bankInfo")
    public String bankInfo(Model model){
        Userinfo current = userinfoService.getCurrent();
        if(current.getHasBindBankinfo()){
            model.addAttribute("bankInfo", userBankinfoService.getBankinfoByUserId(current.getId()));
            return "bankInfo_result";
        }
        model.addAttribute("userinfo",current);
        return "bankInfo";
    }

    /**
     * 执行绑定
     */
    @RequestMapping("bankInfo_save")
    public String bankInfoSave(UserBankinfo bankInfo) {
        this.userBankinfoService.bind(bankInfo);
        return "redirect:/bankInfo.do";
    }


}
