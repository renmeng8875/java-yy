package com.yy.ent.platform.signcar.service.yyp;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.commons.protopack.marshal.StringMarshal;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.RankData;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.car.RankService;

import java.util.List;

/**
 * ActivityHandler
 *
 * @author suzhihua
 * @date 2015/11/11.
 */
public class RankHandler extends BaseYYPHandler {
    RankService rankService = SpringHolder.getBean(RankService.class);
    ActivityConfigService activityConfigService = SpringHolder.getBean(ActivityConfigService.class);

    /**
     * top5榜单
     *
     * @throws Exception
     */
    @URI(YYPConstant.SERVER.REQUEST_RANK_TOP5)
    public void rankTop5Server() throws Exception {
        Long activityId = activityConfigService.getLastActivityId("car");
        logger.debug("rankTop5Server activityId={}",activityId);
        List<RankData> rankDataList = rankService.getRankDataListTop5(activityId);
        String json = ResultWrapper.wrapJsonResultSuccess(rankDataList);
        StringMarshal marshal = new StringMarshal(json);
        responseServer(YYPConstant.SERVER.RESPONSE_RANK_TOP5, marshal);
    }

    /**
     * web用，所有榜单数据
     *
     * @throws Exception
     */
    @URI(YYPConstant.SERVER.REQUEST_RANK)
    public void rankAllServer() throws Exception {
        Long activityId = activityConfigService.getLastActivityId("car");
        logger.debug("rankAllServer activityId={}",activityId);
        List<RankData> rankDataList = rankService.getRankDataList(activityId);
        String json = ResultWrapper.wrapJsonResultSuccess(rankDataList);
        StringMarshal marshal = new StringMarshal(json);
        responseServer(YYPConstant.SERVER.RESPONSE_RANK, marshal);
    }

}
