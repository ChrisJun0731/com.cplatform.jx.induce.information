package com.cplatform.jx.induce.information.sync.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.TDeviceInfo;
import com.cplatform.jx.induce.information.model.info.ModelElevenConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelFourConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelNineConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelNineConfigTextInfo;
import com.cplatform.jx.induce.information.model.info.ModelOneConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelSevenConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixInfo;
import com.cplatform.jx.induce.information.model.info.ModelTenConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelThirteenConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelTwelveConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelTwoConfigInfo;
import com.cplatform.jx.induce.information.report.CmsInfo;
import com.cplatform.jx.induce.information.sync.AeroInfo;
import com.cplatform.jx.induce.information.sync.CslsInfo;
import com.cplatform.jx.induce.information.sync.VdInfo;
import com.cplatform.jx.induce.information.sync.ViInfo;
import com.cplatform.jx.induce.information.traffic.info.EventResultInfo;
import com.cplatform.jx.induce.information.traffic.info.TrafficEventInfo;
import com.cplatform.util2.DBAccess;
import com.cplatform.util2.TimeStamp;

public class ServiceDb {

	public static Logger log = Logger.getLogger(ServiceDb.class);
	private static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	/** 数据库连接器. */
	private final static DBAccess dba = new DBAccess();
	
	public static void DateReportTask(List<CmsInfo> lis) {
		String sql = CmsInfo.saveSQL();
		log.info("开始上报数据");

		int batchCount = 0;
		int addCount = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dba.getConnection("sqlserver");
			ps = conn.prepareStatement(sql);

			for (CmsInfo obj : lis) {
				int i = 1;
				ps.setLong(i++, obj.getN_CODE());
				ps.setString(i++, obj.getVC_CONTENT());
				// 读取文件
				File myFile = new File(obj.getB_PICTURE());
				InputStream in = new FileInputStream(myFile);
				ps.setBinaryStream(2, in, (int) myFile.length());

				ps.setString(i++, obj.getC_STATUS());
				ps.setString(i++, obj.getVC_STATUS_DES());
				ps.setDate(i++, obj.getT_REC_TIME());
				ps.setString(i++, obj.getC_SEND_FLAG());

			}

			ps.addBatch();
			batchCount++;

			// 到1000条时提交
			if (batchCount == 1000) {
				try {
					ps.executeBatch();
					// 成功即累加计数器，同时清除临时数据
					addCount += batchCount;
					batchCount = 0;
				} catch (Exception e) {
					log.error("保存数据失败:" + e.getMessage() + sql);
				}
			}

			// 剩余不满1000条的提交
			if (batchCount != 0) {

				ps.executeBatch();
				// 成功即累加计数器，同时清除临时数据
				addCount += batchCount;
				batchCount = 0;

			}

			if (addCount > 0) {
				log.info("本次提交数据：" + addCount + "条");
			}
			addCount = 0;
		}

		catch (Exception e) {
			log.error("保存数据失败:" + e.getMessage() + sql.toString());
		} finally {

			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"sqlserver");
			}
		}
	}

	public static void DataSync() throws Exception{
		List<Object> lis = null;
		lis = ServiceDb.DataSyncTask( 1);
		String nowTime = dateFormat.format(new Date()); // 获取当前时间
		
		if (lis.size() > 0) {
			staticDelete(dba, 1, nowTime);
			SaveData(lis, dba, 1);
		} 
		Thread.sleep(1000);
		lis = ServiceDb.DataSyncTask( 2);
		if (lis.size() > 0) {
			staticDelete(dba, 2, nowTime);
			SaveData(lis, dba, 2);
		} 
		Thread.sleep(1000);
		lis = ServiceDb.DataSyncTask( 3);
		if (lis.size() > 0) {
			staticDelete(dba, 3, nowTime);
			SaveData(lis, dba, 3);
		} 
		Thread.sleep(1000);
		lis = ServiceDb.DataSyncTask( 4);
		if (lis.size() > 0) {
			staticDelete(dba, 4, nowTime);
			SaveData(lis, dba, 4);
		} 
		Thread.sleep(1000);
	}
	/**
		 * 
		 */
	private static List<Object> DataSyncTask(int type) {

		String selectsql = "";
		List<Object> lis = new ArrayList<Object>();
		switch (type) {
		case 1:
			selectsql = AeroInfo.getSQL();
			log.info("开始获取 [CUR_AERO] 数据");
			break;
		case 2:
			selectsql = CslsInfo.getSQL();
			log.info("开始获取 [cur_csls] 数据");
			break;
		case 3:
			selectsql = VdInfo.getSQL();
			log.info("开始获取 [CUR_VD] 数据");
			break;
		case 4:
			selectsql = ViInfo.getSQL();
			log.info("开始获取 [CUR_VI] 数据");
			break;
		}
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		String nowTime = dateFormat.format(new Date()); // 获取当前时间

		try {
			conn = dba.getConnection("sqlserver");
			st = conn.prepareStatement(selectsql);
			log.info("SQL:" + selectsql);

			int i = 1;
			st.setString(i++, nowTime);
			// st.setString(i++, time);

			rs = st.executeQuery();

			while (rs.next()) {
				switch (type) {
				case 1:
					AeroInfo acg = new AeroInfo();
					acg.setData(rs);
					lis.add(acg);
					break;
				case 2:
					CslsInfo por = new CslsInfo();
					por.setData(rs);
					lis.add(por);
					break;
				case 3:
					VdInfo are = new VdInfo();
					are.setData(rs);
					lis.add(are);
					break;
				case 4:
					ViInfo sty = new ViInfo();
					sty.setData(rs);
					lis.add(sty);
					break;
				}
			}

		return lis;
		} catch (Exception ex) {
			log.error("数据异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"sqlserver");
		}
		return null;
	}

	private static void staticDelete(DBAccess dba, int type, String nowTime) {
		String selectsql = "";
		switch (type) {
		case 1:
			log.info("删除已处理[CUR_AERO]增量");
			selectsql = AeroInfo.delSQL();
			break;
		case 2:
			log.info("删除已处理[cur_csls]增量");
			selectsql = CslsInfo.delSQL();
			break;
		case 3:
			log.info("删除已处理[CUR_VD]增量");
			selectsql = VdInfo.delSQL();
			break;
		case 4:
			log.info("删除已处理[CUR_VI]增量");
			selectsql = ViInfo.delSQL();
			break;
		}

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dba.getConnection("sqlserver");
			ps = conn.prepareStatement(selectsql);
			ps.setString(1, nowTime);
			boolean flag = ps.execute();
			log.info("删除 状态" + flag);
		} catch (Exception e) {
			log.error("删除数据失败:" + e.getMessage() + selectsql);
		} finally {

			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"sqlserver");
			}
		}

	}

	/**
	 * 
	 * 
	 * @param lis
	 * @param dba
	 */
	private static void SaveData(List<Object> lis, DBAccess dba, int type) {

		int batchCount = 0;
		int addCount = 0;

		String sql = "";

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dba.getConnection("oracle");

			switch (type) {
			case 1:
				sql = AeroInfo.saveSQL();
				ps = conn.prepareStatement(sql);
				log.info("开始保存[CUR_AERO] 数据");
				break;
			case 2:
				sql = CslsInfo.saveSQL();
				ps = conn.prepareStatement(sql);
				log.info("开始保存[cur_csls]数据");
				break;
			case 3:
				sql = VdInfo.saveSQL();
				ps = conn.prepareStatement(sql);
				log.info("开始保存[CUR_VD]数据");
				break;
			case 4:
				sql = ViInfo.saveSQL();
				ps = conn.prepareStatement(sql);
				log.info("开始保存[CUR_VI]数据");
				break;
			}
			for (Object obj : lis) {
				int i = 1;

				switch (type) {
				case 1:
					AeroInfo info = (AeroInfo) obj;
					ps.setLong(i++, info.getN_CODE());
					ps.setString(i++, info.getC_ICING());
					ps.setString(i++, info.getC_SEND_FLAG());
					ps.setString(i++, info.getC_STAT_FALG());
					ps.setString(i++, info.getC_STATUS());
					ps.setString(i++, info.getC_WIND_DIR());
					ps.setFloat(i++, info.getD_HUMIDITY());
					ps.setFloat(i++, info.getD_RAINFALL());
					ps.setFloat(i++, info.getD_TEMP());
					ps.setFloat(i++, info.getD_WIND_SPEED());
					ps.setString(i++, strToDate(info.getN_TIME_STAMP()));
					ps.setLong(i++, info.getN_VISIBILITY());
					ps.setString(i++, strToDate(info.getT_REC_TIME()));
					ps.setString(i++, info.getVC_STATUS_DES());
					break;
				case 2:
					CslsInfo pinfo = (CslsInfo) obj;
					ps.setLong(i++, pinfo.getN_code());
					ps.setLong(i++, pinfo.getN_limit());
					ps.setString(i++, pinfo.getC_status());
					ps.setString(i++, pinfo.getVc_status_des());
					ps.setString(i++, strToDate(pinfo.getT_rec_time()));
					break;
				case 3:
					VdInfo infos = (VdInfo) obj;
					ps.setLong(i++, infos.getN_CODE());
					ps.setString(i++, strToDate(infos.getN_TIME_STAMP()));
					ps.setFloat(i++, infos.getD_UP_LANE1_OCCUPY());
					ps.setFloat(i++, infos.getD_UP_LANE1_SPEED());
					ps.setLong(i++, infos.getN_UP_LANE1_QUANTITY());
					ps.setFloat(i++, infos.getD_UP_LANE2_OCCUPY());
					ps.setFloat(i++, infos.getD_UP_LANE2_SPEED());
					ps.setLong(i++, infos.getN_UP_LANE2_QUANTITY());
					ps.setFloat(i++, infos.getD_UP_LANE3_OCCUPY());
					ps.setFloat(i++, infos.getD_UP_LANE3_SPEED());
					ps.setLong(i++, infos.getN_UP_LANE3_QUANTITY());
					ps.setFloat(i++, infos.getD_UP_LANE4_OCCUPY());
					ps.setFloat(i++, infos.getD_UP_LANE4_SPEED());
					ps.setLong(i++, infos.getN_UP_LANE4_QUANTITY());
					ps.setFloat(i++, infos.getD_UP_LANE5_OCCUPY());
					ps.setFloat(i++, infos.getD_UP_LANE5_SPEED());
					ps.setLong(i++, infos.getN_UP_LANE5_QUANTITY());

					ps.setFloat(i++, infos.getD_DOWN_LANE1_OCCUPY());
					ps.setFloat(i++, infos.getD_DOWN_LANE1_SPEED());
					ps.setLong(i++, infos.getN_DOWN_LANE1_QUANTITY());
					ps.setFloat(i++, infos.getD_DOWN_LANE2_OCCUPY());
					ps.setFloat(i++, infos.getD_DOWN_LANE2_SPEED());
					ps.setLong(i++, infos.getN_DOWN_LANE2_QUANTITY());
					ps.setFloat(i++, infos.getD_DOWN_LANE3_OCCUPY());
					ps.setFloat(i++, infos.getD_DOWN_LANE3_SPEED());
					ps.setLong(i++, infos.getN_DOWN_LANE3_QUANTITY());
					ps.setFloat(i++, infos.getD_DOWN_LANE4_OCCUPY());
					ps.setFloat(i++, infos.getD_DOWN_LANE4_SPEED());
					ps.setLong(i++, infos.getN_DOWN_LANE4_QUANTITY());
					ps.setFloat(i++, infos.getD_DOWN_LANE5_OCCUPY());
					ps.setFloat(i++, infos.getD_DOWN_LANE5_SPEED());
					ps.setLong(i++, infos.getN_DOWN_LANE5_QUANTITY());

					ps.setString(i++, infos.getC_STATUS());
					ps.setString(i++, infos.getVC_STATUS_DES());
					ps.setString(i++, strToDate(infos.getT_REC_TIME()));
					ps.setString(i++, infos.getC_SEND_FLAG());
					ps.setString(i++, infos.getC_STAT_FALG());
					break;
				case 4:
					ViInfo sinfo = (ViInfo) obj;
					ps.setLong(i++, sinfo.getN_CODE());
					ps.setString(i++, sinfo.getC_SEND_FLAG());
					ps.setString(i++, sinfo.getC_STAT_FALG());
					ps.setString(i++, sinfo.getC_STATUS());
					ps.setString(i++, strToDate(sinfo.getN_TIME_STAMP()));
					ps.setLong(i++, sinfo.getN_VISIBILITY());
					ps.setString(i++, strToDate(sinfo.getT_REC_TIME()));
					ps.setString(i++, sinfo.getVC_STATUS_DES());
					break;
				}

				ps.addBatch();
				batchCount++;

				// 到1000条时提交
				if (batchCount == 1000) {
					try {
						ps.executeBatch();
						// 成功即累加计数器，同时清除临时数据
						addCount += batchCount;
						batchCount = 0;
					} catch (Exception e) {
						log.error("保存数据失败:" + e.getMessage() + sql);
					}
				}
			}

			// 剩余不满1000条的提交
			if (batchCount != 0) {

				ps.executeBatch();
				// 成功即累加计数器，同时清除临时数据
				addCount += batchCount;
				batchCount = 0;

			}

			if (addCount > 0) {
				log.info("本次提交数据：" + addCount + "条");
			}
			addCount = 0;
		}

		catch (Exception e) {
			log.error("保存数据失败:" + e.getMessage() + sql.toString());
		} finally {

			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"oracle");
			}
		}
	}

	private static String strToDate(String str) {
		if (str == null)
			return null;
		String temp = str;
		temp = str.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
		return temp;
	}

	/**
	 * 获取需要自动推送的设备信息
	 * 
	 * @param dba
	 * @return
	 */
	public static List<TDeviceInfo> getVaildDevices() {

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = TDeviceInfo.getSQL();

		List<TDeviceInfo> lis = new ArrayList<TDeviceInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setString(i++, "1");
			st.setString(i++, "1");
			rs = st.executeQuery();

			while (rs.next()) {

				TDeviceInfo sty = new TDeviceInfo();
				sty.setData(rs);
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取设备信息异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}

	/**
	 * 获取小于200 的 信息
	 * 
	 * @param nCode
	 * @return
	 */
	public static List<ViInfo> getViInfolimit( int limit) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" N_CODE").append(",");
		buf.append(" c_SEND_FLAG").append(",");
		buf.append(" c_STAT_FALG").append(",");
		buf.append(" c_STATUS").append(",");
		buf.append(" n_TIME_STAMP").append(",");
		buf.append(" n_VISIBILITY").append(",");
		buf.append(" t_REC_TIME").append(",");
		buf.append(" vC_STATUS_DES");
		buf.append(" from ").append(" CUR_VI ");
		buf.append(" where ").append(" t_REC_TIME >? ");
		buf.append(" and ").append(" c_STATUS = ? ");
		buf.append(" and ").append(" n_VISIBILITY >= ? ");
		buf.append(" order by  t_REC_TIME desc ");
		selectsql = buf.toString();
		List<ViInfo> lis = new ArrayList<ViInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			String time = TimeStamp.getTime(14);
			int i = 1;
			st.setString(i++, time);
			st.setString(i++, "0");
			st.setInt(i++, limit);
			rs = st.executeQuery();

			while (rs.next()) {

				ViInfo sty = new ViInfo();
				sty.setData(rs);
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取能见度信息异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}

	
	public static List<AeroInfo> getAeroInfolimit( int limit) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
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
		buf.append(" n_TIME_STAMP").append(",");
		buf.append(" n_VISIBILITY").append(",");
		buf.append(" t_REC_TIME").append(",");
		buf.append(" vC_STATUS_DES");
		buf.append(" from ").append(" CUR_AERO ");
		buf.append(" where ").append(" t_REC_TIME >? ");
		buf.append(" and ").append(" c_STATUS = ? ");
		buf.append(" and ").append(" (d_RAINFALL >= ? ");
		buf.append(" or ").append(" c_ICING = ?) ");
		buf.append(" order by  t_REC_TIME desc ");
		selectsql = buf.toString();
		List<AeroInfo> lis = new ArrayList<AeroInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			String time = TimeStamp.getTime(14);
			st.setString(i++, time);
			st.setString(i++, "0");
			st.setInt(i++, limit);
			st.setString(i++, "1");
			rs = st.executeQuery();

			while (rs.next()) {

				AeroInfo sty = new AeroInfo();
				sty.setData(rs);
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取雨量信息异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	
	/**
	 * 获取待处理传送的事件消息
	 * @param limit
	 * @return
	 */
	public static List<TrafficEventInfo> getTrafficEvent() {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" ID").append(",");
		buf.append(" TYPE").append(",");
		buf.append(" LOC").append(",");
		buf.append(" LANES").append(",");
		buf.append(" STARTTIME").append(",");
		buf.append(" ENDTIME").append(",");
		buf.append(" DESCRIBE");
		buf.append(" from ").append(" T_TRAFFIC_EVENT ");
		buf.append(" where ").append(" STATUS = ? ");
		buf.append(" and  ").append(" ENDTIME > ? ");
		buf.append(" and rownum < ? ");
		selectsql = buf.toString();
		List<TrafficEventInfo> lis = new ArrayList<TrafficEventInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 0);
			st.setString(i++, TimeStamp.getTime(14));
			st.setInt(i++, 51);

			rs = st.executeQuery();
			while (rs.next()) {
				TrafficEventInfo sty = new TrafficEventInfo();
				sty.setDesc(rs.getString("DESCRIBE"));
				sty.setEndTime(rs.getString("ENDTIME"));
				sty.setId(rs.getInt("ID"));
				sty.setLanes(rs.getString("LANES"));
				sty.setLoc(rs.getString("LOC"));
				sty.setStartTime(rs.getString("STARTTIME"));
				sty.setType(rs.getString("TYPE"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取待传递的交通事件信息异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	/**
	 * 失效之前未处理的自动推送item
	 * @param items
	 * @param dba
	 */
	public static void invalidPushItem(List<DeviceItemInfo> items) {
		int batchCount = 0;
		int addCount = 0;
		
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			
			StringBuffer buf = new StringBuffer();
			//将未处理，信息类型为自动消息置为失效
			buf.append("UPDATE T_DEVICE_ITEM_INFO t SET t.status=2 WHERE t.info_type=2 AND t.status=0 AND t.device_code=?");
			
			conn = dba.getConnection("oracle");
			sql = buf.toString();
			log.info("更新自动推送item sql:"+sql);
			ps = conn.prepareStatement(sql);
			for (DeviceItemInfo info : items) {
				int i = 1;
				
				ps.setString(i++, info.getDEVICE_CODE());
				
				ps.addBatch();
				batchCount++;
				
				// 到1000条时提交
				if (batchCount == 1000) {
					try {
						ps.executeBatch();
						// 成功即累加计数器，同时清除临时数据
						addCount += batchCount;
						batchCount = 0;
					} catch (Exception e) {
						log.error("将自动消息置失效:" + e.getMessage() + sql);
					}
				}
			}
			
			// 剩余不满1000条的提交
			if (batchCount != 0) {
				
				ps.executeBatch();
				// 成功即累加计数器，同时清除临时数据
				addCount += batchCount;
				batchCount = 0;
				
			}
			
			if (addCount > 0) {
				log.info("本次更新提交数据(将自动消息置失效)：" + addCount + "条");
			}
			addCount = 0;
		}
		
		catch (Exception e) {
			log.error("将自动消息置失败:" + e.getMessage() + sql.toString());
		} finally {
			
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"oracle");
			}
		}
	}
	/**
	 * 保存item
	 * @param items
	 * @param dba
	 */
	public synchronized static void SavePushItemWithSingle(List<DeviceItemInfo> items) {

		//将自动消息status为0，即未处理的自动消息status置为2，即已失效。
		invalidPushItem(items);
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			StringBuffer buf = new StringBuffer();
			buf.append("Insert into T_DEVICE_ITEM_INFO (");
			buf.append(" ID").append(",");
			buf.append(" PLAY_TIME").append(",");
			buf.append(" ITEM_PATH").append(",");
			buf.append(" INFO_TYPE").append(",");
			buf.append(" INFO_SOURCE").append(",");
			buf.append(" START_TIME").append(",");
			buf.append(" END_TIME").append(",");
			buf.append(" STATUS").append(",");
			buf.append(" DEVICE_CODE").append(",");
			buf.append(" CREATE_TIME").append(",");
			buf.append(" ROAD_LEVEL )").append(" values ( SEQ_T_DEVICE_ITEM_INFO.NEXTVAL,?,?,?,?,?,?,?,?,?,?)");
			
			conn = dba.getConnection("oracle");
			sql = buf.toString();
//			log.info("保存item sql:"+sql);
//			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps = conn.prepareStatement(sql,new String[]{"ID"});
			for (DeviceItemInfo info : items) {
				int i = 1;
				
				ps.setString(i++, info.getPLAY_TIME());
				ps.setString(i++, info.getITEM_PATH());
				ps.setString(i++, info.getINFO_TYPE());
				ps.setString(i++, info.getINFO_SOURCE());
				ps.setString(i++, info.getSTART_TIME());
				ps.setString(i++, info.getEND_TIME());
				ps.setString(i++, "0");
				ps.setString(i++, info.getDEVICE_CODE());
				ps.setString(i++, TimeStamp.getTime(14));
				ps.setString(i++, info.getRoadLevel());
				
					try {

						rs=ps.getGeneratedKeys();
						while (rs.next()) {  ps.execute();
							
							if(StringUtils.equals(info.getSendType(), "2")){//自动上屏
								//调用接口
			                	System.out.println("数据主键：" + String.valueOf(rs.getInt(1)));

			                	//发起post请求
								//将item.lst以追加的形式添加到playlist中，playlist先根据设备号查询出来
			                	doAutoIssure(String.valueOf(rs.getInt(1)));
							}
						}  
					} catch (Exception e) {
						log.error("保存自动消息数据失败:" + e.getMessage() + sql);
					}
			}
			
		}
		
		catch (Exception e) {
			log.error("保存自动消息数据失败:" + e.getMessage() + sql.toString());
		} finally {
			
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"oracle");
			}
		}
	}
	/**
	 * 保存item
	 * @param items
	 * @param dba
	 */
	public static void SavePushItem(List<DeviceItemInfo> items) {
		int batchCount = 0;
		int addCount = 0;

		String sql = "";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			StringBuffer buf = new StringBuffer();
			buf.append("Insert into T_DEVICE_ITEM_INFO (");
			buf.append(" ID").append(",");
			buf.append(" PLAY_TIME").append(",");
			buf.append(" ITEM_PATH").append(",");
			buf.append(" INFO_TYPE").append(",");
			buf.append(" INFO_SOURCE").append(",");
			buf.append(" START_TIME").append(",");
			buf.append(" END_TIME").append(",");
			buf.append(" STATUS").append(",");
			buf.append(" DEVICE_CODE").append(",");
			buf.append(" CREATE_TIME )").append(" values ( SEQ_T_DEVICE_ITEM_INFO.NEXTVAL,?,?,?,?,?,?,?,?,?)");

			conn = dba.getConnection("oracle");
			sql = buf.toString();
//			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps = conn.prepareStatement(sql,new String[]{"ID"});
			for (DeviceItemInfo info : items) {
				int i = 1;

				ps.setString(i++, info.getPLAY_TIME());
				ps.setString(i++, info.getITEM_PATH());
				ps.setString(i++, info.getINFO_TYPE());
				ps.setString(i++, info.getINFO_SOURCE());
				ps.setString(i++, info.getSTART_TIME());
				ps.setString(i++, info.getEND_TIME());
				ps.setString(i++, "0");
				ps.setString(i++, info.getDEVICE_CODE());
				ps.setString(i++, TimeStamp.getTime(14));

				ps.addBatch();
				batchCount++;
				// 到1000条时提交
				if (batchCount == 1000) {
					try {
						ps.executeBatch();
						rs=ps.getGeneratedKeys();
						while (rs.next()) {  
			                  
			                if(StringUtils.equals(info.getSendType(), "2")){//自动上屏
			                	
			                }
			            }  
						// 成功即累加计数器，同时清除临时数据
						addCount += batchCount;
						batchCount = 0;
					} catch (Exception e) {
						log.error("保存自动消息数据失败:" + e.getMessage() + sql);
					}
				}
			}

			// 剩余不满1000条的提交
			if (batchCount != 0) {

				ps.executeBatch();
				rs=ps.getGeneratedKeys();
				while (rs.next()) {  
//					 if(StringUtils.equals(info.getSendType(), "2")){//自动上屏
//		                	//调用接口
//		                	System.out.println("数据主键：" + String.valueOf(rs.getInt(1)));
//		                }
	            }   
				// 成功即累加计数器，同时清除临时数据
				addCount += batchCount;
				batchCount = 0;

			}

			if (addCount > 0) {
				log.info("本次提交数据：" + addCount + "条");
			}
			addCount = 0;
		}

		catch (Exception e) {
			log.error("保存自动消息数据失败:" + e.getMessage() + sql.toString());
		} finally {

			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"oracle");
			}
		}
	}
	
	public static List<ModelOneConfigInfo> getModelOneConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" END_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		buf.append(" DISTANCE").append(",");
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_ONE_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelOneConfigInfo> lis = new ArrayList<ModelOneConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelOneConfigInfo sty = new ModelOneConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setEND_NAME(rs.getString("END_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				sty.setDISTANCE(rs.getString("DISTANCE"));
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板一参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	
	public static List<ModelThirteenConfigInfo> getModelThireteenConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" START_NAME").append(",");
		buf.append(" END_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		buf.append(" FONT_SIZE").append(",");
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_THIRTEEN_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelThirteenConfigInfo> lis = new ArrayList<ModelThirteenConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);
			
			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();
			
			while (rs.next()) {
				
				ModelThirteenConfigInfo sty = new ModelThirteenConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setEND_NAME(rs.getString("END_NAME"));
				sty.setSTART_NAME(rs.getString("START_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				sty.setEND_NAME(rs.getString("END_NAME"));
				sty.setFONT_SIZE(rs.getString("FONT_SIZE"));
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}
			
		} catch (Exception ex) {
			log.error("获取带处理模板一参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}
		
		return lis;
	}
	
	public static List<ModelTwoConfigInfo> getModelTwoConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" START_NAME").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" END_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		buf.append(" DISTANCE").append(",");
		buf.append(" SPEED").append(",");
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_TWO_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelTwoConfigInfo> lis = new ArrayList<ModelTwoConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelTwoConfigInfo sty = new ModelTwoConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setEND_NAME(rs.getString("END_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				sty.setSTART_NAME(rs.getString("START_NAME"));
				sty.setDISTANCE(rs.getString("DISTANCE"));
				sty.setSPEED(rs.getInt("SPEED"));
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板二参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	
	public static List<ModelSixConfigInfo> getModelSixConfigs (int type){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" ID").append(",");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" PIC_PATH").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" STATUS").append(",");
		buf.append(" type").append(",");
		buf.append(" font_color").append(",");
		buf.append(" font_size").append(",");
		buf.append(" text_name").append(",");
		buf.append(" text_x").append(",");
		buf.append(" text_y");
		buf.append(" from ").append(" T_ACT_MODEL_SIX_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		buf.append(" and ").append(" TYPE = ? ");
		
		selectsql = buf.toString();
		List<ModelSixConfigInfo> lis = new ArrayList<ModelSixConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 1);
			st.setInt(i++, type);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelSixConfigInfo sty = new ModelSixConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setPIC_PATH(rs.getString("PIC_PATH"));
				sty.setSTATUS(rs.getInt("STATUS"));
			    int id = rs.getInt("ID");
				sty.setSIXINFOS(getModelSixs(id));
				sty.setFontColor(rs.getInt("font_color"));
				sty.setFontSize(rs.getInt("font_size"));
				sty.setTextName(rs.getString("text_name"));
				sty.setTextX(rs.getInt("text_x"));
				sty.setTextY(rs.getInt("text_y"));
				sty.setType(rs.getInt("type"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板六参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	
	public synchronized static List<ModelSixInfo> getModelSixs (int id){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" PID").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" UP_LEFT_X").append(",");
		buf.append(" UP_LEFT_Y").append(",");
		buf.append(" UNDER_RIGHT_X").append(",");
		buf.append(" UNDER_RIGHT_Y").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" ROAD_CODE").append(",");
		buf.append(" TEXT_PX_X").append(",");
		buf.append(" TEXT_PX_Y").append(",");
		buf.append(" TEXT_SIZE").append(",");
		buf.append(" TEXT_COLOR").append(",");
		buf.append(" ID");
		buf.append(" from ").append(" T_ACT_MODEL_SIX_CONFIG_PARAM ");
		buf.append(" where ").append(" PID = ? ");
		buf.append(" and ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelSixInfo> lis = new ArrayList<ModelSixInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, id);
			st.setInt(i++, 1);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelSixInfo sty = new ModelSixInfo();
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setID(rs.getInt("ID"));
				sty.setPID(rs.getInt("PID"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				sty.setTEXT_PX_X(rs.getInt("TEXT_PX_X"));
				sty.setTEXT_PX_Y(rs.getInt("TEXT_PX_Y"));
				sty.setUNDER_RIGHT_X(rs.getInt("UNDER_RIGHT_X"));
				sty.setUNDER_RIGHT_Y(rs.getInt("UNDER_RIGHT_Y"));
				sty.setUP_LEFT_X(rs.getInt("UP_LEFT_X"));
				sty.setUP_LEFT_Y(rs.getInt("UP_LEFT_Y"));
				sty.setTEXT_SIZE(rs.getInt("TEXT_SIZE"));
				sty.setTEXT_COLOR(rs.getInt("TEXT_COLOR"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板六参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	
	public static List<ModelNineConfigInfo> getModelNineConfigs (int type){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" ID").append(",");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" PIC_PATH").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" font_size").append(",");
		buf.append(" text_name").append(",");
		buf.append(" text_x").append(",");
		buf.append(" text_y,");
		buf.append(" text_name1").append(",");
		buf.append(" text_x1").append(",");
		buf.append(" text_y1").append(",");
		buf.append(" road_code").append(",");
		buf.append(" road_code1").append(",");
		buf.append(" start_point").append(",");
		buf.append(" start_point1").append(",");
		buf.append(" end_point").append(",");
		buf.append(" end_point1").append(",");
		
		buf.append(" weather_x_point").append(",");
		buf.append(" weather_y_point").append(",");
		buf.append(" wind_x_point").append(",");
		buf.append(" wind_y_point").append(",");
		buf.append(" weather_font_size").append(",");
		
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_SIX_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		buf.append(" and ").append(" TYPE = ? ");
		
		selectsql = buf.toString();
		List<ModelNineConfigInfo> lis = new ArrayList<ModelNineConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 1);
			st.setInt(i++, type);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelNineConfigInfo sty = new ModelNineConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setPIC_PATH(rs.getString("PIC_PATH"));
				sty.setTextX(rs.getInt("text_x"));
				sty.setTextX1(rs.getInt("text_x1"));
				sty.setTextName(rs.getString("text_name"));
				sty.setTextName1(rs.getString("text_name1"));
				sty.setTextY(rs.getInt("text_y"));
				sty.setTextY1(rs.getInt("text_y1"));
				sty.setFontSize(rs.getInt("font_size"));
				sty.setSTATUS(rs.getInt("STATUS"));
				sty.setRoadCode(rs.getString("road_code"));
				sty.setRoadCode1(rs.getString("road_code1"));
				sty.setStartPoint(rs.getString("start_point"));
				sty.setStartPoint1(rs.getString("start_point1"));
				sty.setEndPoint(rs.getString("end_point"));
				sty.setEndPoint1(rs.getString("end_point1"));
				sty.setWeatherXPoint(rs.getInt("weather_x_point"));
				sty.setWeatherYPoint(rs.getInt("weather_y_point"));
				sty.setWindXPoint(rs.getInt("wind_x_point"));
				sty.setWindYPoint(rs.getInt("wind_y_point"));
				sty.setWeatherFontSize(rs.getInt("weather_font_size"));
				
			    int id = rs.getInt("ID");
				sty.setSIXINFOS(getModelSixs(id));
				sty.setModelText(getModelNineTextInfos(id));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板九参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	public static List<ModelNineConfigTextInfo> getModelNineTextInfos(int id){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" PID").append(",");
		buf.append(" TEXT_PX_X").append(",");
		buf.append(" TEXT_PX_Y").append(",");
		buf.append(" PERIOD_NUM").append(",");
		buf.append(" TYPE").append(",");
		buf.append(" ID");
		buf.append(" from ").append(" T_ACT_MODEL_NINE_CONFIG_PARAM ");
		buf.append(" where ").append(" PID = ? ");
		buf.append(" and ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelNineConfigTextInfo> lis = new ArrayList<ModelNineConfigTextInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, id);
			st.setInt(i++, 1);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelNineConfigTextInfo sty = new ModelNineConfigTextInfo();
				sty.setPID(rs.getInt("PID"));
				sty.setTEXT_PX_X(rs.getInt("TEXT_PX_X"));
				sty.setTEXT_PX_Y(rs.getInt("TEXT_PX_Y"));
				sty.setPERIOD_NUM(rs.getString("PERIOD_NUM"));
				sty.setTYPE(rs.getInt("TYPE"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板九参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	public static List<ModelFourConfigInfo> getModelFourConfigs (int type){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
//		buf.append(" START_NAME").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" END_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		buf.append(" REC_ROAD_NAME").append(",");
		buf.append(" REC_POINT").append(",");
		buf.append(" TYPE").append(",");
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_FOUR_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
//		buf.append(" and ").append(" TYPE = ? ");
		
		selectsql = buf.toString();
		List<ModelFourConfigInfo> lis = new ArrayList<ModelFourConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 1);
//			st.setInt(i++, type);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelFourConfigInfo sty = new ModelFourConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setEND_NAME(rs.getString("END_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				sty.setREC_POINT(rs.getString("REC_POINT"));
//				sty.setDISTANCE(rs.getString("DISTANCE"));
				sty.setREC_ROAD_NAME(rs.getString("REC_ROAD_NAME"));
				sty.setSTATUS(rs.getInt("STATUS"));
				sty.setTYPE(rs.getInt("TYPE"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板四参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	
	public static List<ModelTenConfigInfo> getModelTenConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" ROAD_NAME").append(",");
		buf.append(" SECOND_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		
		buf.append(" START_POINT2").append(",");
		buf.append(" END_POINT2").append(",");
		buf.append(" ROAD_NAME2").append(",");
		buf.append(" SECOND_NAME2").append(",");
		buf.append(" ROAD_CODE2").append(",");
		buf.append(" FONT_SIZE").append(",");
		
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_TEN_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelTenConfigInfo> lis = new ArrayList<ModelTenConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);
			
			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();
			
			while (rs.next()) {
				
				ModelTenConfigInfo sty = new ModelTenConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setROAD_NAME(rs.getString("ROAD_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setSECOND_NAME(rs.getString("SECOND_NAME"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				
				sty.setROAD_NAME2(rs.getString("ROAD_NAME2"));
				sty.setEND_POINT2(rs.getString("END_POINT2"));
				sty.setROAD_NAME2(rs.getString("ROAD_NAME2"));
				sty.setSECOND_NAME2(rs.getString("SECOND_NAME2"));
				sty.setROAD_CODE2(rs.getString("ROAD_CODE2"));
				sty.setSTART_POINT2(rs.getString("START_POINT2"));
				sty.setFONT_SIZE(rs.getString("FONT_SIZE"));
				
				
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}
			
		} catch (Exception ex) {
			log.error("获取带处理模板十参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}
		
		return lis;
	}
	public static List<ModelSevenConfigInfo> getModelSevenConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" ROAD_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		
		buf.append(" START_POINT2").append(",");
		buf.append(" END_POINT2").append(",");
		buf.append(" ROAD_NAME2").append(",");
		buf.append(" ROAD_CODE2").append(",");
		
		buf.append(" START_POINT3").append(",");
		buf.append(" END_POINT3").append(",");
		buf.append(" ROAD_NAME3").append(",");
		buf.append(" ROAD_CODE3").append(",");
		buf.append(" FONT_SIZE").append(",");
		
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_SEVEN_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelSevenConfigInfo> lis = new ArrayList<ModelSevenConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();

			while (rs.next()) {

				ModelSevenConfigInfo sty = new ModelSevenConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setROAD_NAME(rs.getString("ROAD_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				
				sty.setROAD_NAME2(rs.getString("ROAD_NAME2"));
				sty.setEND_POINT2(rs.getString("END_POINT2"));
				sty.setROAD_CODE2(rs.getString("ROAD_CODE2"));
				sty.setSTART_POINT2(rs.getString("START_POINT2"));
				
				sty.setROAD_NAME3(rs.getString("ROAD_NAME3"));
				sty.setEND_POINT3(rs.getString("END_POINT3"));
				sty.setROAD_CODE3(rs.getString("ROAD_CODE3"));
				sty.setSTART_POINT3(rs.getString("START_POINT3"));
				
				sty.setFONT_SIZE(rs.getString("FONT_SIZE"));
				
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}

		} catch (Exception ex) {
			log.error("获取带处理模板七参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}

		return lis;
	}
	public static List<ModelTwelveConfigInfo> getModelTwelveConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" ROAD_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		
		buf.append(" START_POINT2").append(",");
		buf.append(" END_POINT2").append(",");
		buf.append(" ROAD_NAME2").append(",");
		buf.append(" ROAD_CODE2").append(",");
		buf.append(" FONT_SIZE").append(",");
		
		
		
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_TWELVE_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelTwelveConfigInfo> lis = new ArrayList<ModelTwelveConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);
			
			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();
			
			while (rs.next()) {
				
				ModelTwelveConfigInfo sty = new ModelTwelveConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setROAD_NAME(rs.getString("ROAD_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				
				sty.setROAD_NAME2(rs.getString("ROAD_NAME2"));
				sty.setEND_POINT2(rs.getString("END_POINT2"));
				sty.setROAD_CODE2(rs.getString("ROAD_CODE2"));
				sty.setSTART_POINT2(rs.getString("START_POINT2"));
				sty.setFONT_SIZE(rs.getString("FONT_SIZE"));
				
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}
			
		} catch (Exception ex) {
			log.error("获取带处理模板11参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}
		
		return lis;
	}
	public static List<ModelElevenConfigInfo> getModelElevenConfigs (){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("Select ");
		buf.append(" DEVICE_CODE").append(",");
		buf.append(" START_POINT").append(",");
		buf.append(" END_POINT").append(",");
		buf.append(" ROAD_NAME").append(",");
		buf.append(" CITY_CODE").append(",");
		buf.append(" ROAD_CODE").append(",");
		
		buf.append(" START_POINT2").append(",");
		buf.append(" END_POINT2").append(",");
		buf.append(" ROAD_NAME2").append(",");
		buf.append(" ROAD_CODE2").append(",");
		buf.append(" FONT_SIZE").append(",");
		
		
		buf.append(" STATUS");
		buf.append(" from ").append(" T_ACT_MODEL_ELEVEN_CONFIG ");
		buf.append(" where ").append(" STATUS = ? ");
		
		selectsql = buf.toString();
		List<ModelElevenConfigInfo> lis = new ArrayList<ModelElevenConfigInfo>();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);
			
			int i = 1;
			st.setInt(i++, 1);
			rs = st.executeQuery();
			
			while (rs.next()) {
				
				ModelElevenConfigInfo sty = new ModelElevenConfigInfo();
				sty.setCITY_CODE(rs.getString("CITY_CODE"));
				sty.setDEVICE_CODE(rs.getString("DEVICE_CODE"));
				sty.setROAD_NAME(rs.getString("ROAD_NAME"));
				sty.setEND_POINT(rs.getString("END_POINT"));
				sty.setROAD_CODE(rs.getString("ROAD_CODE"));
				sty.setSTART_POINT(rs.getString("START_POINT"));
				
				sty.setROAD_NAME2(rs.getString("ROAD_NAME2"));
				sty.setEND_POINT2(rs.getString("END_POINT2"));
				sty.setROAD_CODE2(rs.getString("ROAD_CODE2"));
				sty.setSTART_POINT2(rs.getString("START_POINT2"));
				sty.setFONT_SIZE(rs.getString("FONT_SIZE"));
				
				sty.setSTATUS(rs.getInt("STATUS"));
				lis.add(sty);
			}
			
		} catch (Exception ex) {
			log.error("获取带处理模板11参数异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}
		
		return lis;
	}
	/**
	 * 根据设备号判断是否处于紧急状态
	 * @param deviceCode
	 * @return
	 */
	public static Boolean isEmergent (String deviceCode){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String selectsql = "";
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT id,is_emergent FROM (SELECT * FROM t_screen_cont_info t WHERE t.device_code=? AND t.is_emergent!=2 ORDER BY settime DESC) WHERE ROWNUM=1");
		
		selectsql = buf.toString();
		try {
			conn = dba.getConnection("oracle");
			st = conn.prepareStatement(selectsql);
//			log.info("SQL:" + selectsql);

			int i = 1;
			st.setString(i++, deviceCode);
			rs = st.executeQuery();
			if(rs.next()){
				String isEmergent= rs.getString("is_emergent");
				if(StringUtils.equals("1", isEmergent)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}

		} catch (Exception ex) {
			log.error("获取紧急状态异常", ex);
		} finally {
			try {
				st.close();
				rs.close();
			} catch (Exception ex) {
			}
			dba.freeConnection(conn,"oracle");
		}
		return null;
	}
	/**
	 * 自动发布
	 * @return
	 */
	@SuppressWarnings("unused")
	public static void doAutoIssure(String id){
		String result="";
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(SysManager.getInstance().getAutoIssuePath());//
		post.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		post.addParameter("ids",id);
		try {
			httpclient.executeMethod(post);
			result = new String(post.getResponseBody(), "UTF-8");
			log.info("id为"+id+"的自动上屏结果："+result);
		} catch (HttpException e) {
			
			log.error("id为"+id+"的消息自动上屏失败:"+e);
		} catch (IOException e) {
			
			log.error("id为"+id+"的消息自动上屏失败:"+e);
		}
	}

	/**
	 * 更新事件状态
	 * @param ids
	 * @param result
	 */
	public static void updateTrafficEvent(List<Integer> ids, EventResultInfo result) {
		int batchCount = 0;
		int addCount = 0;
		
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			
			StringBuffer buf = new StringBuffer();
			//将未处理，信息类型为自动消息置为失效
			buf.append("UPDATE T_TRAFFIC_EVENT t SET t.STATUS=? , t.REMARK=? , t.DEALTIME=? WHERE t.ID=?");
			
			conn = dba.getConnection("oracle");
			sql = buf.toString();
			log.info("更新投递高德事件信息 sql:"+sql);
			ps = conn.prepareStatement(sql);
			for (Integer id : ids) {
				int i = 1;
				if(result!=null) {
				ps.setInt(i++, Integer.valueOf( result.getCode()));
				ps.setString(i++, result.getMessage());
				ps.setString(i++, TimeStamp.getTime(14));
				}
				else {
					ps.setInt(i++, -1);
					ps.setString(i++, "传递异常");
					ps.setString(i++, TimeStamp.getTime(14));
				}
				ps.setInt(i++,  id.intValue());
				ps.addBatch();
				batchCount++;
				
				
				// 到1000条时提交
				if (batchCount == 1000) {
					try {
						ps.executeBatch();
						// 成功即累加计数器，同时清除临时数据
						addCount += batchCount;
						batchCount = 0;
					} catch (Exception e) {
						log.error("将自动消息置失效:" + e.getMessage() + sql);
					}
				}
			}
			
			// 剩余不满1000条的提交
			if (batchCount != 0) {
				
				ps.executeBatch();
				// 成功即累加计数器，同时清除临时数据
				addCount += batchCount;
				batchCount = 0;
				
			}
			
			if (addCount > 0) {
				log.info("本次更新提交数据：" + addCount + "条");
			}
			addCount = 0;
		}
		
		catch (Exception e) {
			log.error("更新数据失败:" + e.getMessage() + sql.toString());
		} finally {
			
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
			}
			if (conn != null) {
				dba.freeConnection(conn,"oracle");
			}
		}
	}
}
