package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class EventResultInfo {

	private String result;//"true/false",     
    private String code;//"错误代码",     
    private String message;//"错误描述",     
    private String timestamp;//"服务器时间戳，1970 年到当前的秒数" }
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
    
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
