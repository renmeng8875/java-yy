package com.yy.ent.platform.signcar.service.yyp;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.commons.protopack.marshal.IntegerMarshal;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;

/**
 * 
 * @ClassName: HeartBeatHandler
 * @Desc:心跳检测服务类
 * @author: renmeng
 * @date: 2015年11月20日 上午10:46:22
 */
public class HeartBeatHandler extends BaseYYPHandler{

    @URI(YYPConstant.SERVER.REQUEST_HEARTBEAT)
    public void heartBeat() throws Exception{
        logger.info("test service is running... ");
        IntegerMarshal marshal = new IntegerMarshal(1);
        responseServer(YYPConstant.SERVER.RESPONSE_HEARTBEAT, marshal);
    }
    
    
}
