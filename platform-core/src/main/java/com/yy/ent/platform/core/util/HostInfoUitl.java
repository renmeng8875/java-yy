package com.yy.ent.platform.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

/**
 * HostInfoUitl
 *
 * @author suzhihua
 * @date 2015/10/13.
 */
public class HostInfoUitl {
    private static final Logger logger = LoggerFactory.getLogger(HostInfoUitl.class);


    private static final String HOST_INFO_PATH = "/home/dspeak/yyms/hostinfo.ini";
    private static final String IP_ISP_LIST = "ip_isp_list";
    private static final String PRI_GROUP_ID = "pri_group_id";

    private static final Properties p = getProperties();

    public static String getHostInfo() {
        return (String) p.get(IP_ISP_LIST);
    }

    public static String getHostId() {
        return (String) p.get(PRI_GROUP_ID);
    }

    private static Properties getProperties() {
        File file = new File(HOST_INFO_PATH);
        if (!file.exists()) {
            logger.warn("not exists " + HOST_INFO_PATH);
            return new Properties();
        }
        return FileUtil.loadProperties(HOST_INFO_PATH);
    }
}
