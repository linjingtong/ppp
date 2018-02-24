package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserinfoService userinfoService;

    @RequestMapping("/realAuth")
    public String realAuth(Model model) {
        if(userinfoService.getCurrent().getIsRealAuth()){
            model.addAttribute("auditing",false);
            model.addAttribute("realAuth",realAuthService.get(userinfoService.getCurrent().getRealAuthId()));
            return "realAuth_result";
        }else{
            //如果userinfo中realAuth为null,跳到申请验证页面
            if (userinfoService.getCurrent().getRealAuthId()==null){
                return "realAuth";
            }
            //如果realAuth为0,跳转到待审核页面
            model.addAttribute("auditing",true);
            return "realAuth_result";
        }
    }

    @RequestMapping("/realAuth_save")
    @ResponseBody
    public AjaxResult realAuth_save(RealAuth realAuth) {
        AjaxResult ajaxResult = null;
        try {
            realAuthService.save(realAuth);
            ajaxResult = new AjaxResult("提交成功,等待审核");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }

    @Value("${file.path}")
    private String filePath;

    @RequestMapping("/uploadImg")
    @ResponseBody
    public String upload(MultipartFile pic){
        return UploadUtil.upload(pic,filePath);
    }
}
