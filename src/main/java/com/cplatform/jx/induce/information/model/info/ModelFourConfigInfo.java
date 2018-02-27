package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

public class ModelFourConfigInfo extends BaseModelConfigInfo{

	private String END_NAME;
	private String REC_ROAD_NAME;
	private String REC_POINT;
	private int TYPE;
	
	
	public String getEND_NAME() {
		return END_NAME;
	}




	public String getREC_ROAD_NAME() {
		return REC_ROAD_NAME;
	}




	public String getREC_POINT() {
		return REC_POINT;
	}




	public void setEND_NAME(String eND_NAME) {
		END_NAME = eND_NAME;
	}




	public void setREC_ROAD_NAME(String rEC_ROAD_NAME) {
		REC_ROAD_NAME = rEC_ROAD_NAME;
	}




	public void setREC_POINT(String rEC_POINT) {
		REC_POINT = rEC_POINT;
	}




	public int getTYPE() {
		return TYPE;
	}




	public void setTYPE(int tYPE) {
		TYPE = tYPE;
	}




	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
