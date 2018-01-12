package com.yy.ent.platform.core.mybatis;

/**
 * DbHash，按hash值索引
 *
 * @author suzhihua
 * @date 2015/9/7.
 */
public class DbHash  implements IDbIndex {
    public final static String TYPE = "hash";

    /**
     * from cherroot，以达到兼容
     * @param obj
     * @return
     */
    @Override
    public Long getDbIndex(Object obj) {
        String hashStr = obj.toString();
        long hash = 5381;
        for (int i = 0; i < hashStr.length(); i++) {
            hash = ((hash << 5) + hash) + hashStr.charAt(i);
            hash = hash & 0xFFFFFFFFl;
        }
        return hash;
    }
}
