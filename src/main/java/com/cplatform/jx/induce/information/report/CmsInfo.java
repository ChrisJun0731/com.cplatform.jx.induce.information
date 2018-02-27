package com.cplatform.jx.induce.information.report;

import java.sql.Date;

public class CmsInfo {
	private long N_CODE;
	private String VC_CONTENT;
	private String B_PICTURE;
	private String C_STATUS;
	private String VC_STATUS_DES;
	private Date T_REC_TIME;
	private String C_SEND_FLAG;
	public long getN_CODE() {
		return N_CODE;
	}
	public void setN_CODE(long n_CODE) {
		N_CODE = n_CODE;
	}
	public String getVC_CONTENT() {
		return VC_CONTENT;
	}
	public void setVC_CONTENT(String vC_CONTENT) {
		VC_CONTENT = vC_CONTENT;
	}
	public String getB_PICTURE() {
		return B_PICTURE;
	}
	public void setB_PICTURE(String b_PICTURE) {
		B_PICTURE = b_PICTURE;
	}
	public String getC_STATUS() {
		return C_STATUS;
	}
	public void setC_STATUS(String c_STATUS) {
		C_STATUS = c_STATUS;
	}
	public String getVC_STATUS_DES() {
		return VC_STATUS_DES;
	}
	public void setVC_STATUS_DES(String vC_STATUS_DES) {
		VC_STATUS_DES = vC_STATUS_DES;
	}
	public Date getT_REC_TIME() {
		return T_REC_TIME;
	}
	public void setT_REC_TIME(Date t_REC_TIME) {
		T_REC_TIME = t_REC_TIME;
	}
	public String getC_SEND_FLAG() {
		return C_SEND_FLAG;
	}
	public void setC_SEND_FLAG(String c_SEND_FLAG) {
		C_SEND_FLAG = c_SEND_FLAG;
	}
	
	public static String saveSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("insert into ");
		buf.append(" CUR_CMS (");
		buf.append(" N_CODE").append(",");
		buf.append(" VC_ CONTENT").append(",");
		buf.append(" B_PICTURE").append(",");
		buf.append(" C_STATUS").append(",");
		buf.append(" VC_STATUS_DES").append(",");
		buf.append(" T_REC_TIME").append(",");
		buf.append(" C_SEND_FLAG )");
		buf.append(" values ");
		buf.append(" (?,?,?,?,?,?,?) ");
		
		return buf.toString();
	}
}
