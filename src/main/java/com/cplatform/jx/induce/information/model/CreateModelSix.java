package com.cplatform.jx.induce.information.model;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixInfo;
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

public class CreateModelSix extends AbstractProcessThread {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	private Map<String, List<Integer>> modelSix = new HashMap<String, List<Integer>>();

	public CreateModelSix() {
		init(1000*60*2+7000);
//		init(1000);
	}

	@Override
	protected void execTask() throws Exception {
		// 从数据库 获取模板1的任务，和参数配置
		List<ModelSixConfigInfo> lis = new ArrayList<ModelSixConfigInfo>();
		lis = ServiceDb.getModelSixConfigs(6);

		if (lis == null || lis.isEmpty()) {
			Thread.sleep(1000 * 30 );
		}

		// 获取所有设备信息
		List<TDeviceInfo> devices = ServiceDb.getVaildDevices();
		if (devices == null || devices.isEmpty()) {
			logger.info("未获取到在线设备数据");
			Thread.sleep(1000 * 60 * 10);
			return;
		}

		List<DeviceItemInfo> items = new ArrayList<DeviceItemInfo>();
		for (ModelSixConfigInfo info : lis) {
			for (TDeviceInfo dInfo : devices) {
				if (info.getDEVICE_CODE().equals(dInfo.getDEVICE_CODE())) {

					
					Boolean isEmergent=ServiceDb.isEmergent(info.getDEVICE_CODE());
					if(isEmergent==null||isEmergent==true){
						logger.info("设备"+info.getDEVICE_CODE()+"在播放紧急消息");
						//模板在播放紧急消息
						modelSix.remove(info.getDEVICE_CODE());
						break;
					}
					
					String time = TimeStamp.getTime(14);
					StringBuffer buf = new StringBuffer(100);
					buf.append(SysManager.getInstance().getTempImagePath());
					buf.append("/");
					buf.append(time.substring(0, 6));
					buf.append("/");
					buf.append(time.substring(6, 8));
					buf.append("/");
					buf.append(time.substring(8, 14));
					buf.append("/");
					String filePath = buf.toString();
					
//					buf.append(info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_6.png");

					String fileName = info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_6_"+time.substring(8, 14)+".png";
					if (info.getSIXINFOS() != null && (!info.getSIXINFOS().isEmpty())) {
						List<ModelSixInfo> result = new ArrayList<ModelSixInfo>();
						List<Integer> colorList=new ArrayList<Integer>();
						for (ModelSixInfo six : info.getSIXINFOS()) {
							ModelSixInfo sixinfo = getImageParam(info.getCITY_CODE(), six);
							if (sixinfo != null) {
								colorList.add(sixinfo.getIMG_COLOR());
								result.add(sixinfo);
							}
						}
						List<Integer> oldList=getOldColor(dInfo.getDEVICE_CODE());
							
						if (!result.isEmpty()) {
							//比较新的道路颜色和之前的道路颜色是否相同
							//如果不同则需要重新绘制效果图
							if(!compare(colorList, oldList)){
								//生成效果图，并输出到指定的目录下。绘制成功返回true，绘制失败返回false
								boolean ret = changeImage(filePath,fileName, info.getPIC_PATH(), result,info);
								if (ret) {
									//更新map中该设备的道路颜色列表
									putColor(dInfo.getDEVICE_CODE(), colorList);

									//1.生成图片item的lst文件，存放图片的路径和停留时间信息
									//2.将生成的路况图，拷贝至和lst文件相同的目录中
									//3.生成DeviceItemInfo对象并返回
									DeviceItemInfo item = WriteSixItem(dInfo.getDISPLAY_RESO().trim(), dInfo.getDEVICE_CODE(),
											filePath+fileName, "1",6,Collections.max(colorList));
									if (item != null && checkItemMap(dInfo.getDEVICE_CODE().trim(), item)) {
										item.setRoadLevel(Collections.max(colorList)+"");
										item.setSendType(dInfo.getSEND_TYPE());
										items.add(item);
										logger.debug(
												"模板六创建消息成功! equipment:" + dInfo.getDEVICE_CODE() + " 新图路径：" + buf.toString());
									}
								}
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

	private List<Integer> getOldColor(String provCode) {
		try {
			return modelSix.get(provCode)==null?new ArrayList<Integer>():modelSix.get(provCode);
		} catch (Exception e) {
			logger.error("遍历异常", e);
		}
		return new ArrayList<Integer>();
	}

	private void putColor(String provCode, List<Integer> list) {
		try {
			modelSix.remove(provCode);
			modelSix.put(provCode, list);
		} catch (Exception e) {
			logger.error("更新异常", e);
		}
	}

	/**
	 * 基于基图绘制 图片
	 * 
	 * @param city_code
	 * @param info
	 */
	private boolean changeImage(String dealPath,String dealName, String path, List<ModelSixInfo> info,ModelSixConfigInfo confInfo) {

		try {
			return ChangIMGColor.replaceImageColor(info, path, Color.white, dealPath,dealName,confInfo,null);
		} catch (IOException e) {
			logger.error("绘制基图异常", e);
		}
		return false;
	}

	private ModelSixInfo getImageParam(String city_code, ModelSixInfo info) {
		int color = getColor(city_code, info.getROAD_CODE());
		int time = getTime(city_code, info.getSTART_POINT(), info.getEND_POINT());
		if (color > 0 && time > 0) {
			switch (color) {
			case 1:
				info.setColor(Color.green);
				break;
			case 2:
				info.setColor(Color.yellow);
				break;
			default:
				info.setColor(Color.red);
				break;
			}
			String  times = String.valueOf(time / 60)+"分钟";
			info.setText(times);
			info.setIMG_COLOR(color);
			return info;
		}
		
		if(color==0&&time>0){
			info.setColor(Color.green);
			String  times = String.valueOf(time / 60)+"分钟";
			info.setText(times);
			info.setIMG_COLOR(1);
			return info;
		}
		return null;
	}

	/*private int getColor(String city_code, String road_code) {
		try {
			int color = 0;
			int all = 0;
			InduceStateRequest req = new InduceStateRequest();
			req.setCity(city_code);
			req.setRoadId(road_code);
			InduceStateResponse stateResp = GetInduceStateByAmap.getInduceState(req);
			if (stateResp != null) {
				logger.debug("调用高德接口! req:" + req.toString() + " 结果：" + stateResp.toString());
				if (stateResp.getStatus().getCode() == 0) {
					// 计算系数
					int num = 0;
					for (DataInfo d : stateResp.getData()) {
						if (d.getStatus() >= 2) {
							num += d.getLength() * d.getStatus();
							all += d.getLength();
						}
					}
					if (num > 0 && all > 0)
						color = num / all;
				} else {
					logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp.toString());
				}
			} else {
				logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp);
			}
			return color;
		} catch (Exception e) {
			logger.error("请求接口异常:", e);
		}
		return 0;
	}*/

	/*private int getTime(String city_code, String start_point, String end_point) {
		try {
			int allTime = 0;
			InduceTimeRequest treq = new InduceTimeRequest();
			treq.setCity(city_code);
			treq.setEndpos(start_point);
			treq.setStartpos(end_point);
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
				}
			} else {
				logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" + tresp);

			}
			return allTime;
		} catch (Exception e) {
			logger.error("请求接口异常:", e);
		}
		return 0;
	}*/
	public static void main(String[] args) {
		 	List<Integer> a = Arrays.asList(1, 2, 3, 4);
	        List<Integer> b = Arrays.asList(1, 2, 3, 4,5);
	        List<Integer> c=new ArrayList<Integer>();
//	        System.out.println(Collections.max(b)+"");
//	        System.out.println(Collections.max(c)+"");
	}
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
