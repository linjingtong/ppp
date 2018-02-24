package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class UserFileController {
    @Autowired
    private IUserFileService             userFileService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @RequestMapping("/userFile")
    public String userFile(Model model, @ModelAttribute("qo") UserFileQueryObject qo) {
        //查询是否存在存在fileType为null的数据
        List<UserFile> list = userFileService.getHasNullFileType();
        if (list.size() > 0) {
            //有为空的则跳到userFiles_commit页面
            model.addAttribute("userFiles", list);
            model.addAttribute("fileTypes", systemDictionaryItemService.selectAll());
            return "userFiles_commit";
        }
        model.addAttribute("userFiles", userFileService.selectAll());
        return "userFiles";
    }

    @Value("${file.path}")
    private String filePath;

    @RequestMapping("/userFileUpload")
    @ResponseBody
    public String userFileUpload(MultipartFile file) {
        String fileName = UploadUtil.upload(file, filePath);
        //将选择的材料保存到数据库中
        userFileService.saveFirst(fileName);
        return fileName;
    }

    @RequestMapping("/userFile_selectType")
    public String userFile_selectType(Long[] fileType, Long[] id) {
        userFileService.addFileType(fileType, id);
        return "redirect:/userFile";
    }
}
