package com.yy.ent.platform.core.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * FileUtil
 *
 * @author suzhihua
 * @date 2015/7/29.
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 加载配置文件
     *
     * @param absoluteFilePath
     * @return 异常时只打印错误, 不抛异常
     */
    public static Properties loadProperties(String absoluteFilePath) {
        Properties prop = new Properties();
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(new File(absoluteFilePath));
            if (absoluteFilePath.toLowerCase().endsWith(".xml")) {
                prop.loadFromXML(inStream);
            } else {
                prop.load(inStream);
            }
        } catch (IOException e) {
            logger.warn("load " + absoluteFilePath + " error", e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
        return prop;
    }

    /**
     * 加载配置文件
     *
     * @param resource
     * @return 异常时只打印错误, 不抛异常
     */
    public static Properties loadResource(Resource resource) {
        String path = null;
        try {
            path = resource.getURL().getPath();
        } catch (IOException e) {
            logger.warn("load " + resource.getFilename() + " error", e);
            return new Properties();
        }
        return loadProperties(path);
    }
}
