package com.cplatform.jx.induce.information.sync;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CslsInfo {
	private long n_code;
	private long n_limit;
    private String c_status;
    private String vc_status_des;
    private String t_rec_time;
	public long getN_code() {
		return n_code;
	}
	public void setN_code(long n_code) {
		this.n_code = n_code;
	}
	public long getN_limit() {
		return n_limit;
	}
	public void setN_limit(long n_limit) {
		this.n_limit = n_limit;
	}
	public String getC_status() {
		return c_status;
	}
	public void setC_status(String c_status) {
		this.c_status = c_status;
	}
	public String getVc_status_des() {
		return vc_status_des;
	}
	public void setVc_status_des(String vc_status_des) {
		this.vc_status_des = vc_status_des;
	}
	public String getT_rec_time() {
		return t_rec_time;
	}
	public void setT_rec_time(String t_rec_time) {
		this.t_rec_time = t_rec_time;
	}
	 
	public void setData(ResultSet rs) throws SQLException{
    	this.setN_code(rs.getLong("N_CODE"));
    	this.setN_limit(rs.getLong("n_limit"));
    	this.setC_status(rs.getString("c_STATUS"));
    	this.setT_rec_time(rs.getString("t_REC_TIME"));
    	this.setVc_status_des(rs.getString("vC_STATUS_DES"));
    }
	public static String getSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("Select ");
		buf.append(" n_code").append(",");
		buf.append(" n_limit").append(",");
		buf.append(" c_status").append(",");
		buf.append(" vc_status_des").append(",");
		buf.append(" CONVERT(varchar(100), [t_rec_time], 20) as t_rec_time");
		buf.append(" from ").append(" cur_csls ");
		buf.append(" where ").append(" CONVERT(varchar(100), [t_REC_TIME], 20) < ?  ");
		
		return buf.toString();
	}
	
	public static String saveSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("insert into ");
		buf.append(" cur_csls (");
		buf.append(" n_code").append(",");
		buf.append(" n_limit").append(",");
		buf.append(" c_status").append(",");
		buf.append(" vc_status_des").append(",");
		buf.append(" t_rec_time )");
		buf.append(" values ");
		buf.append(" (?,?,?,?,?) ");
		
		return buf.toString();
	}
	
	public static String delSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("delete from ");
		buf.append(" cur_csls ");	
		buf.append(" where ").append(" CONVERT(varchar(100), [t_REC_TIME], 20) < ?  ");
		return buf.toString();
	}
}
