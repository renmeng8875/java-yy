package com.yy.ent.platform.signcar.common.mongodb;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 个人送心的记录表
 */
@Document(collection = HeartConsumeDetail.TABLE_NAME)
@CompoundIndexes({@CompoundIndex(def = "{fansUid:1,createDate:-1}"), @CompoundIndex(def = "{createDate:-1}")})
public class HeartConsumeDetail extends BaseMongoModel {
    public static final String TABLE_NAME = "heartConsumeDetail";
    private Long fansUid;
    private Long idolUid;
    private Integer num;
    private Date createDate;

    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Long getIdolUid() {
        return idolUid;
    }

    public void setIdolUid(Long idolUid) {
        this.idolUid = idolUid;
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
}
