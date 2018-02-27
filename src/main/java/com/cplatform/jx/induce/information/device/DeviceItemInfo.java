package com.cplatform.jx.induce.information.device;

/**
 * 
 * 自动消息发布对象. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年4月5日 下午5:23:52
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class DeviceItemInfo {

	private String PLAY_TIME;
	private String ITEM_PATH;
	private String INFO_TYPE;
	private String INFO_SOURCE;
	private String START_TIME;
	private String END_TIME;
	private String CREATE_TIME;
	private String DEVICE_CODE;
	
	private String roadLevel;
	
	private String sendType;
	
	/**
	 * 拥堵级别1畅通2缓行3拥堵
	 * @return
	 */
	public String getRoadLevel() {
		return roadLevel;
	}
	public void setRoadLevel(String roadLevel) {
		this.roadLevel = roadLevel;
	}
	/***
	 * 推送类型  1:手动 2:自动
	 * @return
	 */
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	/**内容类别*/
	private int contType;

	public String getPLAY_TIME() {
		return PLAY_TIME;
	}
	public void setPLAY_TIME(String pLAY_TIME) {
		PLAY_TIME = pLAY_TIME;
	}
	public String getITEM_PATH() {
		return ITEM_PATH;
	}
	public void setITEM_PATH(String iTEM_PATH) {
		ITEM_PATH = iTEM_PATH;
	}
	public String getINFO_TYPE() {
		return INFO_TYPE;
	}
	public void setINFO_TYPE(String iNFO_TYPE) {
		INFO_TYPE = iNFO_TYPE;
	}
	public String getINFO_SOURCE() {
		return INFO_SOURCE;
	}
	public void setINFO_SOURCE(String iNFO_SOURCE) {
		INFO_SOURCE = iNFO_SOURCE;
	}
	public String getSTART_TIME() {
		return START_TIME;
	}
	public void setSTART_TIME(String sTART_TIME) {
		START_TIME = sTART_TIME;
	}
	public String getEND_TIME() {
		return END_TIME;
	}
	public void setEND_TIME(String eND_TIME) {
		END_TIME = eND_TIME;
	}
	public String getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(String cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public int getContType() {
		return contType;
	}
	public void setContType(int contType) {
		this.contType = contType;
	}
	public String getDEVICE_CODE() {
		return DEVICE_CODE;
	}
	public void setDEVICE_CODE(String dEVICE_CODE) {
		DEVICE_CODE = dEVICE_CODE;
	}

	
	
}
