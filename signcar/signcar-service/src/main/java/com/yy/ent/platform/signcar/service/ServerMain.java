package com.yy.ent.platform.signcar.service;

import com.yy.ent.clients.appmonitor.act.Activation;
import com.yy.ent.platform.modules.yyp.YYPInit;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * ServerMain
 *
 * @author suzhihua
 * @date 2015/7/24.
 */
public class ServerMain {
    protected static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
        context.getBean(ActivityConfigService.class).init();
        Task.init();
        initAppMonitor();
        shutdownGracefully(10);
        YYPInit.initCherrio();
        logger.info("init over!");
        LockSupport.park();
    }

    private static void initAppMonitor() {
        try {
            ClassPathResource daemon = new ClassPathResource("daemon.properties");
            ClassPathResource halbAppmonitor = new ClassPathResource("halb_appmonitor.properties");

            Activation.activate(daemon.getFile().getAbsolutePath(), halbAppmonitor.getFile().getAbsolutePath(), "entSignCar");
            logger.info("initAppMonitor over!");
        } catch (Exception e) {
            logger.error("initAppMonitor error", e);
        }
    }

    /**
     * 关闭应用时会注销daemon注册，然后这里等待N秒后再关闭进程
     *
     * @param seconds
     */
    private static void shutdownGracefully(final int seconds) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    TimeUnit.SECONDS.sleep(seconds);
                } catch (InterruptedException e) {
                    logger.warn("shutdownGracefully InterruptedException error!", e);
                }
            }
        });
    }
}