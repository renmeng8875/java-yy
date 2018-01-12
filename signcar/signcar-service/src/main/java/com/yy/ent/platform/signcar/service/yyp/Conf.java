package com.yy.ent.platform.signcar.service.yyp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Conf{

    @Value("${yyp.appId}")
    private int appId;

    @Value("${yyp.mobAppId}")
    private int mobAppId;

    @Value("${yyp.moduleId}")
    private int moduleId;
    public int getAppId() {
        return appId;
    }

    public int getMobAppId() {
        return mobAppId;
    }

    public int getModuleId() {
        return moduleId;
    }
}
