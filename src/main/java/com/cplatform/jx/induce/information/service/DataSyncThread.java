package com.cplatform.jx.induce.information.service;

import org.apache.log4j.Logger;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;


/**
 * 任务处理 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月4日 下午2:47:27
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class DataSyncThread extends Thread {

	/** 数据库访问 */
//	private final DBAccess dba = new DBAccess();

	private final Logger logger = Logger.getLogger(this.getClass());

	private boolean runFlag = true;

	@Override
	public void run() {

		logger.info("数据同步线程启动!");
		while (runFlag) {
			try {

				execTask();// 执行任务

				Thread.sleep(1000 * 60 * 5);

			} catch (Exception e) {
				logger.error("数据同步线程处理异常", e);
			}
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 任务处理
	 * 
	 * @throws Exception
	 */
	private void execTask() throws Exception {

//		long start = System.currentTimeMillis();
		ServiceDb.DataSync();
		
//		logger.info("结束同步数据， 耗时：" + (System.currentTimeMillis() - start));

	}

}
