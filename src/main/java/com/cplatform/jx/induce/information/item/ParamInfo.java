package com.cplatform.jx.induce.information.item;

/**
 * 
 * item param 参数. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月17日 下午5:01:45
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ParamInfo {

	/** 停留时间 */
	private int stayTime;
	/**
	 * 入屏方式 1-立即显示 2-上展 3 下展 4-左展 5-右展 6 上移 7-下移 8-左移 9 右移 10－淡入（入屏）
	 * 淡出（出屏）255-随机
	 */
	private int inScreenMode;
	/** 出屏方式 */
	private int outScreenMode;
	/** 入屏速度 */
	private int inScreenSpeed;
	/** 闪烁速度 0-4 */
	private int flashingSpeed;
	/** 闪烁次数 */
	private int flashingNum;
	/** 播放次数 */
	private int playNum;

	/**
	 * 停留时间
	 * 
	 * @return
	 */
	public int getStayTime() {
		return stayTime;
	}

	/**
	 * 停留时间
	 * 
	 * @param stayTime
	 */
	public void setStayTime(int stayTime) {
		this.stayTime = stayTime;
	}

	/**
	 * 入屏方式
	 * 
	 * @return
	 */
	public int getInScreenMode() {
		return inScreenMode;
	}

	/**
	 * 入屏方式
	 * 
	 * @param inScreenMode
	 */
	public void setInScreenMode(int inScreenMode) {
		this.inScreenMode = inScreenMode;
	}

	/**
	 * 出屏方式
	 * 
	 * @return
	 */
	public int getOutScreenMode() {
		return outScreenMode;
	}

	/**
	 * 出屏方式
	 * 
	 * @param outScreenMode
	 */
	public void setOutScreenMode(int outScreenMode) {
		this.outScreenMode = outScreenMode;
	}

	/**
	 * 入屏速度
	 * 
	 * @return
	 */
	public int getInScreenSpeed() {
		return inScreenSpeed;
	}

	/**
	 * 入屏速度
	 * 
	 * @param inScreenSpeed
	 */
	public void setInScreenSpeed(int inScreenSpeed) {
		this.inScreenSpeed = inScreenSpeed;
	}

	/**
	 * 闪烁速度
	 * 
	 * @return
	 */
	public int getFlashingSpeed() {
		return flashingSpeed;
	}

	/**
	 * 闪烁速度
	 * 
	 * @param flashingSpeed
	 */
	public void setFlashingSpeed(int flashingSpeed) {
		this.flashingSpeed = flashingSpeed;
	}

	/**
	 * 闪烁次数
	 * 
	 * @return
	 */
	public int getFlashingNum() {
		return flashingNum;
	}

	/**
	 * 闪烁次数
	 * 
	 * @param flashingNum
	 */
	public void setFlashingNum(int flashingNum) {
		this.flashingNum = flashingNum;
	}

	/**
	 * 播放次数
	 * 
	 * @return
	 */
	public int getPlayNum() {
		return playNum;
	}

	/**
	 * 播放次数
	 * 
	 * @param playNum
	 */
	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(this.stayTime).append(",");
		buf.append(this.inScreenMode).append(",");
		buf.append(this.outScreenMode).append(",");
		buf.append(this.inScreenSpeed).append(",");
		buf.append(this.flashingSpeed).append(",");
		buf.append(this.flashingNum).append(",");
		buf.append(this.playNum);
		return buf.toString();

	}
}
