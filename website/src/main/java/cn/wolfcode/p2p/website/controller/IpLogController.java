package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IpLogController {
    @Autowired
    private IIpLogService ipLogService;

    @RequestMapping("/ipLog")
    public String ipLog(Model model, @ModelAttribute("qo") IpLogQueryObject qo) {
        qo.setUsername(UserContext.getCurrent().getUsername());
        model.addAttribute("pageResult",ipLogService.query(qo));
        return "ipLog_list";
    }
}
