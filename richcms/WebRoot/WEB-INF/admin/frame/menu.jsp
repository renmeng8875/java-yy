<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
</head>
<body >
<div class="frmmenu_wraper">
<div class="frmmenu_out">
<div class="frmmenu_bg" id="frmmenu" data-ctx="${ctx }">
  <c:forEach items="${leftmenulist }" var="vo" varStatus="status">
    <c:if test="${vo.isHidden==0 }">
		<div class="close">
		<h2 class="h21">${vo.menu}</h2>
		<c:forEach items="${vo.children }" var="vo0" varStatus="c">
			<c:if test="${vo0.isHidden==0 }">
			<p class="p0"><a class="block" id="${vo0.menuId }" href="${ctx }/${vo0.control}/${vo0.action}" target="main">${vo0.menu}</a></p>
		    </c:if>
		</c:forEach>
		</div>
	</c:if>
  </c:forEach>

 
</div>
<div class="frmmenu_bottom">
<div class="btn" id="menubtn"></div>
<div class="bo"></div>
</div>
</div>
</div>
</body>
<script type="text/javascript">
var $frmmenu = $('#frmmenu'),fh = $frmmenu.height(),wh = $(window).height(),$frmmenuchch = $frmmenu.children().children(),menunum=0,getmenu=function(pid){ //*pid 菜单父路径id menuid 菜单id
	var arg = arguments;
	var ctx = $frmmenu.data("ctx");
	$.ajax({
		url:ctx+"/admin/getleftmenu.do?csrfToken=${csrfToken}",
		ifModified:true,
		cache:true,
		type:'POST',
		data:{pid:pid},
		dataType:'json',
		success:function(data){
			$frmmenu.html(createMenuHtml(data,ctx));
			if(arg[1]){
				$('#'+arg[1]).click();
			}
		}
	})
};
var createMenuHtml = function(data,ctx){
    var _html = '';
    var _chtml = '';
    $.each(data.leftmenulist,function(idx,item){
       if(item.isHidden==0){
          _html += '<div class="close">'+
		           '<h2 class="h21">'+item.menu+'</h2>';
		  $.each(item.children,function(cidx,citem){
		      if(citem.isHidden==0){
		      	_chtml += '<p class="p0"><a class="block" id="'+citem.menuId+'" href="'+ctx+'/'+citem.control+'/'+citem.action+'" target="main">'+citem.menu+'</a></p>';
		      }
		  });
       }
		  _html += _chtml + '</div>';
		  _chtml = ''; 
    });
    return _html;
}
$(window).resize(function(){
	wh = $(window).height();
	//if(menunum>0 && wh>=fh+48){
		$frmmenu.css('top','0');menunum=0;
	//}	
});
$frmmenu.find('h2').live('click',function(){
	$(this).parent().toggleClass('close');
	fh = $frmmenu.height();
	//if(wh>=fh+48){
		$frmmenu.css('top','0');menunum=0;
	//}
});
$frmmenu.find('p>a').live('click',function(){
	//if($(this).hasClass('on')) return false;
	$frmmenu.find('.on').removeClass('on');
	$(this).addClass('on').parent().parent().removeClass('close');
});


</script>
</html>