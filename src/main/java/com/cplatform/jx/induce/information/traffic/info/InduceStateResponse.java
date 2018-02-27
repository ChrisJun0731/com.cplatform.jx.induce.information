package com.cplatform.jx.induce.information.traffic.info;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 高德路况响应. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月27日 下午2:02:34
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class InduceStateResponse {

	private String sid;
	private StatusInfo status;
	private List<DataInfo> data;
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public StatusInfo getStatus() {
		return status;
	}
	public void setStatus(StatusInfo status) {
		this.status = status;
	}
	public List<DataInfo> getData() {
		return data;
	}
	public void setData(List<DataInfo> data) {
		this.data = data;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
