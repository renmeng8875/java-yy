package com.yy.ent.platform.modules.yyp;

import com.yy.ent.clients.daemon.*;
import com.yy.ent.clients.s2s.S2SClient;
import com.yy.ent.srv.protocol.SrvReceiver;
import com.yy.ent.srv.util.XmlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * YYPInit
 *
 * @author suzhihua
 * @date 2016/1/26.
 */
public class YYPInit {
    protected static final Logger logger = LoggerFactory.getLogger(YYPInit.class);
    private static String daemonAbsolutePath;
    private static SrvReceiver srv = new SrvReceiver();
    private static DaemonClient registerDaemonClient;
    private static boolean isRegisterDaemon = true;//初始化是否注册daemon，默认注册，配置-DisRegisterDaemon=false不注册

    static {
        String tmp = System.getProperty("registerDaemon");
        if ("0".equals(tmp) || "false".equalsIgnoreCase(tmp)) {
            isRegisterDaemon = false;
        }
    }

    public static void initCherrio() throws Exception {
        URL cherrio = Thread.currentThread().getContextClassLoader().getResource("cherrio.xml");
        File cherrioConfig = new File(cherrio.toURI());
        String cherrioPath = cherrioConfig.getAbsolutePath();
        URL daemon = Thread.currentThread().getContextClassLoader().getResource("daemon.properties");
        File daemonFile = new File(daemon.toURI());
        String daemonPath = daemonFile.getAbsolutePath();
        initCherrio(cherrioPath, daemonPath);
    }

    public static void initCherrio(String cherrioAbsolutePath, String daemonAbsolutePath) throws Exception {
        logger.info("cherrioPath:" + cherrioAbsolutePath);
        XmlConfig.init(daemonAbsolutePath);
        //srv.regDaemon(daemonAbsolutePath);
        YYPInit.daemonAbsolutePath = daemonAbsolutePath;
        if (isRegisterDaemon) registerDaemon();
        srv.initReceiver(cherrioAbsolutePath);
    }

    /**
     * 删除daemon信息，即不对外提供服务
     *
     * @return
     * @throws Exception
     */
    public static boolean removeDaemon() throws Exception {
        if (registerDaemonClient == null) {
            return false;
        }
        Field f = DaemonClient.class.getDeclaredField("s2SClient");
        f.setAccessible(true);
        S2SClient s2SClient = (S2SClient) f.get(registerDaemonClient);
        s2SClient.delMine();
        registerDaemonClient = null;
        return true;
    }

    /**
     * 注册daemon
     *
     * @throws Exception
     */
    public static void registerDaemon() throws Exception {
        removeDaemon();
        registerDaemonClient = registerDaemon(daemonAbsolutePath);
    }

    /**
     * 注册daemon
     *
     * @param daemonAbsolutePath
     * @return
     * @throws Exception
     * @see DaemonService#register(java.lang.String)
     */
    private static DaemonClient registerDaemon(String daemonAbsolutePath) throws Exception {
        Properties properties = ConfigUtil.readProperties(daemonAbsolutePath);

        int regPort = ConfigUtil.getIntValue(properties, DaemonConfig.REG_SV_PORT);
        String regName = properties.getProperty(DaemonConfig.ACCESS_NAME);
        String regKey = properties.getProperty(DaemonConfig.ACCESS_KEY);
        int groupId = ConfigUtil.getIntValue(properties, DaemonConfig.GROUP_ID);
        String daemonHost = properties.getProperty(DaemonConfig.DAEMON_HOST);
        int daemonPort = ConfigUtil.getIntValue(properties, DaemonConfig.DAEMON_PORT);
        int readytime = ConfigUtil.getIntValue(properties, "readytime");
        if (readytime == 0) {
            readytime = 3000;
        }

        DaemonConfig cfg = new DaemonConfig();
        cfg.setAccessAccount(regName);
        cfg.setAccessKey(regKey);
        cfg.setRegSvPort(regPort);
        cfg.setGroupId(groupId);
        cfg.setDaemonPort(daemonPort);
        cfg.setDaemonHost(daemonHost);
        logger.warn("start register to daemon...");
        DaemonClient client = DaemonService.register(cfg);
        if (client.isReady(readytime)) {
            List<DaemonServerInfo> serverInfoList = client.getServiceByName(regName);
            if (null != serverInfoList && serverInfoList.size() != 0) {
                logger.info(regName + " first server:" + serverInfoList.get(0).toString());
            }
            logger.info("ok and serverinfo size is:  " + (null != serverInfoList ? serverInfoList.size() : 0));
        } else {
            logger.warn("register to daemon failed...");
        }
        logger.warn("end register to daemon...");
        return client;
    }
}
