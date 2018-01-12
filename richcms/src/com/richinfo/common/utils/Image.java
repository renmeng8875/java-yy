package com.richinfo.common.utils;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.imageio.ImageIO;


import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.richinfo.comm.Numeric;
import com.richinfo.common.SystemProperties;
import com.richinfo.file.FileManager;
import com.richinfo.image.ImageS;
import com.richinfo.image.ImageSize;
import com.richinfo.net.HttpClass;

public class Image{
	public static enum IMGSIZE {
			Logo(1), // 成功
			ShotScreen(2), // 无效任务
			AdPhoto(3); // 系统错误
			int key = 0;
			private IMGSIZE(int key) {
				this.key = key;
			}
			public int getKey() {
				return this.key;
			}
	}
	private final Logger log = Logger.getLogger(this.getClass().getName());
	private final StringBuilder m_Speed = new StringBuilder();
	private final DecimalFormat m_Decimal = new DecimalFormat("#0.0");
	private static String staticPath = "";
	
	
	static{
		staticPath = SystemProperties.getInstance().getProperty("fileupload.savePath") ;
	}
	
	public HashMap<String, String> downloadFile(String url, String saveDir,IMGSIZE size,boolean isConvert,String alt,int i) {
		HashMap<String, String> img = null;
		//ImageSize pixels = null;
		//ImageS imgExc = new ImageS();
		File file = new File(staticPath+saveDir);
		if (!file.exists())
			file.mkdirs();
		
		file = new File(staticPath+saveDir+FileManager.getFileName(url)+i+".jpg");
	/*	if (file.exists()) {
			log.log(Level.INFO, "file exists: " + file.getAbsolutePath());
			img = new HashMap<String, String>();
			img.put("path",  SystemProperties.getInstance().getProperty("fileupload.prefix") + saveDir+FileManager.getFileName(url)+i+".jpg");
			//pixels = imgExc.getSize(staticPath+saveDir+FileManager.getFileName(url)+i+".jpg");
			img.put("alt", alt);
//			if(pixels!=null)
//			{
//				img.put("size",pixels.getWidth()+"x"+pixels.getHeight());
//			}else{
				img.put("size","");
//				file.delete();
//			}
			return img;
		}*/
		String tmpFile = "tmp"+FileManager.getFileName(url);
		if(tmpFile.indexOf("?")!=-1){
			tmpFile = tmpFile.substring(0,tmpFile.indexOf("?"));
		}
		file = new File(staticPath+saveDir+tmpFile);
		log.info("url="+url+"||file="+file.getAbsolutePath());
		int times = 0;
		while (true) {
			HttpClass httpClass = new HttpClass();
			long startTime = System.currentTimeMillis();			
			InputStream stream = httpClass.reqStream(url, null, null);
			if (stream != null) {
				FileOutputStream out = null;
				BufferedOutputStream buff = null;
				try {
					out = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					closeStream(stream);
					httpClass.disConnect();
					log.log(Level.ERROR, e.getMessage(), e);
					return img;
				}
				
				buff = new BufferedOutputStream(out);
				
				int len = 0;
				long total = 0;
				byte[] b = new byte[8192];
				
				try {																
					while (true) {
						len = stream.read(b);
						if (len > 0) {
							total += len;
							buff.write(b, 0, len);
						} else
							break;													
					}
				} catch (IOException e) {
					closeStream(buff);
					closeStream(out);
					closeStream(stream);
					httpClass.disConnect();
					
					if (file.exists())
						file.delete();
																					
					log.log(Level.ERROR, e.getMessage(), e);
													
					if (times == 3) {
						return img;
					} else {
						times++;
						continue;
					}									
				}
													
				long endTime = System.currentTimeMillis();
				getDownloadSpeed(url, startTime, endTime, total);
				closeStream(buff);
				closeStream(out);
				closeStream(stream);
				httpClass.disConnect();
				break;
			} else {
				httpClass.disConnect();
				return img;
			}
		}
		//add by zwl
		String filename = FileManager.getFileName(url);
		if(filename.indexOf("?")!=-1){
			filename = filename.substring(0,filename.indexOf("?"));
		}
		if("tiff".equals(filename.substring(filename.lastIndexOf(".")+1, filename.length()))){
			filename = filename+i+"-0";
		}
		filename = filename + i +".jpg";
		if(isConvert)
		{
			switch(size.getKey())
			{
			case 1://logo
				imageReSize(staticPath+saveDir,filename,staticPath+saveDir+tmpFile,140,140,80);
				break;
			case 2://shotscreen
				imageReSize(staticPath+saveDir,filename,staticPath+saveDir+tmpFile,480,320,80);
				break;
			case 3://ad photo
				imageReSize(staticPath+saveDir,filename,staticPath+saveDir+tmpFile,668,300,80);
				break;
			}
		}else{
			URL curl = this.getClass().getClassLoader().getResource("/"); 
			File pfile = new File(curl.getPath());  
			File parentFile = new File(pfile.getParent());  
			String webRoot = parentFile.getParent();
			File destFile = new File(staticPath+saveDir+filename);
			FileManager.copy(file, destFile);
			File waterMask = new File(webRoot+File.separator+SystemProperties.getInstance().getProperty("watermark.sfyyImg"));
			try {
				Thumbnails.of(destFile.getAbsoluteFile()) 
				.watermark(Positions.TOP_CENTER, ImageIO.read(waterMask), 1f) 
				.scale(1.0)
				.toFile(destFile.getAbsoluteFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (file.exists())
			file.delete();
		img = new HashMap<String, String>();
		img.put("path", SystemProperties.getInstance().getProperty("fileupload.prefix") + saveDir+filename);
		img.put("alt", alt);
//		pixels = imgExc.getSize(staticPath+saveDir+filename);
//		if(pixels!=null)
//		{
//			img.put("size",pixels.getWidth()+"x"+pixels.getHeight());
//		}else{
			img.put("size","");
//			file = new File(staticPath+saveDir+filename);
//			if (file.exists())
//				file.delete();
//		}
		/**/
		
		return img;
	}
	public String getNetImgSize(String url) {
		ImageSize pixels = null;
		ImageS imgExc = new ImageS();
		String size="";
		String tmpFilePath = staticPath+"tmp/"+Numeric.rndNumber(1,100000)+FileManager.getFileName(url);
		File file = new File(staticPath+"tmp/");
		if (!file.exists())
			file.mkdirs();
		
		file = new File(tmpFilePath);
		log.info("Download tmp file: " + url);
		int times = 0;
		while (true) {
			HttpClass httpClass = new HttpClass();
			long startTime = System.currentTimeMillis();			
			InputStream stream = httpClass.reqStream(url, null, null);
			if (stream != null) {
				FileOutputStream out = null;
				BufferedOutputStream buff = null;
				try {
					out = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					closeStream(stream);
					httpClass.disConnect();
					log.log(Level.ERROR, e.getMessage(), e);
					return size;
				}
				
				buff = new BufferedOutputStream(out);
				
				int len = 0;
				long total = 0;
				byte[] b = new byte[8192];
				
				try {																
					while (true) {
						len = stream.read(b);
						if (len > 0) {
							total += len;
							buff.write(b, 0, len);
						} else
							break;													
					}
				} catch (IOException e) {
					closeStream(buff);
					closeStream(out);
					closeStream(stream);
					httpClass.disConnect();
					
					if (file.exists())
						file.delete();
																					
					log.log(Level.ERROR, e.getMessage(), e);
													
					if (times == 3) {
						return size;
					} else {
						times++;
						continue;
					}									
				}
													
				long endTime = System.currentTimeMillis();
				getDownloadSpeed(url, startTime, endTime, total);
				closeStream(buff);
				closeStream(out);
				closeStream(stream);
				httpClass.disConnect();
				break;
			} else {
				httpClass.disConnect();
				return size;
			}
		}
		pixels = imgExc.getSize(tmpFilePath);
		if(pixels!=null)
		{
			size = pixels.getWidth()+"x"+pixels.getHeight();
		}
		file.delete();
		return size;
	}
	private void imageReSize(String dir,String filename,String tmpFile,int width,int height,int quality){
		ImageS img = new ImageS();
		img.resize(dir, filename, tmpFile, width, height, quality);
	}
	private void closeStream(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			log.log(Level.ERROR, e.getMessage(), e);
		}
	}
	
	private void closeStream(FileOutputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			log.log(Level.ERROR, e.getMessage(), e);
		}
	}
	
	private void closeStream(BufferedOutputStream stream) {
		try {
			stream.flush();
			stream.close();
		} catch (IOException e) {
			log.log(Level.ERROR, e.getMessage(), e);
		}
	}
	private void getDownloadSpeed(String url, long startTime, long endTime, long total) {
		if (endTime > startTime && total > 0) {
			double length = (double)total / (double)1024;
			double useTime = (double)(endTime - startTime) / (double)1000;															
			double speed = length / useTime;																
			
			m_Speed.setLength(0);
			m_Speed.append(url);
			m_Speed.append("  ");
			m_Speed.append(m_Decimal.format(length));
			m_Speed.append("k");
			m_Speed.append("  ");																
			m_Speed.append(m_Decimal.format(useTime));
			m_Speed.append("s");
			m_Speed.append("  ");
			m_Speed.append(m_Decimal.format(speed));
			m_Speed.append("k/s");
			
			log.log(Level.INFO, m_Speed.toString());
		}
	}
}
