package com.cplatform.jx.induce.information.model.info;

import java.util.List;

public class ModelItemInfo {

	private List<ImgsInfo> imgs; 
	private List<TextImgsInfo> textImgs; 
	private List<TextsInfo> texts; 
	private List<TimesInfo> times; 
	private List<VideosInfo> videos;
	
	public List<ImgsInfo> getImgs() {
		return imgs;
	}
	public List<TextImgsInfo> getTextImgs() {
		return textImgs;
	}
	public List<TextsInfo> getTexts() {
		return texts;
	}
	public List<TimesInfo> getTimes() {
		return times;
	}
	public List<VideosInfo> getVideos() {
		return videos;
	}
	public void setImgs(List<ImgsInfo> imgs) {
		this.imgs = imgs;
	}
	public void setTextImgs(List<TextImgsInfo> textImgs) {
		this.textImgs = textImgs;
	}
	public void setTexts(List<TextsInfo> texts) {
		this.texts = texts;
	}
	public void setTimes(List<TimesInfo> times) {
		this.times = times;
	}
	public void setVideos(List<VideosInfo> videos) {
		this.videos = videos;
	} 
	
	
}
