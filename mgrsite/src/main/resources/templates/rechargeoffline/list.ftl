<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台(系统管理平台)</title>
<#include "../common/header.ftl"/>
<link type="text/css" rel="stylesheet" href="/css/account.css" />
<script type="text/javascript" src="/js/bank.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/layDate/laydate/laydate.js"></script>

<script type="text/javascript">
	$(function() {
        //时间控件
        laydate.render({
            elem: '#beginDate',
            max: 0
        });
        laydate.render({
            elem: '#endDate',
            max: 0
        });
		
		$('#pagination').twbsPagination({
			totalPages : ${pageResult.pages}||1,
			startPage : ${qo.currentPage},
			visiblePages : 5,
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页",
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
		
		$("#query").click(function(){
			$("#currentPage").val(1);
			$("#searchForm").submit();
		})


        $('#rechargeoffline_dialog').dialog({
            title: '实名认证审核',
            width: 750,
            height: 500,
            closed: true,
            cache: false,
            modal: true,
        });
		$(".auditClass").click(function(){
			$("#rechargeoffline_dialog").dialog("open");
			var data=$(this).data("json");
			$("[name=id]").val(data.id);
			$("[name=username]").html(data.username);
			$("[name=tradeCode]").html(data.tradeCode);
			$("[name=amount]").html(data.amount);
			$("[name=tradeTime]").html(data.tradeTime);
		});
		
		$(".btn_audit").click(function(){
			var form=$("#editform");
			form.find("[name=state]").val($(this).val());
			$("#myModal").modal("hide");
			form.ajaxSubmit(function(data){
				if(data.success){
					$.messager.confirm("提示","审核成功!",function(){
						window.location.reload();
					});
				}
			});
			return false;
		});
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-10">
				<div class="page-header">
					<h3>用户线下充值审核管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/rechargeOffline.do">
					<input type="hidden" id="currentPage" name="currentPage" value=""/>
					<div class="form-group">
					    <label>状态</label>
					    <select class="form-control" name="state">
					    	<option value="-1">全部</option>
					    	<option value="0">申请中</option>
					    	<option value="1">审核通过</option>
					    	<option value="2">审核拒绝</option>
					    </select>
					    <script type="text/javascript">
					    	$("[name=state] option[value='${(qo.state)!''}']").attr("selected","selected");
					    </script>
					</div>
					<div class="form-group">
					    <label>开户行</label>
					    <select class="form-control" name="bankInfoId">
					    	<option value="-1">全部</option>
					    	<#list banks as bank>
	    						<option value="${bank.id}">
									<script>
										var str="("+SITE_BANK_TYPE_NAME_MAP["${bank.bankName}"]+")${bank.bankNumber}${bank.bankForkName}";
										document.write(str);
									</script>
								</option>
	    					</#list>
					    </select>
					    <script type="text/javascript">
					    	$("[name=bankInfoId] option[value='${(qo.bankInfoId)!''}']").attr("selected","selected");
					    </script>
					</div>
					<div class="form-group" style="margin:10px ">
					    <label>交易时间</label>
					    <input class="form-control" type="text" name="beginDate" id="beginDate" value="${(qo.beginDate?string('yyyy-MM-dd'))!''}" />到
					    <input class="form-control" type="text" name="endDate" id="endDate" value="${(qo.endDate?string('yyyy-MM-dd'))!''}" />
					</div>
					<div class="form-group">
					    <label>交易号</label>
					    <input class="form-control" type="text" name="tradeCode" value="${(qo.tradeCode)!''}" />
					</div>
					<div class="form-group">
						<button id="query" class="btn btn-success"><i class="icon-search"></i> 查询</button>
					</div>
				</form>
				<div class="panel panel-default">
					<table class="table">
						<thead>
							<tr>
								<th>用户名</th>
								<th>交易号</th>
								<th>交易时间</th>
								<th>充值金额</th>
								<th>平台账号</th>
								<th>状态</th>
								<th>审核人</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						<#list pageResult.list as info>
							<tr>
								<td>${info.applier.username}</td>
								<td>${info.tradeCode}</td>
								<td>${info.tradeTime?string('yyyy-MM-dd')}</td>
								<td>${info.amount}</td>
								<td>
									<script>
										var str="("+SITE_BANK_TYPE_NAME_MAP["${info.bankInfo.bankName}"]+")${info.bankInfo.bankNumber}${info.bankInfo.bankForkName}";
										document.write(str);
									</script>
								</td>
								<td>${info.stateDisplay}</td>
								<td>${(info.auditor.username)!""}</td>
								<td>
									<#if (info.state == 0)>
									<a href="javascript:void(-1);" class="auditClass" data-json='${info.jsonString}'>审核</a>
									</#if>
								</td>
							</tr>
						</#list>
						</tbody>
					</table>
					<div style="text-align: center;">
						<ul id="pagination" class="pagination"></ul>
					</div>
				</div>
			</div>
		</div>
		
		<div  id="rechargeoffline_dialog" >
		      	<form class="form-horizontal" id="editform" method="post" action="/rechargeOffline_audit.do">
						<div id="legend" class="">
							<legend>线下充值审核</legend>
						</div>
						<input type="hidden" name="id" value="" />
						<input type="hidden" name="state" value="" /> 
						<div class="form-group">
				        	<label class="col-sm-2 control-label" for="name">用户名</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="username"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="name">交易号</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="tradeCode"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="name">交易金额</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="amount"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="name">交易时间</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="tradeTime"></label>
				        	</div>
			        	</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">审核备注</label>
				        	<div class="col-sm-6">
				        		<textarea name="remark" rows="4" cols="60"></textarea>
				        	</div>
						</div>
				</form>
            <div style="margin-left: 255px">
                <button type="button" class="btn btn-success btn_audit" value="1">审核通过</button>
                <button type="button" class="btn btn-warning btn_audit" value="2">审核拒绝</button>
            </div>
		</div>
		    </div>
		  </div>
		</div>
	</div>
</body>
</html>