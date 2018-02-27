package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class EventDataInfo {

	private String sourceId;
	private String id;
	private int stateFlag;
	private String type;
	private String subType;
	private String cityCode;
	private EventPositionInfo position;
	private int level;
	private EventDurationInfo duration;
	private String desc;
	private int infoSourceType;
	private String picUrl;
	private String audioUrl;
	private String videoUrl;
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStateFlag() {
		return stateFlag;
	}
	public void setStateFlag(int stateFlag) {
		this.stateFlag = stateFlag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public EventPositionInfo getPosition() {
		return position;
	}
	public void setPosition(EventPositionInfo position) {
		this.position = position;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public EventDurationInfo getDuration() {
		return duration;
	}
	public void setDuration(EventDurationInfo duration) {
		this.duration = duration;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getInfoSourceType() {
		return infoSourceType;
	}
	public void setInfoSourceType(int infoSourceType) {
		this.infoSourceType = infoSourceType;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
