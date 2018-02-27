package com.cplatform.jx.induce.information.traffic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeResponse;

public class GetInduceTimeByAmap {

	/** Logger. */
	private static Logger logger = Logger.getLogger(GetInduceTimeByAmap.class);
	
	/**
	 * 获取路段交通通过时间
	 * @param request
	 * @return
	 */
	public static  InduceTimeResponse getInduceState(InduceTimeRequest request){
		
		InduceTimeResponse obj = null;

		String serviceKey = SysManager.getInstance().getServiceKey();
		String url = SysManager.getInstance().getTimeAmapUrl();
			
		url = String.format(url, request.toString().replace("{", "%7B").replace("}", "%7D").replaceAll("\"", "%22"),serviceKey);
		
		String result;
		try {
			result = getHttpRequest(url);	
		 obj = JSONObject.parseObject(result, InduceTimeResponse.class);
		} catch (Exception e) {
			logger.error("获取高德路况请求异常",e);
		}
		return obj;
	}
	

	/**
	 * 发送http请求 get
	 * @param req
	 * @param desturl
	 * @return
	 * @throws Exception
	 */
	private static String getHttpRequest(String desturl) throws Exception {

		logger.info("dsetUrl2:"+desturl);
		logger.info("代理Ip为2："+SysManager.getInstance().getProxyHost());
	    logger.info("代理端口为2："+SysManager.getInstance().getProxyPort());
	    if(StringUtils.isNotBlank(SysManager.getInstance().getProxyHost())){
	    	
			    System.setProperty("https.proxyHost",SysManager.getInstance().getProxyHost());
		//	    // 设置http访问要使用的代理服务器的端口
			    System.setProperty("https.proxyPort",SysManager.getInstance().getProxyPort());
	    }
		
		URL url = new URL(desturl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		
//		 Properties   prop   =   System.getProperties();
//		    System.getProperties().put("proxySet","true");
		    // 设置http访问要使用的代理服务器的地址
		    

//		    prop.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//		    prop.setProperty("sun.net.client.defaultReadTimeout", "10000");
		conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-Type","text/html; charset=UTF-8");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.connect();

		InputStream in = null;
		BufferedReader inbufferread = null;

		try {

			if (logger.isInfoEnabled()) {
				logger.info("本地请求");
				logger.info("url:");
				logger.info(desturl);
			}

			in = conn.getInputStream();

			inbufferread = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String inputLine = null;
			StringBuffer resultBuffer = new StringBuffer(100);
			while ((inputLine = inbufferread.readLine()) != null) {
				resultBuffer.append(inputLine);
			}
			
			String resp = resultBuffer.toString().trim();
			if (logger.isInfoEnabled()) {
				logger.info("远程响应为:");
				logger.info(resp);
			}
			return resp;
		}
		catch (Exception e) {
			throw e;
		}
		finally {

			if (inbufferread != null) {
				try {
					inbufferread.close();
				}
				catch (Exception e) {
					logger.error("", e);
				}
			}

			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) {
					logger.error("", e);
				}
			}
			conn.disconnect();
		}
	}
}
