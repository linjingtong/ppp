package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.service.IBidRequestService;
import cn.wolfcode.p2p.bussiness.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BorrowController {
    @Autowired
    private IUserinfoService   userinfoService;
    @Autowired
    private IAccountService    accountService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserFileService   userFileService;
    @Autowired
    private IRealAuthService   realAuthService;
    @Autowired
    private IExpAccountService expAccountService;

    @RequestMapping("/borrow")
    public String borrow(Model model) {
        if (UserContext.getCurrent() == null) {
            return "redirect:borrowStatic.html";
        }
        model.addAttribute("userinfo", userinfoService.getCurrent());
        model.addAttribute("account", accountService.getCurrent());
        model.addAttribute("creditBorrowScore", BidConst.BASE_BORROW_SCORE);
        return "borrow";
    }

    @RequestMapping("/borrowInfo")
    public String borrowInfo(Model model) {
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        if (userinfo != null) {
            //当前用户是否满足借款条件
            if (userinfoService.canBorrow(userinfo)) {
                if (!userinfo.getHasBidrequestProcess()) {
                    model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
                    model.addAttribute("account", account);
                    model.addAttribute("minBidAmount", BidConst.SMALLEST_BID_AMOUNT);
                    return "borrow_apply";
                } else {
                    return "borrow_apply_result";
                }
            }
        }
        return "redirect:/borrow";
    }

    @RequestMapping("/borrow_apply")
    public String borrow_apply(BidRequest bidRequest) {
        bidRequestService.borrow_apply(bidRequest);
        return "redirect:/borrowInfo";
    }

    @RequestMapping("/borrow_info")
    public String borrow_info(Model model, Long id) {
        BidRequest bidRequest = bidRequestService.get(id);
        Logininfo current = UserContext.getCurrent();
        String returnPage = "";
        if (bidRequest != null) {
            //bidRequest.disableDate
            model.addAttribute("bidRequest", bidRequest);
            if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL) {
                //userInfo
                Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                model.addAttribute("userInfo", userinfo);
                //realAuth
                model.addAttribute("realAuth", realAuthService.get(userinfo.getRealAuthId()));
                //userFiles
                model.addAttribute("userFiles", userFileService.queryListByApplierId(userinfo.getId()));
                //是否已经登录
                if (current != null) {
                    //是否借款人本身
                    //account,self
                    if (current.getId().equals(bidRequest.getCreateUser().getId())) {
                        model.addAttribute("self", true);
                    } else {
                        model.addAttribute("self", false);
                        model.addAttribute("account", accountService.getCurrent());
                    }
                } else {
                    model.addAttribute("self", false);
                }
                returnPage = "borrow_info";
            } else {
                if (current != null) {
                    model.addAttribute("expAccount", expAccountService.selectByPrimaryKey(current.getId()));
                }
                returnPage = "exp_borrow_info";
            }


        }
        return returnPage;
    }
}
