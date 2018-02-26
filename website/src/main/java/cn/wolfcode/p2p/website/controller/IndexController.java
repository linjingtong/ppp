package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import cn.wolfcode.p2p.bussiness.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @Autowired
    private IBidRequestService bidRequestService;

    @RequestMapping("/index")
    public  String index(Model model, BidRequestQueryObject qo){
        qo.setCurrentPage(1);
        qo.setPageSize(5);
        //查询三种状态下的标--招标,还款,已还清
        qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,
                BidConst.BIDREQUEST_STATE_PAYING_BACK, BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        qo.setOrder("ORDER BY br.bidRequestState ASC");
        //bidRequests
        //查询最近的5个信用标
        qo.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);
        model.addAttribute("bidRequests",bidRequestService.queryListIndex(qo));
        //查询最近的5个体验标
        qo.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);
        model.addAttribute("expBidRequests",bidRequestService.queryListIndex(qo));
        return "main";
    }


}
