package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 模板2 配置实体类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年8月7日 下午3:37:55
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ModelTwoConfigInfo extends BaseModelConfigInfo{

	private String START_NAME;
	private String END_NAME;
	private int SPEED;
	public String getSTART_NAME() {
		return START_NAME;
	}
	public String getEND_NAME() {
		return END_NAME;
	}
	public int getSPEED() {
		return SPEED;
	}
	public void setSTART_NAME(String sTART_NAME) {
		START_NAME = sTART_NAME;
	}
	public void setEND_NAME(String eND_NAME) {
		END_NAME = eND_NAME;
	}
	public void setSPEED(int sPEED) {
		SPEED = sPEED;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
