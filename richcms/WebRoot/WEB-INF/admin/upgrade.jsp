<%@include file="public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="keywords" content=" " />
		<meta name="description" content="" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>管理栏目</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/gray/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/js/kindeditor/themes/default/default.css" rel="stylesheet"	type="text/css" />
		<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/base.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerTree.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/ligerui/plugins/ligerLayout.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/uploadify/swfobject.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js" type="text/javascript"></script>
		<script src="${ctx}/static/admin/js/kindeditor/kindeditor.js" type="text/javascript"></script>
		<style>
			.catlist td {
				padding: 0;
				margin: 0;
				line-height: 100%;
			}
			.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
			.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
			table.gridtable {width:90%;font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;}
			table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}
			table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;text-align:center;}
			.input{width:500px;}
			.tree{width:90%; height:95%; margin:10px; float:left; border:1px solid #ccc; overflow:auto;}
			body{overflow-x: hidden;overflow-y: hidden; padding: 4px;}
		
			a:link{color:green;font-size:14px;text-decoration: underline;}
			a:visited{color:red;font-size:14px;text-decoration: underline;} 

		</style>
		<script type="text/javascript">
//------------------------------------------function var
 

var tree,keditor;
var data = [{"PID":null,"PK":0,"filePath":"${root.filePath}","children":[],"fileName":"${root.fileName}","fileType":"${root.fileType}","fileId":"${root.fileId}","level":0,"fileSize":"${root.fileSize}","modifyTime":"${root.modifyTime}"}];
var treeMenuData =data;
	function onBeforeExpand(note){              
            if(note.data.children && note.data.children.length == 0){
             	tree.loadData(note.target,"${ctx}/mmport/getChildren.do?devkey=${devkey}&csrfToken=${csrfToken}&pid="+note.data.fileId);
            }
	}

	function setFiledValue(){
		var node = tree.getSelected();
		if(node){
			var fileId='';
			var fileName='';
			var filePath='';

			nodeData=node.data;
			fileId=nodeData.fileId;
			fileName=nodeData.fileName;
			filePath=nodeData.filePath;
			$("#updateFilePath").val(filePath);
			$("#backFilePath").val(fileId);
			$("#delFilePath").val(fileId);
			$("#dwonloadFilePath").val(fileId);
			var url = $("#downloadBtn").attr("href");
			var index = url.indexOf("pid=");
			url = url.substr(0,index+4)+fileId;
			$("#downloadBtn").attr("href",url);
			if(fileName.indexOf(".js")>-1||fileName.indexOf(".css")||fileName.indexOf(".jsp")||fileName.indexOf(".html"))
			{
					$.ajax({
					type: "POST",
					url: "${ctx}/mmport/loadSource.do?csrfToken=${csrfToken}",
					data: "devkey=${devkey}&pid="+fileId,
					success: function(data){
						
						keditor.text("");
						keditor.text(data);
					}
				  });	
			}	
		}
	}
//------------------------------------------ligerUi
$(function() {
	$("#layout1").ligerLayout({ leftWidth: 300});
	
	tree = $("#categoryTree").ligerTree({
			 data:treeMenuData,
             idFieldName :'fileId',
             single:true,
             textFieldName:'fileName',
             nodeWidth:'100%',
             slide : false,
             checkbox :false,
             parentIDFieldName :'PID',
             isExpand:false,
             onBeforeExpand: onBeforeExpand,
             onSelect:setFiledValue
    });
    treeManager = $("#categoryTree").ligerGetTreeManager();
    treeManager.collapseAll();
    
    $('#file_upload').uploadify({
	    'swf'       : '${ctx}/static/admin/js/uploadify/uploadify.swf',
	    'uploader'  : '${ctx}/mmport/uploadify.do',
	    'cancel'    : '${ctx}/static/admin/images/uploadify-cancel.png',
	    'auto'      : true,
	    'uploadLimit':999,
	    'buttonText': '上传',
	    'removeTimeout':0,
	    'onUploadStart' : function(file) {   
                        $("#file_upload").uploadify("settings", "formData", {'csrfToken':'${csrfToken}','region':$("#updateFilePath").val()});   
         },
	    'onUploadSuccess':function(file,data,response)
	    {
        			if(data&&data=='0'){
						alert("成功");
					}else{
						alert("失败");
					}
	    }
	  });
    
    keditor = KindEditor.create('textarea[name="source"]', 
		{
			uploadJson      : '${ctx}/fileupload/kindeditor.do?region=pk&csrfToken=${csrfToken}',
			fileManagerJson : '${ctx}/fileupload/fileHandler.do?region=pk&csrfToken=${csrfToken}',
			allowFileManager : true,
			mode:'absolute',
			afterCreate : function() 
			{
				var self = this;
				KindEditor.ctrl(document, 13, function() {
					self.sync();
					document.forms['sourceForm'].submit();
				});
				KindEditor.ctrl(self.edit.doc, 13, function() {
					self.sync();
					document.forms['sourceForm'].submit();
				});
			}
	});
    
    $(".adm-btn-big").click(function()
    {
    	alert("暂不开放该功能！");
    	return false;
    	var text = keditor.text();
    	$.ajax({
				type: "POST",
				url: "${ctx}/mmport/modifySource.do?csrfToken=${csrfToken}",
				data: "devkey=${devkey}&pid="+$("#backFilePath").val()+"&source="+text,
				success: function(data){
					alert(data);
				}
			});			
    	
    	
    });
	 
});

function backup(){
			$.ajax({
				type: "POST",
				url: "${ctx}/mmport/upgrade.do?csrfToken=${csrfToken}",
				data: "devkey=${devkey}&pid="+$("#backFilePath").val(),
				dataType:"json",
				async:false,
				success: function(data){
					if(data&&data.status){
						if(data.status=="1"){
							alert("失败");
						}else{
							alert("成功");
						}
					}else{
						alert("失败");
					}
				}
			});				
}


	function del(){
			$.ajax({
				type: "POST",
				url: "${ctx}/mmport/delete.do?csrfToken=${csrfToken}",
				data: "devkey=${devkey}&pid="+$("#delFilePath").val(),
				dataType:"json",
				async:false,
				success: function(data){
					if(data&&data.status){
						if(data.status=="1"){
							alert("失败");
						}else{
							var node = tree.getSelected();
							$("#resultD").html(node.data.fileName+"删除成功");
				            if (node)
				                tree.remove(node.target);
				            else
				                alert('请先选择节点');
						}
					}else{
						alert("失败");
					}
				}
			});	
	}
	
</script>

	</head>
	<body>
	<div id="layout1">
            <div position="left" class="tree">
            	<ul id="categoryTree"></ul>
            </div>
            <div position="center">
            	<table class="gridtable">
            		<thead>
            			<tr><td colspan="2" style="font-size: 18px;font-family: sans-serif;color: black;">操作</td></tr>
            		</thead>
            		<tbody>
            			<tr>
            				<td>更新</td>
            				<td style="text-align:left;">
            					<input type="text" name="updateFilePath" id="updateFilePath" class="input"/><br/><br/>
            					<input name="file_upload" id="file_upload" type="file" width="50"/>
            					<span id="resultU"></span>
            				</td>
            			</tr>
            			<tr>
            				<td>备份</td>
            				<td style="text-align:left;">
            					<input type="text" name="backFilePath" id="backFilePath" class="input"/>
            					<input type="button" name="backbtn" id="backBtn" value="　备份　" onclick="backup();"/><span id="resultB"></span> 
            				</td>
            			</tr>
            			<tr>
            				<td>下载</td>
            				<td style="text-align:left;">
            					<input type="text" name="dwonloadFilePath" id="dwonloadFilePath" class="input"/>
            					<a id="downloadBtn" target="_blank" href="${ctx}/mmport/downloadFile.do?csrfToken=${csrfToken}&devkey=${devkey}&pid=">下载</a>
            				</td>
            			</tr>
            			<tr>
            				<td>删除</td>
            				<td style="text-align:left;">
            					<input type="text" name="delFilePath" id="delFilePath" class="input"/>
            					<input type="button" name="delbtn" id="delBtn" value="　删除　" onclick="del();"/><span id="resultD"></span>
            				</td>
            			</tr>
            			
            		</tbody>
            	</table>
            	
            	<textarea name="source" style="width:90%;height:370px;" >
            	</textarea>
            	<div class="adm-btn-big" style="text-align: left;padding-left: 450px;"><input type="submit" value="修改源码" /></div>
            </div>  
    </div> 
	</body>
</html>