package com.cplatform.jx.induce.information.device;

import java.util.List;

import com.cplatform.jx.induce.information.item.ImgInfo;
import com.cplatform.jx.induce.information.item.ParamInfo;
import com.cplatform.jx.induce.information.item.TxtInfo;


/**
 * 自动信息模板
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年4月5日 下午4:08:30
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class PlayListItemInfo {

	private ParamInfo param;
	private ImgInfo img;
	private List<TxtInfo> txt;
	private String screenType;
	private String screenDesc;
	public ParamInfo getParam() {
		return param;
	}
	public void setParam(ParamInfo param) {
		this.param = param;
	}
	public ImgInfo getImg() {
		return img;
	}
	public void setImg(ImgInfo img) {
		this.img = img;
	}
	public List<TxtInfo> getTxt() {
		return txt;
	}
	public void setTxt(List<TxtInfo> txt) {
		this.txt = txt;
	}
	public String getScreenType() {
		return screenType;
	}
	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	public String getScreenDesc() {
		return screenDesc;
	}
	public void setScreenDesc(String screenDesc) {
		this.screenDesc = screenDesc;
	}
	
	
}
