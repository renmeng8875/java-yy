package com.richinfo.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
* 类说明:此处不要使用jdk自身的api，无法解决中文乱码问题，
* 使用apache的ant jar来修改jdk自身的bug      
* @mailTo renmeng@richinfo.cn
* @copyright richinfo 
* @author renmeng  
* @date 2014-7-1
 */
public class ZipUtil {
	public static void ZipFiles(File zip,String path,File... srcFiles) throws IOException
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
		ZipFiles(out,path,srcFiles);
		out.close();
		System.out.println("*****************压缩完毕*******************");
	}
	
	public static void ZipFiles(ZipOutputStream out,String path,File... srcFiles)
	{
		path = path.replaceAll("\\*", "/");
		if(!path.endsWith("/"))
		{
			path+="/";
		}
		byte[] buf = new byte[1024];
		try {
			for(int i=0;i<srcFiles.length;i++){
				if(srcFiles[i].isDirectory()){
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if(!srcPath.endsWith("/")){
						srcPath+="/";
					}
					out.putNextEntry(new ZipEntry(path+srcPath));
					ZipFiles(out,path+srcPath,files);
				}
				else{
					FileInputStream in = new FileInputStream(srcFiles[i]);
					System.out.println(path + srcFiles[i].getName());
					out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
					int len;
					while((len=in.read(buf))>0){
						out.write(buf,0,len);
					}
					out.closeEntry();
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void unZipFiles(String zipPath,String descDir)throws IOException
	{
		unZipFiles(new File(zipPath), descDir);
	}
	
	
	public static void unZipFiles(File zipFile,String descDir)throws IOException
	{
		File pathFile = new File(descDir);
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for(Enumeration<?> entries = zip.getEntries();entries.hasMoreElements();)
		{
			ZipEntry entry = (ZipEntry)entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream( entry);
			String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
			//判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if(!file.exists())
			{
				file.mkdirs();
			}
			//判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if(new File(outPath).isDirectory())
			{
				continue;
			}
			
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while((len=in.read(buf1))>0)
			{
				out.write(buf1,0,len);
			}
			in.close();
			out.close();
		}
		zip.close();
		zip = null;
	}
	
	public static void main(String[] args) throws IOException 
	{
		
		File zipFile = new File("F:/test/activity.zip");
		String path = "F:/test/";
		unZipFiles(zipFile, path);
	}
	
}