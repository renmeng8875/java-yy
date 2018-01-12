<%@include file="../public/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<style type="text/css">
body {
	cursor: pointer;
}
</style>
</head>
<body class="bordercolor">
	<table height="100%" cellspacing="0" cellpadding="0" border="0"
		style="position:absolute;left:3px;">
		<tr>
			<td><img src="${ctx}/static/admin/images/bar_close.gif" width="6"
				height="60" id="drag_img" border="0" /></a>
			</td>
		</tr>
	</table>
</body>
<script type="text/javascript">
	var frmbody = parent.document.getElementById('frame-body'), cols = frmbody.cols
			.split(','), cols0 = cols[0], autoOpen = function() {
		if (cols[0] == 0) {
			$('body').click();
			parent.main.$('#cattree').hide();
		}
	};
	$('body').select(function() {
		return false;
	}).click(function() {
		if (cols[0] == 0) {
			cols[0] = cols0;
			$('#drag_img')[0].src = '${ctx}/static/admin/images/bar_open.gif';
		} else {
			cols[0] = 0;
			$('#drag_img')[0].src = '${ctx}/static/admin/images/bar_close.gif';
		}
		frmbody.cols = cols.join(',');
	});
</script>
</html>