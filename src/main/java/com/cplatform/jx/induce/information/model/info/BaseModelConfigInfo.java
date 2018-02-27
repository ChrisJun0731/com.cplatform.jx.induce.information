package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 模板基础配置实体类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年8月7日 下午3:38:43
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class BaseModelConfigInfo {

	/**设备编码*/
	private String DEVICE_CODE;
	/**起始坐标*/
	private String START_POINT;
	/**终点坐标*/
	private String END_POINT;
	/**城市编码*/
	private String CITY_CODE;
	/**道理编码*/
	private String ROAD_CODE;
	/**距离*/
	private String DISTANCE;
	/**状态*/
	private int STATUS;
	
	private String  FONT_SIZE;
	
	
	
	public String getFONT_SIZE() {
		return FONT_SIZE;
	}
	public void setFONT_SIZE(String fONT_SIZE) {
		FONT_SIZE = fONT_SIZE;
	}
	public String getDEVICE_CODE() {
		return DEVICE_CODE;
	}
	public String getSTART_POINT() {
		return START_POINT;
	}
	public String getEND_POINT() {
		return END_POINT;
	}
	public String getCITY_CODE() {
		return CITY_CODE;
	}
	public String getROAD_CODE() {
		return ROAD_CODE;
	}
	public String getDISTANCE() {
		return DISTANCE;
	}
	public int getSTATUS() {
		return STATUS;
	}
	public void setDEVICE_CODE(String dEVICE_CODE) {
		DEVICE_CODE = dEVICE_CODE;
	}
	public void setSTART_POINT(String sTART_POINT) {
		START_POINT = sTART_POINT;
	}
	public void setEND_POINT(String eND_POINT) {
		END_POINT = eND_POINT;
	}
	public void setCITY_CODE(String cITY_CODE) {
		CITY_CODE = cITY_CODE;
	}
	public void setROAD_CODE(String rOAD_CODE) {
		ROAD_CODE = rOAD_CODE;
	}
	public void setDISTANCE(String dISTANCE) {
		DISTANCE = dISTANCE;
	}
	public void setSTATUS(int iTATUS) {
		STATUS = iTATUS;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
