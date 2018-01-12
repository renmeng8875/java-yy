package com.yy.ent.platform.signcar.service.yyp;

import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.srv.builder.Dispatch;
import com.yy.ent.srv.protocol.Constants;

public class CruiseHandler extends BaseYYPHandler
{
    /**
     * @Desc:车队巡游监听用户进频道的处理接口
     * @throws Exception
     * @return:void
     * @author: renmeng  
     * @date: 2015年12月11日 下午2:40:41
     */
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_PC, max = YYPConstant.PC.MAX, min = YYPConstant.PC.REQUEST_CARNUM_CRUISE_LISTEN)
    public void listenUserEnter() throws Exception{
        /*long begin = conf.getCruiseBeginTime();
        long end = conf.getCruiseEndTime();
        long now = commonService.currentTimeMillis();
        if(now>begin&&now<end){
            getPublicComboYYHeaderPC("listenUserEnter");
            String chid = getChidPC();
            Uint uid = getComboYYHeader().getUid();
            logger.info("CruiseHandler listenUserEnter parameters step1=>chid:{},uid:{}",chid,uid);
            JSONObject json = idolCarTeamService.getFansInfo(Long.valueOf(uid.longValue()));
            if(json!=null)
            {
                if(activityService.isLivingRoom(getComboYYHeader().getTopCh().toString(), getComboYYHeader().getSubCh().toString()))
                {    
                    logger.info("CruiseHandler listenUserEnter parameters step2=>chid:{},uid:{},result:{}",chid,uid,json.toJSONString());
                    idolCarTeamService.warpData(uid.longValue(), json);
                    notifyService.notifyFansEnter(chid, json);
                    idolCarTeamService.notifyCarTeamNum(chid, json);
                }
            } 
        }*/
        responsePC(1);  
    }
    
    
    
}
