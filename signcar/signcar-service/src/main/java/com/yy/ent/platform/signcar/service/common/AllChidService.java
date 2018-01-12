package com.yy.ent.platform.signcar.service.common;

import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.external.service.EntProxyHalbService;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AllChidService extends BaseService {
    @Autowired
    private EntProxyHalbService entProxyHalbService;
    /**
     * 接口一次请求数据
     */
    private final static int MAX_NUM = 1000;
    /**
     * 接口异常返回码
     */
    private static final int RESULT_ERROR = -9999;


    public void doAllChid(ChidCallback exe) throws Exception {
        logger.info("doAllChid begin");
        int offset = 0;
        int current;
        for (; ; ) {
            logger.info("doAllChid current offset={}", offset);
            current = requestOne(offset, exe);
            if (current != RESULT_ERROR) {
                offset += current;
                //结束
                if (current != MAX_NUM) {
                    logger.info("doAllChid over,total={}", offset);
                    break;
                }
            } else {
                //失败的等50ms再请求，单线程请求的不能等太久。
                TimeUnit.MILLISECONDS.sleep(50);
            }
        }
        logger.info("doAllChid over,total={}", offset);
    }

    private int requestOne(int offest, ChidCallback exe) {
        ClientResponse clientResponse = null;
        Uint result = null;
        try {
        /*uint32_t uid;
        uint32_t roomStatus;
        uint32_t	offset;
        uint32_t	num;*/
            ClientRequest clientRequest = new ClientRequest(YYPConstant.REQUEST_LIVINGROOM_ROOM);
            clientRequest.putUint(Uint.toUInt(0));
            clientRequest.putUint(Uint.toUInt(4));
            clientRequest.putUint(Uint.toUInt(offest));
            clientRequest.putUint(Uint.toUInt(MAX_NUM));
            clientResponse = entProxyHalbService.sendAndWait(clientRequest);
        /*uint32_t result;
        uint32_t uid;
        uint32_t roomStatus;
        uint32_t	offset;
        uint32_t	num;
        std::list< std::map<uint32_t,std::string> >	mInfo;*/
            result = clientResponse.popUint();
            if (result.intValue() != 0) {
                logger.warn("result={}", result);
                return RESULT_ERROR;
            }
        } catch (Exception e) {
            logger.error("REQUEST_LIVINGROOM_ROOM error", e);
            return RESULT_ERROR;
        }
        Uint uid = clientResponse.popUint();
        Uint roomStatus = clientResponse.popUint();
        Uint offset = clientResponse.popUint();
        Uint num = clientResponse.popUint();
        List<Map<Uint, String>> list = clientResponse.popListMap(Uint.class, String.class);
        logger.debug("result={},uid={},roomStatus={},offset={},num={},list.size={}", result, uid, roomStatus, offset, num, list.size());
        for (Map<Uint, String> uintStringMap : list) {
            String chid = uintStringMap.get(Uint.toUInt(8000));
            exe.doInChid(chid);
        }
        return list.size();
    }
}
