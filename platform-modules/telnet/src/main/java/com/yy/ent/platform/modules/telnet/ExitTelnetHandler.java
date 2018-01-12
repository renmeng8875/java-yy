package com.yy.ent.platform.modules.telnet;

/**
 * InvokeTelnetHandler
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
@Telnet(name = "exit", desc = "exit this session")
public class ExitTelnetHandler implements ITelnetHandler {
    @Override
    public String exec(Channel channel, String message) {
        try {
            channel.setAttribute("exit", "1");
            return "exit this session";
        } catch (Throwable e) {
            return e.toString();
        }
    }
}
