package com.cplatform.jx.induce.information.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.model.info.ModelSevenConfigInfo;
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
 * 模板7. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年8月3日 上午9:44:05
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class CreateModelSeven extends AbstractProcessThread{

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());
	
	private Map <String,List<Integer>> modelSeven = new HashMap<String, List<Integer>>();
	public CreateModelSeven(){
		init(1000*60*2+8000);
//		init(1000);
	}
	
	@Override
	protected void execTask() throws Exception {
		// 从数据库 获取模板1的任务，和参数配置
		List<ModelSevenConfigInfo> lis = new ArrayList<ModelSevenConfigInfo>();
		lis = ServiceDb.getModelSevenConfigs();
		
		if(lis ==null||lis.isEmpty()){
			Thread.sleep(1000*60*10);
		}
		
		// 获取所有设备信息
		List<TDeviceInfo> devices = ServiceDb.getVaildDevices();
		if (devices == null || devices.isEmpty()) {
			logger.info("未获取到在线设备数据");
			Thread.sleep(1000*60*10);
			return;
		}
		
		List<DeviceItemInfo> items = new ArrayList<DeviceItemInfo>();
		for(ModelSevenConfigInfo info:lis){
			for(TDeviceInfo dInfo:devices){
				if(info.getDEVICE_CODE().equals(dInfo.getDEVICE_CODE())){
					
					
					Boolean isEmergent=ServiceDb.isEmergent(info.getDEVICE_CODE());
					if(isEmergent==null||isEmergent==true){
						logger.info("设备"+info.getDEVICE_CODE()+"在播放紧急消息");
						//模板在播放紧急消息
						modelSeven.remove(info.getDEVICE_CODE());
						break;
					}
					List<Integer> colorList=new ArrayList<Integer>();
					
					
//					int allcolor =0;
					int color = 0;
					int allTime = 0;
					String[] cont = new String [3];
					String[] minute = new String [3];
					int[] col = new int [3];

					color = getColor(info.getCITY_CODE(), info.getROAD_CODE());
					allTime =getTime(info.getCITY_CODE(), info.getSTART_POINT(),  info.getEND_POINT());
					 
					Thread.sleep(100);
					
					if(allTime>0&&color>0){
						cont[0]= ""+info.getROAD_NAME();	
						col[0] = color;
						minute[0] = ""+allTime/60;
//						allcolor += color;
						colorList.add(color);
					}
					else{
						break;
					}
					
					color = getColor(info.getCITY_CODE(), info.getROAD_CODE2());
					allTime =getTime(info.getCITY_CODE(), info.getSTART_POINT2(),  info.getEND_POINT2());
					 
					Thread.sleep(100);
					
					if(allTime>0&&color>0){
						cont[1]= ""+info.getROAD_NAME2();	
						col[1] = color;
						minute[1] = ""+allTime/60;
//						allcolor += color;
						colorList.add(color);
					}
					else{
						break;
					}
					
					color = getColor(info.getCITY_CODE(), info.getROAD_CODE3());
					allTime =getTime(info.getCITY_CODE(), info.getSTART_POINT3(),  info.getEND_POINT3());
					 
					Thread.sleep(100);
					logger.info("color==========================="+color);
					logger.info("allTime==========================="+allTime);
					if(allTime>0&&color>0){
						cont[2]= ""+info.getROAD_NAME3();
						col[2] = color;
						minute[2] = ""+allTime/60;
//						allcolor += color;
						colorList.add(color);
					}
					else{
						break;
					}
					
					List<Integer> oldList = getOldColor(dInfo.getDEVICE_CODE());
					String time = TimeStamp.getTime(14);
					String fileName = info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_7_"+time.substring(8, 14)+".png";
					if(!compare(colorList, oldList)){
						logger.info("颜色不相同================");
						logger.debug("设备："+colorList +" newColor："+colorList+" 生成新消息"+cont+" color："+col);
						putColor(dInfo.getDEVICE_CODE(),colorList);
//						DeviceItemInfo item = WriteSevenItem(cont,dInfo.getDISPLAY_RESO().trim(),dInfo.getDEVICE_CODE(),"1",col,info);
						DeviceItemInfo item = WriteSevenItemWithImages(cont,dInfo.getDISPLAY_RESO().trim(),dInfo.getDEVICE_CODE(),"1",col,info,minute,fileName,Collections.max(colorList),info.getFONT_SIZE());
						logger.info("item================"+item);
						if(item != null&&checkItemMap(dInfo.getDEVICE_CODE().trim(),item)){
							item.setRoadLevel(Collections.max(colorList)+"");
							item.setSendType(dInfo.getSEND_TYPE());
							items.add(item);
							logger.debug("模板7创建消息成功! equipment:"+dInfo.getDEVICE_CODE()+" 内容："+ cont);
						}
					}
					break;
				}
			}
		}
		
		// 保存自动信息
		if(items != null&&(!items.isEmpty()))
//        ServiceDb.SavePushItem(items);
        ServiceDb.SavePushItemWithSingle(items);
	}
	

	private List<Integer> getOldColor(String provCode) {
		try{
		return modelSeven.get(provCode)== null ? new ArrayList<Integer>():modelSeven.get(provCode);
		}
		catch(Exception e){
			logger.error("遍历异常",e);
		}
		return new ArrayList<Integer>();
	}
	
	private void putColor(String provCode,List<Integer> list){
		try{
			modelSeven.remove(provCode);
			modelSeven.put(provCode, list);
			}
			catch(Exception e){
				logger.error("更新异常",e);
			}
	}

	
	/*private int getColor(String city_code,String road_code){
		try{
		int color = 0;
		int all =0;
		InduceStateRequest req = new InduceStateRequest();
		req.setCity(city_code);
		req.setRoadId(road_code);
		InduceStateResponse stateResp = GetInduceStateByAmap.getInduceState(req);
		if(stateResp != null){
			logger.debug("调用高德接口! req:"+req.toString()+" 结果："+ stateResp.toString());
			if(stateResp.getStatus().getCode() ==0){
				//计算系数
				int num =0;
				for( DataInfo d: stateResp.getData()){
//					if(d.getStatus()>=2){
					num += d.getLength()*d.getStatus();
					all += d.getLength();
//					}
				}
				if(num>0&&all>0)
				color = num/all;
			}
			else{
				logger.error("调用高德接口返回结果异常!req:"+req.toString()+" 结果："+ stateResp.toString());
			}
		}
		else{
			logger.error("调用高德接口返回结果异常!req:"+req.toString()+" 结果："+ stateResp);
		}
		return color;
		}
		catch(Exception e){
			logger.error("请求接口异常:",e);
		}
		return 0;
	}*/
	/*private int getTime(String city_code,String start_point,String end_point){
		try{
				int allTime =0;
				InduceTimeRequest treq = new InduceTimeRequest();
				treq.setCity(city_code);
				treq.setEndpos(start_point);
				treq.setStartpos(end_point);
				treq.setStrategy("0");
				
				InduceTimeResponse tresp = GetInduceTimeByAmap.getInduceState(treq);
				if(tresp != null){
					logger.debug("调用高德接口2! req:"+treq.toString()+" 结果："+ tresp.toString());
					if(tresp.getStatus().getCode() ==0){
						TimeDataInfo td =  tresp.getData();
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
					}
					else{
						logger.debug("调用高德接口2 异常! req:"+treq.toString()+" 结果："+ tresp.toString());
					}
				}
				else{
					logger.debug("调用高德接口2 异常! req:"+treq.toString()+" 结果："+ tresp);
				
				}
				return allTime;
		}
		catch(Exception e){
			logger.error("请求接口异常:",e);
		}
		return 0;
	}*/
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
}
