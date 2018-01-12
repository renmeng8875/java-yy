package com.yy.ent.platform.modules.telnet;

/**
 * InvokeTelnetHandler
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
@Telnet(name = "help", desc = "see this doc")
public class HelpTelnetHandler implements ITelnetHandler {
    @Override
    public String exec(Channel channel, String message) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("charset gbk\r\n");
            sb.append("invoke beanName.method(args)\r\n");
            sb.append("invoke class.method(args)\r\n");
            sb.append("log reset log\r\n");
            sb.append("exit exit this session\r\n");
            sb.append("help see this doc");
            return sb.toString();
        } catch (Throwable e) {
            return e.toString();
        }
    }
}
