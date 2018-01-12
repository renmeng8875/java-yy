package com.yy.ent.platform.signcar.common.mongodb;

import java.util.Date;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;

@Document(collection = "exchangeDetail")
@CompoundIndexes({@CompoundIndex(def = "{uid:1}")})
public class ExchangeDetail extends BaseMongoModel{

    private Long preGiftBizId;
    
    private Long uid;
    
    private Integer preNum;
    
    private Long afterGiftBizId;
    
    private Integer afterNum;
    
    private Integer source;
    
    private Date createTime;
    
    private Long activityId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getPreGiftBizId() {
        return preGiftBizId;
    }

    public void setPreGiftBizId(Long preGiftBizId) {
        this.preGiftBizId = preGiftBizId;
    }

    public Integer getPreNum() {
        return preNum;
    }

    public void setPreNum(Integer preNum) {
        this.preNum = preNum;
    }

    public Long getAfterGiftBizId() {
        return afterGiftBizId;
    }

    public void setAfterGiftBizId(Long afterGiftBizId) {
        this.afterGiftBizId = afterGiftBizId;
    }

    public Integer getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(Integer afterNum) {
        this.afterNum = afterNum;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }


}
