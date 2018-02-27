package com.cplatform.jx.induce.information;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.cplatform.jx.induce.information.model.CreateModelEleven;
import com.cplatform.jx.induce.information.model.CreateModelFour;
import com.cplatform.jx.induce.information.model.CreateModelNine;
import com.cplatform.jx.induce.information.model.CreateModelOne;
import com.cplatform.jx.induce.information.model.CreateModelSeven;
import com.cplatform.jx.induce.information.model.CreateModelSix;
import com.cplatform.jx.induce.information.model.CreateModelTen;
import com.cplatform.jx.induce.information.model.CreateModelThirteen;
import com.cplatform.jx.induce.information.model.CreateModelTwelve;
import com.cplatform.jx.induce.information.model.CreateModelTwelveA;
import com.cplatform.jx.induce.information.model.CreateModelTwo;
import com.cplatform.jx.induce.information.service.DataRainfallThread;
import com.cplatform.jx.induce.information.service.DataReportThread;
import com.cplatform.jx.induce.information.service.DataSyncThread;
import com.cplatform.jx.induce.information.service.DataVisibilityThread;
import com.cplatform.jx.induce.information.service.SendTrafficEventToGmapThread;
import com.cplatform.util2.DBAccess;

/**
 * 入口 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月4日 上午11:43:58
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class MainEntry {

	private static Logger logger = Logger.getLogger(MainEntry.class);

	public static void main(String[] args) {
		System.out.println("");
		System.out.println("系统启动...");

		try {
			DOMConfigurator.configureAndWatch("config/log4j.xml");
			logger.info("加载日志配置成功！");

			//初始化
			SysManager.init();
			// 连接数据库
			new DBAccess();
			logger.info("数据库连接...成功");

			// 添加捕获线程中未能被捕获的异常的默认处理
			ErrHandler handle = new ErrHandler();
			Thread.setDefaultUncaughtExceptionHandler(handle);
			
			new MainEntry().StartSystem();
			logger.info("系统启动...成功");
		}
		catch (Exception ex) {
			logger.error("程序启动失败:" + ex.getMessage());
			ex.printStackTrace();
			System.exit(0);
		}

	}

	public MainEntry() {

	}

	/**
	 * 启动任务管理
	 */
	private void StartSystem() {
//		Thread task = new Thread(new DataSyncThread(), "DataSyncThread");
//		task.start();
//		Thread task2 = new Thread(new DataReportThread(), "DataReportThread");
//		task2.start();
//		new DataRainfallThread();
//		new DataVisibilityThread();
		//交通事件回传
		new SendTrafficEventToGmapThread();

		
		String runModel = SysManager.getInstance().getRunModelNum();
		logger.info("将启动模板编号..." + runModel);
		if (runModel != null) {
			String[] model = runModel.split(",");
			boolean isRun = false;
			String modelNum = "";
			for (String str : model) {
				isRun = false;
				modelNum = str;
				switch (str) {
				case "1":
					new CreateModelOne();
					isRun = true;
					break;
				case "2":
					new CreateModelTwo();
					isRun = true;
					break;
				case "4":
					new CreateModelFour();
					isRun = true;
					break;
				case "6":
					new CreateModelSix();
					isRun = true;
					break;
				case "7":
					new CreateModelSeven();
					isRun = true;
					break;
				case "9":
					new CreateModelNine();
					isRun = true;
					break;
				case "10":
					new CreateModelTen();
					isRun = true;
					break;
				case "11":
					new CreateModelEleven();
					isRun = true;
					break;
				case "12":
					new CreateModelTwelve();
					isRun = true;
					break;
				case "12a":
					new CreateModelTwelveA();
					isRun = true;
					break;
				case "13":
					new CreateModelThirteen();
					isRun = true;
					break;
				default:
					break;
				}
				
				if(isRun) {
				logger.info("启动模板" + modelNum+"成功！");
				}
				else {
					logger.info("没有找到需要启动的模板编号" + modelNum);
				}
				
			}
		}
	}
}

