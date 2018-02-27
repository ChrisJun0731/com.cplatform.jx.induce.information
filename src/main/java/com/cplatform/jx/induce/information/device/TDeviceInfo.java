package com.cplatform.jx.induce.information.device;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 设备信息
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年4月5日 下午1:57:09
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class TDeviceInfo {

		private int id;
		/**情报板编号*/
		private String DEVICE_CODE;
		/***/
		private String DEVICE_NAME;
		/**里程桩号*/
		private String PILE_NUMBER;
		/**省内设备编号*/
		private String PROV_CODE;
		/**设备所在路网方向*/
		private String DIREC;
		/**是否支持自动推送*/
		private String IF_AUTO;
		
		private String DEL_FLAG;
		
		private String DISPLAY_RESO;
		
		
		private String SEND_TYPE;
		
		/***
		 * 推送类型  1:手动 2:自动
		 * @return
		 */
		public String getSEND_TYPE() {
			return SEND_TYPE;
		}
		public void setSEND_TYPE(String sEND_TYPE) {
			SEND_TYPE = sEND_TYPE;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getDEVICE_CODE() {
			return DEVICE_CODE;
		}
		public void setDEVICE_CODE(String dEVICE_CODE) {
			DEVICE_CODE = dEVICE_CODE;
		}
		public String getDEVICE_NAME() {
			return DEVICE_NAME;
		}
		public void setDEVICE_NAME(String dEVICE_NAME) {
			DEVICE_NAME = dEVICE_NAME;
		}
		public String getPILE_NUMBER() {
			return PILE_NUMBER;
		}
		public void setPILE_NUMBER(String pILE_NUMBER) {
			PILE_NUMBER = pILE_NUMBER;
		}
		public String getPROV_CODE() {
			return PROV_CODE;
		}
		public void setPROV_CODE(String pROV_CODE) {
			PROV_CODE = pROV_CODE;
		}
		public String getDIREC() {
			return DIREC;
		}
		public void setDIREC(String dIREC) {
			DIREC = dIREC;
		}
		public String getIF_AUTO() {
			return IF_AUTO;
		}
		public void setIF_AUTO(String iF_AUTO) {
			IF_AUTO = iF_AUTO;
		}
		public String getDEL_FLAG() {
			return DEL_FLAG;
		}
		public void setDEL_FLAG(String dEL_FLAG) {
			DEL_FLAG = dEL_FLAG;
		}
		
		

		public String getDISPLAY_RESO() {
			return DISPLAY_RESO;
		}
		public void setDISPLAY_RESO(String dISPLAY_RESO) {
			DISPLAY_RESO = dISPLAY_RESO;
		}
		public void setData(ResultSet rs) throws SQLException{
	    	this.setId(rs.getInt("ID"));
	    	this.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
	    	this.setDEL_FLAG(rs.getString("DEL_FLAG"));
	    	this.setDEVICE_NAME(rs.getString("DEVICE_NAME"));
	    	this.setDIREC(rs.getString("DIREC"));
	    	this.setIF_AUTO(rs.getString("IF_AUTO"));
	    	this.setPILE_NUMBER(rs.getString("PILE_NUMBER"));
	    	this.setPROV_CODE(rs.getString("PROV_CODE"));
	    	this.setDISPLAY_RESO(rs.getString("DISPLAY_RESO"));
	    	this.setSEND_TYPE(rs.getString("SEND_TYPE"));
	    }
		public static String getSQL(){
			StringBuffer buf =new StringBuffer(100);
			buf.append("Select ");
			buf.append(" ID").append(",");
			buf.append(" DEVICE_CODE").append(",");
			buf.append(" DEL_FLAG").append(",");
			buf.append(" DEVICE_NAME").append(",");
			buf.append(" DIREC").append(",");
			buf.append(" IF_AUTO").append(",");
			buf.append(" PILE_NUMBER").append(",");
			buf.append(" DISPLAY_RESO").append(",");
			buf.append(" PROV_CODE").append(",");			
			buf.append(" SEND_TYPE");			
			buf.append(" from ").append(" T_DEVICE_INFO ");
			buf.append(" where ").append(" DEL_FLAG = ?  ");
			buf.append(" and ").append(" IF_AUTO = ?  ");
			return buf.toString();
		}
		@Override
		public String toString() {
		    
			StringBuffer str = new StringBuffer();
			str.append(" ID=").append(this.getId()).append(",");
			str.append(" DEVICE_CODE=").append(this.getDEVICE_CODE()).append(",");
			str.append(" DEVICE_NAME=").append(this.getDEVICE_NAME()).append(",");
			str.append(" DIREC=").append(this.getDIREC()).append(",");
			str.append(" IF_AUTO=").append(this.getIF_AUTO()).append(",");
			str.append(" PILE_NUMBER=").append(this.getPILE_NUMBER()).append(",");
			str.append(" PROV_CODE=").append(this.getPROV_CODE()).append(",");
			str.append(" DISPLAY_RESO=").append(this.getDISPLAY_RESO()).append(",");
			str.append(" DEL_FLAG=").append(this.getDEL_FLAG()).append(",");
			str.append(" SEND_TYPE=").append(this.getSEND_TYPE());
			return str.toString();
		}
		
}
