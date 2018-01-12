package com.yy.ent.platform.modules.telnet;

/**
 * ITelnetHandler
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
public interface ITelnetHandler {
    String exec(Channel channel, String message);
}
