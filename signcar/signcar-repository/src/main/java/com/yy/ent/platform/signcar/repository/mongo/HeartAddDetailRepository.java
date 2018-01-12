package com.yy.ent.platform.signcar.repository.mongo;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.HeartAddDetail;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class HeartAddDetailRepository extends BaseMongoRepository<HeartAddDetail> {
    public void insert(Long fansUid, Integer num, Date date, String serial) {
        HeartAddDetail bean = new HeartAddDetail();
        bean.setFansUid(fansUid);
        bean.setNum(num);
        bean.setCreateDate(date);
        bean.setSerial(serial);
        insert(bean, HeartAddDetail.TABLE_NAME + TimeUtil.getYYYYMM());
    }
}
