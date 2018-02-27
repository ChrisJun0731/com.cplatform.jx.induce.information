package com.cplatform.jx.induce.information.model.info;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class ModelSixConfigInfo extends BaseModelConfigInfo {

	private String PIC_PATH;
	private List<ModelSixInfo> SIXINFOS; 

	
	private Integer fontColor;
	
	
	private Integer fontColor1;
	
	
	
	private String roadCode;
	
	private String roadCode1;
	
	
	private String startPoint;
	
	
	private String startPoint1;
	
	
	private String endPoint;
	
	
	private String endPoint1;
	
	
	private Integer weatherXPoint;
	
	
	private Integer weatherYPoint;
	private Integer windXPoint;
	
	
	private Integer windYPoint;
	
	
	private Integer weatherFontSize;
	
	
	
	
	


	public Integer getWeatherXPoint() {
		return weatherXPoint;
	}

	public void setWeatherXPoint(Integer weatherXPoint) {
		this.weatherXPoint = weatherXPoint;
	}

	public Integer getWeatherYPoint() {
		return weatherYPoint;
	}

	public void setWeatherYPoint(Integer weatherYPoint) {
		this.weatherYPoint = weatherYPoint;
	}

	public Integer getWindXPoint() {
		return windXPoint;
	}

	public void setWindXPoint(Integer windXPoint) {
		this.windXPoint = windXPoint;
	}

	public Integer getWindYPoint() {
		return windYPoint;
	}

	public void setWindYPoint(Integer windYPoint) {
		this.windYPoint = windYPoint;
	}

	public Integer getWeatherFontSize() {
		return weatherFontSize;
	}

	public void setWeatherFontSize(Integer weatherFontSize) {
		this.weatherFontSize = weatherFontSize;
	}

	public String getRoadCode() {
		return roadCode;
	}

	public void setRoadCode(String roadCode) {
		this.roadCode = roadCode;
	}

	public String getRoadCode1() {
		return roadCode1;
	}

	public void setRoadCode1(String roadCode1) {
		this.roadCode1 = roadCode1;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	public String getStartPoint1() {
		return startPoint1;
	}

	public void setStartPoint1(String startPoint1) {
		this.startPoint1 = startPoint1;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getEndPoint1() {
		return endPoint1;
	}

	public void setEndPoint1(String endPoint1) {
		this.endPoint1 = endPoint1;
	}

	public Integer getFontColor1() {
		return fontColor1;
	}

	public void setFontColor1(Integer fontColor1) {
		this.fontColor1 = fontColor1;
	}

	private Integer fontSize;
	
	private String textName;
	
	public String getTextName1() {
		return textName1;
	}

	public void setTextName1(String textName1) {
		this.textName1 = textName1;
	}

	public Integer getTextY1() {
		return textY1;
	}

	public void setTextY1(Integer textY1) {
		this.textY1 = textY1;
	}

	public Integer getTextX1() {
		return textX1;
	}

	public void setTextX1(Integer textX1) {
		this.textX1 = textX1;
	}

	private String textName1;
	
	public Integer getFontColor() {
		return fontColor;
	}

	public void setFontColor(Integer fontColor) {
		this.fontColor = fontColor;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	public Integer getTextX() {
		return textX;
	}

	public void setTextX(Integer textX) {
		this.textX = textX;
	}

	public Integer getTextY() {
		return textY;
	}

	public void setTextY(Integer textY) {
		this.textY = textY;
	}

	private Integer textX;
	
	private Integer textY;
	private Integer textY1;
	
	private Integer textX1;
	
	
	private Integer type;
	
	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPIC_PATH() {
		return PIC_PATH;
	}

	public void setPIC_PATH(String pIC_PATH) {
		PIC_PATH = pIC_PATH;
	}
	
	public List<ModelSixInfo> getSIXINFOS() {
		return SIXINFOS;
	}

	public void setSIXINFOS(List<ModelSixInfo> sIXINFOS) {
		SIXINFOS = sIXINFOS;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
