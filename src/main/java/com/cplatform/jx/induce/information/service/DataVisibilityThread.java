package com.cplatform.jx.induce.information.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.sync.ViInfo;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;
/**
 * 能见度监测 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年4月5日 下午2:58:53
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class DataVisibilityThread extends AbstractProcessThread {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());
	
	public DataVisibilityThread(){
		init();
	}
	/**
	 * 任务处理 过滤已经发布的目前还没有失效的自动信息
	 * 
	 * @throws Exception
	 */
	protected void execTask() throws Exception {

		// 获取能见度信息
		int limt = SysManager.getInstance().getVisibilityLimit();
		List<ViInfo> viInfos = ServiceDb.getViInfolimit(limt);
		List<DeviceItemInfo> items = null;
		if (viInfos == null || viInfos.isEmpty()) {
			logger.info("未获取到能见度数据");
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
					for (ViInfo info : viInfos) {
						if (info.getN_CODE() == Long.valueOf(dev.getPROV_CODE())) {
							// 能见度差 谨慎驾驶

							// 生成自动信息
							String[] cont = new String[2];
							if(dev.getDISPLAY_RESO().trim().compareTo("160*96")==0){
								cont[0]="能见度差";
								cont[1]="谨慎驾驶";
							}else{
								cont[0]="能见度差 谨慎驾驶";
							}
							DeviceItemInfo item = WriteItem(cont,dev.getDISPLAY_RESO().trim(),dev.getDEVICE_CODE(),SysManager.getInstance().getVisibImgFile(),"2");
							if(item != null&&checkItemMap(dev.getDEVICE_CODE().trim(),item)){
								items.add(item);
							}
							break;
						}
						else{
							logger.info("未找到对应的在线设备"+info.toString());
						}
					}
				}

				// 保存自动信息
				ServiceDb.SavePushItem(items);
			}
		}
	}
}
