package com.yy.ent.platform.signcar.service.common;

import com.yy.ent.commons.base.dto.OrderMapProperty;
import com.yy.ent.commons.base.dto.Property;
import com.yy.ent.external.service.WebdbHalbService;
import com.yy.ent.platform.signcar.common.constant.CacheKeyConstant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * WebdbService
 *
 * @author suzhihua
 * @date 2015/11/11.
 */
@Service
public class WebdbService extends BaseService {
    @Autowired
    private WebdbHalbService webdbHalbService;

    @Autowired
    private LocalCacheService cacheService;

    private static final String DEFAULTLOGO = "http://c1.web.yy.com/img/avatar/10001.png";


    /**
     * 根据频道id查主播uid
     *
     * @param chid
     * @return
     */
    public String getUid(String chid) {
        String key = CacheKeyConstant.WEBDB_CHID.getKey(chid);
        String owUid = (String) cacheService.get(MIDCACHE, key);
        if (StringUtils.isEmpty(owUid)) {
            int i = chid.indexOf("_");
            if (i > -1) {
                chid = chid.substring(0, i);
            }
            try {
                Property p = webdbHalbService.getChannelInfoBySid(chid);
                owUid = p.get("ch_owner_id");
                logger.debug("get uid by chid from webdb,{}={}", chid, owUid);
            } catch (Exception e) {
                logger.warn("webdb error:chid={}", chid, e);
            }
            if (StringUtils.isBlank(owUid)) {
                owUid = "0";
            } else {
                cacheService.add(MIDCACHE, key, owUid);
            }
        }
        return owUid;
    }

    public String getNice(String uid) {
        String key = CacheKeyConstant.WEBDB_NICK.getKey(uid);
        String fansName = (String) cacheService.get(MIDCACHE, key);
        if (StringUtils.isBlank(fansName)) {
            try {
                fansName = webdbHalbService.getUserNickByUid(uid);
                logger.debug("get nickname from webdb,{}={}", uid, fansName);
            } catch (Exception e) {
                logger.warn("get nickname by uid from webdb error:uid={}", uid, e);
            }
            if (StringUtils.isBlank(fansName)) {
                fansName = "佚名";
            } else {
                cacheService.add(MIDCACHE, key, fansName);
            }
        }
        return fansName;
    }

    public String getHead(String uid) {
        String key = CacheKeyConstant.WEBDB_HEAD.getKey(uid);
        String head = (String) cacheService.get(MIDCACHE, key);
        if (StringUtils.isBlank(head)) {
            try {
                OrderMapProperty orderMapProperty = webdbHalbService.listUserInfoByUidsNew(Collections.singletonList(uid));
                Property property = orderMapProperty.get(uid);
                String logoIndex = property.get("logoIndex");
                String im_logo = property.get("im_logo");
                if ("-1".equals(logoIndex)) {
                    head = DEFAULTLOGO;
                } else if ("0".equals(logoIndex)) {
                    head = im_logo;
                } else {
                    head = "http://c1.web.yy.com/img/avatar/" + logoIndex + ".png";
                }
                logger.debug("get Head from webdb,{}={}", uid, head);
            } catch (Exception e) {
                logger.warn("get head by uid from webdb error:uid={}", uid, e);
            }
            if (StringUtils.isBlank(head)) {
                head = DEFAULTLOGO;
            }
            cacheService.add(MIDCACHE, key, head);
        }
        return head;
    }
    
    public String getYYNo(Long uid){
        String key = CacheKeyConstant.WEBDB_YYNO.getKey(String.valueOf(uid));
        String yyno = (String) cacheService.get(MIDCACHE, key);
        if(StringUtils.isBlank(yyno)){
            try {
                yyno = webdbHalbService.getYYNumByUid(String.valueOf(uid));
            } catch (Exception e) {
                logger.warn("get yyno by uid from webdb error:uid={}", uid, e);
                yyno = "-1";
            }
        }
        if (StringUtils.isNotBlank(yyno)) {
            cacheService.add(MIDCACHE, key, yyno);
        }
        
        return yyno;
    }
}
