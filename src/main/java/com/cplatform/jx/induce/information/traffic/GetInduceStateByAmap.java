package com.cplatform.jx.induce.information.traffic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.traffic.info.DataInfo;
import com.cplatform.jx.induce.information.traffic.info.InduceStateRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceStateResponse;

/**
 * 
 * 通过高德接口 获取道路诱导状态. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月27日 下午1:45:54
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class GetInduceStateByAmap {

	/** Logger. */
	private static Logger logger = Logger.getLogger(GetInduceStateByAmap.class);
	
	/**
	 * 获取路段交通状态
	 * @param request
	 * @return
	 */
	public static  InduceStateResponse getInduceState(InduceStateRequest request){
		
		InduceStateResponse obj = null;

		String serviceKey = SysManager.getInstance().getServiceKey();
		String url = SysManager.getInstance().getAmapUrl();
	
		url = String.format(url, request.toString().replace("{", "%7B").replace("}", "%7D").replaceAll("\"", "%22"),serviceKey);
		
		String result;
		try {
			result = getHttpRequest(url);	
		 obj = JSONObject.parseObject(result, InduceStateResponse.class);
		} catch (Exception e) {
			logger.error("获取高德路况请求异常",e);
		}
		return obj;
	}
	
	//最终计算为(1*200+3*100)/300=1.66，通过时间为理想时间1.6倍，渲染为黄色。
	public static double CalcStatus(InduceStateResponse resp){
		if(resp == null)return 0;
		List<DataInfo> infos = resp.getData();
		if(infos==null|| infos.isEmpty()){
			return 0;
		}
		double roadSize =0;
		double allRoad = 0;
		for(DataInfo info :infos){
			roadSize += info.getLength();
			allRoad += info.getStatus()*info.getLength();
		}
		
		return allRoad/roadSize;
	}
	/**
	 * 发送http请求 get
	 * @param req
	 * @param desturl
	 * @return
	 * @throws Exception
	 */
	private static String getHttpRequest(String desturl) throws Exception {

		logger.info("dsetUrl1:"+desturl);
		logger.info("代理Ip1为："+SysManager.getInstance().getProxyHost());
	    logger.info("代理端口1为："+SysManager.getInstance().getProxyPort());
		 // 设置http访问要使用的代理服务器的地址
	    if(StringUtils.isNotBlank(SysManager.getInstance().getProxyHost())){
	    		    System.setProperty("https.proxyHost",SysManager.getInstance().getProxyHost());
	    	// 设置http访问要使用的代理服务器的端口
	   	 System.setProperty("https.proxyPort",SysManager.getInstance().getProxyPort());
	    }
	    
	    
		URL url = new URL(desturl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
//		 Properties   prop   =   System.getProperties();
//		    System.getProperties().put("proxySet","true");
		   

//		    prop.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//		    prop.setProperty("sun.net.client.defaultReadTimeout", "10000");
//		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
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
