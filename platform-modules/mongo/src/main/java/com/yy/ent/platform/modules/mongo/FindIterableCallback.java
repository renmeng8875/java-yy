package com.yy.ent.platform.modules.mongo;

import com.mongodb.client.FindIterable;
import org.bson.Document;

/**
 * FindIterableCallback
 *
 * @author suzhihua
 * @date 2015/12/23.
 */
public interface FindIterableCallback {
    void doInFindIterable(FindIterable<Document> findIterable);
}
