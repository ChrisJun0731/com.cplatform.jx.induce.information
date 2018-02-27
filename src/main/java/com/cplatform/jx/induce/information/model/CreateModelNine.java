package com.cplatform.jx.induce.information.model;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.model.info.ModelNineConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelNineConfigTextInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixInfo;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;
import com.cplatform.jx.induce.information.traffic.GetInduceTimeByAmap;
import com.cplatform.jx.induce.information.traffic.GetweatherByAlicity;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherDataInfo;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherForecastInfo;
import com.cplatform.jx.induce.information.traffic.info.AliWeatherInfo;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeResponse;
import com.cplatform.jx.induce.information.traffic.info.PathsInfo;
import com.cplatform.jx.induce.information.traffic.info.RouteInfo;
import com.cplatform.jx.induce.information.traffic.info.TimeDataInfo;
import com.cplatform.util2.TimeStamp;

public class CreateModelNine extends AbstractProcessThread {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	private Map<String, List<Integer>> modelSix = new HashMap<String, List<Integer>>();

	private Map<String, Integer> codeMap = null;

	public CreateModelNine() {
		init(1000 * 60 * 2+6000);
		// init(1000);
	}

	@Override
	protected void execTask() throws Exception {
		// 从数据库 获取模板9的任务，和参数配置
		List<ModelNineConfigInfo> lis = new ArrayList<ModelNineConfigInfo>();
		lis = ServiceDb.getModelNineConfigs(9);

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

		codeMap = new HashMap<String, Integer>();
		List<DeviceItemInfo> items = new ArrayList<DeviceItemInfo>();
		for (ModelNineConfigInfo info : lis) {
			for (TDeviceInfo dInfo : devices) {
				if (info.getDEVICE_CODE().equals(dInfo.getDEVICE_CODE())) {

					Boolean isEmergent = ServiceDb.isEmergent(info.getDEVICE_CODE());
					if (isEmergent == null || isEmergent == true) {
						logger.info("设备" + info.getDEVICE_CODE() + "在播放紧急消息");
						// 模板在播放紧急消息
						modelSix.remove(info.getDEVICE_CODE());
						break;
					}
					List<Integer> colorList = new ArrayList<Integer>();

					String time = TimeStamp.getTime(14);
					StringBuffer buf = new StringBuffer(100);
					buf.append(SysManager.getInstance().getTempImagePath());
					buf.append("/");
					buf.append("9/");
					buf.append(time.substring(0, 6));
					buf.append("/");
					buf.append(time.substring(6, 8));
					buf.append("/");
					buf.append(time.substring(8, 14));
					buf.append("/");
					String filePath = buf.toString();

					String fileName = info.getDEVICE_CODE() + "_" + dInfo.getDISPLAY_RESO().replace("*", "_") + "_9_"
							+ time.substring(8, 14) + ".png";
					if (info.getSIXINFOS() != null && (!info.getSIXINFOS().isEmpty())) {
						List<ModelSixInfo> result = new ArrayList<ModelSixInfo>();
						for (ModelSixInfo six : info.getSIXINFOS()) {
							ModelSixInfo sixinfo = getImageParam(info.getCITY_CODE(), six);
							if (sixinfo != null) {
								colorList.add(sixinfo.getIMG_COLOR());
								result.add(sixinfo);
							}
						}
						List<Integer> oldList = getOldColor(dInfo.getDEVICE_CODE());
						result = mergeTime(result, info.getModelText());
						if (!result.isEmpty()) {
							if (!compare(colorList, oldList)) {
								ModelSixConfigInfo sixConf = new ModelSixConfigInfo();
								sixConf.setTextX(info.getTextX());
								sixConf.setTextY(info.getTextY());
								sixConf.setTextX1(info.getTextX1());
								sixConf.setTextY1(info.getTextY1());
								sixConf.setTextName(info.getTextName());
								sixConf.setTextName1(info.getTextName1());
								sixConf.setType(9);
								sixConf.setWeatherXPoint(info.getWeatherXPoint());
								sixConf.setWeatherYPoint(info.getWeatherYPoint());
								sixConf.setWindXPoint(info.getWindXPoint());
								sixConf.setWindYPoint(info.getWindYPoint());
								sixConf.setFontSize(info.getFontSize());
								sixConf.setWeatherFontSize(info.getWeatherFontSize());

								// 处理
								// 调用 高德1
								// 0，1，2，3
								int color = 0;
								if (codeMap.get(info.getRoadCode()) != null) {
									color = codeMap.get(info.getRoadCode());
								} else {
									color = getColor(info.getCITY_CODE(), info.getRoadCode());
									codeMap.put(info.getRoadCode(), color);
								}

								// 调用 高德2
								int allTime = 0;
								InduceTimeRequest treq = new InduceTimeRequest();
								treq.setCity(info.getCITY_CODE());
								treq.setEndpos(info.getEndPoint());
								treq.setStartpos(info.getStartPoint());
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
									long dtime = 0;
									dtime = Math.round(allTime / 60);

									// 生成自动信息
									String[] cont = new String[2];
									String txt = "";
									switch (color) {
									case 1:
										txt = "畅通";
										break;
									case 2:
										txt = "缓行";
										break;
									case 3:
										txt = "拥堵";
										break;
									case 4:
										txt = "拥堵";
										break;
									default:
										txt = "缓行";
										break;
									}

									cont[0] = info.getTextName() + txt + " " + "行驶时间" + dtime + "分钟";

									sixConf.setTextName(cont[0]);
									sixConf.setFontColor(color);

									// 处理
									// 调用 高德1
									// 0，1，2，3
									int color1 = 0;
									if (codeMap.get(info.getRoadCode1()) != null) {
										color1 = codeMap.get(info.getRoadCode1());
									} else {

										color1 = getColor(info.getCITY_CODE(), info.getRoadCode1());
										codeMap.put(info.getRoadCode1(), color1);
									}
									// 调用 高德2
									int allTime1 = 0;
									InduceTimeRequest treq1 = new InduceTimeRequest();
									treq1.setCity(info.getCITY_CODE());
									treq1.setEndpos(info.getEndPoint1());
									treq1.setStartpos(info.getStartPoint1());
									treq1.setStrategy("0");

									InduceTimeResponse tresp1 = GetInduceTimeByAmap.getInduceState(treq1);
									if (tresp1 != null) {
										logger.debug("调用高德接口2! req:" + treq1.toString() + " 结果：" + tresp1.toString());
										if (tresp1.getStatus().getCode() == 0) {
											TimeDataInfo td = tresp1.getData();
											RouteInfo rinfo = td.getRoute();
											if (rinfo != null) {
												List<PathsInfo> path = rinfo.getPaths();
												if (path != null && (!path.isEmpty())) {
													for (PathsInfo pinfo : path) {
														pinfo.getDistance();
														allTime1 += Integer.valueOf(pinfo.getDuration());
													}

												}
											}
										} else {
											logger.debug(
													"调用高德接口2 异常! req:" + treq1.toString() + " 结果：" + tresp1.toString());
											break;
										}
									} else {
										logger.debug("调用高德接口2 异常! req:" + treq1.toString() + " 结果：" + tresp1);
										break;
									}

									if (allTime1 > 0 && color1 > 0) {
										long dtime1 = 0;
										dtime1 = Math.round(allTime1 / 60);

										// 生成自动信息
										String[] cont1 = new String[2];
										String txt1 = "";
										switch (color1) {
										case 1:
											txt1 = "畅通";
											break;
										case 2:
											txt1 = "缓行";
											break;
										case 3:
											txt1 = "拥堵";
											break;
										case 4:
											txt1 = "拥堵";
											break;
										default:
											txt1 = "缓行";
											break;
										}

										cont1[0] = info.getTextName1() + txt1 + " " + "行驶时间" + dtime1 + "分钟";
										sixConf.setTextName1(cont1[0]);
										sixConf.setFontColor1(color1);
									}

									String serviceKey = SysManager.getInstance().getAliAppCode();
									String cityId = SysManager.getInstance().getAliCityId();

									AliWeatherInfo weatherInfo = GetweatherByAlicity.getWeather(serviceKey, cityId);
									String[] weaInfo = new String[2];
									if (weatherInfo != null) {
										if (StringUtils.equals(weatherInfo.getCode(), "0")) {
											AliWeatherDataInfo dataInfo = weatherInfo.getData();
											List<AliWeatherForecastInfo> forecelist = dataInfo.getForecast();
											if (forecelist != null && forecelist.size() > 0) {
												AliWeatherForecastInfo info2 = forecelist.get(0);
												String c1 = info2.getConditionDay() + " " + info2.getTempDay() + "℃";
												String c2 = info2.getWindDirDay() + info2.getWindLevelDay() + "米/秒";
												weaInfo[0] = c1;
												weaInfo[1] = c2;
											}
										}
									}

									boolean ret = changeImage(filePath, fileName, info.getPIC_PATH(), result, sixConf,
											weaInfo);
									if (ret) {
										putColor(dInfo.getDEVICE_CODE(), colorList);
										DeviceItemInfo item = WriteSixItem(dInfo.getDISPLAY_RESO().trim(),
												dInfo.getDEVICE_CODE(), filePath + fileName, "1", 9,
												Collections.max(colorList));
										if (item != null && checkItemMap(dInfo.getDEVICE_CODE().trim(), item)) {
											item.setRoadLevel(Collections.max(colorList) + "");
											item.setSendType(dInfo.getSEND_TYPE());
											items.add(item);
											logger.debug("模板九创建消息成功! equipment:" + dInfo.getDEVICE_CODE() + " 新图路径："
													+ buf.toString());
										}
									}
								}
							}
						}
						break;
					}
				}
			}

		}
		codeMap = null;
		// 保存自动信息
		if (items != null && (!items.isEmpty()))
			ServiceDb.SavePushItemWithSingle(items);
	}

	private List<Integer> getOldColor(String provCode) {
		try {
			return modelSix.get(provCode) == null ? new ArrayList<Integer>() : modelSix.get(provCode);
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
	private boolean changeImage(String dealPath, String dealName, String path, List<ModelSixInfo> info,
			ModelSixConfigInfo contInfo, String[] weaInfo) {

		try {
			return ChangIMGColor.replaceImageColor(info, path, Color.white, dealPath, dealName, contInfo, weaInfo);
		} catch (IOException e) {
			logger.error("绘制基图异常", e);
		}
		return false;
	}

	private ModelSixInfo getImageParam(String city_code, ModelSixInfo info) {
		int color = 0;
		if (codeMap.get(info.getROAD_CODE()) != null) {
			color = codeMap.get(info.getROAD_CODE());
		} else {

			color = getColor(city_code, info.getROAD_CODE());
			codeMap.put(info.getROAD_CODE(), color);
		}
		// int time = getTime(city_code, info.getSTART_POINT(), info.getEND_POINT());
		int time = 1;

		if (color > 0 && time > 0) {
			switch (color) {
			case 1:
				info.setColor(new Color(0x00FF00));
				break;
			case 2:
				info.setColor(new Color(0xFFFF00));
				break;
			default:
				info.setColor(new Color(0xFF0000));
				break;
			}
			info.setText("" + time / 60);
			info.setIMG_COLOR(color);
			return info;
		}
		if (color == 0 && time > 0) {
			info.setColor(new Color(0x00FF00));
			info.setText("" + time / 60);
			info.setIMG_COLOR(1);
			return info;
		}
		return null;
	}

	/**
	 * 合并时间
	 * 
	 * @param lis
	 * @param txt
	 * @return
	 */
	private List<ModelSixInfo> mergeTime(List<ModelSixInfo> lis, List<ModelNineConfigTextInfo> txt) {
		List<ModelSixInfo> ret = new ArrayList<ModelSixInfo>();
		for (ModelNineConfigTextInfo info : txt) {
			int alltime = 0;
			if (info.getTYPE() == 1)
				continue;
			String[] pid = info.getPERIOD_NUM().split(",");
			if (pid == null || pid.length == 0)
				continue;
			int length = pid.length;
			for (int i = 0; i < length; i++) {
				ModelSixInfo model = getModelsixInfo(lis, Integer.valueOf(pid[i]));
				if (model != null) {
					alltime += Integer.valueOf(model.getText());
				}
			}

			if (alltime > 0) {
				ModelSixInfo model = new ModelSixInfo();
				model.setTEXT_PX_X(info.getTEXT_PX_X());
				model.setTEXT_PX_Y(info.getTEXT_PX_Y());
				model.setText("" + alltime + "分钟");
				ret.add(model);
			}

		}

		return getResult(lis, ret);
	}

	private List<ModelSixInfo> getResult(List<ModelSixInfo> oldlis, List<ModelSixInfo> lis) {

		for (int i = 0; i < lis.size(); i++) {
			ModelSixInfo temp = oldlis.get(0);
			temp.setTEXT_PX_X(lis.get(i).getTEXT_PX_X());
			temp.setTEXT_PX_Y(lis.get(i).getTEXT_PX_Y());
			temp.setText(lis.get(i).getText());
			oldlis.remove(0);
			oldlis.add(temp);
		}
		return oldlis;
	}

	private ModelSixInfo getModelsixInfo(List<ModelSixInfo> lis, int id) {
		for (ModelSixInfo info : lis) {
			if (info.getID() == id)
				return info;
		}
		return null;
	}

	// private int getColor(String city_code, String road_code) {
	// try {
	// int color = 0;
	// int all = 0;
	// InduceStateRequest req = new InduceStateRequest();
	// req.setCity(city_code);
	// req.setRoadId(road_code);
	// InduceStateResponse stateResp = GetInduceStateByAmap.getInduceState(req);
	// if (stateResp != null) {
	// logger.debug("调用高德接口! req:" + req.toString() + " 结果：" +
	// stateResp.toString());
	// if (stateResp.getStatus().getCode() == 0) {
	// // 计算系数
	// int num = 0;
	// for (DataInfo d : stateResp.getData()) {
	//// if (d.getStatus() >= 2) {
	// num += d.getLength() * d.getStatus();
	// all += d.getLength();
	//// }
	// }
	// if (num > 0 && all > 0)
	// color = num / all;
	// } else {
	// logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" +
	// stateResp.toString());
	// }
	// } else {
	// logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp);
	// }
	// return color;
	// } catch (Exception e) {
	// logger.error("请求接口异常:", e);
	// }
	// return 0;
	// }

	// private int getTime(String city_code, String start_point, String end_point) {
	// try {
	// int allTime = 0;
	// InduceTimeRequest treq = new InduceTimeRequest();
	// treq.setCity(city_code);
	// treq.setEndpos(start_point);
	// treq.setStartpos(end_point);
	// treq.setStrategy("0");
	//
	// InduceTimeResponse tresp = GetInduceTimeByAmap.getInduceState(treq);
	// if (tresp != null) {
	// logger.debug("调用高德接口2! req:" + treq.toString() + " 结果：" + tresp.toString());
	// if (tresp.getStatus().getCode() == 0) {
	// TimeDataInfo td = tresp.getData();
	// RouteInfo rinfo = td.getRoute();
	// if (rinfo != null) {
	// List<PathsInfo> path = rinfo.getPaths();
	// if (path != null && (!path.isEmpty())) {
	// for (PathsInfo pinfo : path) {
	// pinfo.getDistance();
	// allTime += Integer.valueOf(pinfo.getDuration());
	// }
	//
	// }
	// }
	// } else {
	// logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" +
	// tresp.toString());
	// }
	// } else {
	// logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" + tresp);
	//
	// }
	// return allTime;
	// } catch (Exception e) {
	// logger.error("请求接口异常:", e);
	// }
	// return 0;
	// }
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
		if (a.size() != b.size())
			return false;
		// Collections.sort(a);
		// Collections.sort(b);
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i)))
				return false;
		}
		return true;
	}
}
