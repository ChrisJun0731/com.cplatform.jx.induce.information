package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 高德路况请求. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月27日 下午2:02:52
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class InduceStateRequest {

	/**道路编码*/
	private String roadId;
	private String city;

	public String getRoadId() {
		return roadId;
	}

	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
