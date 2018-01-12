#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录页面</title>
</head>

<body>
<h2 style="cursor: pointer">${symbol_dollar}{exception.message}</h2>
<script type="text/javascript">

    aysnAddUdbFile();

    loginCfg.loginCallBack = function () {
        location.reload();
    }

    loginCfg.logoutCallBack = function () {
    }

    loginCfg.closeLoginCallBack = function () {
        if (!isLogin())showLoginBox();
    }

    // 打开udb登录框
    function openUdbSdk() {
        var error = false;
        try {
            if (!isLogin()) {
                showLoginBox();
            } else {
                location.reload();
            }
            if (!error) {
                clearInterval(loginTimer);
            }
        } catch (e) {
            error = true;
        }
    }
    var loginTimer = window.setInterval(openUdbSdk, 300);

    ${symbol_dollar}("h2").click(function () {
        showLoginBox();
    });
</script>
</body>
</html>
