package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

/**
 * 模板 10 配置实体类.
 * Title.<br>
 * Description.
 * <p>
 * Copyright: Copyright (c) 2017年11月6日 上午9:32:50
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * Author: shixw
 * <p>
 * Version: 1.0
 * <p>
 */
public class ModelTenConfigInfo extends BaseModelConfigInfo {

	private String ROAD_NAME;
	private String ROAD_NAME2;
	private String SECOND_NAME;
	private String SECOND_NAME2;
	private String START_POINT;
	private String START_POINT2;
	private String END_POINT;
	private String END_POINT2;
	private String ROAD_CODE;
	private String ROAD_CODE2;

	
	public String getROAD_NAME() {
		return ROAD_NAME;
	}


	public void setROAD_NAME(String rOAD_NAME) {
		ROAD_NAME = rOAD_NAME;
	}


	public String getROAD_NAME2() {
		return ROAD_NAME2;
	}


	public void setROAD_NAME2(String rOAD_NAME2) {
		ROAD_NAME2 = rOAD_NAME2;
	}


	public String getSECOND_NAME() {
		return SECOND_NAME;
	}


	public void setSECOND_NAME(String sECOND_NAME) {
		SECOND_NAME = sECOND_NAME;
	}


	public String getSECOND_NAME2() {
		return SECOND_NAME2;
	}


	public void setSECOND_NAME2(String sECOND_NAME2) {
		SECOND_NAME2 = sECOND_NAME2;
	}


	public String getSTART_POINT() {
		return START_POINT;
	}


	public void setSTART_POINT(String sTART_POINT) {
		START_POINT = sTART_POINT;
	}


	public String getSTART_POINT2() {
		return START_POINT2;
	}


	public void setSTART_POINT2(String sTART_POINT2) {
		START_POINT2 = sTART_POINT2;
	}


	public String getEND_POINT() {
		return END_POINT;
	}


	public void setEND_POINT(String eND_POINT) {
		END_POINT = eND_POINT;
	}


	public String getEND_POINT2() {
		return END_POINT2;
	}


	public void setEND_POINT2(String eND_POINT2) {
		END_POINT2 = eND_POINT2;
	}


	public String getROAD_CODE() {
		return ROAD_CODE;
	}


	public void setROAD_CODE(String rOAD_CODE) {
		ROAD_CODE = rOAD_CODE;
	}


	public String getROAD_CODE2() {
		return ROAD_CODE2;
	}


	public void setROAD_CODE2(String rOAD_CODE2) {
		ROAD_CODE2 = rOAD_CODE2;
	}


	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
