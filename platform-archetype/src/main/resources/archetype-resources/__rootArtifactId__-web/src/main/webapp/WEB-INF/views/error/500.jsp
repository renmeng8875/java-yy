#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>出错页面</title>
</head>

<body>
<h2>${symbol_dollar}{exception.errorCode} - ${symbol_dollar}{exception.message}</h2>
</body>
</html>
