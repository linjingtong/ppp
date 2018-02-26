package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.bussiness.service.IPaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReturnMoneyController {
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IAccountService         accountService;

    @RequestMapping("/borrowBidReturn_list")
    public String borrowBidReturn_list(Model model, @ModelAttribute("qo") PaymentScheduleQueryObject qo) {
        qo.setBorrowUserId(UserContext.getCurrent().getId());
        model.addAttribute("pageResult", paymentScheduleService.query(qo));
        model.addAttribute("account", accountService.selectByPrimaryKey(UserContext.getCurrent().getId()));
        return "returnmoney_list";
    }

    @RequestMapping("/returnMoney")
    @ResponseBody
    public AjaxResult returnMoney(Long id) {
        AjaxResult ajaxResult = null;
        try {
            paymentScheduleService.borrowBidReturn(id);
            ajaxResult = new AjaxResult("");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(false, e.getMessage());
        }
        return ajaxResult;
    }
}
