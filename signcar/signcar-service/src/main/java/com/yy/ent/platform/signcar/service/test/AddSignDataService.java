package com.yy.ent.platform.signcar.service.test;

import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.heart.HeartService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AddSignDataService
 *
 * @author suzhihua
 * @date 2016/3/2.
 */
@Service
public class AddSignDataService extends BaseService {
    private ClassPathResource resource = new ClassPathResource("sign.log");
    @Autowired
    private HeartService heartService;

    public List<String> getData(int num) {
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            List<String> strings = IOUtils.readLines(inputStream);
            if (num <= 0 || num > strings.size()) {
                return strings;
            }
            return strings.subList(0, num);
        } catch (Throwable e) {
            logger.warn("read sign.log error", e);
            return Collections.emptyList();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }

    public void addData(final int num) {
        List<String> data = getData(-1);
        final int size = data.size();
        logger.info("data.size={}", size);
        ExecutorService pool = Executors.newFixedThreadPool(100);
        final AtomicInteger errorNum = new AtomicInteger();
        int i = 0;
        for (final String s : data) {
            i++;
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        heartService.addHeart(Long.parseLong(s), num);
                    } catch (Throwable e) {
                        errorNum.incrementAndGet();
                        logger.warn("addHeart error,uid: {}", s);
                    }
                }
            });
            try {
                if (i % 5000 == 0) {
                    logger.warn("index={},TimeUnit.SECONDS.sleep(2)", i);
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (Throwable e) {
                logger.warn("TimeUnit.SECONDS.sleep error");
            }
        }
        pool.shutdown();
        while (!pool.isTerminated()) ;
        logger.info("addHeart over,errorNum={}", errorNum.get());
    }
}
