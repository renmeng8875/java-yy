package com.yy.ent.platform.modules.yyp.handler;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.clients.s2s.util.HostInfoUtils;
import com.yy.ent.platform.modules.yyp.BaseYYPHandler;
import com.yy.ent.platform.modules.yyp.Result;
import com.yy.ent.platform.modules.yyp.YYPInit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * HealthHanlder
 *
 * @author suzhihua
 * @date 2016/2/17.
 */
public class HealthHanlder extends BaseYYPHandler {
    public static final int REQUEST_DEMO = 0 << 8 | 122;
    public static final int REQUEST_HEARTBEAT = 2 << 8 | 122;
    public static final int REQUEST_REMOVE_DAEMON = 4 << 8 | 122;
    public static final int REQUEST_REGISTER_DAEMON = 6 << 8 | 122;
    public static final int REQUEST_IP_LIST = 8 << 8 | 122;

    @URI(REQUEST_DEMO)
    public void demo() throws Exception {
        logger.info("REQUEST_DEMO");
        Result result = getResult();
        responseServer(result);
    }

    @URI(REQUEST_HEARTBEAT)
    public void heartbeat() throws Exception {
        logger.info("REQUEST_HEARTBEAT");
        Result result = getResult();
        responseServer(result);
    }

    @URI(REQUEST_REMOVE_DAEMON)
    public void removeDaemon() throws Exception {
        logger.info("REQUEST_REMOVE_DAEMON");
        Result result = getResult();
        YYPInit.removeDaemon();
        responseServer(result);
    }

    @URI(REQUEST_REGISTER_DAEMON)
    public void registerDaemon() throws Exception {
        logger.info("REQUEST_REGISTER_DAEMON");
        Result result = getResult();
        YYPInit.registerDaemon();
        responseServer(result);
    }

    @URI(REQUEST_IP_LIST)
    public void ipList() throws Exception {
        logger.info("REQUEST_IP_LIST");
        Map<Integer, Long> ipList = HostInfoUtils.getIpList();
        Result result = getResult();
        result.put("ipList", ipList);
        responseServer(result);
    }

    private Result getResult() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = formatter.format(new Date());
        return Result.newResult().put("time", format).put("uri", getYYHeader().getUri());
    }
}
