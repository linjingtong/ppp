<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>蓝源Eloan-P2P平台(系统管理平台)</title>
<#include "common/header.ftl"/>
    <script type="text/javascript">
        $(function () {
            $('#leftMenu').tree({
                method:'get',
                url:'leftMenu.json',
                onClick: function(node){
                    var tab = $("#mainTab");
                    if(tab.tabs("exists",node.text)){
                        tab.tabs('select',node.text)
                    }else{
                        tab.tabs("add",{
                            title:node.text,
                            closable:true,
                            content:'<iframe src="'+node.attributes.url+'" style="width:100%;height:100%" frameborder="0"></iframe>'
                            //href:node.attributes.url//只能加载远程页面中的body部分的内容
                        });

                    }
                }
            });
            $("#mainTab").tabs({
                fit:true,
                pill:true,
                border:false,
                plain:false

            });
        })
    </script>
</head>
<body>
    <div id="cc" class="easyui-layout" data-options="fit:true">
       <#-- <div data-options="region:'north'" style="height:100px;"></div>-->
        <div data-options="region:'west'" style="width:190px;">
            <ul id="leftMenu"></ul>
        </div>
        <div data-options="region:'center'" style="padding:5px;background:#eee;">
            <div id="mainTab" ></div>
            </div>
        </div>
    </div>

</body>
</html>