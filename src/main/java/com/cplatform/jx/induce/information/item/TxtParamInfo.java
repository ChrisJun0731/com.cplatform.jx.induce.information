package com.cplatform.jx.induce.information.item;

public class TxtParamInfo {

	/**字符间距*/
	private int space;
	/**排列方式  0-横向排列， 1-纵向排列 */
	private int orderType;
	public int getSpace() {
		return space;
	}
	public void setSpace(int space) {
		this.space = space;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(this.space).append(",");
		buf.append(this.orderType);
		return buf.toString();

	}
}
