package com.cplatform.jx.induce.information.item;

public class TxtInfo {

	/**X 坐标*/
	private int x;
	/**Y 坐标*/
	private int y;
	/**字体 1-黑 2-楷 3-宋 4-仿
          宋 5-隶书，*/
	private int fontType;
	/**字号  字体像素大小，由宽度和
            高度组成，各占两个字
             符。如：0909、1616、3232
             等 */
	private int fontSize;
	/**字体颜色  1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑*/
	private int fontColor; 
	/**背景颜色 */
	private int fontBackColor;
	/**闪烁*/
	private int flashing;
	/**文字内容*/
	private String content;
	/**显示区域宽度*/
	private int showWidth;
	/**显示区域高度*/
	private int showHight;
	/**字体风格  0-常规，1-加粗，2
倾斜，4-下划线，8
中划线*/
	private int fontStyle;
	
	private TxtParamInfo txtParam;
	
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
	public int getFlashing() {
		return flashing;
	}
	public void setFlashing(int flashing) {
		this.flashing = flashing;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public int getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	
	
	public TxtParamInfo getTxtParam() {
		return txtParam;
	}
	public void setTxtParam(TxtParamInfo txtParam) {
		this.txtParam = txtParam;
	}
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(this.x).append(",");
		buf.append(this.y).append(",");
		buf.append(this.fontType).append(",");
		buf.append(this.fontSize).append(",");
		buf.append(this.fontColor).append(",");
		buf.append(this.fontBackColor).append(",");
		buf.append(this.flashing).append(",");
		buf.append(this.content).append(",");
		buf.append(this.showWidth).append(",");
		buf.append(this.showHight).append(",");
		buf.append(this.fontStyle);
		return buf.toString();

	}
	
	
}
