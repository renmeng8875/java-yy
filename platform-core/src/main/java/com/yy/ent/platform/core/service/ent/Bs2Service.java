package com.yy.ent.platform.core.service.ent;

import com.duowan.udb.util.string.StringUtil;
import com.yy.cs.bs2.client.AppInfo;
import com.yy.cs.bs2.client.Bs2Client;
import com.yy.cs.bs2.client.Bs2ClientUtil;
import com.yy.ent.clients.dfs.util.CompressImage;
import com.yy.ent.clients.dfs.util.DfsUtil;
import com.yy.ent.commons.base.LoaderConfig;
import com.yy.ent.commons.base.valid.BlankUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * bs2 device
 *
 * @author:<a href="mailto:laiguangquan@yy.com">Alex Lai</a>
 * @created 2014年6月10日 上午11:46:51
 * @version:v1.00
 */
public class Bs2Service implements InitializingBean{

	private Logger logger = LoggerFactory.getLogger(Bs2Service.class);

	private Bs2Client bs2Client = null;

    private Resource filePath;

    public void setFilePath(Resource filePath) {
        this.filePath = filePath;
    }


    public void afterPropertiesSet() throws Exception {
        init(filePath.getURL().getPath());
    }
    public void init(String filePath) throws Exception{
		LoaderConfig.loadConfig(filePath);
		Properties cfg = LoaderConfig.getConfiguration();

		String bucket_name = cfg.getProperty("bucket_name");
		String ak  = cfg.getProperty("ak");
		String sk = cfg.getProperty("sk");

		AppInfo appInfo = new AppInfo(bucket_name, sk, ak);
		bs2Client = new Bs2Client(appInfo);
	}

	public Bs2Client getClient() throws Exception{
		if(null == bs2Client){
			logger.error("Bs2Service getClient  Not yet init.");
			throw new IllegalAccessError("Bs2Service Not yet init.");
		}
		return bs2Client;
	}


	 /**
     * 获取图片扩展
     * @param fileName
     * @return
     */
    public static String getPhotoExt(String fileName){
        String extName = "jpg";
        if(!StringUtils.isEmpty(fileName)){
            extName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
        }
        return extName.toLowerCase();
    }

    /**
     * 上传大小图
     * @param source
     * @param fileName
     * @param smallInfos
     * @return
     * @throws Exception
     */
	public Map<String,Map<Integer, String>> uploadPhoto(byte[] source,String fileName,Map<Integer, String> smallInfos)throws Exception{
        String originalPath = uploadPhoto(source,fileName);
        Map<String,Map<Integer, String>> result = new HashMap<String, Map<Integer,String>>();
        if(!BlankUtil.isBlank(originalPath)){
            Map<Integer, String> filePaths = uploadMinPhoto(source,originalPath,smallInfos);
            result.put(originalPath, filePaths);
        }
        return result;
    }

	/**
	 * 上传图片不带小图
	 * @param source
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String uploadPhoto(byte[] source,String fileName)throws Exception{
	    String bigFileExtName = getPhotoExt(fileName);
        boolean flag = false;
        if(bigFileExtName.equalsIgnoreCase("bmp")){
            bigFileExtName = "jpg";
            flag = true;
        }
        InputStream suitIn = new ByteArrayInputStream(source);
        byte[] suitBuff = CompressImage.compress(suitIn, bigFileExtName , DfsUtil.MAX_SIZE, CompressImage.QUALITY_DEFAULT,flag);
        String originalPath = getClient().uploadMinFileData(suitBuff==null?source:suitBuff, fileName,"image/"+bigFileExtName, false);
        return originalPath;
	}

    /**
     * 上传文件
     * @param source
     * @param fileName
     * @param mime
     * @return
     * @throws Exception
     */
    public String uploadFile(byte[] source,String fileName,String mime)throws Exception{
        String originalPath = getClient().uploadMinFileData(source, fileName,mime, false);
        return originalPath;
    }
    
    /**删除文件
     * @param fileName
     * @return
     * @throws Exception
     * @author suzhihua
     * @date 2015年3月26日 上午10:40:15
     */
    public boolean deleteFile(String fileName)throws Exception{
        int lastIndexOf = fileName.lastIndexOf("/");
        if (lastIndexOf != -1) {
            fileName = fileName.substring(lastIndexOf + 1);
        }
        boolean res = getClient().deleteFile(fileName);
        return res;
    }

	/**
	 * 只上传小图
	 * @param source
	 * @param originalPath
	 * @param smallInfos
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, String> uploadMinPhoto(byte[] source,String originalPath,Map<Integer, String> smallInfos)throws Exception{
        String fileName = originalPath.substring(originalPath.lastIndexOf("/")+1,originalPath.lastIndexOf("."));
	    Map<Integer, String> filePaths = new HashMap<Integer, String>();
	    if(BlankUtil.isBlank(smallInfos)){
	        return filePaths;
	    }
	    String fileExtName = "jpg";
        for (int smallSize : smallInfos.keySet()) {
            InputStream miniIn = new ByteArrayInputStream(source);
            byte[] miniBuff;
            String compressType = smallInfos.get(smallSize);
            if(DfsUtil.COMPRESS_TYPE_SQUARE.equals(compressType)){
                miniBuff = CompressImage.compressGeometric(miniIn, fileExtName, smallSize, CompressImage.QUALITY_DEFAULT,true);
            }else{
                miniBuff = CompressImage.compress(miniIn, fileExtName, smallSize, CompressImage.QUALITY_DEFAULT,true);
            }

            String miniPath =  getClient().uploadMinFileFixName(miniBuff, fileName + "_" + smallSize+"."+fileExtName,"image/"+fileExtName, false);
            logger.info("file_length:" + source.length + " bytes ,mini_file_length:"+miniBuff.length+",miniPath:" + miniPath);
            filePaths.put(smallSize, miniPath);
        }
        return filePaths;
	}

	  public String uploadLargeFile(byte[] source,String fileName,String mime)throws Exception{
	        String originalPath = getClient().blockUpload(source,"", fileName, mime) ;
	        if (StringUtil.isEmpty(originalPath)) {
	        	return null ;
	        }
	        return  Bs2ClientUtil.generateDownLoadURI(getClient().getAppInfo().getBucketName(), originalPath);
	    }
	
	public static void main(String[] args) {
	    long l = System.currentTimeMillis();
        String primaryKey = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(primaryKey+"      "+(System.currentTimeMillis() - l));
    }
}
