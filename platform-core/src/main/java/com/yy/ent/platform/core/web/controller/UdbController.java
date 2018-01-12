package com.yy.ent.platform.core.web.controller;

import com.duowan.udb.auth.UserinfoForOauth;
import com.duowan.udb.util.CookieUtils;
import com.duowan.udb.util.codec.AESHelper;
import com.duowan.universal.login.BasicCredentials;
import com.duowan.universal.login.Credentials;
import com.duowan.universal.login.Errors;
import com.duowan.universal.login.OAuthHeaderNames;
import com.duowan.universal.login.client.UniversalLoginClient;
import com.duowan.universal.login.client.UniversalLoginClient.CookieDomainEnum;
import com.duowan.universal.login.client.YYSecCenterOpenWSInvoker;
import com.yy.ent.commons.base.http.HttpUtil;
import com.yy.ent.external.udb.CookieProcesse;
import com.yy.ent.platform.core.service.ent.UdbEnv;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static com.yy.ent.platform.core.web.util.HttpUtil.*;

/**
 * 类说明：udb登录成功后回调的action;;
 *
 * @create:创建时间：2013-6-26 上午11:11:26
 * @author:<a href="mailto:chenxu@yy.com">陈顼</a>
 * @version:v0.0.1-SNAPSHOT0
 */
@Controller
@RequestMapping(value = "/udb")
public class UdbController extends BaseController {

    /**
     * udb退出登录时，返回给前端的key*
     */
    public static final String LOGOUT_DELETE_COOKIE_URL = "deleteCookieURL";

    /**
     * 告诉js前端，如果点取消时加调的请求地址*
     */
    public static final String DENY_CALLBACK_URL_KEY = "denyCallbackURL";

    /**
     * getSdkAuth处理完成后，返回给前端是否成功的key*
     */
    public static final String IS_SUCCESS = "success";

    /**
     * getSdkAuth处理正常后，返回给前端的url*
     */
    public static final String SUCCESS_URL = "url";

    /**
     * getSdkAuth处理正常后，返回给前端的ttokensec*
     */
    public static final String TTOKEN_SEC = "ttokensec";

    /**
     * udboauthtmptokensec*
     */
    public static final String UDB_OAUTH_TMP_TOKEN_SEC = "udboauthtmptokensec";

    /**
     * getSdkAuth处理出导常时，返回给前端的错误消息的key*
     */
    public static final String GET_SDK_AUTH_ERROR_MSG = "errMsg";

    /**
     * 设置临时请求token secret(因为session存在同步的问题，我们系统中用的是cookie)*
     */
    public static final String TEMP_TOKEN_SECRET = "temp_tokenSecret";

    /**
     * 用户的uid*
     */
    public static final String UID = "uid";

    /**
     * 用户的通行证*
     */
    public static final String USER_NAME = "username";

    @Autowired
    private UdbEnv udbEnv;

    @RequestMapping(value = "/getSdkAuth")
    @ResponseBody
    public String getSdkAuth() throws Exception {
        JSONObject json = new JSONObject();
        try {

            Credentials cc = new BasicCredentials(udbEnv.getAppId(), udbEnv.getAppKey());
            UniversalLoginClient duowan = new UniversalLoginClient(cc);
            // 把initialize()方法的参数设置为商城中的某个URL
            duowan.initialize(udbEnv.getUdbCallBack());
            // 设置临时请求token secret(因为session存在同步的问题，我们系统中用的是cookie)
            String tokenSecret = duowan.getTokenSecret();
            tokenSecret = AESHelper.encrypt(tokenSecret, udbEnv.getAppKey()); //新增加密

            URL redirectURL = duowan.getAuthorizationURL();
            String url = redirectURL.toExternalForm() + "&" + DENY_CALLBACK_URL_KEY + "=" + udbEnv.getUdbDenyCallBack();

            json.put(IS_SUCCESS, 1);
            json.put(SUCCESS_URL, url);
            json.put(TTOKEN_SEC, tokenSecret);
            return json.toString();

        } catch (Exception e) {
            String msg = e.getMessage();
            if (StringUtils.isNotEmpty(msg)) {
                msg = msg.replaceAll("[\r|\n]", ".");
                if (msg.length() > 150)
                    msg = msg.substring(0, 150);
                if (msg.contains("oauth_signature doesn't match")) {
                    logger.error("GetSdkAuthAction getSdkAuth oauth_signature doesn't match.");
                } else {
                    logger.error("GetSdkAuthAction getSdkAuth getSdkAuth error,msg:" + msg);
                }
            }
            json.put(IS_SUCCESS, 0);
            json.put(GET_SDK_AUTH_ERROR_MSG, "UniversalLoginClient error!");
            return json.toString();
        }
    }


    /**
     * 退出
     *
     * @throws Exception
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public String logout() throws Exception {
        CookieProcesse.clearCookie(getRequest(), getResponse());
        JSONObject json = new JSONObject();
        try {
            //得到此URL传给前端，调用UDB的js清除所以有域下面的公共cookie以达所有域下都是退出状态
            String deleteCookieURL = YYSecCenterOpenWSInvoker
                    .getOAuthCookieDeleteURL(udbEnv.getAppId(), udbEnv.getAppKey());
            json.put(LOGOUT_DELETE_COOKIE_URL, deleteCookieURL);
        } catch (Exception e) {
            logger.error("GetSdkAuthAction logout deleteCookieURL error.", e);
        }
        return json.toString();
    }

    /**
     * UDB回调社区的请求
     * 1 回调时候显示相关信息要用到
     * 2 默认值
     * 3 代表有回调方法,执行回调方法,同时刷新社区相关信息
     * 4 不做任务处理，只关闭窗口.如果有回调方法将执行回调方法.
     * #singer;uid 进入个人中心弹出登录窗后,后端进行相关处理
     * outApp 其它用应调用登录后,不做社区的相关业务处理
     *
     * @throws IOException
     */
    @RequestMapping(value = "/udbCallback")
    @ResponseBody
    public String udbCallback()
            throws IOException {
        StringBuilder out = new StringBuilder();
        out.append("<html><head>");

        String cancel = getRequest().getParameter("cancel");
        String oauth_token = null;
        String tokenSecret = null;
        String oauth_verifier = null;
//		String username = null;
        try {
            if (cancel != null) {
                // 被cancle时返回前台流
                out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
//		    	  out.append("self.parent.UDB.sdk.QLogin.hide_div();function callback(){self.parent.refreshOperate('"+cancel+"');};callback();");
                out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();function callback(){self.parent.refreshOperate('" + cancel + "');};callback();");
                out.append("</script>");
                out.append("</head><body>");
            } else {
                //服务端返回的信息
                oauth_token = getRequest().getParameter(OAuthHeaderNames.TOKEN_KEY);//requestToken
                oauth_verifier = getRequest().getParameter(OAuthHeaderNames.VERIFIER);//veriferCode

                tokenSecret = AESHelper.decrypt(CookieUtils.getCookie(UDB_OAUTH_TMP_TOKEN_SEC, getRequest()), udbEnv.getAppKey());

                //校验下
                if (StringUtils.isEmpty(oauth_token)) {
                    sendBadParameterError(Errors.MISSING_TOKEN, getRequest(), getResponse());
                    out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
//			    	  out.append("self.parent.UDB.sdk.QLogin.hide_div();");
                    out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();");
                    out.append("</script>");
                    out.append("</head><body>");
                    out.append("</body></html>");
                    return "";
                }
                if (StringUtils.isEmpty(oauth_verifier)) {
                    sendBadParameterError(Errors.MISSING_VERIFIER, getRequest(), getResponse());
                    out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
//			    	  out.append("self.parent.UDB.sdk.QLogin.hide_div();");
                    out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();");
                    out.append("</script>");
                    out.append("</head><body>");
                    out.append("</body></html>");
                    return "";
                }
                if (StringUtils.isEmpty(tokenSecret)) {
                    logger.error("UdbCallbackAction udbCallback get session tokenSecret is null,tokenSecret:" + tokenSecret);
                    out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
//			    	  out.append("self.parent.UDB.sdk.QLogin.hide_div();");
                    out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();");
                    out.append("</script>");
                    out.append("</head><body>");
                    out.append("</body></html>");
                    return out.toString();
                }
                //用返回的      requestToken以及veriferCode，来做：①换取accessToken；②验证返回信息是否都是合法
                Credentials cc = new BasicCredentials(udbEnv.getAppId(), udbEnv.getAppKey());
                UniversalLoginClient duowan = new UniversalLoginClient(cc);

                String[] accessToken = null;
                accessToken = duowan.getAccessToken(oauth_token, tokenSecret, oauth_verifier);
//				String[] uProfile = duowan.getUserProfile(accessToken[0]);
//			    username = uProfile[0];
                //此URL是为了传给前端，使用UDB提供的js写入所以有域下面公共的cookie，实用多域登录状态
//			    String writeCookieURL = duowan.getOAuthCookieWriteURL(accessToken[0], username);

                String yyuid = duowan.getYyuid(accessToken[0]);
//			    String writeCookieURL = duowan.getWriteCookieURL(accessToken[0], yyuid);
                List<String> reqDomainList = new LinkedList<String>();
                reqDomainList.add(CookieDomainEnum.DOMAIN_1931);
                reqDomainList.add(CookieDomainEnum.YY_DOMAIN);
                reqDomainList.add(CookieDomainEnum.DUOWAN_DOMAIN);
                String writeCookieURL = duowan.getWriteCookieURL(accessToken[0], yyuid, reqDomainList);

                // 成功时返回前台流
//				out.append("<script language=\"JavaScript\" type=\"text/javascript\">self.parent.UDB.sdk.QLogin.hide_div();function callback(){self.parent.refreshOperate('"+cancel+"','"+writeCookieURL+"');};callback();</script>");
                out.append("<script language=\"JavaScript\" type=\"text/javascript\">self.parent.UDB.sdk.PCWeb.popupCloseLgn();function callback(){self.parent.refreshOperate('" + cancel + "','" + writeCookieURL + "');};callback();</script>");
            }
            out.append("</body></html>");
            return out.toString();
        } catch (Exception e) {
//			if(username!=null)
//				username = new String(username.getBytes("iso-8859-1"), ("UTF-8"));

            String msg = e.getMessage();
            if (StringUtils.isNotEmpty(msg)) {
                msg = msg.replaceAll("[\r|\n]", ".");
                if (msg.length() > 100)
                    msg = msg.substring(0, 100);
            }
            //错误太多不打出堆栈
            logger.error("UdbCallbackAction udbCallback is error!"
//				+" username:"+username
                    + " oauth_token:" + oauth_token
                    + " tokenSecret:" + tokenSecret
                    + " oauth_verifier:" + oauth_verifier
                    + " ip:" + HttpUtil.getIpAddr(getRequest())
                    + " request url:" + getRequest().getRequestURL()
                    + " User-Agent:" + getRequest().getHeader("User-Agent")
                    + " msg:" + msg);
            out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
//	    	 out.append("self.parent.UDB.sdk.QLogin.hide_div();");
            out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();");
            out.append("</script>");
            out.append("</head><body>");
            out.append("</body></html>");
            return out.toString();
        }
    }


    /**
     * UDB回调社区的请求
     * 1 回调时候显示相关信息要用到
     * 2 默认值
     * 3 代表有回调方法,执行回调方法,同时刷新社区相关信息
     * 4 不做任务处理，只关闭窗口.如果有回调方法将执行回调方法.
     * #singer;uid 进入个人中心弹出登录窗后,后端进行相关处理
     * outApp 其它用应调用登录后,不做社区的相关业务处理
     *
     * @throws IOException
     */
    @RequestMapping(value = "/iframeUdbCallback")
    @ResponseBody
    public String iframeUdbCallback() throws Exception {
        StringBuilder out = new StringBuilder();
        out.append("<html><head>");
        UserinfoForOauth userinfo4oauth = udbEnv.userinfoForOauth(getRequest(), getResponse());
        if (userinfo4oauth == null || !userinfo4oauth.validate()) {    //验证失败
            logger.info("UdbCallbackAction iframeUdbCallback UserinfoForOauth fail!");
            return "";
        }
        // 成功时返回前台流
        out.append("<script language=\"JavaScript\" type=\"text/javascript\">function callback(){self.parent.iframeRefreshOperate();};callback();</script>");
        out.append("</body></html>");
        return out.toString();
    }

    private void sendBadParameterError(int errorCode, HttpServletRequest req,
                                       HttpServletResponse resp)
            throws IOException {
        String errorMessage = Errors.getErrorDescription(errorCode);
        resp.addHeader("X-DUOWAN-UDB-ERROR", String.valueOf(errorCode));
        resp.sendError(400, errorMessage);
    }
}
