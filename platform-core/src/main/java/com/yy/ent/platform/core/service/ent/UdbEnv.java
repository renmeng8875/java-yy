package com.yy.ent.platform.core.service.ent;

import com.duowan.udb.auth.UserinfoForOauth;
import com.yy.ent.external.service.UdbServiceAgentHalbService;
import com.yy.ent.external.udb.UdbValidate;
import com.yy.ent.platform.core.util.FileUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xieyong on 2014/11/10.
 */
public class UdbEnv implements InitializingBean {

    protected final Logger logger = Logger.getLogger(UdbEnv.class);
    private static final String CALLBACK_URL_KEY = "CALLBACK_URL";
    private static final String DENY_CALLBACK_URL_KEY = "DENY_CALLBACK_URL";
    private static final String APP_ID_KEY = "APP_ID";
    private static final String APP_KEY_KEY = "APP_KEY";

    private static final String LOGIN_UID = "udb.login.uid";
    public static final String LOGIN_TICKET = "udbTicket";
    private static final String TICKET_RET_UID = "uid";

    private String udbCallBack;
    private String udbDenyCallBack;
    private String appId;
    private String appKey;

    @Autowired
    private UdbServiceAgentHalbService udbSAService;


    public String getUdbCallBack() {
        return udbCallBack;
    }

    public void setUdbCallBack(String udbCallBack) {
        this.udbCallBack = udbCallBack;
    }

    public String getUdbDenyCallBack() {
        return udbDenyCallBack;
    }

    public void setUdbDenyCallBack(String udbDenyCallBack) {
        this.udbDenyCallBack = udbDenyCallBack;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    private Resource configPath;

    public void setConfigPath(Resource configPath) {
        this.configPath = configPath;
    }

    public void afterPropertiesSet() throws Exception {
        init(configPath.getURL().getPath());
    }

    public void init(String configPath) throws Exception {
        Properties prop = FileUtil.loadProperties(configPath);

        udbCallBack = prop.getProperty(CALLBACK_URL_KEY);
        udbDenyCallBack = prop.getProperty(DENY_CALLBACK_URL_KEY);
        appId = prop.getProperty(APP_ID_KEY);
        appKey = prop.getProperty(APP_KEY_KEY);
        logger.info("\n\n ---------------please attention httpRequest ticket param name is set to [" + LOGIN_TICKET + "]------------------\n\n");
    }


    public String getLoginUid(HttpServletRequest req, HttpServletResponse rep) throws Exception {
        Object uidObj = req.getAttribute(LOGIN_UID);
        String uid = null;
        if (uidObj != null) {
            uid = uidObj.toString();
        } else {
            String ticket = req.getParameter(LOGIN_TICKET);
            if (StringUtils.isBlank(ticket) || "undefined".equals(ticket)) {
                uid = UdbValidate.getLoginUid(req, rep, appId, appKey);
            } else {
                Map<String, String> resp = udbSAService.verifyAppToken(ticket);
                uid = resp.get(TICKET_RET_UID);
            }
            req.setAttribute(LOGIN_UID, uid);
        }
        return uid;
    }

    public UserinfoForOauth userinfoForOauth(HttpServletRequest req, HttpServletResponse rep) throws Exception {
        return UdbValidate.UserinfoForOauth(req, rep, appId, appKey);
    }

    public JSONObject validAccessToken(HttpServletRequest req, HttpServletResponse rep) throws Exception {

        return UdbValidate.validAccessToken(req, rep, appId, appKey);
    }

    public boolean isTicketValid(HttpServletRequest req) {
        String ticket = req.getParameter(LOGIN_TICKET);
        if (StringUtils.isNotBlank(ticket)) {

            return true;
        }
        return false;
    }
}
