package com.cplatform.jx.induce.information.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.PlayListItemInfo;
import com.cplatform.jx.induce.information.item.ImgInfo;
import com.cplatform.jx.induce.information.item.TxtInfo;
import com.cplatform.util2.FileTools;
import com.cplatform.util2.TimeStamp;

public abstract class AbstractProcessThread extends Thread {

	private static final int SLEEP_TIME = 500;

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	/** 线程停止标记 */
	private boolean shutdownTag = false;

	/** 自动消息缓存*/
	private Map<String, Set<DeviceItemInfo>> itemMap = new ConcurrentHashMap<String, Set<DeviceItemInfo>>();


	protected String getThreadName() {
		return getClass().getSimpleName() + "-" + getId();
	}

	protected void init() {
		this.setName(getThreadName());
		this.start();
	}

	protected abstract void execTask() throws Exception;

	@Override
	public void run() {
		boolean needSleep = true;
		logger.info("自动消息：" + getThreadName() + "线程启动!");
		while (shutdownTag == false) {
			try {
				if (needSleep) {
					sleep(SLEEP_TIME);

					// continue;
				}
				execTask();
			} catch (Exception ex) {
				logger.error("处理异常", ex);
			}
		}
	}

	protected  void  putItemMap(String ncode, DeviceItemInfo info){
		Set<DeviceItemInfo> item = itemMap.get(ncode);
		if(item == null){
			item =  new HashSet<DeviceItemInfo>();
    		itemMap.put(ncode, item);
    	}
		item.add(info);
	}
	
	// 遍历itemMap 是否已经发送此消息
	protected boolean checkItemMap(String ncode, DeviceItemInfo info) {

		Set<DeviceItemInfo> item = itemMap.get(ncode);
		if (item != null && (!item.isEmpty())) {
			for (DeviceItemInfo entry : item) {
				// 是否失效
				if (entry.getEND_TIME().compareTo(TimeStamp.getTime(14)) < 0) {
					// 移除对象
					item.remove(entry);
					continue;
				}
				// 比较对象
				if (info.getINFO_SOURCE().compareTo(entry.getINFO_SOURCE()) == 0
						&& info.getINFO_TYPE().compareTo(entry.getINFO_TYPE()) == 0
						&& info.getContType() == entry.getContType()) {
					return false;
				}
			}
			item.add(info);
			return true;
		} else {
			item = new HashSet<DeviceItemInfo>();
			itemMap.put(ncode, item);
			item.add(info);
			return true;
		}
	}

	// 生成自动信息
	/*
	 * [item3] param=100,0,0,0,0,0,0,0,0 txt1=0,0,3,1616,FF0000,0,0,国人,32,0,0
	 * txtparam1=0,0 img1=32,0,test02.png,0,64,32 imgparam1=100
	 */

	protected DeviceItemInfo WriteItem(String[] cont, String screenType, String nCode, String imgFileName,String infoSource) {
		String time = TimeStamp.getTime(14);
		StringBuffer buf = new StringBuffer(100);
		buf.append(SysManager.getInstance().getItemPath());
		buf.append("/");
		buf.append(time.substring(0, 6));
		buf.append("/");
		buf.append(time.substring(6, 8));
		buf.append("/");
		buf.append(time.substring(8, 14));
		buf.append("/");

		String outputPath = buf.toString();
		buf.setLength(0);
		// 获取模板
		PlayListItemInfo model = SysManager.getInstance().getPlayListItemInfo(screenType);
		// param
		buf.append("param=").append(model.getParam().toString()).append("\n");
		// txt
		if (screenType.compareTo("160*96") == 0) {
			TxtInfo info = model.getTxt().get(0);
			info.setContent(cont[0]);
			TxtInfo info2 = model.getTxt().get(1);
			info2.setContent(cont[1]);
			buf.append("txt1=").append(info.toString()).append("\n");
			buf.append("txtparam1=").append(info.getTxtParam().toString()).append("\n");
			buf.append("txt2=").append(info2.toString()).append("\n");
			buf.append("txtparam2=").append(info2.getTxtParam().toString()).append("\n");
		} else {
			TxtInfo info = model.getTxt().get(0);
			info.setContent(cont[0]);

			buf.append("txt1=").append(info.toString()).append("\n");
			buf.append("txtparam1=").append(info.getTxtParam().toString()).append("\n");
		}
		
		if(imgFileName != null){
		// img
		ImgInfo img = model.getImg();
		img.setFileName(SysManager.getInstance().getVisibImgFile());
		buf.append("img1=").append(img.toString()).append("\n");
		buf.append("imgparam1=").append(img.getImgParam().toString()).append("\n");
		}

		try {

			saveFile(buf.toString().getBytes("utf-8"), outputPath, nCode + "_" + screenType.replace("*", "_") + ".lst");
			if(imgFileName != null){
			// 拷贝图片资源文件
			FileTools.copy(SysManager.getInstance().getSourceImgPath() + "/" + imgFileName, outputPath + imgFileName);
			}

			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12)+"-"+endTime.substring(8, 12);
			DeviceItemInfo item = new DeviceItemInfo();
			item.setCREATE_TIME(TimeStamp.getTime(14));
			item.setEND_TIME(endTime);
			// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
			item.setINFO_SOURCE(infoSource);
			item.setINFO_TYPE("2");
			item.setITEM_PATH(outputPath + nCode + "_" + screenType.replace("*", "_") + ".lst");
			item.setPLAY_TIME(playTime);
			item.setSTART_TIME(times);
			return item;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void saveFile(byte[] bytes, String path, String filename) throws Exception {
		try {
			File filedir = new File(path);
			if (!filedir.exists())
				filedir.mkdirs();
			File file = new File(filedir, filename);
			OutputStream os = new FileOutputStream(file);
			os.write(bytes, 0, bytes.length);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
