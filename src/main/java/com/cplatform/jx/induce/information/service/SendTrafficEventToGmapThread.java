package com.cplatform.jx.induce.information.service;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;
import com.cplatform.jx.induce.information.traffic.Cryptos;
import com.cplatform.jx.induce.information.traffic.TimeConverterUtil;
import com.cplatform.jx.induce.information.traffic.info.EventDataInfo;
import com.cplatform.jx.induce.information.traffic.info.EventDurationInfo;
import com.cplatform.jx.induce.information.traffic.info.EventInfo;
import com.cplatform.jx.induce.information.traffic.info.EventPositionInfo;
import com.cplatform.jx.induce.information.traffic.info.EventResultInfo;
import com.cplatform.jx.induce.information.traffic.info.TrafficEventInfo;

/**
 * 
 * 传递交通事件数据给高德. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年4月6日 下午3:10:57
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SendTrafficEventToGmapThread extends AbstractProcessThread {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());
	
	public SendTrafficEventToGmapThread(){
		init();
	}
	/**
	 * 任务处理 过滤已经发布的目前还没有失效的自动信息
	 * 
	 * @throws Exception
	 */
	protected void execTask() throws Exception {

		List<TrafficEventInfo> aeroInfos = ServiceDb.getTrafficEvent();
		List<Integer> ids = new ArrayList<Integer>();
	
		if (aeroInfos == null || aeroInfos.isEmpty()) {

//			logger.info("未获取到待处理的交通事件数据");
			return;
		} else {
			EventInfo eventInfo = new EventInfo(); 
			String sign = createSign(String.valueOf(aeroInfos.get(0).getId()));
			eventInfo.setSign(sign);
			List<EventDataInfo> data = new ArrayList<EventDataInfo>();
			EventDataInfo temp = null;
			for (TrafficEventInfo info : aeroInfos) {
				try {
					//处理投递事件
					temp = new EventDataInfo();
					
					temp.setCityCode(SysManager.getInstance().getCityCode());
					temp.setDesc(info.getDesc());
					//时间
					EventDurationInfo duration = new EventDurationInfo();
					duration.setDaySel(0);
					String utcStartTime = TimeConverterUtil.getUTCTimeStr(info.getStartTime());
					String utcEndTime = TimeConverterUtil.getUTCTimeStr(info.getEndTime());
					long ucts =TimeConverterUtil.getTimeEx(utcStartTime);
					long ucte =TimeConverterUtil.getTimeEx(utcEndTime);
					
					long s =TimeConverterUtil.getTimeEx(utcStartTime.substring(0,10)+" 00:00:00");
					long e =TimeConverterUtil.getTimeEx(utcEndTime.substring(0,10)+" 00:00:00");
					
					long sm = ucts-s;
					long em = ucte-e;
					//System.out.println("s="+s+",sm="+sm+",e="+e+",em="+em);
					
					duration.setEndDate(Integer.valueOf(String.valueOf(e)));
					duration.setEndTime(Integer.valueOf(String.valueOf(em)));
					duration.setStartDate(Integer.valueOf(String.valueOf(s)));
					duration.setStartTime(Integer.valueOf(String.valueOf(sm)));
					
					temp.setDuration(duration);
					temp.setId(""+info.getId());
					temp.setInfoSourceType(3);
					temp.setLevel(0);
					//位置
					EventPositionInfo position = new EventPositionInfo();
					position.setDirection("");
					try {
					if(info.getLanes()!=null&&(!info.getLanes().isEmpty())) {
						String [] tempLan = info.getLanes().split(",");
						int [] lans = new int[tempLan.length];
						int i =0;
						for(String str:tempLan) {
							lans[i] = Integer.valueOf(str.trim());
							i++;
						}
						position.setLanes(lans);
					}
					}
					catch(Exception e1) {
						logger.error("处理车道异常"+info.getLanes(),e1);
					}				
					String [] loc =new String[1];
					loc[0] = info.getLoc();
					position.setLoc(loc);
					position.setLocType(1);
					position.setRoadName("");
//					int [] tollLanes =new int[1];
//					position.setTollLanes(tollLanes);
					
					temp.setPosition(position);
					temp.setSourceId(SysManager.getInstance().getEventGMapSourceId());
					temp.setStateFlag(0);
					temp.setSubType("");
					temp.setType(info.getType());
					temp.setPicUrl("");
					temp.setVideoUrl("");
					temp.setAudioUrl("");
					
					data.add(temp);
					ids.add(info.getId());
				} catch (Exception e) {
					logger.error("处理交通事件消息异常" + info.toString() + " 原因：" + e);
				}
			}
			
			eventInfo.setData(data);

			logger.info("开始投递交通事件:"+eventInfo.toString());
			// 修改数据库状态
			//发送PSOT	
			// 数据加密
	        byte[] aesEncrypt = helpAESEncode(eventInfo.toString(), SysManager.getInstance().getEventGMapKey());
	        // 发送数据
	        String _url = SysManager.getInstance().getEventGMapURL() + "/json?channel=" + SysManager.getInstance().getEventGMapChannel();
	        String result = "";
	        EventResultInfo obj = null;
	    	try {
				result = helpSendPost(_url, aesEncrypt, SysManager.getInstance().getEventGMapKey());	
				 logger.info("投递交通事件响应:"+result);
			 obj = JSONObject.parseObject(result, EventResultInfo.class);
			 
			} catch (Exception e) {
				logger.error("投递高德路况请求异常",e);
			}
	    	finally {
	    		//更新数据库状态
	    		ServiceDb.updateTrafficEvent(ids,obj);
	    	}
		}

	}
	/**
	 * 创建本次签名
	 * @param id
	 * @return
	 */
	private String createSign(String id) {
		String temp = SysManager.getInstance().getEventGMapChannel() + SysManager.getInstance().getEventGMapSourceId() + id + "@" + SysManager.getInstance().getEventGMapKey();
        return DigestUtils.md5Hex(temp);
	}
	
	
    private byte[] helpAESEncode(String jsonStr, String key) {
        byte[] aesEncrypt = null;
        try {
            byte[] bytesKey = key.getBytes("UTF-8");
            aesEncrypt = Cryptos.aesEncrypt(jsonStr.getBytes("UTF-8"), bytesKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return aesEncrypt;
    }

    private String helpParse(byte[] respStr, String key) {
        String re = null;
        try {
            re = new String(respStr, "UTF-8");
            byte[] bytesKey = key.getBytes("UTF-8");
            byte[] byteArray = respStr;
            byte[] aesDecrypt = Cryptos.aesDecrypt(byteArray, bytesKey);
            String decryptStr = new String(aesDecrypt, "UTF-8");
            return decryptStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return re;
    }
    
    private String helpSendPost(String _url, byte[] aesEncrypt, String key) {
        if (aesEncrypt != null) {
            // 发送数据
            try {
                byte[] bytes = sendPosts(_url, aesEncrypt);
                // 解密返回内容
                String s = helpParse(bytes, key);
                logger.info("解密返回内容:"+s);
                return s;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("上报路况异常:",e);
            }
        } else {
        	logger.error("解析失败：AES encrypt failed.");
//            System.err.println("AES encrypt failed.");
            
        }
        return null;
    }
    
    protected byte[] sendPosts(String strurl, byte[] datas) throws Exception {

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
			conn.setRequestProperty("Content-Type", "text/json;charset=UTF-8");
			conn.connect();

			OutputStream out = conn.getOutputStream();
			try {

				out.write(datas);
				out.flush();
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				out.close();
			}

			InputStream in = conn.getInputStream();
			try {
				byte[] responseData = IOUtils.toByteArray(in);			
				return responseData;
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				in.close();
			}

		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			conn.disconnect();
		}
	}
}
