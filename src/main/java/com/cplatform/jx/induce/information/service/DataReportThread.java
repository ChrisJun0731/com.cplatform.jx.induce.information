package com.cplatform.jx.induce.information.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.report.CmsInfo;
import com.cplatform.jx.induce.information.sync.db.ServiceDb;

/**
 * 
 * 数据上报 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月24日 下午4:45:03
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class DataReportThread extends Thread {

	private final Logger logger = Logger.getLogger(this.getClass());

	private boolean runFlag = true;

	@Override
	public void run() {

		logger.info("数据上报线程启动!");
		while (runFlag) {
			try {

				execTask();// 执行任务

				Thread.sleep(1000 * 60 * 5);

			} catch (Exception e) {
				logger.error("数据上报线程处理异常", e);
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
		// 任务类型

		List<CmsInfo> lis =null;
		
		ServiceDb.DateReportTask(lis);
		Thread.sleep(1000);
//		logger.info("结束同步数据， 耗时：" + (System.currentTimeMillis() - start));

	}

}
