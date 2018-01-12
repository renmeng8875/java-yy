package com.yy.ent.platform.signcar.common.constant;

public enum CacheKeyConstant {
   
    WEBDB_NICK,
    WEBDB_HEAD,
    WEBDB_CHID,
    WEBDB_YYNO,
    IS_LIVING_ROOM,
    CARLEVEL;

    private static final String PREV_KEY = "signcar_";
    private String name;

    CacheKeyConstant() {
        name = PREV_KEY + name().toLowerCase();
    }

    CacheKeyConstant(String name) {
        this.name = PREV_KEY + name.toLowerCase();
    }

    public String getKey() {
        return name;
    }

    public String getKey(String key) {
        return name + "_" + key;
    }
    
    public String getKey(int key) {
        return name + "_" + key;
    }
}
