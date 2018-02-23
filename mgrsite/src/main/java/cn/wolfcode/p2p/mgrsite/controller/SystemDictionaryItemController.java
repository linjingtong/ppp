package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemDictionaryItemController {
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    private ISystemDictionaryService     systemDictionaryService;

    @RequestMapping("/systemDictionaryItem_list")
    public String systemDictionaryItemList(Model model, @ModelAttribute("qo") SystemDictionaryItemQueryObject queryObject) {
        model.addAttribute("systemDictionaryGroups",systemDictionaryService.selectAll());
        model.addAttribute("pageResult", systemDictionaryItemService.query(queryObject));
        return "systemdic/systemDictionaryItem_list";
    }

    @RequestMapping("/systemDictionaryItem_update")
    @ResponseBody
    public AjaxResult saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        AjaxResult ajaxResult = null;
        try {
            systemDictionaryItemService.saveOrUpdate(systemDictionaryItem);
            ajaxResult = new AjaxResult("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
