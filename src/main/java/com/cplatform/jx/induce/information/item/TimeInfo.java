package com.cplatform.jx.induce.information.item;

public class TimeInfo {

	/** X 坐标 */
	private int x;
	/** Y 坐标 */
	private int y;
	/** 显示区域宽度 */
	private int showWidth;
	/** 显示区域高度 */
	private int showHight;
	/**
	 * 字体 1-黑 2-楷 3-宋 4-仿 宋 5-隶书，
	 */
	private int fontType;
	/**
	 * 字号 字体像素大小，由宽度和 高度组成，各占两个字 符。如：0909、1616、3232 等
	 */
	private int fontSize;
	/**
	 * 字体风格 0-常规，1-加粗，2 倾斜，4-下划线，8 中划线
	 */
	private int fontStyle;
	/** 字体颜色 1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑 */
	private int fontColor;
	/** 背景颜色 */
	private int fontBackColor;
	/** 是否显示年，月，日，时，分，秒，星期 */
	private int isShowDate;
	/** 是否单行显示 */
	private int isShowSingle;
	/** 是否四位年显示 */
	private int isShowYear;
	/** 日期显示格 0–年月日； 1-日月年； 2-月日年 */
	private int showType;
	/** 播放时长 */
	private int playTime;
	/** 播放次数 */
	private int playNum;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getShowWidth() {
		return showWidth;
	}

	public void setShowWidth(int showWidth) {
		this.showWidth = showWidth;
	}

	public int getShowHight() {
		return showHight;
	}

	public void setShowHight(int showHight) {
		this.showHight = showHight;
	}

	public int getFontType() {
		return fontType;
	}

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getFontBackColor() {
		return fontBackColor;
	}

	public void setFontBackColor(int fontBackColor) {
		this.fontBackColor = fontBackColor;
	}

	public int getIsShowDate() {
		return isShowDate;
	}

	public void setIsShowDate(int isShowDate) {
		this.isShowDate = isShowDate;
	}

	public int getIsShowSingle() {
		return isShowSingle;
	}

	public void setIsShowSingle(int isShowSingle) {
		this.isShowSingle = isShowSingle;
	}

	public int getIsShowYear() {
		return isShowYear;
	}

	public void setIsShowYear(int isShowYear) {
		this.isShowYear = isShowYear;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public int getPlayNum() {
		return playNum;
	}

	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(this.x).append(",");
		buf.append(this.y).append(",");
		buf.append(this.showWidth).append(",");
		buf.append(this.showHight).append(",");
		buf.append(this.fontType).append(",");
		buf.append(this.fontSize).append(",");
		buf.append(this.fontStyle).append(",");
		buf.append(this.fontColor).append(",");
		buf.append(this.fontBackColor).append(",");
		buf.append(this.isShowDate).append(",");
		buf.append(this.isShowSingle).append(",");
		buf.append(this.isShowYear).append(",");
		buf.append(this.showType).append(",");
		buf.append(this.playTime).append(",");
		buf.append(this.playNum);
		return buf.toString();

	}

}
