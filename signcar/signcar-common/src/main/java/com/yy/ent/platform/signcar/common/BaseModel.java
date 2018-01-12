package com.yy.ent.platform.signcar.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * BaseModel
 *
 * @author suzhihua
 * @date 2015/8/21.
 */
public class BaseModel implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
