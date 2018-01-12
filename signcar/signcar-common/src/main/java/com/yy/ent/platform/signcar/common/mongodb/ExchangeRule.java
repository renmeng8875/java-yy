package com.yy.ent.platform.signcar.common.mongodb;

import com.yy.ent.platform.modules.mongo.BaseMongoModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "exchangeRule")
public class ExchangeRule extends BaseMongoModel{
    
    private String ruleName;
    
    private Long preGiftBizId;
    
    private Integer preNum;
    
    private Long afterGiftBizId;
    
    private Integer afterNum;
    
    private Integer type;
    
    private String invokeClass;
    
    private Date startTime;
    
    private Date endTime;
    
    private Integer status;

    private Integer limitNum;

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getPreGiftBizId() {
        return preGiftBizId;
    }

    public void setPreGiftBizId(Long preGiftBizId) {
        this.preGiftBizId = preGiftBizId;
    }

    public Integer getPreNum() {
        if(preNum == null)return 0;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInvokeClass() {
        return invokeClass;
    }

    public void setInvokeClass(String invokeClass) {
        this.invokeClass = invokeClass;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    

}
