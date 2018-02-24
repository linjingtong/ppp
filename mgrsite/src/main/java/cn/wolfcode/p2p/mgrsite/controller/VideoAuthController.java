package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class VideoAuthController {
    @Autowired
    private IVideoAuthService videoAuthService;
    @Autowired
    private ILogininfoService loginInfoService;

    @RequestMapping("/vedioAuth")
    public String vedioAuth(Model model, @ModelAttribute("qo") VideoAuthQueryObject qo) {
        model.addAttribute("pageResult", videoAuthService.query(qo));
        return "videoAuth/list";
    }

    @RequestMapping("/videoAuth_autoSearch")
    @ResponseBody
    public List<Map<String, Object>> videoAuth_autoSearch(String keyword) {
        return loginInfoService.videoAuth_autoSearch(keyword);
    }

    @RequestMapping("/vedioAuth_audit")
    @ResponseBody
    public AjaxResult audit(Long loginInfoValue, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            videoAuthService.audit(loginInfoValue,state,remark);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
