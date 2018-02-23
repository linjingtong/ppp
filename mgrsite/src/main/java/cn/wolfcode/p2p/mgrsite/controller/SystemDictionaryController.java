package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemDictionaryController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    @RequestMapping("/systemDictionary_list")
    public String systemDictionaryList(Model model, @ModelAttribute("qo") SystemDictionaryQueryObject queryObject) {
        model.addAttribute("pageResult", systemDictionaryService.query(queryObject));
        return "systemdic/systemDictionary_list";
    }

    @RequestMapping("/systemDictionary_update")
    @ResponseBody
    public AjaxResult saveOrUpdate(SystemDictionary systemDictionary) {
        AjaxResult ajaxResult = null;
        try {
            systemDictionaryService.saveOrUpdate(systemDictionary);
            ajaxResult = new AjaxResult("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
