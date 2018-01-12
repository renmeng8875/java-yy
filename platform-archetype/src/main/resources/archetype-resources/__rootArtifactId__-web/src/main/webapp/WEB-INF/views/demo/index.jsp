#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>demo 首页</title>
</head>

<body>
<p><a href="/${parentArtifactId}/demo/text">text</a></p>
<p><a href="/${parentArtifactId}/demo/html">html</a></p>
<p><a href="/${parentArtifactId}/demo/html/error">html error</a></p>
<p><a href="/${parentArtifactId}/demo/json.json">json</a></p>
<p><a href="/${parentArtifactId}/demo/json/error.json">json error</a></p>
<p><a href="/${parentArtifactId}/demo/sdfasdfasdf">404 error</a></p>
<p><a href="/${parentArtifactId}/demo/html/login">html login</a></p>
<p><a href="/${parentArtifactId}/demo/json/login.json">json login</a></p>
<p><a href="/${parentArtifactId}/demo/file/form">file upload</a></p>
<p><a href="/${parentArtifactId}/demo/echo.json?msg=adfsdkes">dubbo echo</a></p>
</body>
</html>
