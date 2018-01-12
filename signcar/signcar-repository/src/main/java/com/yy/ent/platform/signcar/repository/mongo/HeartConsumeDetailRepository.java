package com.yy.ent.platform.signcar.repository.mongo;

import com.yy.ent.platform.modules.mongo.BaseMongoRepository;
import com.yy.ent.platform.signcar.common.mongodb.HeartConsumeDetail;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class HeartConsumeDetailRepository extends BaseMongoRepository<HeartConsumeDetail> {
    public void insert(Long fansUid, Long idolUid, Integer num, Date date) {
        HeartConsumeDetail bean = new HeartConsumeDetail();
        bean.setFansUid(fansUid);
        bean.setIdolUid(idolUid);
        bean.setNum(num);
        bean.setCreateDate(date);
        insert(bean, HeartConsumeDetail.TABLE_NAME + TimeUtil.getYYYYMMDDWeekStart(date));
    }
}
