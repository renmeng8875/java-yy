package com.yy.ent.platform.signcar.service.yyp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.cherrio.yyp.ServerResponse;
import com.yy.ent.commons.protopack.base.Marshallable;
import com.yy.ent.commons.protopack.base.Packet;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.protocol.YYProtoHeader;
import com.yy.ent.srv.handler.BaseSrvHandler;
import com.yy.ent.srv.handler.MessageResponse;
import com.yy.ent.srv.protocol.MobileComboSendContent;
import com.yy.ent.srv.protocol.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

/**
 * YYPHandler
 *
 * @author suzhihua
 * @date 2015/10/26.
 */
public class BaseYYPHandler extends BaseSrvHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected void getPublicComboYYHeaderPC(String methodName) {
        if (!logger.isDebugEnabled()) return;
        int maxType = getComboYYHeader().getMax();
        int minType = getComboYYHeader().getMin();
        int moduleId = getComboYYHeader().getModuleId();
        Uint srvId = getComboYYHeader().getServerId();
        Uint subCh = getComboYYHeader().getSubCh();
        Uint topCh = getComboYYHeader().getTopCh();
        Uint uid = getComboYYHeader().getUid();
        String urilong = getComboYYHeader().uri();
        logger.debug("pc:" + methodName + "||maxType :" + maxType + " minType :" + minType + " moduleId :" + moduleId + " srvId :" + srvId + " subCh :" + subCh + " topCh :" + topCh + " uid :" + uid + " uri :" + getComboYYHeader().getUri() + " long uri: " + urilong);
    }

    protected void getPublicComboYYHeaderMobile(String methodName) {
        if (!logger.isDebugEnabled()) return;
        short maxType = getMobileHeader().mobileMaxType;
        short minType = getMobileHeader().mobileMinType;
        Uint appId = getMobileHeader().mobileAppId;
        Uint subCh = getMobileHeader().mobileSubCh;
        Uint topCh = getMobileHeader().mobileTopCh;
        Uint uid = getMobileHeader().mobileUid;
        Map<Short, String> mobileExtend = getMobileHeader().mobileExtend;

        logger.debug("mobile:" + methodName + "||maxType :" + maxType + " minType :" + minType + " appId :" + appId + " subCh :" + subCh + " topCh :" + topCh + " uid :" + uid);
    }

    protected String getChidPC() {
        Uint subCh = getComboYYHeader().getSubCh();
        Uint topCh = getComboYYHeader().getTopCh();
        return topCh.longValue() + "_" + subCh.longValue();
    }

    protected String getChidMobile() {
        Uint subCh = getMobileHeader().mobileSubCh;
        Uint topCh = getMobileHeader().mobileTopCh;
        return topCh.longValue() + "_" + subCh.longValue();
    }


    /**
     * 原路返回给server
     *
     * @param uri
     * @param content
     */
    public void responseServer(int uri, Marshallable... content) {
        logger.debug("srv msg content :" + Arrays.toString(content));
        Packet pk = new Packet(new YYProtoHeader(uri), content);
        send(pk);
    }

    /**
     * 原路返回给pc
     *
     * @param content
     * @throws Exception
     */
    public void responsePC(Object content) throws Exception {
        int max = getComboYYHeader().getMax();
        int min = getComboYYHeader().getMin() + 1;
        MessageResponse res;
        if (content instanceof Result) {
            res = ((Result) content).toMessageResponse(max, min);
        } else {
            res = new MessageResponse(max, min);
            res.putString(ResultWrapper.wrapJsonResultSuccess(content));
        }
        sendUnicast(res);
    }
    
    
   
    /**
     * 原路返回给mobile
     *
     * @param content
     * @throws Exception
     */
    public void responseMobile(Object content) throws Exception {
        Short max = getMobileHeader().mobileMaxType;
        int min = getMobileHeader().mobileMinType + 1;
        MessageResponse res;
        if (content instanceof Result) {
            res = ((Result) content).toMessageResponse(max, min);
        } else {
            res = new MessageResponse(max, min);
            res.putString(ResultWrapper.wrapJsonResultSuccess(content));
        }
        sendUnicastMobile(res);
    }


    protected JSONObject getRequestJson() {
        String jsonStr = getRequest().popString();
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        logger.debug("jsonObject:{}", jsonObject);
        return jsonObject;
    }

    /**
     * mobile发送的方法
     *
     * @param rsp 发送的包
     * @throws Exception
     */
    protected void sendUnicastMobile(MessageResponse rsp) {
        if (getMobileHeader().getMobileUid().intValue() == 0) {
            logger.warn("environment is null : uid: " + getMobileHeader().getMobileUid());
            return;
        }
        MobileComboSendContent body = new MobileComboSendContent((short) rsp.getMaxType(), (short) rsp.getMinType());

        logger.debug("maxType:" + body.maxType + " minType: " + body.minType);
        ServerResponse tmp = (ServerResponse) rsp;
        body.setYYP(tmp);
        body.setSendType(MsgType.UNICAST);
        body.mobileExtend = getMobileHeader().mobileExtend;

        sendPacket(body);
    }
}
