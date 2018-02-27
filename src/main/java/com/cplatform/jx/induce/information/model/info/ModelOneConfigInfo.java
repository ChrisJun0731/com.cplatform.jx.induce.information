package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 模板一配置参数. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年8月4日 下午3:05:05
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ModelOneConfigInfo extends BaseModelConfigInfo{

	private String END_NAME;
	
	public String getEND_NAME() {
		return END_NAME;
	}
	
	public void setEND_NAME(String eND_NAME) {
		END_NAME = eND_NAME;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
