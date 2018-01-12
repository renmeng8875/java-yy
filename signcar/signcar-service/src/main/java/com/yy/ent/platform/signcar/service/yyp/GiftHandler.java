package com.yy.ent.platform.signcar.service.yyp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.commons.protopack.marshal.StringMarshal;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.car.RankService;

/**
 * 入队卡礼物
 *
 * @author suzhihua
 * @date 2015/11/11.
 */
public class GiftHandler extends BaseYYPHandler {
    RankService rankService = SpringHolder.getBean(RankService.class);
    
    ActivityConfigService activityConfigService = SpringHolder.getBean(ActivityConfigService.class);
    

    /**
     * web用，所有榜单数据
     *
     * @throws Exception
     */
    @URI(YYPConstant.SERVER.REQUEST_GIFT)
    public void giftNotify() throws Exception {
        StringMarshal marshal;
        try {
            String json = getRequest().popString();
            logger.debug("giftRequest={}", json);
            JSONObject jsonObject = JSON.parseObject(json);
            long uid = jsonObject.getLong("uid");
            long idolUid = jsonObject.getLong("idolUid");
            int num = jsonObject.getInteger("num");
            String chid = jsonObject.getString("chid");
            Long activityId = activityConfigService.getCurrentActivityId("car");
            
            marshal = new StringMarshal(ResultWrapper.wrapJsonResultSuccess("ok"));
        } catch (Throwable e) {
            logger.error("giftNotity error",e);
            marshal = new StringMarshal(ResultWrapper.wrapJsonResultError(-1, null, e.getMessage()));
        }
        responseServer(YYPConstant.SERVER.RESPONSE_GIFT, marshal);
    }


}
