package com.yy.ent.platform.signcar.common.mongodb;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 个人增加心的记录表
 */
@Document(collection = HeartAddDetail.TABLE_NAME)
@CompoundIndexes({@CompoundIndex(def = "{fansUid:1}")})
public class HeartAddDetail extends BaseMongoModel {
    public static final String TABLE_NAME = "heartAddDetail";
    private Long fansUid;
    private Integer num;
    private Date createDate;
    private String serial;
    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
