package com.cplatform.jx.induce.information.sync;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AeroInfo {

	private long N_CODE;
	private String N_TIME_STAMP;
	private float D_TEMP;
	private float D_HUMIDITY;
	private String C_ICING;
	private long N_VISIBILITY;
	private String C_WIND_DIR;
	private float D_WIND_SPEED;
	private float D_RAINFALL;
	private String C_STATUS;
	private String VC_STATUS_DES;
	private String T_REC_TIME;
	private String C_SEND_FLAG;
	private String C_STAT_FALG;
	
	
    public long getN_CODE() {
		return N_CODE;
	}
	public void setN_CODE(long n_CODE) {
		N_CODE = n_CODE;
	}
	public String getN_TIME_STAMP() {
		return N_TIME_STAMP;
	}
	public void setN_TIME_STAMP(String n_TIME_STAMP) {
		N_TIME_STAMP = n_TIME_STAMP;
	}
	public float getD_TEMP() {
		return D_TEMP;
	}
	public void setD_TEMP(float d_TEMP) {
		D_TEMP = d_TEMP;
	}
	public float getD_HUMIDITY() {
		return D_HUMIDITY;
	}
	public void setD_HUMIDITY(float d_HUMIDITY) {
		D_HUMIDITY = d_HUMIDITY;
	}
	public String getC_ICING() {
		return C_ICING;
	}
	public void setC_ICING(String c_ICING) {
		C_ICING = c_ICING;
	}
	public long getN_VISIBILITY() {
		return N_VISIBILITY;
	}
	public void setN_VISIBILITY(long n_VISIBILITY) {
		N_VISIBILITY = n_VISIBILITY;
	}
	public String getC_WIND_DIR() {
		return C_WIND_DIR;
	}
	public void setC_WIND_DIR(String c_WIND_DIR) {
		C_WIND_DIR = c_WIND_DIR;
	}
	public float getD_WIND_SPEED() {
		return D_WIND_SPEED;
	}
	public void setD_WIND_SPEED(float d_WIND_SPEED) {
		D_WIND_SPEED = d_WIND_SPEED;
	}
	public float getD_RAINFALL() {
		return D_RAINFALL;
	}
	public void setD_RAINFALL(float d_RAINFALL) {
		D_RAINFALL = d_RAINFALL;
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
	public String getT_REC_TIME() {
		return T_REC_TIME;
	}
	public void setT_REC_TIME(String t_REC_TIME) {
		T_REC_TIME = t_REC_TIME;
	}
	public String getC_SEND_FLAG() {
		return C_SEND_FLAG;
	}
	public void setC_SEND_FLAG(String c_SEND_FLAG) {
		C_SEND_FLAG = c_SEND_FLAG;
	}
	public String getC_STAT_FALG() {
		return C_STAT_FALG;
	}
	public void setC_STAT_FALG(String c_STAT_FALG) {
		C_STAT_FALG = c_STAT_FALG;
	}
	public void setData(ResultSet rs) throws SQLException{
    	this.setN_CODE(rs.getLong("N_CODE"));
    	this.setC_ICING(rs.getString("c_ICING"));
    	this.setC_SEND_FLAG(rs.getString("c_SEND_FLAG"));
    	this.setC_STAT_FALG(rs.getString("c_STAT_FALG"));
    	this.setC_STATUS(rs.getString("c_STATUS"));
    	this.setC_WIND_DIR(rs.getString("c_WIND_DIR"));
    	this.setD_HUMIDITY(rs.getFloat("d_HUMIDITY"));
    	this.setD_RAINFALL(rs.getFloat("d_RAINFALL"));
    	this.setD_TEMP(rs.getFloat("d_TEMP"));
    	this.setD_WIND_SPEED(rs.getFloat("d_WIND_SPEED"));
    	this.setN_TIME_STAMP(rs.getString("n_TIME_STAMP"));
    	this.setN_VISIBILITY(rs.getLong("n_VISIBILITY"));
    	this.setT_REC_TIME(rs.getString("t_REC_TIME"));
    	this.setVC_STATUS_DES(rs.getString("vC_STATUS_DES"));
    }
	public static String getSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("Select ");
		buf.append(" N_CODE").append(",");
		buf.append(" c_ICING").append(",");
		buf.append(" c_SEND_FLAG").append(",");
		buf.append(" c_STAT_FALG").append(",");
		buf.append(" c_STATUS").append(",");
		buf.append(" c_WIND_DIR").append(",");
		buf.append(" d_HUMIDITY").append(",");
		buf.append(" d_RAINFALL").append(",");
		buf.append(" d_TEMP").append(",");
		buf.append(" d_WIND_SPEED").append(",");
		buf.append(" CONVERT(varchar(100), [N_TIME_STAMP], 20) as n_TIME_STAMP").append(",");
		buf.append(" n_VISIBILITY").append(",");
		buf.append(" CONVERT(varchar(100), [t_REC_TIME], 20) as t_REC_TIME").append(",");
		buf.append(" vC_STATUS_DES");
		buf.append(" from ").append(" CUR_AERO ");
		buf.append(" where ").append(" CONVERT(varchar(100), [t_REC_TIME], 20) < ?  ");
		
		return buf.toString();
	}
	
	public static String saveSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("insert into ");
		buf.append(" CUR_AERO (");
		buf.append(" N_CODE").append(",");
		buf.append(" c_ICING").append(",");
		buf.append(" c_SEND_FLAG").append(",");
		buf.append(" c_STAT_FALG").append(",");
		buf.append(" c_STATUS").append(",");
		buf.append(" c_WIND_DIR").append(",");
		buf.append(" d_HUMIDITY").append(",");
		buf.append(" d_RAINFALL").append(",");
		buf.append(" d_TEMP").append(",");
		buf.append(" d_WIND_SPEED").append(",");
		buf.append(" n_TIME_STAMP").append(",");
		buf.append(" n_VISIBILITY").append(",");
		buf.append(" t_REC_TIME").append(",");
		buf.append(" vC_STATUS_DES )");
		buf.append(" values ");
		buf.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		return buf.toString();
	}
	
	public static String delSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("delete from ");
		buf.append(" CUR_AERO ");	
		buf.append(" where ").append(" CONVERT(varchar(100), [t_REC_TIME], 20) < ?  ");
		return buf.toString();
	}
}
