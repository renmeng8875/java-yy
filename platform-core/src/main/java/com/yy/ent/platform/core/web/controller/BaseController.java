package com.yy.ent.platform.core.web.controller;

import com.yy.ent.platform.core.service.ent.UdbEnv;
import com.yy.ent.platform.core.web.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    protected UdbEnv udbEnv;

    public Long getLoginUid() {
        String uidStr = getLoginUidStr();
        if (StringUtils.isNotBlank(uidStr)) {
            return Long.parseLong(uidStr);
        }
        return null;
    }

    public String getLoginUidStr() {
        String uid = "";
        try {
            uid = udbEnv.getLoginUid(HttpUtil.getRequest(), HttpUtil.getResponse());
        } catch (Exception e) {
            logger.warn("取uid出错", e);
        }
        return uid;
    }
}
