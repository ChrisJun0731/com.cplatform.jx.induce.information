package com.cplatform.jx.induce.information.traffic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherCityInfo;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherDataInfo;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherForecastInfo;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherInfo;
import com.cplatform.util2.TimeStamp;

/**
 * 
 * 通过阿里云墨迹天气API 获取天气信息. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月27日 下午1:45:54
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class GetweatherByAlicity {

	/** Logger. */
	private static Logger logger = Logger.getLogger(GetweatherByAlicity.class);

	/**
	 * 获取天气信息
	 * 
	 * @param request
	 * @return
	 */
	public static AliWeatherInfo getWeather(String serviceKey, String cityId) {

		AliWeatherInfo obj = null;

		// String serviceKey = SysManager.getInstance().getAliAppCode();
		//"http://freecityid.market.alicloudapi.com/whapi/json/alicityweather/briefforecast3days";//
		String url =  SysManager.getInstance().getAliWeatherUrl();
		// String cityId = SysManager.getInstance().getAliCityId();
		String token = "677282c2f1b3d718152c4e25ed434bc4";

		String temp = null;
		try {
			temp = getRecordCount("weather" + cityId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (temp != null) {
			obj = JSONObject.parseObject(temp, AliWeatherInfo.class);
			if (obj != null) {
				AliWeatherDataInfo data = obj.getData();
				if (data != null) {
					AliWeatherCityInfo city = data.getCity();
					if (city != null && String.valueOf(city.getCityId()).equals(cityId)) {
						List<AliWeatherForecastInfo> forcase = data.getForecast();
						if (forcase != null && (!forcase.isEmpty())) {
							for (AliWeatherForecastInfo info : forcase) {
								if (info.getPredictDate().replaceAll("-", "").equals(TimeStamp.getTime(8))) {
									return obj;
								}
							}
						}
					}
				}
			}
		}

		String result;
		try {
			result = sendStrHttpRequest(cityId, token, serviceKey, url);
			obj = JSONObject.parseObject(result, AliWeatherInfo.class);
			if (obj.getCode().equals("0")) {
				saveRecord("weather" + cityId, result);
			}
		} catch (Exception e) {
			logger.error("获取墨迹天气请求异常", e);
		}
		return obj;
	}

	/**
	 * 发送http请求post
	 * 
	 * @param req
	 * @param desturl
	 * @return
	 * @throws Exception
	 *             bodys.put("cityId", "2"); bodys.put("token",
	 *             "677282c2f1b3d718152c4e25ed434bc4");
	 */
	protected static String sendStrHttpRequest(String cityId, String token, String appcode, String strurl)
			throws Exception {

		logger.info("dsetUrl1:"+strurl);
		logger.info("代理Ip1为："+SysManager.getInstance().getProxyHost());
	    logger.info("代理端口1为："+SysManager.getInstance().getProxyPort());
		 // 设置http访问要使用的代理服务器的地址
	    if(StringUtils.isNotBlank(SysManager.getInstance().getProxyHost())){
	    		    System.setProperty("https.proxyHost",SysManager.getInstance().getProxyHost());
	    	// 设置http访问要使用的代理服务器的端口
	   	 System.setProperty("https.proxyPort",SysManager.getInstance().getProxyPort());
	    }
	    
		URL url = new URL(strurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		try {
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("Authorization", "APPCODE " + appcode);
			conn.connect();

			OutputStream out = conn.getOutputStream();
			try {

				StringBuffer params = new StringBuffer();
				// 表单参数与get形式一样
				params.append("cityId").append("=").append(cityId).append("&").append("token").append("=")
						.append(token);
				out.write(params.toString().getBytes("utf-8"));
				out.flush();
			} catch (Exception ex) {
				throw ex;
			} finally {
				out.close();
			}

			InputStream in = conn.getInputStream();
			try {
				BufferedReader inbufferread = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String inputLine = null;
				StringBuffer resultBuffer = new StringBuffer(100);
				while ((inputLine = inbufferread.readLine()) != null) {
					resultBuffer.append(inputLine);
				}

				String resp = resultBuffer.toString().trim();

				return resp;
			} catch (Exception ex) {
				throw ex;
			} finally {
				in.close();
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			conn.disconnect();
		}
	}

	@SuppressWarnings("resource")
	protected synchronized static String getRecordCount(String fileMode) throws Exception {
		StringBuffer buf = new StringBuffer(500);
		buf.append("./config/");
		buf.append(fileMode).append(".mark");
		File file = new File(buf.toString());
		if (file.exists()) {

			FileReader fileReader = null;
			try {
				fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader, 1024 * 64);
				String line = bufferedReader.readLine();
				if (line != null) {
					return line;
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				try {
					fileReader.close();
				} catch (IOException e) {

				}
			}
		}

		return null;
	}

	protected synchronized static void saveRecord(String fileMode, String markTime) throws Exception {

		StringBuffer buf = new StringBuffer(500);
		buf.append("./config/");
		String dirName = buf.toString();
		File dir = new File(dirName);
		if (!dir.exists())
			dir.mkdirs();
		buf.append(fileMode).append(".mark");
		String filename = buf.toString();
		FileWriter fw = new FileWriter(filename);
		try {
			fw.write(markTime);
			fw.flush();
		} catch (Exception ex) {
			throw ex;
		} finally {
			fw.close();
		}
	}

	public static void main(String[] args) {
		System.out.println("");

		try {
			AliWeatherInfo info = getWeather("6e4a94d3fe7c46ddb7c3b93d4c4a97a7", "1208");
			System.out.println("" + info);
		} catch (Exception ex) {

		}

	}

}
