package com.yy.ent.platform.signcar.common.mongodb;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userPrize")
@CompoundIndexes({@CompoundIndex(def = "{uid:1,prizeType:1,activityId:1}")})
public class UserPrize extends BaseMongoModel {

    private Long uid;
    
    //1为车碎片，2为入队卡
    private Integer prizeType;
    
    private Integer totalNum;

    private Integer consumeNum;
    
    private Long activityId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

  
    
    public Integer getConsumeNum() {
        return consumeNum;
    }

    public void setConsumeNum(Integer consumeNum) {
        this.consumeNum = consumeNum;
    }

}
