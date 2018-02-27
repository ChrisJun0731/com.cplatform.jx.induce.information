package com.cplatform.jx.induce.information.item;

public class TxtextInfo {

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
	/**水平对齐 0-靠左 1-靠右 2-居中 */
	private int horizontal;
	/**垂直对齐 0-靠左 1-靠右 2-居中*/
	private int vertical;
	/**行距*/
	private int line;
	/**字距*/
	private int space;
	/** 字体颜色 1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑 */
	private int fontColor;
	/** 背景颜色 */
	private int fontBackColor;
	/**特效类型*/
	private int specificType;
	/**特效速度*/
	private int specificSpeed;
	/** 播放时长 */
	private int playTime;
	/** 播放次数 */
	private int playNum;
	/**文本内容*/
	private String content;
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
	public int getHorizontal() {
		return horizontal;
	}
	public void setHorizontal(int horizontal) {
		this.horizontal = horizontal;
	}
	public int getVertical() {
		return vertical;
	}
	public void setVertical(int vertical) {
		this.vertical = vertical;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getSpace() {
		return space;
	}
	public void setSpace(int space) {
		this.space = space;
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
	public int getSpecificType() {
		return specificType;
	}
	public void setSpecificType(int specificType) {
		this.specificType = specificType;
	}
	public int getSpecificSpeed() {
		return specificSpeed;
	}
	public void setSpecificSpeed(int specificSpeed) {
		this.specificSpeed = specificSpeed;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
		buf.append(this.horizontal).append(",");
		buf.append(this.vertical).append(",");
		buf.append(this.line).append(",");
		buf.append(this.space).append(",");
		buf.append(this.fontColor).append(",");
		buf.append(this.fontBackColor).append(",");
		buf.append(this.specificType).append(",");
		buf.append(this.specificSpeed).append(",");;
		buf.append(this.playTime).append(",");
		buf.append(this.playNum).append(",");
		buf.append(this.content);
		return buf.toString();

	}
}
