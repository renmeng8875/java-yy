package com.yy.ent.platform.signcar.service.common;

import com.yy.ent.cherrio.yyp.ServerResponse;
import com.yy.ent.clients.halb.proxy.YYPNioClientHALBProxy;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.YYPNioClient;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.protocol.YYProtoHeader;
import com.yy.ent.platform.signcar.service.yyp.Conf;
import com.yy.ent.srv.handler.MessageResponse;
import com.yy.ent.srv.protocol.Constants;
import com.yy.ent.srv.protocol.MobileComboSendContent;
import com.yy.ent.srv.protocol.MsgType;
import com.yy.ent.srv.protocol.YYComboSendContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * YYPService
 *
 * @author suzhihua
 * @date 2015/11/12.
 */
@Service
public class YYPService extends BaseService {
    private final static int RETRY_TIMES = 3;
    @Resource(name = "yypClientPC")
    private YYPNioClientHALBProxy yypClientPC;
    @Resource(name = "yypClientMobile")
    private YYPNioClientHALBProxy yypClientMobile;
    @Autowired
    private Conf conf;
    private int MSG_MAX_COMBO_APP = 300;
    private int COMBO_APP_ACTIVITY_ClIENT_MSG = 123;

    /**
     * 手机广播
     *
     * @param rsp
     * @param chid
     * @param subChid
     * @throws Exception
     */
    public void sendBraodcastMobile(MessageResponse rsp, Uint chid, Uint subChid) throws Exception {
        sendMobile(rsp, chid, subChid, new Uint(0), MsgType.BCSUBCH);
    }

    /**
     * 手机单播
     *
     * @param rsp
     * @param chid
     * @param subChid
     * @throws Exception
     */
    public void sendUnicastMobile(MessageResponse rsp, Uint chid, Uint subChid, Uint uid) throws Exception {
        sendMobile(rsp, chid, subChid, uid, MsgType.UNICAST);
    }

    /**
     * 手机广播/单播
     *
     * @param rsp
     * @param chid
     * @param subChid
     * @param uid
     * @param msgType
     * @throws Exception
     */
    public void sendMobile(MessageResponse rsp, Uint chid, Uint subChid, Uint uid, MsgType msgType) throws Exception {
        MobileComboSendContent body = new MobileComboSendContent((short) rsp.getMaxType(), (short) rsp.getMinType());

        logger.debug("maxType:" + body.maxType + " minType: " + body.minType);
        ServerResponse tmp = (ServerResponse) rsp;
        body.setYYP(tmp);
        body.setSendType(msgType);

        body.setAppid(conf.getMobAppId());
        body.setChid(chid);
        body.setSubChid(subChid);
        body.setUid(uid);
        logger.debug("mobile srv {} ,msg content :{}", msgType, body.toString());

        ClientRequest req = new ClientRequest(new YYProtoHeader(Constants.MSG_MAX_SEND_SERVER_PROXY_MOBILE), body);
        sendOnly(yypClientMobile, req);

    }

    /**
     * pc广播
     *
     * @param rsp
     * @param chid
     * @param subChid
     * @throws Exception
     */
    public void sendBraodcastPC(MessageResponse rsp, Uint chid, Uint subChid) throws Exception {
        sendPC(rsp, chid, subChid, new Uint(0), MsgType.BCSUBCH);
    }

    /**
     * pc单播
     *
     * @param rsp
     * @param chid
     * @param subChid
     * @param uid
     * @throws Exception
     */
    public void sendUnicastPC(MessageResponse rsp, Uint chid, Uint subChid, Uint uid) throws Exception {
        sendPC(rsp, chid, subChid, uid, MsgType.UNICAST);
    }

    /**
     * pc广播/单播
     *
     * @param rsp
     * @param chid
     * @param subChid
     * @param uid
     * @param msgType
     * @throws Exception
     */
    public void sendPC(MessageResponse rsp, Uint chid, Uint subChid, Uint uid, MsgType msgType) throws Exception {
        YYComboSendContent body = new YYComboSendContent();
        body.setRspHeader(rsp);
        body.setSendType(msgType);

        body.setAppid(conf.getAppId());
        body.setChid(chid);
        body.setSubChid(subChid);

        body.setModuleId(conf.getModuleId());
        body.setUid(uid);
        body.setMaxType(MSG_MAX_COMBO_APP);
        body.setMinType(COMBO_APP_ACTIVITY_ClIENT_MSG);

        logger.debug("pc srv {} ,msg content :{}", msgType, body.toString());


        ClientRequest req = new ClientRequest(new YYProtoHeader(Constants.MSG_MAX_SEND_SERVER_PROXY_PC), body);
        sendOnly(yypClientPC, req);
    }

    private void sendOnly(YYPNioClientHALBProxy clientHALBProxy, ClientRequest req) throws Exception {
        int i = 0;
        while (i <= RETRY_TIMES) {
            try {
                _sendOnly(clientHALBProxy, req);
                if (i >= 1) {
                    //重试的
                    logger.info("sendOnly ok,retryTimes:{}", i);
                } else {
                    logger.info("sendOnly ok");
                }
                break;
            } catch (Exception e) {
                logger.warn("sendOnly error,times:{}", i + 1, e);
                //最后一次重试后还失败
                if (i == RETRY_TIMES) {
                    throw e;
                }
            }
            i++;
        }
    }

    private void _sendOnly(YYPNioClientHALBProxy clientHALBProxy, ClientRequest req) throws Exception {

        YYPNioClient client = clientHALBProxy.getClient();
        try {
            client.send(req);
        } catch (Exception e) {
            throw e;
        } finally {
            //client.close();
        }
    }
}
