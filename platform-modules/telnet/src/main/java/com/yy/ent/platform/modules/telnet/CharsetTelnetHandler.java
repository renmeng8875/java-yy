package com.yy.ent.platform.modules.telnet;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * InvokeTelnetHandler
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
@Telnet(name = "charset", desc = "set charset")
public class CharsetTelnetHandler implements ITelnetHandler {
    @Override
    public String exec(Channel channel, String message) {
        try {
            if (StringUtils.isBlank(message)) {
                return channel.getAttribute("charset", "UTF-8");
            }
            Charset charset = Charset.forName(message);
            channel.setAttribute("charset", charset.toString());
            return "set charset:" + message;
        } catch (Throwable e) {
            return e.toString();
        }
    }
}
