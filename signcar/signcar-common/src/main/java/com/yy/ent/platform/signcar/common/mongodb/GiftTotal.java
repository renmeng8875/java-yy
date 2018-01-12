package com.yy.ent.platform.signcar.common.mongodb;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;

@Document(collection = "giftTotal")
@CompoundIndexes({@CompoundIndex(def = "{uid:1,giftBizId:1}")})
public class GiftTotal extends BaseMongoModel{

    private Long giftBizId;
    
    private Integer num;
    
    private Integer consumeNum;
    
    private Long uid;

    public Long getGiftBizId() {
        return giftBizId;
    }

    public void setGiftBizId(Long giftBizId) {
        this.giftBizId = giftBizId;
    }

    public Integer getNum() {
        if(num == null)return 0;
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getConsumeNum() {
        if(consumeNum == null)return 0;
        return consumeNum;
    }

    public void setConsumeNum(Integer remainNum) {
        this.consumeNum = remainNum;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    
}
