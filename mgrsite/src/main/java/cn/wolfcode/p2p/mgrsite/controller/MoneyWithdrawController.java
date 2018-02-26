package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.bussiness.query.MoneyWithdrawQueryObject;
import cn.wolfcode.p2p.bussiness.service.IMoneyWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 提现申请审核
 * 
 * @author Administrator
 * 
 */
@Controller
public class MoneyWithdrawController {

	@Autowired
	private IMoneyWithdrawService moneyWithdrawService;

	@RequestMapping("moneyWithdraw")
	public String moneyWithdrawList(
			@ModelAttribute("qo") MoneyWithdrawQueryObject qo, Model model) {
		model.addAttribute("pageResult", this.moneyWithdrawService.query(qo));
		return "moneywithdraw/list";
	}

	@RequestMapping("moneyWithdraw_audit")
	@ResponseBody
	public AjaxResult audit(Long id, String remark, int state) {
		 AjaxResult ajaxResult = null;
			 try{
				 this.moneyWithdrawService.audit(id, remark, state);
				 ajaxResult = new AjaxResult("");
			 }catch(Exception e){
				 e.printStackTrace();
				 ajaxResult = new AjaxResult(false,e.getMessage());
			 }
			 return ajaxResult;
	}

}
