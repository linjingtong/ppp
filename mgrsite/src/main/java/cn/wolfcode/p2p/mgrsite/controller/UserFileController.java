package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserFileController {
    @Autowired
    private IUserFileService userFileService;

    @RequestMapping("/userFileAuth")
    public String userFileAuth(Model model, @ModelAttribute("qo") UserFileQueryObject qo) {
        model.addAttribute("pageResult", userFileService.query(qo));
        return "userFileAuth/list";
    }

    @RequestMapping("/userFile_audit")
    @ResponseBody
    public AjaxResult audit(Long id, int state, int score, String remark) {
        AjaxResult ajaxResult = null;
        try {
            userFileService.audit(id,state,score,remark);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
