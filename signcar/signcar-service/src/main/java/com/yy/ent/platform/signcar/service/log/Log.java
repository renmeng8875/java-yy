package com.yy.ent.platform.signcar.service.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log
 *
 * @author suzhihua
 * @date 2015/12/14.
 */
public class Log {

    private static final Logger LOGGER_RECOMMEND = LoggerFactory.getLogger("log.recommend");

    public static void recommendLog(String fansUid, String idolUid, int time, String day) {
        LOGGER_RECOMMEND.info("{},{},{},{}", fansUid, idolUid, time, day);
    }
}
