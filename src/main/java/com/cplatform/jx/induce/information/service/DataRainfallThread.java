package com.cplatform.jx.induce.information.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.sync.AeroInfo;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;
/**
 * 
 * 获取雨量. <br>
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
public class DataRainfallThread extends AbstractProcessThread {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());
	
	public DataRainfallThread(){
		init();
	}
	/**
	 * 任务处理 过滤已经发布的目前还没有失效的自动信息
	 * 
	 * @throws Exception
	 */
	protected void execTask() throws Exception {

		// long start = System.currentTimeMillis();
		// 获取能见度信息
		int limt = SysManager.getInstance().getRainfallLimit();
		List<AeroInfo> aeroInfos = ServiceDb.getAeroInfolimit(limt);
		List<DeviceItemInfo> items = null;
		if (aeroInfos == null || aeroInfos.isEmpty()) {

			logger.info("未获取到雨量数据");
			return;
		} else {
			// 获取所有设备信息
			List<TDeviceInfo> devices = ServiceDb.getVaildDevices();
			if (devices == null || devices.isEmpty()) {
				logger.info("未获取到在线设备数据");
				return;
			} else {
				items = new ArrayList<DeviceItemInfo>();
				// 遍历所有信息
				for (TDeviceInfo dev : devices) {
					
					for (AeroInfo info : aeroInfos) {
						
						try{
						if (info.getN_CODE() == Long.valueOf(dev.getPROV_CODE())) {							
							String[] cont = new String[2];
							String imgFileName="";
							if (info.getD_RAINFALL() > 15) {
								// 雨天路滑 谨慎驾驶
								imgFileName = SysManager.getInstance().getRainfalImgFile1();
								if(dev.getDISPLAY_RESO().trim().compareTo("160*96")==0){
									cont[0]="雨天路滑";
									cont[1]="谨慎驾驶";
								}else{
									cont[0]="雨天路滑 谨慎驾驶";
								}
							} else {
								// 雨天路滑 小心驾驶
								imgFileName = SysManager.getInstance().getRainfalImgFile2();
								if(dev.getDISPLAY_RESO().trim().compareTo("160*96")==0){
									cont[0]="雨天路滑";
									cont[1]="小心驾驶";
								}else{
									cont[0]="雨天路滑 小心驾驶";
								}
							}

							if (info.getC_ICING().compareTo("1") == 0) {
								// 路面结冰 谨慎驾驶
								imgFileName = SysManager.getInstance().getIceImgFile();
								if(dev.getDISPLAY_RESO().trim().compareTo("160*96")==0){
									cont[0]="路面结冰";
									cont[1]="谨慎驾驶";
								}else{
									cont[0]="路面结冰 谨慎驾驶";
								}
							}

							// 生成自动信息
							DeviceItemInfo item = WriteItem(cont,dev.getDISPLAY_RESO().trim(),dev.getDEVICE_CODE(),imgFileName,"1");
							if(item != null&&checkItemMap(dev.getDEVICE_CODE().trim(),item)){
								items.add(item);
							}
							break;
						}
						else{
//							logger.info("未找到对应的在线设备"+info.toString());
						}
						}
						catch(Exception e){
							logger.error("处理雨量自动消息异常"+info.toString()+" 原因："+e);
						}
					}
				}

				// 保存自动信息
                ServiceDb.SavePushItem(items);
			}
		}

	}
	
}
