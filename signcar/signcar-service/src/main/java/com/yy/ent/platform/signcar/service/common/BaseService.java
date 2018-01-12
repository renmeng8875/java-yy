package com.yy.ent.platform.signcar.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final static String FASTCACHE = "fast-cache";
    protected final static String MIDCACHE = "mid-cache";
    protected final static String SLOWCACHE = "slow-cache";
}
