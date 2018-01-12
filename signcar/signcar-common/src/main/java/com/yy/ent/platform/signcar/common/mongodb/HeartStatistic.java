package com.yy.ent.platform.signcar.common.mongodb;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;

@Document(collection = "heartStatistic")
@CompoundIndexes({@CompoundIndex(def = "{idolUid:1,statDate:1}"),@CompoundIndex(def = "{statDate:1}")})
public class HeartStatistic extends BaseMongoModel{

    private static final long serialVersionUID = -3436033194798715890L;

    public HeartStatistic(Long idolUid){
        this.idolUid = idolUid;
    }
    public HeartStatistic(){
        
    }
    private Long idolUid;
    
    private String statDate;
    
    private Long fansNum;
    
    private Long heartNum;
    
    private Long coverNum;
    
    private Double rank;

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public Long getIdolUid() {
        return idolUid;
    }

    public void setIdolUid(Long idolUid) {
        this.idolUid = idolUid;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Long getFansNum() {
        return fansNum;
    }

    public void setFansNum(Long fansNum) {
        this.fansNum = fansNum;
    }

    public Long getHeartNum() {
        return heartNum;
    }

    public void setHeartNum(Long heartNum) {
        this.heartNum = heartNum;
    }

    public Long getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(Long coverNum) {
        this.coverNum = coverNum;
    }

}
