#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;
/**
 * ServerMain
 *
 * @author suzhihua
 * @date 2015/7/24.
 */
public class ServerMain {
    protected static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
        logger.info("dubbo-server服务正在监听");
        LockSupport.park();
    }
}
