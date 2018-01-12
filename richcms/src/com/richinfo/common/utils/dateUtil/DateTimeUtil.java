package com.richinfo.common.utils.dateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 时间处理类 功能描述：
 * 
 */
public class DateTimeUtil
{

	
    public DateTimeUtil()
    {
    }

    public static String getDateBeforeDay(Date dateTime, int day, String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(5, calendar.get(5) - day);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getDateAfterDay(Date dateTime, int day, String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(5, calendar.get(5) + day);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getDayBeforeDay(Date dateTime, int day, String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(5, calendar.get(5) - day);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getDayAfterDay(Date dateTime, int day, String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(5, calendar.get(5) + day);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getMonthAfterDay(Date dateTime, int month,
        String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(2, calendar.get(2) + month);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getMonthBeforeDay(Date dateTime, int month,
        String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(2, calendar.get(2) - month);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getSecondBeforeSecond(Date dateTime, int second,
        String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(13, calendar.get(13) - second);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String getSecondAfterSecond(Date dateTime, int second,
        String format)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(13, calendar.get(13) + second);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    /**
     * 获取指定格式的当前时间
     * @param format
     * @return
     */
    public static String getNowDateTime(String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }
    
   

    public static String getFormatDateTime(Date date, String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间的秒数 1970/01/01至今的秒数
     * @param date
     * @return
     * @throws Exception
     */
    public static long getTimeStamp()
    {
        long stamp = 0L;
        Date date1 = new Date();
        Date date2 = null;
		try {
			date2 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).parse("1970/01/01 08:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
        stamp = (date1.getTime() - date2.getTime()) / 1000L;
        return stamp;
    }

    /**
     * 时间转换成秒 1970/01/01至今的秒数
     * @param date
     * @return
     * @throws Exception
     */
    public static long getTimeStamp(Date date) 
    {
        long stamp = 0L;
        Date date1 = date;
        Date date2 = null;
		try {
			date2 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).parse("1970/01/01 08:00:00");
			stamp = (date1.getTime() - date2.getTime()) / 1000L;
		} catch (ParseException e) {
			stamp = 0L;
		}
        
        return stamp;
    }

    /**
     * 将时间由秒转换成指定格式，如：yyyy-MM-dd HH:mm:ss
     * @param second
     * @param format
     * @return
     * @throws Exception
     */
    public static String getTimeStamp(Long second, String format)
    {
    	if(second==null||second==0){
    		return "";
    	}
        Date da = null;
		try {
			da = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1970-01-01 08:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Date date = new Date(da.getTime() + second * 1000L);
        return (new SimpleDateFormat(format)).format(date);
    }

    public static String getFormatDateTime(String date, String from, String to)
        throws Exception
    {
        Date da = (new SimpleDateFormat(from)).parse(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(to);
        return dateFormat.format(da);
    }

    public static Date getDateFromStr(String dateStr, String format)
        throws ParseException
    {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(dateStr);
    }
    
    /**
     * @param format yyyy-MM-dd HH:mm:ss
     * @param target
     * @return
     */
    public static long converToTimestamp(String target,String format)
    {
    	 SimpleDateFormat sdf=new SimpleDateFormat(format);//小写的mm表示的是分钟  
         try {
             return sdf.parse(target).getTime()/1000;
         } catch (ParseException e) {
             e.printStackTrace();
             return 0;
         }  
    }
    
    /**
     * 将时间由秒转换成指定格式，如：yyyy-MM-dd HH:mm:ss
     * @param second
     * @param format
     * @param isNullAllow
     * @return
     * @throws Exception
     */
    public static String getTimeStamp(Long second, String format,boolean isNullAllow)
    {
    	if(!isNullAllow && second==null){
    		return "";
    	}
    	
    	if(second==null){
    		second = 0L;
    	}
    	
        Date da = null;
		try {
			da = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1970-01-01 08:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Date date = new Date(da.getTime() + second * 1000L);
        return (new SimpleDateFormat(format)).format(date);
    }
    
   
    public static String getDataStrFromTimeStamp(String timestamp,String format){
    	  
    	SimpleDateFormat sdf=new SimpleDateFormat(format);  
    	  
    	String sd = sdf.format(new Date(Long.parseLong(timestamp)*1000));  
    	  
    	return sd;
    }
    
    public static boolean isValidDate(String source,String format){
    	if(StringUtils.isEmpty(source))
    		return true;
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);
            dateFormat.parse(source);
            return true;
		} catch (Exception e) {
			return false;
		}
    }
  
}
