package com.cplatform.jx.induce.information.item;

public class ImgParamInfo {

	/**停留时间  (单位为 0.1 秒)*/
	private int stopTime;

	public int getStopTime() {
		return stopTime;
	}

	public void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(this.stopTime);
		return buf.toString();

	}
}
