<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台(系统管理平台)</title>
<#include "../common/header.ftl"/>
<link type="text/css" rel="stylesheet" href="/css/account.css" />
<link type="text/css" rel="stylesheet" href="/js/plugins/uploadifive/uploadifive.css" />
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/layDate/laydate/laydate.js"></script>
<script type="text/javascript" src="/js/plugins/uploadifive/jquery.uploadifive.min.js"></script>
	<#--点击图片放大-->
<link href="/js/fancyapps-fancyBox/source/jquery.fancybox.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/fancyapps-fancyBox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript">
	$(function() {
		//点击放大
        $("#image1_big,#image2_big").fancybox();
		/*$("#beginDate,#endDate").click(function(){
			WdatePicker();
		});*/
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

        $('#realAuth_dialog').dialog({
            title: '实名认证审核',
            width: 750,
            height: 500,
            closed: true,
            cache: false,
            modal: true,
            buttons:"#realAuth_dialog_button"
        });

        //审核
		$(".auditClass").click(function () {
            $('#realAuth_dialog').dialog("open")
			var jsonMap = $(this).data("json");
			$("#id").val(jsonMap.id);
			$("#state").val(jsonMap.state);
			$("#username").text(jsonMap.username);
			$("#realname").text(jsonMap.realName);
			$("#idNumber").text(jsonMap.idNumber);
			$("#sex").text(jsonMap.sex);
			$("#birthDate").text(jsonMap.bornDate);
			$("#address").text(jsonMap.address);
			$("#image1").prop("src",jsonMap.image1);
			$("#image1_big").prop("href",jsonMap.image1);
			$("#image2").prop("src",jsonMap.image2);
            $("#image2_big").prop("href",jsonMap.image2);
			$("#myModal").modal("show");
        })

		//审核操作
		$(".btn_audit").click(function(){
			$("#state").val($(this).val());
            $("#editform").ajaxSubmit({
                dataType:'json',
                success:function(data){
                    if(data.success){
                        window.location.reload();
                    }
                }
            })
		});
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-9">
				<div class="page-header">
					<h3>实名审核管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/realAuth">
					<input type="hidden" id="currentPage" name="currentPage" value=""/>
					<div class="form-group">
					    <label>状态</label>
					    <select class="form-control" name="state">
					    	<option value="-1">全部</option>
					    	<option value="0">申请中</option>
					    	<option value="1">审核通过</option>
					    	<option value="2">审核拒绝</option>
					    </select>
					</div>
                    <script type="text/javascript">
                        $(":input option[value=${qo.state}]").prop("selected",true);
                    </script>
					<div class="form-group">
					    <label>申请时间</label>
					    <input class="form-control" type="text" name="beginDate" id="beginDate" value="${(qo.beginDate?string('yyyy-MM-dd'))!''}" />到
					    <input class="form-control" type="text" name="endDate" id="endDate" value="${(qo.endDate?string('yyyy-MM-dd'))!''}" />
					</div>
					<div class="form-group">
						<button id="query" class="btn btn-success"><i class="icon-search"></i> 查询</button>
					</div>
				</form>
				<div class="panel panel-default" style="margin-top: 15px">
					<table class="table">
						<thead>
							<tr>
								<th>用户名</th>
								<th>真实姓名</th>
								<th>性别</th>
								<th>身份证号码</th>
								<th>身份证地址</th>
								<th>状态</th>
								<th>审核人</th>
							</tr>
						</thead>
						<tbody>
						<#list pageResult.list as info>
							<tr>
								<td>${info.applier.username}</td>
								<td>${info.realName}</td>
								<td>${info.sexDisplay}</td>
								<td>${info.idNumber}</td>
								<td>${info.address}</td>
								<td>${info.stateDisplay}</td>
								<td>${(info.auditor.username)!""}</td>
								<td>
									<#if (info.state == 0)>
									<a href="javascript:void(-1);" class="auditClass" data-json='${info.jsonMap}'>审核</a>
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
		<#--easyui-->
		<div id="realAuth_dialog">
            <form  id="editform" method="post" action="/realAuth_audit">
                    <input type="hidden" name="id" id="id" value="" />
                    <input type="hidden" name="state" id="state" value="" />
                    <div class="row">
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">用户名</label>
                        <div class="col-sm-3">
                            <label class="form-control" name="username" id="username"></label>
                        </div>
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">真实姓名</label>
                        <div class="col-sm-3">
                            <label class="form-control" name="realname" id="realname"></label>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">身份证号</label>
                        <div class="col-sm-3">
                            <label class="form-control" name="idNumber" id="idNumber"></label>
                        </div>
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">性别</label>
                        <div class="col-sm-3">
                            <label class="form-control" id="sex"></label>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">生日</label>
                        <div class="col-sm-3">
                            <label class="form-control" id="birthDate"></label>
                        </div>
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">身份证地址</label>
                        <div class="col-sm-3">
                            <label class="form-control" id="address"></label>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">身份证正面</label>
                        <div class="col-sm-3">
                            <a id="image1_big" class="pic" href="#" data-fancybox-group="gallery" title="身份证正面">
                                <img src="" id="image1" style="width: 150px;"/>
                            </a>
                        </div>
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">身份证背面</label>
                        <div class="col-sm-3">
                            <a id="image2_big" class="pic" href="#" data-fancybox-group="gallery" title="身份证背面">
                                <img src="" id="image2" style="width: 150px;"/>
                            </a>
                        </div>
                    </div>
                    <div style="margin-top: 15px">
                        <label class="col-sm-1 control-label" for="name" style="width: 90px">审核备注</label>
                        <div class="col-sm-6">
                            <textarea name="remark" rows="4" cols="60"></textarea>
                        </div>
                    </div>
            </form>
            <div style="margin-top: 155px;margin-left: 255px" >
                <button type="button" class="btn btn-success btn_audit" value="1">审核通过</button>
                <button type="button" class="btn btn-warning btn_audit" value="2">审核拒绝</button>
            </div>
		</div>


	<#--	<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-body">
		      	<form class="form-horizontal" id="editform" method="post" action="/realAuth_audit">
					<fieldset>
						<div id="legend" class="">
							<legend>实名认证审核</legend>
						</div>
						<input type="hidden" name="id" id="id" value="" />
						<input type="hidden" name="state" id="state" value="" />
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">用户名</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="username" id="username"></label>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">真实姓名</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="realname" id="realname"></label>
				        	</div>
						</div>
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">身份证号</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="idNumber" id="idNumber"></label>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">性别</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" id="sex"></label>
				        	</div>
						</div>
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">生日</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" id="birthDate"></label>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">身份证地址</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" id="address"></label>
				        	</div>
						</div>
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">身份证正面</label>
				        	<div class="col-sm-4">
                                <a id="image1_big" class="pic" href="#" data-fancybox-group="gallery" title="身份证正面">
				        		<img src="" id="image1" style="width: 150px;"/>
								</a>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">身份证背面</label>
				        	<div class="col-sm-4">
                                <a id="image2_big" class="pic" href="#" data-fancybox-group="gallery" title="身份证背面">
				        		<img src="" id="image2" style="width: 150px;"/>
                                </a>
				        	</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">审核备注</label>
				        	<div class="col-sm-6">
				        		<textarea name="remark" rows="4" cols="60"></textarea>
				        	</div>
						</div>
					</fieldset>
				</form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		        <button type="button" class="btn btn-success btn_audit" value="1">审核通过</button>
		        <button type="button" class="btn btn-warning btn_audit" value="2">审核拒绝</button>
		      </div>
		    </div>
		  </div>
		</div>-->
	</div>
</body>
</html>