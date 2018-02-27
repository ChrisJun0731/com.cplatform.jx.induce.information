package com.cplatform.jx.induce.information.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.model.info.ModelTenConfigInfo;
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
 * Title.<br>
 * Description.
 * <p>
 * Copyright: Copyright (c) 2017年11月6日 上午9:59:55
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * Author: shixw
 * <p>
 * Version: 1.0
 * <p>
 */
public class CreateModelTen extends AbstractProcessThread{

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());
	
	private Map <String,List<Integer>> modelTen = new HashMap<String, List<Integer>>();
	public CreateModelTen(){
		init(1000*60*2+6000);
//		init(1000);
	}
	
	@Override
	protected void execTask() throws Exception {
		// 从数据库 获取模板1的任务，和参数配置
		List<ModelTenConfigInfo> lis = new ArrayList<ModelTenConfigInfo>();
		lis = ServiceDb.getModelTenConfigs();
		
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
		for(ModelTenConfigInfo info:lis){
			for(TDeviceInfo dInfo:devices){
				System.out.println(dInfo.getDEVICE_CODE());
				if(info.getDEVICE_CODE().equals(dInfo.getDEVICE_CODE())){
					
					Boolean isEmergent=ServiceDb.isEmergent(info.getDEVICE_CODE());
					if(isEmergent==null||isEmergent==true){
						logger.info("设备"+info.getDEVICE_CODE()+"在播放紧急消息");
						//模板在播放紧急消息
						modelTen.remove(info.getDEVICE_CODE());
						break;
					}
					List<Integer> colorList=new ArrayList<Integer>();
					
					
					int color = 0;
					int allTime = 0;
					List<String []> contList=new ArrayList<String[]>();
					List<int[]> colList=new ArrayList<int[]>();
					List<String> fileNameList=new ArrayList<String>();
					List<Integer> color1List=new ArrayList<Integer>();
					String[] cont = new String [2];
					String[] cont2 = new String [2];
					int[] col = new int [2];
					int[] col2=new int[2];
					String[] minute = new String [2];

					color = getColor(info.getCITY_CODE(), info.getROAD_CODE());
					allTime =getTime(info.getCITY_CODE(), info.getSTART_POINT(), info.getEND_POINT());
					 
					Thread.sleep(100);
					
					if(allTime>0&&color>0){
						cont[0]= ""+info.getROAD_NAME()+getRodeState(color);//第一屏第一条	
						col[0] = color;
						cont2[0] = ""+info.getSECOND_NAME();//第二屏第一条
						col2[0]=color;
						minute[0] = ""+allTime/60;
						colorList.add(color);
					}
					else{
						break;
					}
					color = getColor(info.getCITY_CODE(), info.getROAD_CODE2());
					if(StringUtils.isNotBlank(info.getROAD_NAME2())){//第一屏第二条不为空
						cont[1]= ""+info.getROAD_NAME2()+getRodeState(color);
						col[1] = color;
						colorList.add(color);
					}else{
						cont[1]="";
					}
					
					allTime =getTime(info.getCITY_CODE(), info.getSTART_POINT2(),  info.getEND_POINT2());
					 
					Thread.sleep(100);
					
					if(allTime>0&&color>0){
						col2[1]=color;
						cont2[1] = ""+info.getSECOND_NAME2();//第二屏第二条
						minute[1] = ""+allTime/60;
					}
					else{
						break;
					}
					contList.add(cont);
					contList.add(cont2);
					colList.add(col);
					colList.add(col2);
					
					List<Integer> oldList = getOldColor(dInfo.getDEVICE_CODE());
					String time = TimeStamp.getTime(14);
					String fileName1 = info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_10_1_"+time.substring(8, 14)+".png";
					String fileName2 = info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_10_2_"+time.substring(8, 14)+".png";
					fileNameList.add(fileName1);
					fileNameList.add(fileName2);
					if(!compare(colorList, oldList)){
						logger.debug("设备："+colorList +" newColor："+colorList+" 生成新消息"+cont+" color："+col);
						putColor(dInfo.getDEVICE_CODE(),colorList);
						color1List.add(1);
						color1List.add(Collections.max(colorList));
						DeviceItemInfo item = WriteTenItemWithImages(contList,dInfo.getDISPLAY_RESO().trim(),dInfo.getDEVICE_CODE(),colList,fileNameList,color1List,minute,info.getFONT_SIZE());
						if(item != null&&checkItemMap(dInfo.getDEVICE_CODE().trim(),item)){
							item.setRoadLevel(Collections.max(colorList)+"");
							item.setSendType(dInfo.getSEND_TYPE());
							items.add(item);
							logger.debug("模板10创建消息成功! equipment:"+dInfo.getDEVICE_CODE()+" 内容："+ cont);
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
	/***
	 * 获得路况描述
	 * @param color
	 * @return
	 */
	private String getRodeState(int color){
		String res="";
		switch (color) {
		case 1:
			res="畅通";
			break;
		case 2:
			res="缓行";
			break;
		case 3:
			res="拥堵";
			break;
		case 4:
			res="拥堵";
			break;

		default:
			res="畅通";
			break;
		}
			
		return res;
	}

	private List<Integer> getOldColor(String provCode) {
		try{
		return modelTen.get(provCode)== null ? new ArrayList<Integer>():modelTen.get(provCode);
		}
		catch(Exception e){
			logger.error("遍历异常",e);
		}
		return new ArrayList<Integer>();
	}
	
	private void putColor(String provCode,List<Integer> list){
		try{
			modelTen.remove(provCode);
			modelTen.put(provCode, list);
			}
			catch(Exception e){
				logger.error("更新异常",e);
			}
	}

	
/*	private int getColor(String city_code,String road_code){
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
