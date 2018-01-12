package com.yy.ent.platform.modules.telnet;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * TelnetService
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
public class TelnetService implements BeanFactoryAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(TelnetService.class);
    public static BeanFactory beanFactory;
    private static final int DEFAULT_PROT = 30000;
    private static final ThreadLocal<Channel> CHANNEL_THREAD_LOCAL = new ThreadLocal<Channel>() {
        @Override
        protected Channel initialValue() {
            super.initialValue();
            return new Channel();
        }
    };
    private static final ConcurrentMap<String, ITelnetHandler> telnetHandler = new ConcurrentHashMap<String, ITelnetHandler>();
    private List<String> scanPackages;
    private Map<String, Class<?>> beanDefinitionMap;
    private static final String domain = System.getProperty("dragon.businessDomain", "localhost");
    private static final String PS1 = domain + ":" + getPid() + "> ";

    public void init(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("start telnet port:" + port);
            for (; ; ) {
                final Socket socket = serverSocket.accept();
                SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
                logger.info("remote port:" + remoteSocketAddress);
                //只能本地才能连接
                if (remoteSocketAddress.toString().indexOf("/127.0.0.1:") == -1) {
                    logger.info("Illegal host!");
                    try {
                        socket.getOutputStream().write("Illegal host!\r\n".getBytes());
                        socket.close();
                    } catch (Throwable e) {
                        logger.warn("error Illegal host", e);
                    }
                    continue;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String threadName = RandomStringUtils.randomAlphanumeric(10);
                            Thread.currentThread().setName("telnet_" + threadName);
                            BufferedReader reader = getReader(socket);
                            OutputStream out = socket.getOutputStream();
                            println(out, "Welcome!" + domain + "<" + threadName + ">Input help to show help doc!");
                            String line = null;
                            int tmp;
                            Channel channel = CHANNEL_THREAD_LOCAL.get();
                            while ((line = reader.readLine()) != null) {

                                while ((tmp = line.indexOf('\b')) != -1) {
                                    line = line.substring(0, tmp - 1) + line.substring(tmp + 1);
                                }
                                line = line.trim();
//                                line = line.replaceAll("[^\\w\\s.'\"()]", "");
                                if (line.length() > 0) {
                                    String result = getResult(channel, line);
                                    println(out, result);
                                } else {
                                    println(out, "");
                                }
                                if ("1".equals(channel.getAttribute("exit"))) {
                                    break;
                                }
                            }
                            reader.close();
                            out.close();
                            socket.close();
                        } catch (Throwable e) {
                            logger.warn("error telnet Thread", e);
                        } finally {
                            CHANNEL_THREAD_LOCAL.remove();
                        }
                    }


                }).start();
            }
        } catch (IOException e) {
            logger.warn("error start telnet port:" + port, e);
        }
    }

    private void println(OutputStream out, String msg) throws IOException {
        out.write(((StringUtils.isBlank(msg) ? "" : msg + "\r\n") + PS1).getBytes(CHANNEL_THREAD_LOCAL.get().getAttribute("charset", "UTF-8")));
        out.flush();
    }

    protected String getResult(Channel channel, String line) {
        String result;
        try {
            int index = line.indexOf(" ");
            String key;
            String value;
            if (index == -1) {
                key = line;
                value = "";
            } else {
                key = line.substring(0, index);
                value = line.substring(index + 1).trim();
            }
            key = key.toUpperCase();
            logger.info("cmd:" + key + " " + value);
            ITelnetHandler telnetHandler = getTelnetHandler(key);
            if (telnetHandler == null) {
                result = "not found " + key + " telnet handler!";
            } else {
                result = telnetHandler.exec(channel, value);
            }
        } catch (Exception e) {
            result = e.toString();
        }
        return result;
    }

    private ITelnetHandler getTelnetHandler(String key) {
        ITelnetHandler iTelnetHandler = telnetHandler.get(key);
        if (iTelnetHandler == null) {
            try {
                Class<?> aClass = beanDefinitionMap.get(key);
                ITelnetHandler bean = (ITelnetHandler) beanFactory.getBean(aClass);
                telnetHandler.putIfAbsent(key, bean);
                iTelnetHandler = telnetHandler.get(key);
            } catch (Exception e) {
                //ignore
            }
        }
        return iTelnetHandler;
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = Splitter.on(Pattern.compile(",|;")).trimResults().omitEmptyStrings().splitToList(scanPackages);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (scanPackages == null) {
            scanPackages = new ArrayList<String>(1);
            scanPackages.add("com.yy.ent.platform.modules.telnet");
        }

        TelnetScanner scanner = new TelnetScanner((BeanDefinitionRegistry) beanFactory, false);
        AnnotationTypeFilter filter = new AnnotationTypeFilter(Telnet.class);
        scanner.addIncludeFilter(filter);
        String[] strings = {};
        scanner.scan(scanPackages.toArray(strings));
        beanDefinitionMap = scanner.getBeanDefinitionMap();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int port = DEFAULT_PROT; ; port++) {
                    init(port);
                }
            }
        }).start();
    }

    public static int getPid() {
        int pid = 0;
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName(); // format: "pid@hostname"
            pid = Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable e) {
            //ignore
        }
        return pid;
    }
}
