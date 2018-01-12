package com.yy.ent.platform.signcar.common.util;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

/**
 * HashUtil
 *
 * @author suzhihua
 * @date 2015/12/28.
 */
public class HashUtil {
    public static int hashLong(long num, int range) {
        int i = Hashing.murmur3_32().hashLong(num).hashCode();
        i = i % range;
        return i < 0 ? i + range : i;
    }

    public static int hashString(String v, int range) {
        int i = Hashing.murmur3_32().hashString(v, Charset.defaultCharset()).hashCode();
        i = i % range;
        return i < 0 ? i + range : i;
    }
}
