package com.cplatform.jx.induce.information.model.info;

import com.alibaba.fastjson.JSON;

/**
 * 模板 13 配置实体类.
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
public class ModelThirteenConfigInfo extends BaseModelConfigInfo {

	private String START_NAME;
	public String getSTART_NAME() {
		return START_NAME;
	}

	public void setSTART_NAME(String sTART_NAME) {
		START_NAME = sTART_NAME;
	}

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
