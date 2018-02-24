package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import cn.wolfcode.p2p.bussiness.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
public class BidController {
    @Autowired
    private IBidRequestService bidRequestService;


    //==========投资明细列表=============
    @RequestMapping("/invest")
    public String invest(){
        return "invest";
    }

    //ajax方式请求数据,返回html页面嵌套到tbody中,实现局部刷新
    @RequestMapping("/invest_list")
    //@ResponseBody注意不要反悔json
    public String invest_list(Model model, BidRequestQueryObject qo){
        qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,
                BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        model.addAttribute("pageResult",bidRequestService.query(qo));
        //查询用户投资的标
        return "invest_list";
    }

    @RequestMapping("/borrow_bid")
    @ResponseBody
    public AjaxResult bid(Long bidRequestId, BigDecimal amount) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.bid(bidRequestId,amount);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
