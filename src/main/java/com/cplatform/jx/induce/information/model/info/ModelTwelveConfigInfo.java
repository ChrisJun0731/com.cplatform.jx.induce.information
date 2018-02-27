package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 模板 7 配置实体类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年8月7日 下午3:47:49
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ModelTwelveConfigInfo extends BaseModelConfigInfo {

	private String ROAD_NAME;
	private String ROAD_NAME2;
	private String ROAD_NAME3;
	private String START_POINT2;
	private String START_POINT3;
	private String END_POINT2;
	private String END_POINT3;
	private String ROAD_CODE2;
	private String ROAD_CODE3;
	
	public String getROAD_NAME() {
		return ROAD_NAME;
	}

	public void setROAD_NAME(String rOAD_NAME) {
		ROAD_NAME = rOAD_NAME;
	}
	
	
	public String getROAD_NAME2() {
		return ROAD_NAME2;
	}

	public String getROAD_NAME3() {
		return ROAD_NAME3;
	}

	public String getSTART_POINT2() {
		return START_POINT2;
	}

	public String getSTART_POINT3() {
		return START_POINT3;
	}

	public String getEND_POINT2() {
		return END_POINT2;
	}

	public String getEND_POINT3() {
		return END_POINT3;
	}

	public String getROAD_CODE2() {
		return ROAD_CODE2;
	}

	public String getROAD_CODE3() {
		return ROAD_CODE3;
	}

	public void setROAD_NAME2(String rOAD_NAME2) {
		ROAD_NAME2 = rOAD_NAME2;
	}

	public void setROAD_NAME3(String rOAD_NAME3) {
		ROAD_NAME3 = rOAD_NAME3;
	}

	public void setSTART_POINT2(String sTART_POINT2) {
		START_POINT2 = sTART_POINT2;
	}

	public void setSTART_POINT3(String sTART_POINT3) {
		START_POINT3 = sTART_POINT3;
	}

	public void setEND_POINT2(String eND_POINT2) {
		END_POINT2 = eND_POINT2;
	}

	public void setEND_POINT3(String eND_POINT3) {
		END_POINT3 = eND_POINT3;
	}

	public void setROAD_CODE2(String rOAD_CODE2) {
		ROAD_CODE2 = rOAD_CODE2;
	}

	public void setROAD_CODE3(String rOAD_CODE3) {
		ROAD_CODE3 = rOAD_CODE3;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
