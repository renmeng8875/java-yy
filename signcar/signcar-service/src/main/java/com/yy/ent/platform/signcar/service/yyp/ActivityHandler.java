package com.yy.ent.platform.signcar.service.yyp;

import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.mongodb.ActivityConfig;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.car.ActivityService;
import com.yy.ent.platform.signcar.service.car.NotifyService;
import com.yy.ent.srv.builder.Dispatch;
import com.yy.ent.srv.protocol.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * ActivityHandler
 *
 * @author suzhihua
 * @date 2015/11/11.
 */
public class ActivityHandler extends BaseYYPHandler {
    ActivityConfigService activityConfigService = SpringHolder.getBean(ActivityConfigService.class);

    NotifyService notifyService = SpringHolder.getBean(NotifyService.class);
    ActivityService activityService = SpringHolder.getBean(ActivityService.class);

    /**
     * 活动是否进行中
     *
     * @throws Exception
     */
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_PC, max = YYPConstant.PC.MAX, min = YYPConstant.PC.REQUEST_CAR_IS_OPEN)
    public void isOpenActivity() throws Exception {
        getPublicComboYYHeaderPC("isOpenActivity");
        Long activityId = activityConfigService.getCurrentActivityId();
        Map<String, Object> result = new HashMap<String, Object>();
        ActivityConfig activityConfig = activityConfigService.getActivityConfig(activityId);
        if (activityId >= 0 && activityConfigService.isActivityOpen(activityId)) {
            String url = activityConfig.getUrl();
            if (StringUtils.isNotBlank(url)) result.put("url", url);
        }

        if (activityId < 0) {
            result.put("isOpen", false);
            responsePC(result);
            return;
        }
        String clazz = activityConfig.getClazz();
        if ("car".equals(clazz)) {
            if (!isLivingRoom(getComboYYHeader().getTopCh().toString(), getComboYYHeader().getSubCh().toString())) {
                result.put("isOpen", false);
                responsePC(result);
                return;
            }
            //正常流程
            String chid = getChidPC();
            Map<String, Object> result2 = activityService.getActivityResult(activityId, chid);
            result.putAll(result2);
            responsePC(result);
            //广播用户进来
            Uint uid = getComboYYHeader().getUid();
            notifyService.addNotifyUserEntry(activityId, chid, uid.longValue());
        } else {
            result.put("isOpen", false);
            responsePC(result);

        }
    }

    /**
     * 检测是否直播间
     *
     * @return
     * @throws Exception
     */
    private boolean isLivingRoom(String topCh, String subCh) throws Exception {
        if (!topCh.equals(subCh)) {
            return false;
        }
        return activityService.isLivingRoom(topCh, subCh);
    }


}
