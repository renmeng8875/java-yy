package com.yy.ent.platform.signcar.common.util;

import com.yy.ent.commons.base.valid.BlankUtil;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xieyong on 2014/10/17.
 */
public class TimeUtil {

    /**
     * 获得当前日期 日期格式为 yyyyMMdd
     *
     * @return
     */

    private static Logger logger = Logger.getLogger(TimeUtil.class);

    public static long formatDateToUTCTime(String dateTime) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(dateTime);
        return date.getTime();
    }

    public static String getYYYYMM() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }

    public static String getYYYYMMDDWeekStart() {
        Calendar c = Calendar.getInstance();
        return getYYYYMMDDWeekStart(c);
    }

    public static String getYYYYMMDDWeekStart(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return getYYYYMMDDWeekStart(c);
    }

    /**
     * 周一到周日都返回这周一的日期
     *
     * @param c
     * @return
     */
    public static String getYYYYMMDDWeekStart(Calendar c) {
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String getYYYYMMDD() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String getYYYYMMDDHHMM() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(date);
    }

    public static String getYYYYMMDDHH() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        return sdf.format(date);
    }

    public static String getyyyyMMddHHmmss() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }


    /**
     * 格式化为时间格式 为yyyy-MM-dd HH:mm:ss的日期
     *
     * @return
     */
    public static Date parseTimeDate(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(time);
    }

    /**
     * 格式化为时间格式 为yyyy-MM-dd 的日期
     *
     * @return
     */
    public static Date parseDate(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(time);
    }

    /**
     * 格式化时间为 yyyy-MM-dd格式
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 格式化时间为 yyyyMMdd格式
     *
     * @param date
     * @return
     */
    public static String formatYYYYMMDD(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    public static String formatYYYYMMDDHH(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        return formatter.format(date);
    }


    public static String formatTimeEx(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }


    public static Date getWeekDateLater() {
        Date d = new Date();
        d.setTime(d.getTime() + 1000 * 60 * 60 * 24 * 7);
        return d;
    }


    /**
     * 获得两个时间之间的相差的分钟数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getMinutesBetween(Date d1, Date d2) {

        long diff = d1.getTime() - d2.getTime();
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long min = diff / nm;//计算差多少分钟
        return min;

    }

    public static String getBeforeTime(long beforeMillTime) {
        Long beforeMillis = System.currentTimeMillis() - beforeMillTime;
        SimpleDateFormat formatUtil = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = formatUtil.format(beforeMillis);
        logger.info("before time is: " + nowTime);
        return nowTime;
    }

    public static String getBeforeWeekTime() {
        Long beforeMillis = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000;
        SimpleDateFormat formatUtil = new SimpleDateFormat("yyyyMMddHHmm");
        String nowTime = formatUtil.format(beforeMillis);
        logger.info("before time is: " + nowTime);
        return nowTime;
    }

    public static Date getCurrentDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.getTime();
    }


    public static String getDateFromNow(int n, String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n);
        Date d = cal.getTime();
        SimpleDateFormat formatUtil = new SimpleDateFormat(format);
        String nowTime = formatUtil.format(d);
        return nowTime;
    }


    public static String formatLongToTimeStr(Long date) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = date.intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = "";

        if (hour < 10) {
            strtime += "0" + hour + ":";
        } else {
            strtime += hour + ":";
        }

        if (minute < 10) {
            strtime += "0" + minute + ":";
        } else {
            strtime += minute + ":";
        }

        if (second < 10) {
            strtime += "0" + second + "";
        } else {
            strtime += second + "";
        }


        return strtime;

    }

    public static String formatChinaDate(String date) throws Exception {
        if (!BlankUtil.isBlank(date)) {
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            String day = date.substring(8, 10);
            return year + "年" + month + "月" + day + "日";
        } else {
            return "";
        }

    }


}
