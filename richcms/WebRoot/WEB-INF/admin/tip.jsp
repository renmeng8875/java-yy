<%@include file="public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
       <title></title>
<style type="text/css">
table.gridtable {font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;}
table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}
table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}
</style>
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
		var pageIndex = 1,pageSize = 10;
		$(function () {
            $("#txtBtn").click(function(){
                pageIndex = 1;
                AjaxGetData(pageIndex,pageSize);
            });
        });
		
		function AjaxGetData(pageIndex, pageSize){
			$.ajax({
				type: "POST",
				url: "${ctx}/mmport/devkey.do?csrfToken=${csrfToken}",
				data: "devkey=${devkey}&sql="+$("#content").val()+"&pageIndex=" + pageIndex + "&pageSize=" + pageSize,
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
					if(data && data.list)
					{
						var size=data.size;
						var list=data.list;
						var htmlStr="<table class='gridtable' style='margin-left:50px;margin-right:100px;'>";
						var col=data.col;
						htmlStr+="<thead><tr>";
						for(var i=0;i<size;i++)
						{
							var index=i+1;
							var cn='column'+index;
							htmlStr+="<td>"+col[cn]+"</td>";
						}
						htmlStr+="</thead></tr>";
						htmlStr+="<tbody>";
						for(var i=0;i<list.length;i++)
						{
							var item=list[i];
							htmlStr+="<tr>";
							for(var j=0;j<size;j++)
							{
								var index=j+1;
								var cn='data'+index;
								htmlStr+="<td>"+item[cn]+"</td>";
							}
							htmlStr+="</tr>";
						}
						var totalPage=(data.count % 10 == 0 ? parseInt(data.count / 10) : parseInt(data.count / 10 + 1));
						htmlStr+="</tbody>";
						htmlStr += "<tfoot>";
		                htmlStr += "<tr>";
		                htmlStr += "<td colspan='"+size+"'>";
		                htmlStr += "<span>共有记录" + data.count + ";共<span id='count'>" + totalPage + "</span>页" + "</span>";
		                
		                if(pageIndex>1)
		                {
		                	htmlStr += "<a href='javascript:void(0);' onclick='GoToFirstPage()' id='aFirstPage' >首    页</a>&nbsp;&nbsp; ";
		                }else{
		                	htmlStr += "首    页&nbsp;&nbsp; ";
		                }
		                
		                if(pageIndex>1)
		                {
		                	htmlStr += "<a href='javascript:void(0);' onclick='GoToPrePage()' id='aPrePage' >前一页</a>&nbsp;&nbsp; ";
		                }else{
		                	htmlStr += "前一页&nbsp;&nbsp; ";
		                }
		                if(pageIndex<totalPage)
		                {
		                	htmlStr += "<a href='javascript:void(0);' onclick='GoToNextPage()' id='aNextPage'>后一页</a>&nbsp;&nbsp; ";
		                	htmlStr += "<a href='javascript:void(0);' onclick='GoToEndPage()' id='aEndPage' >尾    页</a>&nbsp;&nbsp; ";
		                }else{
		                	htmlStr += "后一页&nbsp;&nbsp; ";
		                	htmlStr += "尾    页&nbsp;&nbsp; ";
		                }
		                
		                htmlStr += "<input type='text' value='"+pageIndex+"' /><input type='button'  value='跳转' onclick='GoToAppointPage(this)' /> ";
		                htmlStr += "</td>";
		                htmlStr += "</tr>";
		                htmlStr += "</tfoot>";
						htmlStr+="</table>";
						
						$("#listTab").html(htmlStr);
					}
				}
			});			
		}

		//首页
        function GoToFirstPage() {
            pageIndex = 1;
            AjaxGetData(pageIndex, pageSize);
        }
        //前一页
        function GoToPrePage() {
            pageIndex -= 1;
            pageIndex = pageIndex > 1 ? pageIndex : 1;
            AjaxGetData(pageIndex, pageSize);
        }
        //后一页
        function GoToNextPage() {
            if (pageIndex + 1 <= parseInt($("#count").text())) {
                pageIndex += 1;
            }
               AjaxGetData(pageIndex, pageSize);
        }
        //尾页
        function GoToEndPage() {
            pageIndex = parseInt($("#count").text());
            AjaxGetData(pageIndex, pageSize);
        }
        //跳转
        function GoToAppointPage(e) {
            var page = $(e).prev().val();
            if (isNaN(page)) {
                alert("请输入数字!");
            }
            else {
                var tempPageIndex = pageIndex;
                pageIndex = parseInt($(e).prev().val())-1;
                if (pageIndex < 0 || pageIndex >= parseInt($("#count").text())) {
                    pageIndex = tempPageIndex;
                    alert("请输入有效的页面范围!");
                }
                else {
                    AjaxGetData(pageIndex, pageSize);
                }
            }
        }
</script>
    </head>
    <body style="background-color: white;">
    	<form name="form1">
    		<table id="baseInfo" class="gridtable" style="margin-left:400px;margin-right:200px;">
    			<tr>
    				<td style="text-align: center;">
    					<textarea name="content" id="content" cols="100" rows="20" >${text}</textarea>
    				</td>
    			</tr>
    			<tr>
    				<td style="text-align:center;"><input type="button" id="txtBtn" value="提交"/><input type="reset" value="重设"/>　执行结果:<span id="result"></span></td>
    			</tr>
    		</table>
    		<br/>
    		<div id="listTab"></div>
    	</form>
    </body>
</html>
