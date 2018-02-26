package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.bussiness.service.IMoneyWithdrawService;
import cn.wolfcode.p2p.bussiness.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
public class MoneyWithdrawController {
    @Autowired
    private IMoneyWithdrawService moneyWithdrawService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserBankinfoService userBankinfoService;

    @RequestMapping("/moneyWithdraw")
    public String moneyWithdraw(Model model){
        Userinfo current = userinfoService.getCurrent();
        model.addAttribute("haveProcessing",current.getHasMoneyWithDraw_Process());
        model.addAttribute("bankInfo",userBankinfoService.getBankinfoByUserId(current.getId()));
        model.addAttribute("account",accountService.getCurrent());
        return "moneyWithdraw_apply";
    }

    /*8
	 * 提现申请
	 */
    @RequestMapping("moneyWithdraw_apply")
    @ResponseBody
    public AjaxResult apply(BigDecimal moneyAmount){

         AjaxResult ajaxResult = null;
             try{
                 this.moneyWithdrawService.moneyWithdraw_apply(moneyAmount);
                 ajaxResult = new AjaxResult("");
             }catch(Exception e){
                 e.printStackTrace();
                 ajaxResult = new AjaxResult(false,e.getMessage());
             }
             return ajaxResult;
    }
}
