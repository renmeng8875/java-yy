package com.yy.ent.platform.signcar.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yy.ent.clients.halb.proxy.YYPClientHALBProxy;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.commons.yypclient.adapter.ClientRequest;
import com.yy.ent.commons.yypclient.adapter.ClientResponse;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;

@Service
public class ProfitService extends BaseService{

    
    @Autowired
    @Qualifier("propmnew")
    private YYPClientHALBProxy yypclient;
    
    private final static int RETRY_TIMES = 2;
    
    public long notifyProfit(long giftId,int addNum,long fansUid,int limit) throws Exception{
        long beginTime = System.currentTimeMillis();
        long result = 0;
        int i = 0;
        while (i <= RETRY_TIMES) {
            try {
                ClientRequest clientRequest = new ClientRequest(YYPConstant.PROGRAM_TICKET_REQ_URI) ;
                clientRequest.putUint(new Uint(giftId));
                clientRequest.putUint(new Uint(addNum)) ;
                clientRequest.putUint(new Uint(fansUid));
                clientRequest.putInteger(limit);
                clientRequest.putMap(Maps.<String,String>newHashMap(),String.class,String.class);
                clientRequest.putString("");
                ClientResponse clientResponse = yypclient.getClient().sendAndWait(clientRequest);
                result = clientResponse.popUint().longValue();
                if(result == 100107){
                    result = 0;
                }
                if (i >= 1) {
                    logger.info("notify profit service ok,retryTimes:{}", i);
                } else {
                    logger.info("notify profit service ok");
                }
                break;
            } catch (Exception e) {
                logger.warn("notify profit service error,times:{}", i + 1, e);
                if (i == RETRY_TIMES) {
                    throw e;
                }
            }
            i++;
        }
        long endTime = System.currentTimeMillis();
        logger.debug("invoke notifyProfit method use time:{}ms",(endTime-beginTime));
        logger.info("invoke notifyProfit method ,giftId:{},addNum:{},fansUid:{},limit:{},result:{}",giftId,addNum,fansUid,limit,result);
        return  result;
    }
    
}
