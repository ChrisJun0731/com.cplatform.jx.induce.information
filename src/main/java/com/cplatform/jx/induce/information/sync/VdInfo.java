package com.cplatform.jx.induce.information.sync;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VdInfo {
	private long N_CODE;
	private String N_TIME_STAMP;
	private float D_UP_LANE1_OCCUPY;
	private float D_UP_LANE1_SPEED;
	private long N_UP_LANE1_QUANTITY;
	private float D_UP_LANE2_OCCUPY;
	private float D_UP_LANE2_SPEED;
	private long N_UP_LANE2_QUANTITY;
	private float D_UP_LANE3_OCCUPY;
	private float D_UP_LANE3_SPEED;
	private long N_UP_LANE3_QUANTITY;
	private float D_UP_LANE4_OCCUPY;
	private float D_UP_LANE4_SPEED;
	private long N_UP_LANE4_QUANTITY;
	private float D_UP_LANE5_OCCUPY;
	private float D_UP_LANE5_SPEED;
	private long N_UP_LANE5_QUANTITY;
	private float D_DOWN_LANE1_OCCUPY;
	private float D_DOWN_LANE1_SPEED;
	private long N_DOWN_LANE1_QUANTITY;
	private float D_DOWN_LANE2_OCCUPY;
	private float D_DOWN_LANE2_SPEED;
	private long N_DOWN_LANE2_QUANTITY;
	private float D_DOWN_LANE3_OCCUPY;
	private float D_DOWN_LANE3_SPEED;
	private long N_DOWN_LANE3_QUANTITY;
	private float D_DOWN_LANE4_OCCUPY;
	private float D_DOWN_LANE4_SPEED;
	private long N_DOWN_LANE4_QUANTITY;
	private float D_DOWN_LANE5_OCCUPY;
	private float D_DOWN_LANE5_SPEED;
	private long N_DOWN_LANE5_QUANTITY;
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
	public float getD_UP_LANE1_OCCUPY() {
		return D_UP_LANE1_OCCUPY;
	}
	public void setD_UP_LANE1_OCCUPY(float d_UP_LANE1_OCCUPY) {
		D_UP_LANE1_OCCUPY = d_UP_LANE1_OCCUPY;
	}
	public float getD_UP_LANE1_SPEED() {
		return D_UP_LANE1_SPEED;
	}
	public void setD_UP_LANE1_SPEED(float d_UP_LANE1_SPEED) {
		D_UP_LANE1_SPEED = d_UP_LANE1_SPEED;
	}
	public long getN_UP_LANE1_QUANTITY() {
		return N_UP_LANE1_QUANTITY;
	}
	public void setN_UP_LANE1_QUANTITY(long n_UP_LANE1_QUANTITY) {
		N_UP_LANE1_QUANTITY = n_UP_LANE1_QUANTITY;
	}
	public float getD_UP_LANE2_OCCUPY() {
		return D_UP_LANE2_OCCUPY;
	}
	public void setD_UP_LANE2_OCCUPY(float d_UP_LANE2_OCCUPY) {
		D_UP_LANE2_OCCUPY = d_UP_LANE2_OCCUPY;
	}
	public float getD_UP_LANE2_SPEED() {
		return D_UP_LANE2_SPEED;
	}
	public void setD_UP_LANE2_SPEED(float d_UP_LANE2_SPEED) {
		D_UP_LANE2_SPEED = d_UP_LANE2_SPEED;
	}
	public long getN_UP_LANE2_QUANTITY() {
		return N_UP_LANE2_QUANTITY;
	}
	public void setN_UP_LANE2_QUANTITY(long n_UP_LANE2_QUANTITY) {
		N_UP_LANE2_QUANTITY = n_UP_LANE2_QUANTITY;
	}
	public float getD_UP_LANE3_OCCUPY() {
		return D_UP_LANE3_OCCUPY;
	}
	public void setD_UP_LANE3_OCCUPY(float d_UP_LANE3_OCCUPY) {
		D_UP_LANE3_OCCUPY = d_UP_LANE3_OCCUPY;
	}
	public float getD_UP_LANE3_SPEED() {
		return D_UP_LANE3_SPEED;
	}
	public void setD_UP_LANE3_SPEED(float d_UP_LANE3_SPEED) {
		D_UP_LANE3_SPEED = d_UP_LANE3_SPEED;
	}
	public long getN_UP_LANE3_QUANTITY() {
		return N_UP_LANE3_QUANTITY;
	}
	public void setN_UP_LANE3_QUANTITY(long n_UP_LANE3_QUANTITY) {
		N_UP_LANE3_QUANTITY = n_UP_LANE3_QUANTITY;
	}
	public float getD_UP_LANE4_OCCUPY() {
		return D_UP_LANE4_OCCUPY;
	}
	public void setD_UP_LANE4_OCCUPY(float d_UP_LANE4_OCCUPY) {
		D_UP_LANE4_OCCUPY = d_UP_LANE4_OCCUPY;
	}
	public float getD_UP_LANE4_SPEED() {
		return D_UP_LANE4_SPEED;
	}
	public void setD_UP_LANE4_SPEED(float d_UP_LANE4_SPEED) {
		D_UP_LANE4_SPEED = d_UP_LANE4_SPEED;
	}
	public long getN_UP_LANE4_QUANTITY() {
		return N_UP_LANE4_QUANTITY;
	}
	public void setN_UP_LANE4_QUANTITY(long n_UP_LANE4_QUANTITY) {
		N_UP_LANE4_QUANTITY = n_UP_LANE4_QUANTITY;
	}
	public float getD_UP_LANE5_OCCUPY() {
		return D_UP_LANE5_OCCUPY;
	}
	public void setD_UP_LANE5_OCCUPY(float d_UP_LANE5_OCCUPY) {
		D_UP_LANE5_OCCUPY = d_UP_LANE5_OCCUPY;
	}
	public float getD_UP_LANE5_SPEED() {
		return D_UP_LANE5_SPEED;
	}
	public void setD_UP_LANE5_SPEED(float d_UP_LANE5_SPEED) {
		D_UP_LANE5_SPEED = d_UP_LANE5_SPEED;
	}
	public long getN_UP_LANE5_QUANTITY() {
		return N_UP_LANE5_QUANTITY;
	}
	public void setN_UP_LANE5_QUANTITY(long n_UP_LANE5_QUANTITY) {
		N_UP_LANE5_QUANTITY = n_UP_LANE5_QUANTITY;
	}
	public float getD_DOWN_LANE1_OCCUPY() {
		return D_DOWN_LANE1_OCCUPY;
	}
	public void setD_DOWN_LANE1_OCCUPY(float d_DOWN_LANE1_OCCUPY) {
		D_DOWN_LANE1_OCCUPY = d_DOWN_LANE1_OCCUPY;
	}
	public float getD_DOWN_LANE1_SPEED() {
		return D_DOWN_LANE1_SPEED;
	}
	public void setD_DOWN_LANE1_SPEED(float d_DOWN_LANE1_SPEED) {
		D_DOWN_LANE1_SPEED = d_DOWN_LANE1_SPEED;
	}
	public long getN_DOWN_LANE1_QUANTITY() {
		return N_DOWN_LANE1_QUANTITY;
	}
	public void setN_DOWN_LANE1_QUANTITY(long n_DOWN_LANE1_QUANTITY) {
		N_DOWN_LANE1_QUANTITY = n_DOWN_LANE1_QUANTITY;
	}
	public float getD_DOWN_LANE2_OCCUPY() {
		return D_DOWN_LANE2_OCCUPY;
	}
	public void setD_DOWN_LANE2_OCCUPY(float d_DOWN_LANE2_OCCUPY) {
		D_DOWN_LANE2_OCCUPY = d_DOWN_LANE2_OCCUPY;
	}
	public float getD_DOWN_LANE2_SPEED() {
		return D_DOWN_LANE2_SPEED;
	}
	public void setD_DOWN_LANE2_SPEED(float d_DOWN_LANE2_SPEED) {
		D_DOWN_LANE2_SPEED = d_DOWN_LANE2_SPEED;
	}
	public long getN_DOWN_LANE2_QUANTITY() {
		return N_DOWN_LANE2_QUANTITY;
	}
	public void setN_DOWN_LANE2_QUANTITY(long n_DOWN_LANE2_QUANTITY) {
		N_DOWN_LANE2_QUANTITY = n_DOWN_LANE2_QUANTITY;
	}
	public float getD_DOWN_LANE3_OCCUPY() {
		return D_DOWN_LANE3_OCCUPY;
	}
	public void setD_DOWN_LANE3_OCCUPY(float d_DOWN_LANE3_OCCUPY) {
		D_DOWN_LANE3_OCCUPY = d_DOWN_LANE3_OCCUPY;
	}
	public float getD_DOWN_LANE3_SPEED() {
		return D_DOWN_LANE3_SPEED;
	}
	public void setD_DOWN_LANE3_SPEED(float d_DOWN_LANE3_SPEED) {
		D_DOWN_LANE3_SPEED = d_DOWN_LANE3_SPEED;
	}
	public long getN_DOWN_LANE3_QUANTITY() {
		return N_DOWN_LANE3_QUANTITY;
	}
	public void setN_DOWN_LANE3_QUANTITY(long n_DOWN_LANE3_QUANTITY) {
		N_DOWN_LANE3_QUANTITY = n_DOWN_LANE3_QUANTITY;
	}
	public float getD_DOWN_LANE4_OCCUPY() {
		return D_DOWN_LANE4_OCCUPY;
	}
	public void setD_DOWN_LANE4_OCCUPY(float d_DOWN_LANE4_OCCUPY) {
		D_DOWN_LANE4_OCCUPY = d_DOWN_LANE4_OCCUPY;
	}
	public float getD_DOWN_LANE4_SPEED() {
		return D_DOWN_LANE4_SPEED;
	}
	public void setD_DOWN_LANE4_SPEED(float d_DOWN_LANE4_SPEED) {
		D_DOWN_LANE4_SPEED = d_DOWN_LANE4_SPEED;
	}
	public long getN_DOWN_LANE4_QUANTITY() {
		return N_DOWN_LANE4_QUANTITY;
	}
	public void setN_DOWN_LANE4_QUANTITY(long n_DOWN_LANE4_QUANTITY) {
		N_DOWN_LANE4_QUANTITY = n_DOWN_LANE4_QUANTITY;
	}
	public float getD_DOWN_LANE5_OCCUPY() {
		return D_DOWN_LANE5_OCCUPY;
	}
	public void setD_DOWN_LANE5_OCCUPY(float d_DOWN_LANE5_OCCUPY) {
		D_DOWN_LANE5_OCCUPY = d_DOWN_LANE5_OCCUPY;
	}
	public float getD_DOWN_LANE5_SPEED() {
		return D_DOWN_LANE5_SPEED;
	}
	public void setD_DOWN_LANE5_SPEED(float d_DOWN_LANE5_SPEED) {
		D_DOWN_LANE5_SPEED = d_DOWN_LANE5_SPEED;
	}
	public long getN_DOWN_LANE5_QUANTITY() {
		return N_DOWN_LANE5_QUANTITY;
	}
	public void setN_DOWN_LANE5_QUANTITY(long n_DOWN_LANE5_QUANTITY) {
		N_DOWN_LANE5_QUANTITY = n_DOWN_LANE5_QUANTITY;
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
    	this.setC_SEND_FLAG(rs.getString("c_SEND_FLAG"));
    	this.setC_STAT_FALG(rs.getString("c_STAT_FALG"));
    	this.setC_STATUS(rs.getString("c_STATUS"));
    	this.setN_TIME_STAMP(rs.getString("n_TIME_STAMP"));
    	this.setT_REC_TIME(rs.getString("t_REC_TIME"));
    	this.setVC_STATUS_DES(rs.getString("vC_STATUS_DES"));
    	this.setD_DOWN_LANE1_OCCUPY(rs.getFloat("d_DOWN_LANE1_OCCUPY"));
    	this.setD_DOWN_LANE1_SPEED(rs.getFloat("d_DOWN_LANE1_SPEED"));
    	this.setD_DOWN_LANE2_OCCUPY(rs.getFloat("d_DOWN_LANE2_OCCUPY"));
    	this.setD_DOWN_LANE2_SPEED(rs.getFloat("d_DOWN_LANE2_SPEED"));
    	this.setD_DOWN_LANE3_OCCUPY(rs.getFloat("d_DOWN_LANE3_OCCUPY"));
    	this.setD_DOWN_LANE3_SPEED(rs.getFloat("d_DOWN_LANE3_SPEED"));
    	this.setD_DOWN_LANE4_OCCUPY(rs.getFloat("d_DOWN_LANE4_OCCUPY"));
    	this.setD_DOWN_LANE4_SPEED(rs.getFloat("d_DOWN_LANE4_SPEED"));
    	this.setD_DOWN_LANE5_OCCUPY(rs.getFloat("d_DOWN_LANE5_OCCUPY"));
    	this.setD_DOWN_LANE5_SPEED(rs.getFloat("d_DOWN_LANE5_SPEED"));
    	this.setD_UP_LANE1_OCCUPY(rs.getFloat("d_UP_LANE1_OCCUPY"));
    	this.setD_UP_LANE1_SPEED(rs.getFloat("d_UP_LANE1_SPEED"));
    	this.setD_UP_LANE2_OCCUPY(rs.getFloat("d_UP_LANE2_OCCUPY"));
    	this.setD_UP_LANE2_SPEED(rs.getFloat("d_UP_LANE2_SPEED"));
    	this.setD_UP_LANE3_OCCUPY(rs.getFloat("d_UP_LANE3_OCCUPY"));
    	this.setD_UP_LANE3_SPEED(rs.getFloat("d_UP_LANE3_SPEED"));
    	this.setD_UP_LANE4_OCCUPY(rs.getFloat("d_UP_LANE4_OCCUPY"));
    	this.setD_UP_LANE4_SPEED(rs.getFloat("d_UP_LANE4_SPEED"));
    	this.setD_UP_LANE5_OCCUPY(rs.getFloat("d_UP_LANE5_OCCUPY"));
    	this.setD_UP_LANE5_SPEED(rs.getFloat("d_UP_LANE5_SPEED"));
    	this.setN_DOWN_LANE1_QUANTITY(rs.getLong("n_DOWN_LANE1_QUANTITY"));
    	this.setN_DOWN_LANE2_QUANTITY(rs.getLong("n_DOWN_LANE2_QUANTITY"));
    	this.setN_DOWN_LANE3_QUANTITY(rs.getLong("n_DOWN_LANE3_QUANTITY"));
    	this.setN_DOWN_LANE4_QUANTITY(rs.getLong("n_DOWN_LANE4_QUANTITY"));
    	this.setN_DOWN_LANE5_QUANTITY(rs.getLong("n_DOWN_LANE5_QUANTITY"));
    	this.setN_UP_LANE1_QUANTITY(rs.getLong("n_UP_LANE1_QUANTITY"));
    	this.setN_UP_LANE2_QUANTITY(rs.getLong("n_UP_LANE2_QUANTITY"));
    	this.setN_UP_LANE3_QUANTITY(rs.getLong("n_UP_LANE3_QUANTITY"));
    	this.setN_UP_LANE4_QUANTITY(rs.getLong("n_UP_LANE4_QUANTITY"));
    	this.setN_UP_LANE5_QUANTITY(rs.getLong("n_UP_LANE5_QUANTITY"));
    	
    }
	public static String getSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("Select ");
		buf.append(" N_CODE").append(","); 
		buf.append(" CONVERT(varchar(100), [N_TIME_STAMP], 20) as N_TIME_STAMP").append(","); 
		buf.append(" D_UP_LANE1_OCCUPY").append(","); 
		buf.append(" D_UP_LANE1_SPEED").append(","); 
		buf.append(" N_UP_LANE1_QUANTITY").append(","); 
		buf.append(" D_UP_LANE2_OCCUPY").append(","); 
		buf.append(" D_UP_LANE2_SPEED").append(","); 
		buf.append(" N_UP_LANE2_QUANTITY").append(","); 
		buf.append(" D_UP_LANE3_OCCUPY").append(","); 
		buf.append(" D_UP_LANE3_SPEED").append(","); 
		buf.append(" N_UP_LANE3_QUANTITY").append(","); 
		buf.append(" D_UP_LANE4_OCCUPY").append(","); 
		buf.append(" D_UP_LANE4_SPEED").append(",");
		buf.append(" N_UP_LANE4_QUANTITY").append(","); 
		buf.append(" D_UP_LANE5_OCCUPY").append(","); 
		buf.append(" D_UP_LANE5_SPEED").append(",");
		buf.append(" N_UP_LANE5_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE1_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE1_SPEED").append(",");
		buf.append(" N_DOWN_LANE1_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE2_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE2_SPEED").append(",");
		buf.append(" N_DOWN_LANE2_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE3_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE3_SPEED").append(",");
		buf.append(" N_DOWN_LANE3_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE4_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE4_SPEED").append(",");
		buf.append(" N_DOWN_LANE4_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE5_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE5_SPEED").append(",");
		buf.append(" N_DOWN_LANE5_QUANTITY").append(","); 
		buf.append(" C_STATUS").append(","); 
		buf.append(" VC_STATUS_DES").append(",");
		buf.append(" CONVERT(varchar(100), T_REC_TIME, 20) as T_REC_TIME").append(","); 
		buf.append(" C_SEND_FLAG").append(","); 
		buf.append(" C_STAT_FALG");
	
		buf.append(" from ").append(" CUR_VD ");
		buf.append(" where ").append(" CONVERT(varchar(100), [t_REC_TIME], 20) < ?  ");
		
		return buf.toString();
	}
	
	public static String saveSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("insert into ");
		buf.append(" CUR_VD (");
		buf.append(" N_CODE").append(","); 
		buf.append(" N_TIME_STAMP").append(","); 
		buf.append(" D_UP_LANE1_OCCUPY").append(","); 
		buf.append(" D_UP_LANE1_SPEED").append(","); 
		buf.append(" N_UP_LANE1_QUANTITY").append(","); 
		buf.append(" D_UP_LANE2_OCCUPY").append(","); 
		buf.append(" D_UP_LANE2_SPEED").append(","); 
		buf.append(" N_UP_LANE2_QUANTITY").append(","); 
		buf.append(" D_UP_LANE3_OCCUPY").append(","); 
		buf.append(" D_UP_LANE3_SPEED").append(","); 
		buf.append(" N_UP_LANE3_QUANTITY").append(","); 
		buf.append(" D_UP_LANE4_OCCUPY").append(","); 
		buf.append(" D_UP_LANE4_SPEED").append(",");
		buf.append(" N_UP_LANE4_QUANTITY").append(","); 
		buf.append(" D_UP_LANE5_OCCUPY").append(","); 
		buf.append(" D_UP_LANE5_SPEED").append(",");
		buf.append(" N_UP_LANE5_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE1_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE1_SPEED").append(",");
		buf.append(" N_DOWN_LANE1_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE2_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE2_SPEED").append(",");
		buf.append(" N_DOWN_LANE2_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE3_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE3_SPEED").append(",");
		buf.append(" N_DOWN_LANE3_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE4_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE4_SPEED").append(",");
		buf.append(" N_DOWN_LANE4_QUANTITY").append(","); 
		buf.append(" D_DOWN_LANE5_OCCUPY").append(","); 
		buf.append(" D_DOWN_LANE5_SPEED").append(",");
		buf.append(" N_DOWN_LANE5_QUANTITY").append(","); 
		buf.append(" C_STATUS").append(","); 
		buf.append(" VC_STATUS_DES").append(",");
		buf.append(" T_REC_TIME").append(","); 
		buf.append(" C_SEND_FLAG").append(","); 
		buf.append(" C_STAT_FALG )");

		buf.append(" values ");
		buf.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		return buf.toString();
	}
	
	public static String delSQL(){
		StringBuffer buf =new StringBuffer(100);
		buf.append("delete from ");
		buf.append(" CUR_VD ");		
		buf.append(" where ").append(" CONVERT(varchar(100), [t_REC_TIME], 20) < ?  ");
		return buf.toString();
	}
}
