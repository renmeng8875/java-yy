package com.yy.ent.platform.signcar.service.yyp;

import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.commons.protopack.marshal.IntegerMarshal;
import com.yy.ent.commons.protopack.marshal.UintMarshal;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.TaskbarInfo;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.service.common.CommonService;
import com.yy.ent.platform.signcar.service.heart.HeartNotifyService;
import com.yy.ent.platform.signcar.service.heart.HeartService;
import com.yy.ent.srv.builder.Dispatch;
import com.yy.ent.srv.protocol.Constants;
import org.apache.commons.lang3.StringUtils;

import static com.yy.ent.platform.signcar.service.yyp.Result.ERROR_ILLEGAL_ARGS;
import static com.yy.ent.platform.signcar.service.yyp.Result.ERROR_UNKNOWN;

/**
 * HeartServiceHandler
 *
 * @author suzhihua
 * @date 2015/12/19.
 */
public class HeartServiceHandler extends BaseYYPHandler {
    private HeartService heartService = SpringHolder.getBean(HeartService.class);
    private HeartNotifyService notifyService = SpringHolder.getBean(HeartNotifyService.class);
    private CommonService commonService = SpringHolder.getBean(CommonService.class);

    /**
     * 签到点心接口(军华签到调用)
     *
     * @throws Exception
     */
    @URI(YYPConstant.SERVER.REQUEST_SIGN_HEART)
    public void signHeart() throws Exception {
        Long topChid = getRequest().popLong();//顶级频道
        Long subChid = getRequest().popLong();//子频道
        Long fansUid = getRequest().popLong();//签到用户uid
        Boolean isPC = getRequest().popBoolean();//签到类型 pc端返回true，手机返回false
        Integer num = getRequest().popInteger();//签到领取心数

        int result = ERROR_UNKNOWN;
        if (topChid == null || topChid < 0 || subChid == null || subChid < 0 || fansUid == null || fansUid < 0 || num == null || num <= 0 || isPC == null) {
            result = ERROR_ILLEGAL_ARGS;
            logger.info("signHeart Illegal args");
        } else {
            try {
                int fansHeartNum = heartService.addHeart(fansUid, num);
                result = 0;
                long time = commonService.currentTimeMillis();
                notifyService.notifySignHeart(topChid, subChid, fansUid, fansHeartNum, isPC, time);
            } catch (Throwable e) {
                logger.warn("signHeart addHeart error", e);
                result = ERROR_UNKNOWN;
            }
        }
        logger.info("signHeart result:{}", result);
        IntegerMarshal marshal = new IntegerMarshal(result);//0成功，其他失败  101：未知错误， 102:参数错误
        responseServer(YYPConstant.SERVER.RESPONSE_SIGN_HEAET, marshal);
    }

    /**
     * 签到点心接口(页游调用)
     *
     * @throws Exception
     */
    @URI(YYPConstant.SERVER.REQUEST_WEB_GAME_SIGN_HEART)
    public void webGameSignHeart() throws Exception {
        String serial = getRequest().popString();//流水号
        long fansUid = getRequest().popUint().longValue();//签到用户uid
        int num = getRequest().popUint().intValue();//签到领取心数

        int result = ERROR_UNKNOWN;
        if (fansUid < 0 || num <= 0 || StringUtils.isBlank(serial)) {
            result = ERROR_ILLEGAL_ARGS;
            logger.info("webGameSignHeart Illegal args");
        } else {
            try {
                if (heartService.addSerial(fansUid, serial)) {
                    try {
                        int fansHeartNum = heartService.addHeart(fansUid, num, serial);
                        result = 0;
                        notifyService.notifySignHeart(fansUid, fansHeartNum, true, commonService.currentTimeMillis());
                    } catch (Throwable e) {
                        logger.warn("webGameSignHeart addHeart error 1", e);
                        heartService.clearSerial(fansUid, serial);
                        result = 104;
                    }
                } else {
                    result = 103;
                }
            } catch (Throwable e) {
                logger.warn("webGameSignHeart addHeart error 2", e);
                result = ERROR_UNKNOWN;
            }
        }
        logger.info("webGameSignHeart result:{}", result);
        UintMarshal marshal = new UintMarshal(new Uint(result));//0成功，其他失败  101：未知错误， 102:参数错误,103流水号重复,104加心失败
        responseServer(YYPConstant.SERVER.RESPONSE_WEB_GAME_SIGN_HEAET, marshal);
    }

    /**
     * 初始化任务条接口
     *
     * @throws Exception
     */
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_PC, max = YYPConstant.PC.MAX, min = YYPConstant.PC.REQUEST_INIT_TASKBAR)
    public void initTaskbarPC() throws Exception {
        getPublicComboYYHeaderPC("initTaskbarPC");
        Boolean isOpen = heartService.getIsOpen();
        
        if (!isOpen) {
            responsePC(Result.newResult("isOpen", 1));
            return;
        }
        
        Uint subCh = getComboYYHeader().getSubCh();
        Uint topCh = getComboYYHeader().getTopCh();
        Uint fansUid = getComboYYHeader().getUid();
        Boolean isDisplay = heartService.isDisplay(topCh.toString());
        logger.debug("initTaskbarPC parameters :subch:{},topch:{},fansuid:{},isDisplay:{}", subCh,topCh,fansUid,isDisplay);
        if (!isDisplay) {
            responsePC(Result.newResult("isOpen", 1));
            return;
        }
        
        long idolUid = getRequest().popUint().longValue();//首麦
        if (idolUid > 0) {
            try {
                TaskbarInfo taskbarInfo = heartService.getTaskbarInfo(idolUid, fansUid.longValue(), false);
                Result result = Result.newResult().setDataRoot(taskbarInfo).put("fansUid", fansUid.longValue()).put("idolUid", idolUid);
                result.put("isOpen", true);
                //result.put("tip", heartService.getTip());
                responsePC(result);
            } catch (Throwable e) {
                logger.warn("initTaskbarPC error", e);
                responsePC(Result.newResult(ERROR_UNKNOWN));
            }
        } else {
            responsePC(Result.newResult(ERROR_ILLEGAL_ARGS));
        }
    }

    /**
     * 初始化任务条接口
     *
     * @throws Exception
     */
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_MOBILE, max = YYPConstant.MOBILE.MAX, min = YYPConstant.MOBILE.REQUEST_INIT_TASKBAR)
    public void initTaskbarMobile() throws Exception {
        getPublicComboYYHeaderMobile("initTaskbarMobile");
        Uint subCh = getMobileHeader().getMobileSubCh();
        Uint topCh = getMobileHeader().getMobileTopCh();
        Uint fansUid = getMobileHeader().getMobileUid();
        long idolUid = getRequest().popUint().longValue();//首麦
        if (idolUid >= 0) {
            try {
                TaskbarInfo taskbarInfo = idolUid > 0 ? heartService.getTaskbarInfo(idolUid, fansUid.longValue(), true) : heartService.getTaskbarInfo(idolUid, fansUid.longValue(), false);
                Result result = Result.newResult().setDataRoot(taskbarInfo).put("fansUid", fansUid.longValue()).put("idolUid", idolUid);
                result.put("tip", heartService.getTip());
                responseMobile(result);
            } catch (Throwable e) {
                logger.warn("initTaskbarMobile error", e);
                responseMobile(Result.newResult(ERROR_UNKNOWN));
            }
        } else {
            responseMobile(Result.newResult(ERROR_ILLEGAL_ARGS));
        }
    }

    /**
     * 送心接口
     *
     * @throws Exception
     */
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_PC, max = YYPConstant.PC.MAX, min = YYPConstant.PC.REQUEST_CONSUME_HEART)
    public void consumeHeartPC() throws Exception {
        getPublicComboYYHeaderPC("consumeHeartPC");
        Uint subCh = getComboYYHeader().getSubCh();
        Uint topCh = getComboYYHeader().getTopCh();
        Uint fansUid = getComboYYHeader().getUid();
        long idolUid = getRequest().popUint().longValue();//首麦
        int num = getRequest().popUint().intValue();//送心数量
        logger.info("consumeHeartPC parameter fansUid={},idolUid={},num={}", fansUid,idolUid, num);
        int fansNum = 0;
        int code = ERROR_UNKNOWN;
        if (num > 0 && idolUid > 0) {
            try {
                fansNum = heartService.consumeHeart(fansUid.longValue(), idolUid, num);
                if (fansNum >= 0) {
                    //正常情况
                    TaskbarInfo taskbarInfo = heartService.getTaskbarInfo(idolUid, fansUid.longValue(), false);
                    Result result = Result.newResult().setDataRoot(taskbarInfo).put("fansUid", fansUid.longValue()).put("idolUid", idolUid);
                    result.put("time", commonService.currentTimeMillis());
                    logger.info("consumeHeartPC step 1,result:{}",result);
                    responsePC(result);
                    notifyService.notifyConsumeHeart(topCh, subCh, idolUid, fansUid.longValue(), num, taskbarInfo);
                    return;
                } else {
                    code = 1;//心不足
                }
            } catch (Throwable e) {
                logger.warn("consumeHeart error", e);
                code = ERROR_UNKNOWN;
            }
        } else {
            code = ERROR_ILLEGAL_ARGS;
        }
        int fansHeartNum = heartService.getFansHeartNum(fansUid.longValue());
        Result result = Result.newResult(code).put("num", num).put("fansHeartNum", fansHeartNum).put("time", commonService.currentTimeMillis());
        logger.info("consumeHeartPC step 2,result:{}",result);
        responsePC(result);
    }

    /**
     * 送心接口
     *
     * @throws Exception
     */
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_MOBILE, max = YYPConstant.MOBILE.MAX, min = YYPConstant.MOBILE.REQUEST_CONSUME_HEART)
    public void consumeHeartMobile() throws Exception {
        getPublicComboYYHeaderMobile("consumeHeartMobile");
        Uint subCh = getMobileHeader().getMobileSubCh();
        Uint topCh = getMobileHeader().getMobileTopCh();
        Uint fansUid = getMobileHeader().getMobileUid();
        long idolUid = getRequest().popUint().longValue();//首麦
        int num = getRequest().popUint().intValue();//送心数量
        logger.info("consumeHeartMobile parameter idolUid={},num={}", idolUid, num);
        if (num > 0 && idolUid > 0) {
            try {
                int fansNum = heartService.consumeHeart(fansUid.longValue(), idolUid, num);
                if (fansNum >= 0) {
                    TaskbarInfo taskbarInfo = heartService.getTaskbarInfo(idolUid, fansUid.longValue(), false);
                    Result result = Result.newResult().setDataRoot(taskbarInfo).put("fansUid", fansUid.longValue()).put("idolUid", idolUid);
                    logger.info("consumeHeartMobile step 2,result:{}",result);
                    responseMobile(result);
                    notifyService.notifyConsumeHeart(topCh, subCh, idolUid, fansUid.longValue(), num, taskbarInfo);
                } else {
                    responseMobile(Result.newResult(1));
                }
            } catch (Throwable e) {
                logger.warn("consumeHeart error", e);
                responseMobile(Result.newResult(ERROR_UNKNOWN));
            }
        } else {
            responseMobile(Result.newResult(ERROR_ILLEGAL_ARGS));
        }

    }
}
