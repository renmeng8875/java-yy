package com.yy.ent.platform.signcar.common.mongodb;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 个人所拥入有心的记录
 */
@Document(collection = HeartTotal.TABLE_NAME)
@CompoundIndexes({@CompoundIndex(def = "{fansUid:1}")})
public class HeartTotal extends BaseMongoModel {
    public static final String TABLE_NAME = "heartTotal";
    private Long fansUid;
    private Integer num;
    private Date lastUpdate;

    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Integer getNum() {
        if (num == null) return 0;
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
