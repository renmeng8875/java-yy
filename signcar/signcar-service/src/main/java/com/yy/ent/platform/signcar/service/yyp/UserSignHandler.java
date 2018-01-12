package com.yy.ent.platform.signcar.service.yyp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.commons.protopack.marshal.LongMarshal;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.yyp.UserInfoBeanMarshal;
import com.yy.ent.platform.signcar.service.BussinessConf;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.common.ProfitService;

public class UserSignHandler extends BaseYYPHandler{
    
    private final static Logger signLogger = LoggerFactory.getLogger("log.signFile");
    
    private ActivityConfigService activityConfigService = SpringHolder.getBean(ActivityConfigService.class);

    @URI(YYPConstant.SERVER.REQUEST_QUERYUSERINFO)
    public void userInfoHandle() throws Exception{
        long fansUid = getRequest().popLong();
        Long activityId = activityConfigService.getCurrentActivityId("car");
        logger.info("userinfoHandle parameters=>fansUid:{},activityId:{}",fansUid,activityId);
        UserInfoBeanMarshal marshal = new UserInfoBeanMarshal();
        marshal.result = Uint.toUInt(0);
        marshal.data = "{}";
        responseServer(YYPConstant.SERVER.RESPONSE_QUERYUSERINFO, marshal);
    }
    
    @URI(YYPConstant.SERVER.REQUEST_USERSIGN)
    public void userSign() throws Exception{
        Long activityId = activityConfigService.getCurrentActivityId("car");
        long fansUid = getRequest().popLong();
        int prizeType = getRequest().popInteger();
        int addNum = getRequest().popInteger();
        signLogger.info("usersign parameters=>fansUid:{},prizeType:{},addNum:{},activityId:{}",fansUid,prizeType,addNum,activityId);
        long result = 0;
        LongMarshal marshal = new LongMarshal(result);
        responseServer(YYPConstant.SERVER.RESPONSE_USERSIGN, marshal);
        
    }
}
