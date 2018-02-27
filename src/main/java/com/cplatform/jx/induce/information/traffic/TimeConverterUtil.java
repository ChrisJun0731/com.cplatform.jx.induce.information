package com.cplatform.jx.induce.information.traffic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * 时间格式转换工具类(utc时间和本地时间两者的转换)
 * @author guoyangyang
 *
 */
public class TimeConverterUtil {

    private static Logger logger = Logger.getLogger(TimeConverterUtil.class);

    /**
     * 函数功能描述:UTC时间转本地时间格式
     * @param utcTime UTC时间
     * @param utcTimePatten UTC时间格式
     * @param localTimePatten   本地时间格式
     * @return 本地时间格式的时间
     * eg:utc2Local("2017-06-14 09:37:50.788+08:00", "yyyy-MM-dd HH:mm:ss.SSSXXX", "yyyy-MM-dd HH:mm:ss.SSS")
     */
    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));//时区定义并进行时间获取
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    /**
     * 函数功能描述:UTC时间转本地时间格式
     * @param utcTime UTC时间
     * @param localTimePattern 本地时间格式(要转换的本地时间格式)
     * @return 本地时间格式的时间
     */
    public static String utc2Local(String utcTime, String localTimePattern){
        String utcTimePattern = "yyyy-MM-dd";
        String subTime = utcTime.substring(10);//UTC时间格式以 yyyy-MM-dd 开头,将utc时间的前10位截取掉,之后是含有多时区时间格式信息的数据

        //处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
        if(subTime.indexOf("+") != -1){
            subTime = changeUtcSuffix(subTime, "+");
        }
        if(subTime.indexOf("-") != -1){
            subTime = changeUtcSuffix(subTime, "-");
        }
        utcTime = utcTime.substring(0, 10) + subTime;

        //依据传入函数的utc时间,得到对应的utc时间格式
        //步骤一:处理 T
        if(utcTime.indexOf("T") != -1){
            utcTimePattern = utcTimePattern + "'T'";
        }

        //步骤二:处理毫秒SSS
        if(utcTime.indexOf(".") != -1){
            utcTimePattern = utcTimePattern + " HH:mm:ss.SSS";
        }else{
            utcTimePattern = utcTimePattern + " HH:mm:ss";
        }

        //步骤三:处理时区问题
        if(subTime.indexOf("+") != -1 || subTime.indexOf("-") != -1){
            utcTimePattern = utcTimePattern + "XXX";
        }
        else if(subTime.indexOf("Z") != -1){
            utcTimePattern = utcTimePattern + "'Z'";
        }

        if("yyyy-MM-dd HH:mm:ss".equals(utcTimePattern) || "yyyy-MM-dd HH:mm:ss.SSS".equals(utcTimePattern)){
            return utcTime;
        }

        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePattern);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUtcDate = null;
        try {
            gpsUtcDate = utcFormater.parse(utcTime);
        } catch (Exception e) {
            logger.error("utcTime converter localTime failed!!!", e);
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePattern);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUtcDate.getTime());
        return localTime;
    }

    /**
     * 函数功能描述:修改时间格式后缀
     * 函数使用场景:处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
     * @param subTime
     * @param sign
     * @return
     */
    private static String changeUtcSuffix(String subTime, String sign){
        String timeSuffix = null;
        String[] splitTimeArrayOne = subTime.split("\\" + sign);
        String[] splitTimeArrayTwo = splitTimeArrayOne[1].split(":");
        if(splitTimeArrayTwo[0].length() < 2){
            timeSuffix = "+" + "0" + splitTimeArrayTwo[0] + ":" + splitTimeArrayTwo[1];
            subTime = splitTimeArrayOne[0] + timeSuffix;
            return subTime;
        }
        return subTime;
    }

    /**
     * 函数功能描述:获取本地时区的表示(比如:第八区-->+08:00)
     * @return
     */
    public static String getTimeZoneByNumExpress(){
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        int rawOffset = timeZone.getRawOffset();
        int timeZoneByNumExpress = rawOffset/3600/1000;
        String timeZoneByNumExpressStr = "";
        if(timeZoneByNumExpress > 0 && timeZoneByNumExpress < 10){
            timeZoneByNumExpressStr = "+" + "0" + timeZoneByNumExpress + ":" + "00";
        }
        else if(timeZoneByNumExpress >= 10){
            timeZoneByNumExpressStr = "+" + timeZoneByNumExpress + ":" + "00";
        }
        else if(timeZoneByNumExpress > -10 && timeZoneByNumExpress < 0){
            timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
            timeZoneByNumExpressStr = "-" + "0" + timeZoneByNumExpress + ":" + "00";
        }else if(timeZoneByNumExpress <= -10){
            timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
            timeZoneByNumExpressStr = "-" + timeZoneByNumExpress + ":" + "00";
        }else{
            timeZoneByNumExpressStr = "Z";
        }
        return timeZoneByNumExpressStr;
    } 
     
        private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        
        /**
         * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />
         * 如果获取失败，返回null
         * @return
         */
        public static String getUTCTimeStr(String time) {
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            StringBuffer UTCTimeBuffer = new StringBuffer();
            // 1、取得本地时间：
            Calendar cal = Calendar.getInstance() ;
            
        	try {
        		cal.setTime(sdf.parse(time));
            }
            catch (ParseException e) {
    	        e.printStackTrace();
            }
        	
            // 2、取得时间偏移量：
            int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
            // 3、取得夏令时差：
            int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
            // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
            cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE); 
            int s = cal.get(Calendar.SECOND); 
            
            UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day) ;
            UTCTimeBuffer.append(" ").append(hour).append(":").append(minute).append(":").append(s) ;
            
            
            try{
                format.parse(UTCTimeBuffer.toString()) ;
                return UTCTimeBuffer.toString() ;
            }catch(ParseException e)
            {
                e.printStackTrace() ;
            }
            return null ;
        }
        
	public static long getTimeEx(String time) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(time);
			Date d2 = df.parse("1970-01-01 00:00:00");
			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

			return diff / 1000;
		} catch (Exception e) {
		}
		return 0;
	}
        
        public static long getTime(String time) {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date date;
    		try {
    			date = format.parse(time);
    			return date.getTime();
    		} catch (ParseException e) {
    			
    			e.printStackTrace();
    		}
    		return 0l;
    	}
        /**
         * 获取指定时间到格林威治时间的秒数
         * UTC：格林威治时间1970年01月01日00时00分00秒（UTC+8北京时间1970年01月01日08时00分00秒）
         * @param time
         * @return
         * @throws ParseException 
         */
        public static long diffSeconds(String time) throws ParseException{
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar calendar = Calendar.getInstance();

            calendar.clear();
            calendar.setTime(sdf.parse(time));

            TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
            calendar.setTimeZone(timeZone);
            return calendar.getTimeInMillis()/1000;
        }

        public static void main(String[] args) throws Exception {

            String datetime = "19700101140000";
            System.out.println("=================方法一：calendar============================");
            System.out.println(diffSeconds(datetime));
            System.out.println("=================方法二：计算时间差============================");
//            System.out.println(diffSeconds("19700101080000", datetime, DatetimeUtil.PATTERN_YYYYMMDDHHMMSS));
            System.out.println("=================方法三：使用system============================");
            System.out.println(System.currentTimeMillis()/1000);
        }
        
}