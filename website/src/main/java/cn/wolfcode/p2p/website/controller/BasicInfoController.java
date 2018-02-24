package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicInfoController {
    @Autowired
    private IUserinfoService             userinfoService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @RequestMapping("/basicInfo")
    public String basicInfo(Model model) {
        //userinfo
        model.addAttribute("userinfo", userinfoService.getCurrent());
        //educationBackgrounds
        model.addAttribute("educationBackgrounds", systemDictionaryItemService.queryListBySn("educationBackground"));
        //incomeGrades
        model.addAttribute("incomeGrades", systemDictionaryItemService.queryListBySn("incomeGrade"));
        //marriages
        model.addAttribute("marriages", systemDictionaryItemService.queryListBySn("marriage"));
        //kidCounts
        model.addAttribute("kidCounts", systemDictionaryItemService.queryListBySn("kidCount"));
        //houseConditions
        model.addAttribute("houseConditions", systemDictionaryItemService.queryListBySn("houseCondition"));
        return "userInfo";
    }

    @RequestMapping("/basicInfo_save")
    @ResponseBody
    public AjaxResult basicInfo_save(Userinfo userinfo) {
        AjaxResult ajaxResult = null;
        try {
            userinfoService.saveBasicInfo(userinfo);
            ajaxResult = new AjaxResult("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
