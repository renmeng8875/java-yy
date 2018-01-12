#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${symbol_dollar}{pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <title>众筹平台 / <sitemesh:title/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>

    <link rel="stylesheet"
          href="http://${parentArtifactId}.yystatic.com/${parentArtifactId}2/??styles/index.css,styles/default.css,lib/bootstrap/2.3.2/css/bootstrap.min.css,lib/jquery-validation/1.11.1/validate.css"/>
    <script type="text/javascript"
            src="http://${parentArtifactId}.yystatic.com/${parentArtifactId}2/lib/??jquery/jquery.min.js,jquery/jquery.cookie.min.js,jquery/jquery.url.min.js,other/arttemplate.js,other/json2.js"></script>
    <script type="text/javascript"
            src="http://${parentArtifactId}.yystatic.com/${parentArtifactId}2/lib/??ent/jquery-ext.js,ent/aysnAddFile.js,ent/common-util.js,ent/date-util.js,ent/live.js,ent/hiido_click.js,ent/login.js"></script>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="http://${parentArtifactId}.yystatic.com/${parentArtifactId}2/lib/other/html5shiv.js.js"></script>
    <![endif]-->

    <script type="text/javascript"
            src="http://res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/udb.sdk.pcweb.popup.min.js?20150429"></script>

    <link type="image/x-icon" href="http://${parentArtifactId}.yystatic.com/${parentArtifactId}2/images/favicon.ico" rel="shortcut icon">
    <script src="http://${parentArtifactId}.yystatic.com/${parentArtifactId}2/lib/??jquery-validation/1.11.1/jquery.validate.min.js,jquery-validation/1.11.1/messages_bs_zh.js,bootstrap/2.3.2/js/bootstrap.min.js"
            type="text/javascript"></script>


    <sitemesh:head/>
</head>

<body>
<%@ include file="/WEB-INF/layouts/default/header.jsp" %>
<div class="container">
    <sitemesh:body/>
</div>
<%@ include file="/WEB-INF/layouts/default/footer.jsp" %>
</body>
</html>