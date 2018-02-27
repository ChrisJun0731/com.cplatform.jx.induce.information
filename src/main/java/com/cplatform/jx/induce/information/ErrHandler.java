package com.cplatform.jx.induce.information;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

public class ErrHandler implements UncaughtExceptionHandler {

		/** 日志 */
		private static Logger logger = Logger.getLogger(ErrHandler.class.getSimpleName());

		@Override
		public void uncaughtException(Thread thread, Throwable exception) {
			// 将异常记录日志
			logger.error("线程" + thread + "已终止！", exception);
		}
}

