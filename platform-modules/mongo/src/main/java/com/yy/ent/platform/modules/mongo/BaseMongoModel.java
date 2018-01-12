package com.yy.ent.platform.modules.mongo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.Document;

import java.io.Serializable;

/**
 * BaseMongoModel
 *
 * @author suzhihua
 * @date 2015/11/3.
 */
public class BaseMongoModel implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void valueOf(Document doc) {
        id = doc.getObjectId("_id").toHexString();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
