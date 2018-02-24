package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import cn.wolfcode.p2p.bussiness.service.IBidRequestAuditHistoryService;
import cn.wolfcode.p2p.bussiness.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BorrowController {
    @Autowired
    private IBidRequestService             bidRequestService;
    @Autowired
    private IUserinfoService               userinfoService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IRealAuthService               realAuthService;
    @Autowired
    private IUserFileService               userFileService;

    @RequestMapping("/bidrequest_publishaudit_list")
    public String bidrequest_publishaudit(Model model, @ModelAttribute("qo") BidRequestQueryObject qo) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING); //只查询发标前待审核的数据
        model.addAttribute("pageResult", bidRequestService.query(qo));
        return "bidrequest/publish_audit";
    }

    @RequestMapping("/borrow_info")
    public String borrow_info(Model model, Long id) {
        BidRequest bidRequest = bidRequestService.get(id);
        if (bidRequest != null) {
            //bidRequest.disableDate
            model.addAttribute("bidRequest", bidRequest);
            //userInfo
            Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo",userinfo );
            //audits
            model.addAttribute("audits",bidRequestAuditHistoryService.queryByBidRequestId(id));
            //realAuth
            model.addAttribute("realAuth",realAuthService.get(userinfo.getRealAuthId()));
            //userFiles
            model.addAttribute("userFiles",userFileService.queryListByApplierId(userinfo.getId()));
        }
        return "bidrequest/borrow_info";
    }

    @RequestMapping("/bidrequest_publishaudit")
    @ResponseBody
    public AjaxResult public_audit(Long id, String remark, int state) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.audit(id, state, remark);
            ajaxResult = new AjaxResult("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }

    @RequestMapping("/bidrequest_audit1_list")
    public String audit_one_list(Model model, @ModelAttribute("qo") BidRequestQueryObject qo) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
        model.addAttribute("pageResult", bidRequestService.query(qo));
        return "bidrequest/audit1";
    }
    @RequestMapping("/bidrequest_audit1")
    @ResponseBody
    public AjaxResult audit_one(Long id, String remark, int state) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.audit_one(id, state, remark);
            ajaxResult = new AjaxResult("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }


    @RequestMapping("/bidrequest_audit2_list")
    public String audit_2_list(Model model, @ModelAttribute("qo") BidRequestQueryObject qo) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
        model.addAttribute("pageResult", bidRequestService.query(qo));
        return "bidrequest/audit2";
    }
    @RequestMapping("/bidrequest_audit2")
    @ResponseBody
    public AjaxResult audit_2(Long id, String remark, int state) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.audit_2(id, state, remark);
            ajaxResult = new AjaxResult("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }

}
