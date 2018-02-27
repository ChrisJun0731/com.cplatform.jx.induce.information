package com.cplatform.jx.induce.information.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.model.info.ModelOneConfigInfo;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;
import com.cplatform.jx.induce.information.traffic.GetInduceStateByAmap;
import com.cplatform.jx.induce.information.traffic.GetInduceTimeByAmap;
import com.cplatform.jx.induce.information.traffic.info.DataInfo;
import com.cplatform.jx.induce.information.traffic.info.InduceStateRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceStateResponse;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeResponse;
import com.cplatform.jx.induce.information.traffic.info.PathsInfo;
import com.cplatform.jx.induce.information.traffic.info.RouteInfo;
import com.cplatform.jx.induce.information.traffic.info.StepsInfo;
import com.cplatform.jx.induce.information.traffic.info.TimeDataInfo;
import com.cplatform.util2.TimeStamp;

/**
 * 
 * 模板1. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年8月3日 上午9:44:59
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class CreateModelOne extends AbstractProcessThread {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	private Map<String, String> modelOne = new HashMap<String, String>();

	public CreateModelOne() {
		init(1000*60*2+3000);
//		init(1000);
	}

	@Override
	protected void execTask() throws Exception {
		// 从数据库 获取模板1的任务，和参数配置
		List<ModelOneConfigInfo> lis = new ArrayList<ModelOneConfigInfo>();
		lis = ServiceDb.getModelOneConfigs();

		if (lis == null || lis.isEmpty()) {
			Thread.sleep(1000 * 60 * 10);
		}

		// 获取所有设备信息
		List<TDeviceInfo> devices = ServiceDb.getVaildDevices();
		if (devices == null || devices.isEmpty()) {
			logger.info("未获取到在线设备数据");
			Thread.sleep(1000 * 60 * 10);
			return;
		}

		List<DeviceItemInfo> items = new ArrayList<DeviceItemInfo>();
		for (ModelOneConfigInfo info : lis) {
			for (TDeviceInfo dInfo : devices) {
				if (info.getDEVICE_CODE().equals(dInfo.getDEVICE_CODE())) {
					
					Boolean isEmergent=ServiceDb.isEmergent(info.getDEVICE_CODE());
					if(isEmergent==null||isEmergent==true){
						logger.info("设备"+info.getDEVICE_CODE()+"在播放紧急消息");
						//模板在播放紧急消息
						modelOne.remove(info.getDEVICE_CODE());
						break;
					}
					// 处理
					// 调用 高德1
					// 0，1，2，3
					int color = 0;
					float tempColor=0f;
					InduceStateRequest req = new InduceStateRequest();
					req.setCity(info.getCITY_CODE());
					req.setRoadId(info.getROAD_CODE());
					InduceStateResponse stateResp = GetInduceStateByAmap.getInduceState(req);
					if (stateResp != null) {
						logger.debug("调用高德接口! req:" + req.toString() + " 结果：" + stateResp.toString());
						if (stateResp.getStatus().getCode() == 0) {
							// 计算系数
							int num = 0;
							int all = 0;
							for (DataInfo d : stateResp.getData()) {
								num += d.getLength() * d.getStatus();
								all += d.getLength();
							}
							tempColor = num / all;
						} else {
							logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp.toString());
							break;
						}
					} else {
						logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp);
						break;
					}
					color=isCt(tempColor);
					// 调用 高德2
					int allTime = 0;
					InduceTimeRequest treq = new InduceTimeRequest();
					treq.setCity(info.getCITY_CODE());
					treq.setEndpos(info.getEND_POINT());
					treq.setStartpos(info.getSTART_POINT());
					treq.setStrategy("0");

					InduceTimeResponse tresp = GetInduceTimeByAmap.getInduceState(treq);
					if (tresp != null) {
						logger.debug("调用高德接口2! req:" + treq.toString() + " 结果：" + tresp.toString());
						if (tresp.getStatus().getCode() == 0) {
							TimeDataInfo td = tresp.getData();
							RouteInfo rinfo = td.getRoute();
							if (rinfo != null) {
								List<PathsInfo> path = rinfo.getPaths();
								if (path != null && (!path.isEmpty())) {
									for (PathsInfo pinfo : path) {
										pinfo.getDistance();
										allTime += Integer.valueOf(pinfo.getDuration());
									}

								}
							}
						} else {
							logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" + tresp.toString());
							break;
						}
					} else {
						logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" + tresp);
						break;
					}

					if (allTime > 0 && color > 0) {
						// 生成自动信息
						String[] cont = new String[1];
						cont[0] = "距" + info.getEND_NAME() + info.getDISTANCE() + "公里" + " " + allTime / 60 + " 分钟";
						String time = TimeStamp.getTime(14);
						String fileName = info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_1_"+time.substring(8, 14)+".png";
						int oldColor = Integer.valueOf(getOldColor(dInfo.getDEVICE_CODE()));
						if ( oldColor != color) {
							logger.debug("设备：" + oldColor + " newColor：" + color + " 生成新消息" + cont[0]);
							putColor(dInfo.getDEVICE_CODE(), ""+color);
							DeviceItemInfo item = WriteOneAndTwoItemImage(cont, dInfo.getDISPLAY_RESO().trim(),
									dInfo.getDEVICE_CODE(), "1", color,fileName,"1",info.getFONT_SIZE());
							
//							DeviceItemInfo item = WriteOneItem(cont, dInfo.getDISPLAY_RESO().trim(),
//									dInfo.getDEVICE_CODE(), "1", color);
							if (item != null && checkItemMap(dInfo.getDEVICE_CODE().trim(), item)) {
								item.setRoadLevel(color+"");
								item.setSendType(dInfo.getSEND_TYPE());//
								items.add(item);
								logger.debug("模板1创建消息成功! equipment:" + dInfo.getDEVICE_CODE() + " 内容：" + cont[0]);
							}
						}
					}
					break;
				}
			}
		}

		// 保存自动信息
		if (items != null && (!items.isEmpty()))
//			ServiceDb.SavePushItem(items);
		ServiceDb.SavePushItemWithSingle(items);
	}

	private String getOldColor(String provCode) {
		try {
			return modelOne.get(provCode)==null ? "0":modelOne.get(provCode);
		} catch (Exception e) {
			logger.error("遍历异常", e);
		}
		return "0";
	}
	
	private void putColor(String provCode, String color) {
		try {
			modelOne.remove(provCode);
			modelOne.put(provCode, color);
		} catch (Exception e) {
			logger.error("更新异常", e);
		}
	}

}
