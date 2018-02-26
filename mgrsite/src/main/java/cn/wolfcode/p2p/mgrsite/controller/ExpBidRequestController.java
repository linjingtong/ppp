package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import cn.wolfcode.p2p.bussiness.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.bussiness.service.IBidRequestService;
import cn.wolfcode.p2p.bussiness.service.IPaymentScheduleService;
import cn.wolfcode.p2p.bussiness.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExpBidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private ISystemAccountService systemAccountService;

    /**
     * 进入发布页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/expBidRequest_publicPage")
    public String expBidRequest_publicPage(Model model) {
        model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_EXP_BIDREQUEST);
        model.addAttribute("minBidAmount", BidConst.SMALLEST_EXP_BID);
        return "expbidrequest/expbidrequestpublish";
    }

    /**
     * 发布体验标
     */
    @RequestMapping("/expBidRequestPublish")
    @ResponseBody
    public AjaxResult expBidRequestPublish(BidRequest bidRequest) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.applyExpBidRequest(bidRequest);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }

    @RequestMapping("/expBidRequest_list")
    public String expBidRequest_list(Model model, @ModelAttribute("qo") BidRequestQueryObject qo){
        qo.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);
        model.addAttribute("pageResult",bidRequestService.query(qo));
        return "expbidrequest/expbidrequestlist";
    }

    /**
     * 体验标还款列表
     */
    @RequestMapping("/expBidRequestReturn_list")
    public String expBidRequestReturn_list(Model model, @ModelAttribute("qo") PaymentScheduleQueryObject qo) {
        qo.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);
        model.addAttribute("pageResult", paymentScheduleService.query(qo));
        model.addAttribute("account", systemAccountService.selectSystemAccount());
        return "expbidrequest/expbidrequestReturn_list";
    }


}
