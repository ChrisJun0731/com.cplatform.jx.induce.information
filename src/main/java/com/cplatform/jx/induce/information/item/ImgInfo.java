package com.cplatform.jx.induce.information.item;

public class ImgInfo {

	/**X 坐标*/
	private int x;
	/**Y 坐标*/
	private int y;
	/**文件名称*/
	private String fileName;
	/**闪烁*/
	private int param;
	/**显示区域宽度*/
	private int showWidth;
	/**显示区域高度*/
	private int showHight;
	
	private ImgParamInfo imgParam;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getParam() {
		return param;
	}
	public void setParam(int param) {
		this.param = param;
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
	
	public ImgParamInfo getImgParam() {
		return imgParam;
	}
	public void setImgParam(ImgParamInfo imgParam) {
		this.imgParam = imgParam;
	}
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(this.x).append(",");
		buf.append(this.y).append(",");
		buf.append(this.fileName).append(",");
		buf.append(0).append(",");
		buf.append(this.showWidth).append(",");
		buf.append(this.showHight);
		return buf.toString();

	}
}
