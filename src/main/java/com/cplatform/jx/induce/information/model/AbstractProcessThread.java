package com.cplatform.jx.induce.information.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cplatform.jx.induce.information.SysManager;
import com.cplatform.jx.induce.information.device.DeviceItemInfo;
import com.cplatform.jx.induce.information.device.PlayListItemInfo;
import com.cplatform.jx.induce.information.item.ImgInfo;
import com.cplatform.jx.induce.information.item.TxtInfo;
import com.cplatform.jx.induce.information.model.info.ImgsInfo;
import com.cplatform.jx.induce.information.model.info.ModelItemInfo;
import com.cplatform.jx.induce.information.model.info.ModelSevenConfigInfo;
import com.cplatform.jx.induce.information.model.info.TextImgsInfo;
import com.cplatform.jx.induce.information.model.info.TextsInfo;
import com.cplatform.jx.induce.information.model.info.TimesInfo;
import com.cplatform.jx.induce.information.model.info.VideosInfo;
import com.cplatform.jx.induce.information.traffic.GetInduceStateByAmap;
import com.cplatform.jx.induce.information.traffic.GetInduceTimeByAmap;
import com.cplatform.jx.induce.information.traffic.info.DataInfo;
import com.cplatform.jx.induce.information.traffic.info.InduceStateRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceStateResponse;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeRequest;
import com.cplatform.jx.induce.information.traffic.info.InduceTimeResponse;
import com.cplatform.jx.induce.information.traffic.info.PathsInfo;
import com.cplatform.jx.induce.information.traffic.info.RouteInfo;
import com.cplatform.jx.induce.information.traffic.info.TimeDataInfo;
import com.cplatform.util2.FileTools;
import com.cplatform.util2.TimeStamp;

public abstract class AbstractProcessThread extends Thread {

	private int SLEEP_TIME = 500;

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	/** 线程停止标记 */
	private boolean shutdownTag = false;

	/** 自动消息缓存 */
	private Map<String, Set<DeviceItemInfo>> itemMap = new ConcurrentHashMap<String, Set<DeviceItemInfo>>();

	protected String getThreadName() {
		return getClass().getSimpleName() + "-" + getId();
	}

	protected void init(int sleepTime) {
		this.setName(getThreadName());
		this.SLEEP_TIME = sleepTime;
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

	protected void putItemMap(String ncode, DeviceItemInfo info) {
		Set<DeviceItemInfo> item = itemMap.get(ncode);
		if (item == null) {
			item = new HashSet<DeviceItemInfo>();
			itemMap.put(ncode, item);
		}
		item.add(info);
	}

	// 遍历itemMap 是否已经发送此消息
	protected boolean checkItemMap(String ncode, DeviceItemInfo info) {

		// Set<DeviceItemInfo> item = itemMap.get(ncode);
		// if (item != null && (!item.isEmpty())) {
		// for (DeviceItemInfo entry : item) {
		// // 是否失效
		// if (entry.getEND_TIME().compareTo(TimeStamp.getTime(14)) < 0) {
		// // 移除对象
		// item.remove(entry);
		// continue;
		// }
		// // 比较对象
		// if (info.getINFO_SOURCE().compareTo(entry.getINFO_SOURCE()) == 0
		// && info.getINFO_TYPE().compareTo(entry.getINFO_TYPE()) == 0
		// && info.getContType() == entry.getContType()) {
		// return false;
		// }
		// }
		// item.add(info);
		// return true;
		// } else {
		// item = new HashSet<DeviceItemInfo>();
		// itemMap.put(ncode, item);
		// item.add(info);
		// return true;
		// }
		return true;
	}

	// 生成自动信息
	/*
	 * [item3] param=100,0,0,0,0,0,0,0,0 txt1=0,0,3,1616,FF0000,0,0,国人,32,0,0
	 * txtparam1=0,0 img1=32,0,test02.png,0,64,32 imgparam1=100
	 */

	private String createModelItemJson(List<TextsInfo> txt,
			List<TextImgsInfo> txtImg, List<ImgsInfo> img,
			List<VideosInfo> video, List<TimesInfo> times) {
		ModelItemInfo item = new ModelItemInfo();
		item.setImgs(img);
		item.setTextImgs(txtImg);
		item.setTexts(txt);
		item.setVideos(video);
		item.setTimes(times);
		return JSON.toJSONString(item);
	}

	protected DeviceItemInfo WriteOneItem(String[] cont, String screenType,
			String nCode, String infoSource, int color) {
		String time = TimeStamp.getTime(14);
		StringBuffer buf = new StringBuffer(100);
		buf.append(SysManager.getInstance().getItemPath());
		buf.append("/1/");
		buf.append(time.substring(0, 6));
		buf.append("/");
		buf.append(time.substring(6, 8));
		buf.append("/");
		buf.append(time.substring(8, 14));
		buf.append("/");

		String outputPath = buf.toString();
		buf.setLength(0);
		// 获取模板
		PlayListItemInfo model = SysManager.getInstance().getPlayListItemInfo(
				screenType);
		// param
		buf.append("param=").append(model.getParam().toString()).append("\n");
		// txt

		TxtInfo info = model.getTxt().get(0);
		info.setContent(cont[0]);
		// 1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑
		switch (color) {
		case 1:
			info.setFontColor(2);
			break;
		case 2:
			info.setFontColor(4);
			break;
		default:
			info.setFontColor(1);
			break;
		}

		buf.append("txt1=").append(info.toString()).append("\n");
		buf.append("txtparam1=").append(info.getTxtParam().toString())
				.append("\n");

		String times = TimeStamp.getTime(14);
		String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
		String playTime = times.substring(8, 12) + "-"
				+ endTime.substring(8, 12);

		TextsInfo text = new TextsInfo();
		text.setContentWord(cont[0]);
		text.setFontColor("" + info.getFontColor());
		text.setFontStyle("" + info.getFontStyle());
		text.setFontFamily("" + info.getFontBackColor());
		text.setFontSize("" + info.getFontSize());
		text.setHorizontal("" + info.getTxtParam().getOrderType());
		text.setLetterSpace("" + info.getTxtParam().getSpace());
		text.setStayTime("" + model.getParam().getStayTime());
		text.setVertical("1");
		List<TextsInfo> txt = new ArrayList<TextsInfo>();
		txt.add(text);
		List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
		List<ImgsInfo> img = new ArrayList<ImgsInfo>();
		List<VideosInfo> video = new ArrayList<VideosInfo>();
		List<TimesInfo> tim = new ArrayList<TimesInfo>();
		TimesInfo t = new TimesInfo();
		t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
		t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
		tim.add(t);
		String json = createModelItemJson(txt, txtImg, img, video, tim);

		try {

			saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
					+ screenType.replace("*", "_") + "_1" + ".lst");

			DeviceItemInfo item = new DeviceItemInfo();
			item.setCREATE_TIME(TimeStamp.getTime(14));
			item.setEND_TIME(endTime);
			// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
			item.setINFO_SOURCE("4");
			item.setINFO_TYPE("2");
			item.setITEM_PATH(outputPath + nCode + "_"
					+ screenType.replace("*", "_") + "_1" + ".lst");
			item.setPLAY_TIME(playTime);
			item.setSTART_TIME(times);
			item.setDEVICE_CODE(nCode);
			return item;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	protected DeviceItemInfo WriteOneAndTwoItemImage(String[] cont, String screenType,
			String nCode, String infoSource, int color,String imgFileName,String type,String fontSize) {
		{
			
			String time = TimeStamp.getTime(14);
			StringBuffer buf = new StringBuffer(100);
			buf.append(SysManager.getInstance().getItemPath());
			buf.append("/");
			buf.append(type);
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
			PlayListItemInfo model = SysManager.getInstance()
					.getPlayListItemInfo(screenType);
			// param
//			buf.append("param=").append(model.getParam().toString())
//			.append("\n");
			
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (int i = 0; i < cont.length; i++) {
				if (cont[i] != null && cont[i] != "") {
					JSONObject json = new JSONObject();
					json.put("content", cont[i]);
					json.put("color", color);
					json.put("fontStyle", ""
							+ model.getTxt().get(0).getFontStyle());
					json.put("fontType", ""
							+ model.getTxt().get(0).getFontType());
					json.put("fontFamily", model.getTxt().get(0)
							.getFontBackColor());
					json.put("fontSize", fontSize);
//					json.put("fontSize", ""
//							+ model.getTxt().get(0).getFontSize());
					jsonList.add(json);
				}
				
			}
			String imgName = "";
			if (imgFileName == null) {
				return null;
			}
			// img
			imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
			ImgInfo img = model.getImg();
			img.setFileName(imgName);
			buf.append("img1=").append(img.toString()).append("\n");
			buf.append("imgparam1=").append(img.getImgParam().toString())
			.append("\n");
			ImgsInfo imgs = new ImgsInfo();
			imgs.setImgName(imgName);
			imgs.setImgPath(outputPath);
			imgs.setIndex(0);
			
//			imgs.setStayTime("" + img.getImgParam().getStopTime());
			imgs.setStayTime(""+(color==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)
			ChartGraphics chartGraphics = new ChartGraphics();
			if(StringUtils.equals(type, "10")){
				chartGraphics.graphicsGenerationTwelve(jsonList, screenType,outputPath,imgName,color);
			}else{
				
			chartGraphics.graphicsGenerationOneAndTwo(jsonList, screenType,outputPath,imgName,color);
			}
//			chartGraphics.graphicsGenerationTwelve(jsonList, screenType,outputPath,imgName,color);
			
			List<TextsInfo> txts = new ArrayList<TextsInfo>();
			List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
			List<ImgsInfo> imgLis = new ArrayList<ImgsInfo>();
			imgLis.add(imgs);
			List<VideosInfo> video = new ArrayList<VideosInfo>();
			List<TimesInfo> tim = new ArrayList<TimesInfo>();
			
			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12) + "-"
					+ endTime.substring(8, 12);
			
			TimesInfo t = new TimesInfo();
			t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
			t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
			tim.add(t);
			String json = createModelItemJson(txts, txtImg, imgLis, video, tim);
			
			try {
				
				saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
						+ screenType.replace("*", "_") + "_"+type + ".lst");
				
				// String times = TimeStamp.getTime(14);
				// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
				// 14);
				// String playTime = times.substring(8,
				// 12)+"-"+endTime.substring(8, 12);
				DeviceItemInfo item = new DeviceItemInfo();
				item.setCREATE_TIME(TimeStamp.getTime(14));
				item.setEND_TIME(endTime);
				// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
				item.setINFO_SOURCE("4");
				item.setINFO_TYPE("2");
				item.setITEM_PATH(outputPath + nCode + "_"
						+ screenType.replace("*", "_") + "_"+type + ".lst");
				item.setPLAY_TIME(playTime);
				item.setSTART_TIME(times);
				item.setDEVICE_CODE(nCode);
				return item;
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	protected DeviceItemInfo WriteTwoItem(String[] cont, String screenType,
			String nCode, String infoSource, int color) {
		String time = TimeStamp.getTime(14);
		StringBuffer buf = new StringBuffer(100);
		buf.append(SysManager.getInstance().getItemPath());
		buf.append("/2/");
		buf.append(time.substring(0, 6));
		buf.append("/");
		buf.append(time.substring(6, 8));
		buf.append("/");
		buf.append(time.substring(8, 14));
		buf.append("/");

		String outputPath = buf.toString();
		buf.setLength(0);
		// 获取模板
		PlayListItemInfo model = SysManager.getInstance().getPlayListItemInfo(
				screenType);
		// param
		buf.append("param=").append(model.getParam().toString()).append("\n");

		// 1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑
		int co = 2;
		switch (color) {
		case 1:
			co = 2;
			break;
		case 2:
			co = 4;
			break;
		default:
			co = 1;
			break;
		}
		// txt
		// if (screenType.compareTo("160*96") == 0) {
		TxtInfo info = model.getTxt().get(0);
		info.setContent(cont[0] + cont[1]);
		info.setFontColor(co);
		// TxtInfo info2 = model.getTxt().get(1);
		// info2.setContent(cont[1]);
		// info2.setFontColor(co);
		buf.append("txt1=").append(info.toString()).append("\n");
		buf.append("txtparam1=").append(info.getTxtParam().toString())
				.append("\n");
		// buf.append("txt2=").append(info2.toString()).append("\n");
		// buf.append("txtparam2=").append(info2.getTxtParam().toString()).append("\n");
		// } else {
		// TxtInfo info = model.getTxt().get(0);
		// info.setContent(cont[0]);
		// info.setFontColor(co);
		//
		// buf.append("txt1=").append(info.toString()).append("\n");
		// buf.append("txtparam1=").append(info.getTxtParam().toString()).append("\n");
		// }

		String times = TimeStamp.getTime(14);
		String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
		String playTime = times.substring(8, 12) + "-"
				+ endTime.substring(8, 12);

		TextsInfo text = new TextsInfo();
		text.setContentWord(cont[0]);
		text.setFontColor("" + info.getFontColor());
		text.setFontStyle("" + info.getFontStyle());
		text.setFontFamily("" + info.getFontBackColor());
		text.setFontSize("" + info.getFontSize());
		text.setHorizontal("" + info.getTxtParam().getOrderType());
		text.setLetterSpace("" + info.getTxtParam().getSpace());
		text.setStayTime("" + model.getParam().getStayTime());
		text.setVertical("1");
		List<TextsInfo> txt = new ArrayList<TextsInfo>();
		txt.add(text);
		List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
		List<ImgsInfo> img = new ArrayList<ImgsInfo>();
		List<VideosInfo> video = new ArrayList<VideosInfo>();
		List<TimesInfo> tim = new ArrayList<TimesInfo>();
		TimesInfo t = new TimesInfo();
		t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
		t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
		tim.add(t);
		String json = createModelItemJson(txt, txtImg, img, video, tim);

		try {

			saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
					+ screenType.replace("*", "_") + "_2" + ".lst");

			DeviceItemInfo item = new DeviceItemInfo();
			item.setCREATE_TIME(TimeStamp.getTime(14));
			item.setEND_TIME(endTime);
			// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
			item.setINFO_SOURCE("4");
			item.setINFO_TYPE("2");
			item.setITEM_PATH(outputPath + nCode + "_"
					+ screenType.replace("*", "_") + "_2" + ".lst");
			item.setPLAY_TIME(playTime);
			item.setSTART_TIME(times);
			item.setDEVICE_CODE(nCode);
			return item;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected DeviceItemInfo WriteFourItem(String[] cont, String screenType,
			String nCode, String infoSource, int type) {
		String time = TimeStamp.getTime(14);
		StringBuffer buf = new StringBuffer(100);
		buf.append(SysManager.getInstance().getItemPath());
		buf.append("/4/");
		buf.append(type);
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
		PlayListItemInfo model = SysManager.getInstance().getPlayListItemInfo(
				screenType);
		// param
		buf.append("param=").append(model.getParam().toString()).append("\n");
		// txt

		TxtInfo info = model.getTxt().get(0);
		info.setContent(cont[0]);
		// 1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑
		// switch(color){
		// case 1:
		// info.setFontColor(2);
		// break;
		// case 2:
		info.setFontColor(4);
		// break;
		// default:
		// info.setFontColor(1);
		// break;
		// }
		if (type == 5) {
			buf.append("txt1=").append(info.toString()).append("\n");
		} else {
			info.setContent(cont[0] + cont[1]);
			buf.append("txt1=").append(info.toString()).append("\n");
		}
		buf.append("txtparam1=").append(info.getTxtParam().toString())
				.append("\n");

		String times = TimeStamp.getTime(14);
		String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
		String playTime = times.substring(8, 12) + "-"
				+ endTime.substring(8, 12);

		TextsInfo text = new TextsInfo();
		text.setContentWord(info.getContent());
		text.setFontColor("" + info.getFontColor());
		text.setFontStyle("" + info.getFontStyle());
		text.setFontFamily("" + info.getFontBackColor());
		text.setFontSize("" + info.getFontSize());
		text.setHorizontal("" + info.getTxtParam().getOrderType());
		text.setLetterSpace("" + info.getTxtParam().getSpace());
		text.setStayTime("" + model.getParam().getStayTime());
		text.setVertical("1");
		List<TextsInfo> txt = new ArrayList<TextsInfo>();
		txt.add(text);
		List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
		List<ImgsInfo> img = new ArrayList<ImgsInfo>();
		List<VideosInfo> video = new ArrayList<VideosInfo>();
		List<TimesInfo> tim = new ArrayList<TimesInfo>();
		TimesInfo t = new TimesInfo();
		t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
		t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
		tim.add(t);
		String json = createModelItemJson(txt, txtImg, img, video, tim);

		try {

			saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
					+ screenType.replace("*", "_") + "_" + type + ".lst");

			// String times = TimeStamp.getTime(14);
			// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
			// 14);
			// String playTime = times.substring(8, 12)+"-"+endTime.substring(8,
			// 12);
			DeviceItemInfo item = new DeviceItemInfo();
			item.setCREATE_TIME(TimeStamp.getTime(14));
			item.setEND_TIME(endTime);
			// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
			item.setINFO_SOURCE("4");
			item.setINFO_TYPE("2");
			item.setITEM_PATH(outputPath + nCode + "_"
					+ screenType.replace("*", "_") + "_" + type + ".lst");
			item.setPLAY_TIME(playTime);
			item.setSTART_TIME(times);
			item.setDEVICE_CODE(nCode);
			return item;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected DeviceItemInfo WriteSevenItem(String[] cont, String screenType,
			String nCode, String infoSource, int[] col,
			ModelSevenConfigInfo cinfo) {

		String time = TimeStamp.getTime(14);
		StringBuffer buf = new StringBuffer(100);
		buf.append(SysManager.getInstance().getItemPath());
		buf.append("/7/");
		buf.append(time.substring(0, 6));
		buf.append("/");
		buf.append(time.substring(6, 8));
		buf.append("/");
		buf.append(time.substring(8, 14));
		buf.append("/");

		String outputPath = buf.toString();
		buf.setLength(0);
		// 获取模板
		PlayListItemInfo model = SysManager.getInstance().getPlayListItemInfo(
				screenType);
		// param
		buf.append("param=").append(model.getParam().toString()).append("\n");

		int num = 0;
		int color = 0;
		// txt
		// if (screenType.compareTo("160*96") == 0) {
		String txt = "";
		if (cont[0] != null && cont[0] != "" && col[0] > 0) {
			TxtInfo info = model.getTxt().get(0);
			info.setContent(cont[0]);
			info.setFontColor(getColor(col[0]));
			buf.append("txt1=").append(info.toString()).append("\n");
			buf.append("txtparam1=").append(info.getTxtParam().toString())
					.append("\n");
			num++;
			txt += cont[0] + "\\r\\n";
			color = col[0];
		}
		if (cont[1] != null && cont[1] != "" && col[1] > 0) {
			TxtInfo info2 = model.getTxt().get(1);
			info2.setContent(cont[1]);
			info2.setFontColor(getColor(col[1]));

			buf.append("txt2=").append(info2.toString()).append("\n");
			buf.append("txtparam2=").append(info2.getTxtParam().toString())
					.append("\n");
			num++;
			txt += cont[1] + "\\r\\n";
		}
		if (cont[2] != null && cont[2] != "" && col[2] > 0) {
			TxtInfo info3 = model.getTxt().get(2);
			info3.setContent(cont[2]);
			info3.setFontColor(getColor(col[2]));
			buf.append("txt3=").append(info3.toString()).append("\n");
			buf.append("txtparam3=").append(info3.getTxtParam().toString())
					.append("\n");
			num++;
			txt += cont[2] + "\\r\\n";
		}

		if (num == 0)
			return null;
		// }

		// 由于后端不支持多个txt，目前 按第一个 颜色来，
		TextsInfo text = new TextsInfo();
		text.setContentWord(txt);
		text.setFontColor("" + color);
		text.setFontStyle("" + model.getTxt().get(0).getFontStyle());
		text.setFontFamily("" + model.getTxt().get(0).getFontBackColor());
		text.setFontSize("" + model.getTxt().get(0).getFontSize());
		text.setHorizontal(""
				+ model.getTxt().get(0).getTxtParam().getOrderType());
		text.setLetterSpace("" + model.getTxt().get(0).getTxtParam().getSpace());
		text.setStayTime("" + model.getParam().getStayTime());
		text.setVertical("1");
		List<TextsInfo> txts = new ArrayList<TextsInfo>();
		txts.add(text);
		List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
		List<ImgsInfo> img = new ArrayList<ImgsInfo>();
		List<VideosInfo> video = new ArrayList<VideosInfo>();
		List<TimesInfo> tim = new ArrayList<TimesInfo>();

		String times = TimeStamp.getTime(14);
		String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
		String playTime = times.substring(8, 12) + "-"
				+ endTime.substring(8, 12);

		TimesInfo t = new TimesInfo();
		t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
		t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
		tim.add(t);
		String json = createModelItemJson(txts, txtImg, img, video, tim);

		try {

			saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
					+ screenType.replace("*", "_") + "_7" + ".lst");

			// String times = TimeStamp.getTime(14);
			// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
			// 14);
			// String playTime = times.substring(8, 12)+"-"+endTime.substring(8,
			// 12);
			DeviceItemInfo item = new DeviceItemInfo();
			item.setCREATE_TIME(TimeStamp.getTime(14));
			item.setEND_TIME(endTime);
			// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
			item.setINFO_SOURCE("4");
			item.setINFO_TYPE("2");
			item.setITEM_PATH(outputPath + nCode + "_"
					+ screenType.replace("*", "_") + "_7" + ".lst");
			item.setPLAY_TIME(playTime);
			item.setSTART_TIME(times);
			item.setDEVICE_CODE(nCode);
			return item;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected DeviceItemInfo WriteTenItemWithImages(List<String[]>  contList,
			String screenType, String nCode, List<int[]>  colList,List<String> imgFileNameList,List<Integer> color1List, String[] minute,String fontSize) {
		{
			
			String time = TimeStamp.getTime(14);
			StringBuffer buf = new StringBuffer(100);
			buf.append(SysManager.getInstance().getItemPath());
			buf.append("/10/");
			buf.append(time.substring(0, 6));
			buf.append("/");
			buf.append(time.substring(6, 8));
			buf.append("/");
			buf.append(time.substring(8, 14));
			buf.append("/");
			
			String outputPath = buf.toString();
			buf.setLength(0);
			// 获取模板
			PlayListItemInfo model = SysManager.getInstance()
					.getPlayListItemInfo(screenType);
			// param
			List<ImgsInfo> imgLis = new ArrayList<ImgsInfo>();
			
			int num = 0;
			for(int j=0;j<contList.size();j++){
				int color = 0;
				String cont[]=contList.get(j);
				int []col=colList.get(j);
				String imgFileName=imgFileNameList.get(j);
				int color1=color1List.get(j);
//				if (cont[0] != null && cont[0] != "" && col[0] > 0) {
//					TxtInfo info = model.getTxt().get(0);
//					info.setContent(cont[0]);
//					info.setFontColor(col[0]);
//					num++;
//					color = col[0];
//				}
//				if (cont[1] != null && cont[1] != "" && col[1] > 0) {
//					TxtInfo info2 = model.getTxt().get(1);
//					info2.setContent(cont[1]);
//					info2.setFontColor(col[1]);
//					num++;
//				}
				
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				for (int i = 0; i < cont.length; i++) {
					if (cont[i] != null && cont[i] != "" && col[i] > 0) {
						JSONObject json = new JSONObject();
						json.put("content", cont[i]);
						json.put("minute", minute[i]);
						json.put("color", getColor(col[i]));
						json.put("fontStyle", ""
								+ model.getTxt().get(0).getFontStyle());
						json.put("fontType", ""
								+ model.getTxt().get(0).getFontType());
						json.put("fontFamily", model.getTxt().get(0)
								.getFontBackColor());
						json.put("fontSize", fontSize);
//						json.put("fontSize", ""
//								+ model.getTxt().get(0).getFontSize());
						jsonList.add(json);
						num++;
					}
					
				}
				String imgName = "";
				if (imgFileName == null) {
					return null;
				}
				// img
				imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
				ImgInfo img = model.getImg();
				img.setFileName(imgName);
				ImgsInfo imgs = new ImgsInfo();
				imgs.setImgName(imgName);
				imgs.setImgPath(outputPath);
				imgs.setIndex(0);
				imgs.setStayTime(""+(color1==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)
				ChartGraphics chartGraphics = new ChartGraphics();
				if(j==1){
					chartGraphics.graphicsGenerationTen(jsonList, screenType,outputPath,imgName);
				}else{			  
					
					chartGraphics.graphicsGenerationTen2(jsonList, screenType, outputPath, imgName);
				}
				imgLis.add(imgs);
			}
			
			if (num == 0)
				return null;
			List<TextsInfo> txts = new ArrayList<TextsInfo>();
			List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
			
			
			List<VideosInfo> video = new ArrayList<VideosInfo>();
			List<TimesInfo> tim = new ArrayList<TimesInfo>();
			
			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12) + "-"
					+ endTime.substring(8, 12);
			
			TimesInfo t = new TimesInfo();
			t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
			t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
			tim.add(t);
			String json = createModelItemJson(txts, txtImg, imgLis, video, tim);
			
			try {
				
				saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
						+ screenType.replace("*", "_") + "_10" + ".lst");
				
				// String times = TimeStamp.getTime(14);
				// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
				// 14);
				// String playTime = times.substring(8,
				// 12)+"-"+endTime.substring(8, 12);
				DeviceItemInfo item = new DeviceItemInfo();
				item.setCREATE_TIME(TimeStamp.getTime(14));
				item.setEND_TIME(endTime);
				// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
				item.setINFO_SOURCE("4");
				item.setINFO_TYPE("2");
				item.setITEM_PATH(outputPath + nCode + "_"
						+ screenType.replace("*", "_") + "_10" + ".lst");
				item.setPLAY_TIME(playTime);
					item.setSTART_TIME(times);
				item.setDEVICE_CODE(nCode);
				return item;
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	protected DeviceItemInfo WriteTwelveItemWithImages(String  cont,
			String screenType, String nCode, String imgFileName,int color1,String fontSize) {
		{
			
			String time = TimeStamp.getTime(14);
			StringBuffer buf = new StringBuffer(100);
			buf.append(SysManager.getInstance().getItemPath());
			buf.append("/12/");
			buf.append(time.substring(0, 6));
			buf.append("/");
			buf.append(time.substring(6, 8));
			buf.append("/");
			buf.append(time.substring(8, 14));
			buf.append("/");
			
			String outputPath = buf.toString();
			buf.setLength(0);
			// 获取模板
			PlayListItemInfo model = SysManager.getInstance()
					.getPlayListItemInfo(screenType);
			// param
//			buf.append("param=").append(model.getParam().toString())
//			.append("\n");
			
			int num = 0;
			int color = 0;
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
				if (StringUtils.isNotBlank(cont)) {
					JSONObject json = new JSONObject();
					json.put("content", cont);
					json.put("color", "1");
					json.put("fontStyle", ""
							+ model.getTxt().get(0).getFontStyle());
					json.put("fontType", ""
							+ model.getTxt().get(0).getFontType());
					json.put("fontFamily", model.getTxt().get(0)
							.getFontBackColor());
					json.put("fontSize", fontSize);
//					json.put("fontSize", ""
//							+ model.getTxt().get(0).getFontSize());
					jsonList.add(json);
					num++;
			}
			String imgName = "";
			if (imgFileName == null) {
				return null;
			}
			// img
			imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
			ImgInfo img = model.getImg();
			img.setFileName(imgName);
//			buf.append("img1=").append(img.toString()).append("\n");
//			buf.append("imgparam1=").append(img.getImgParam().toString())
//			.append("\n");
			ImgsInfo imgs = new ImgsInfo();
			imgs.setImgName(imgName);
			imgs.setImgPath(outputPath);
			imgs.setIndex(0);
//			imgs.setStayTime("" + img.getImgParam().getStopTime());
			imgs.setStayTime(""+(color1==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)
			ChartGraphics chartGraphics = new ChartGraphics();
//			chartGraphics.graphicsGenerationSenven(jsonList, screenType,outputPath,imgName);
			chartGraphics.graphicsGenerationTwelve(jsonList, screenType, outputPath, imgName, color);
			
			if (num == 0)
				return null;
			// }
			List<TextsInfo> txts = new ArrayList<TextsInfo>();
			List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
			List<ImgsInfo> imgLis = new ArrayList<ImgsInfo>();
			imgLis.add(imgs);
			List<VideosInfo> video = new ArrayList<VideosInfo>();
			List<TimesInfo> tim = new ArrayList<TimesInfo>();
			
			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12) + "-"
					+ endTime.substring(8, 12);
			
			TimesInfo t = new TimesInfo();
			t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
			t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
			tim.add(t);
			String json = createModelItemJson(txts, txtImg, imgLis, video, tim);
			
			try {
				
				saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
						+ screenType.replace("*", "_") + "_11" + ".lst");
				
				// String times = TimeStamp.getTime(14);
				// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
				// 14);
				// String playTime = times.substring(8,
				// 12)+"-"+endTime.substring(8, 12);
				DeviceItemInfo item = new DeviceItemInfo();
				item.setCREATE_TIME(TimeStamp.getTime(14));
				item.setEND_TIME(endTime);
				// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
				item.setINFO_SOURCE("4");
				item.setINFO_TYPE("2");
				item.setITEM_PATH(outputPath + nCode + "_"
						+ screenType.replace("*", "_") + "_11" + ".lst");
				item.setPLAY_TIME(playTime);
				item.setSTART_TIME(times);
				item.setDEVICE_CODE(nCode);
				return item;
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	protected DeviceItemInfo WriteElevenItemWithImages(String[] cont,
			String screenType, String nCode,  int[] col, String[] minute,String imgFileName,int color1,String fontSize) {
		{
			
			String time = TimeStamp.getTime(14);
			StringBuffer buf = new StringBuffer(100);
			buf.append(SysManager.getInstance().getItemPath());
			buf.append("/11/");
			buf.append(time.substring(0, 6));
			buf.append("/");
			buf.append(time.substring(6, 8));
			buf.append("/");
			buf.append(time.substring(8, 14));
			buf.append("/");
			
			String outputPath = buf.toString();
			buf.setLength(0);
			// 获取模板
			PlayListItemInfo model = SysManager.getInstance()
					.getPlayListItemInfo(screenType);
			// param
//			buf.append("param=").append(model.getParam().toString())
//			.append("\n");
			
			int num = 0;
			int color = 0;
//			if (cont[0] != null && cont[0] != "" && col[0] > 0) {
//				TxtInfo info = model.getTxt().get(0);
//				info.setContent(cont[0]);
//				info.setFontColor(col[0]);
//				buf.append("txt1=").append(info.toString()).append("\n");
//				buf.append("txtparam1=").append(info.getTxtParam().toString())
//				.append("\n");
//				num++;
//				color = col[0];
//			}
//			if (cont[1] != null && cont[1] != "" && col[1] > 0) {
//				TxtInfo info2 = model.getTxt().get(1);
//				info2.setContent(cont[1]);
//				info2.setFontColor(col[1]);
//				
//				buf.append("txt2=").append(info2.toString()).append("\n");
//				buf.append("txtparam2=").append(info2.getTxtParam().toString())
//				.append("\n");
//				num++;
//			}
			
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (int i = 0; i < cont.length; i++) {
				if (cont[i] != null && cont[i] != "" && col[i] > 0) {
					JSONObject json = new JSONObject();
					json.put("content", cont[i]);
					json.put("minute", minute[i]);
					json.put("color", getColor(col[i]));
					json.put("fontStyle", ""
							+ model.getTxt().get(0).getFontStyle());
					json.put("fontType", ""
							+ model.getTxt().get(0).getFontType());
					json.put("fontFamily", model.getTxt().get(0)
							.getFontBackColor());
					json.put("fontSize", fontSize);
//					json.put("fontSize", ""
//							+ model.getTxt().get(0).getFontSize());
					jsonList.add(json);
					num++;
				}
				
			}
			String imgName = "";
			if (imgFileName == null) {
				return null;
			}
			// img
			imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
			ImgInfo img = model.getImg();
			img.setFileName(imgName);
			buf.append("img1=").append(img.toString()).append("\n");
			buf.append("imgparam1=").append(img.getImgParam().toString())
			.append("\n");
			ImgsInfo imgs = new ImgsInfo();
			imgs.setImgName(imgName);
			imgs.setImgPath(outputPath);
			imgs.setIndex(0);
//			imgs.setStayTime("" + img.getImgParam().getStopTime());
			imgs.setStayTime(""+(color1==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)
			ChartGraphics chartGraphics = new ChartGraphics();
//			chartGraphics.graphicsGenerationSenven(jsonList, screenType,outputPath,imgName);
			chartGraphics.graphicsGenerationEleven(jsonList, screenType,outputPath,imgName);
			
			if (num == 0)
				return null;
			// }
			List<TextsInfo> txts = new ArrayList<TextsInfo>();
			List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
			List<ImgsInfo> imgLis = new ArrayList<ImgsInfo>();
			imgLis.add(imgs);
			List<VideosInfo> video = new ArrayList<VideosInfo>();
			List<TimesInfo> tim = new ArrayList<TimesInfo>();
			
			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12) + "-"
					+ endTime.substring(8, 12);
			
			TimesInfo t = new TimesInfo();
			t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
			t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
			tim.add(t);
			String json = createModelItemJson(txts, txtImg, imgLis, video, tim);
			
			try {
				
				saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
						+ screenType.replace("*", "_") + "_11" + ".lst");
				
				// String times = TimeStamp.getTime(14);
				// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
				// 14);
				// String playTime = times.substring(8,
				// 12)+"-"+endTime.substring(8, 12);
				DeviceItemInfo item = new DeviceItemInfo();
				item.setCREATE_TIME(TimeStamp.getTime(14));
				item.setEND_TIME(endTime);
				// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
				item.setINFO_SOURCE("4");
				item.setINFO_TYPE("2");
				item.setITEM_PATH(outputPath + nCode + "_"
						+ screenType.replace("*", "_") + "_11" + ".lst");
				item.setPLAY_TIME(playTime);
				item.setSTART_TIME(times);
				item.setDEVICE_CODE(nCode);
				return item;
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	protected DeviceItemInfo WriteSevenItemWithImages(String[] cont,
			String screenType, String nCode, String infoSource, int[] col,
			ModelSevenConfigInfo cinfo, String[] minute,String imgFileName,int color1,String fontSize) {
		{
logger.info("调用WriteSevenItemWithImages>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			String time = TimeStamp.getTime(14);
			StringBuffer buf = new StringBuffer(100);
			buf.append(SysManager.getInstance().getItemPath());
			buf.append("/7/");
			buf.append(time.substring(0, 6));
			buf.append("/");
			buf.append(time.substring(6, 8));
			buf.append("/");
			buf.append(time.substring(8, 14));
			buf.append("/");

			String outputPath = buf.toString();
			buf.setLength(0);
			// 获取模板
//			PlayListItemInfo model = SysManager.getInstance()
//					.getPlayListItemInfo(screenType);
			// param
//			buf.append("param=").append(model.getParam().toString())
//					.append("\n");

			int num = 0;
			int color = 0;
//			if (cont[0] != null && cont[0] != "" && col[0] > 0) {
//				TxtInfo info = model.getTxt().get(0);
//				info.setContent(cont[0]);
//				info.setFontColor(col[0]);
//				buf.append("txt1=").append(info.toString()).append("\n");
//				buf.append("txtparam1=").append(info.getTxtParam().toString())
//						.append("\n");
//				num++;
//				color = col[0];
//			}
//			if (cont[1] != null && cont[1] != "" && col[1] > 0) {
//				TxtInfo info2 = model.getTxt().get(1);
//				info2.setContent(cont[1]);
//				info2.setFontColor(col[1]);
//
//				buf.append("txt2=").append(info2.toString()).append("\n");
//				buf.append("txtparam2=").append(info2.getTxtParam().toString())
//						.append("\n");
//				num++;
//			}
//			if (cont[2] != null && cont[2] != "" && col[2] > 0) {
//				TxtInfo info3 = model.getTxt().get(2);
//				info3.setContent(cont[2]);
//				info3.setFontColor(col[2]);
//				buf.append("txt3=").append(info3.toString()).append("\n");
//				buf.append("txtparam3=").append(info3.getTxtParam().toString())
//						.append("\n");
//				num++;
//			}

			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (int i = 0; i < cont.length; i++) {
				if (cont[i] != null && cont[i] != "" && col[i] > 0) {
					JSONObject json = new JSONObject();
					json.put("content", cont[i]);
					json.put("minute", minute[i]);
					json.put("color", getColor(col[i]));
//					json.put("fontStyle", ""
//							+ model.getTxt().get(0).getFontStyle());
//					json.put("fontType", ""
//							+ model.getTxt().get(0).getFontType());
//					json.put("fontFamily", model.getTxt().get(0)
//							.getFontBackColor());
					json.put("fontSize", fontSize);
					jsonList.add(json);
					num++;
				}

			}
			logger.info("jsonList================="+jsonList);
			String imgName = "";
			logger.info("imgFileName================="+imgFileName);
			if (imgFileName == null) {
				return null;
			}
			// img
			imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
//			ImgInfo img = model.getImg();
//			img.setFileName(imgName);
//			buf.append("img1=").append(img.toString()).append("\n");
//			buf.append("imgparam1=").append(img.getImgParam().toString())
//					.append("\n");
			ImgsInfo imgs = new ImgsInfo();
			imgs.setImgName(imgName);
			imgs.setImgPath(outputPath);
			imgs.setIndex(0);
//			imgs.setStayTime("" + img.getImgParam().getStopTime());
			imgs.setStayTime(""+(color1==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)
			ChartGraphics chartGraphics = new ChartGraphics();
			logger.info("开始调用生成模板7方法>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			chartGraphics.graphicsGenerationSenven(jsonList, screenType,outputPath,imgName);
			logger.info("结束调用生成模板7方法<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			if (num == 0)
				return null;
			// }
			List<TextsInfo> txts = new ArrayList<TextsInfo>();
			List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
			List<ImgsInfo> imgLis = new ArrayList<ImgsInfo>();
			imgLis.add(imgs);
			List<VideosInfo> video = new ArrayList<VideosInfo>();
			List<TimesInfo> tim = new ArrayList<TimesInfo>();

			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12) + "-"
					+ endTime.substring(8, 12);

			TimesInfo t = new TimesInfo();
			t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
			t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
			tim.add(t);
			String json = createModelItemJson(txts, txtImg, imgLis, video, tim);

			try {

				saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
						+ screenType.replace("*", "_") + "_7" + ".lst");

				// String times = TimeStamp.getTime(14);
				// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
				// 14);
				// String playTime = times.substring(8,
				// 12)+"-"+endTime.substring(8, 12);
				DeviceItemInfo item = new DeviceItemInfo();
				item.setCREATE_TIME(TimeStamp.getTime(14));
				item.setEND_TIME(endTime);
				// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
				item.setINFO_SOURCE("4");
				item.setINFO_TYPE("2");
				item.setITEM_PATH(outputPath + nCode + "_"
						+ screenType.replace("*", "_") + "_7" + ".lst");
				item.setPLAY_TIME(playTime);
				item.setSTART_TIME(times);
				item.setDEVICE_CODE(nCode);
				return item;
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	/***
	 * 
	 * @param cont
	 * @param screenType
	 * @param nCode
	 * @param cinfo
	 * @param minute
	 * @param imgFileName
	 * @return
	 */
	protected DeviceItemInfo WriteFourItemWithImages(String[] cont,
			String screenType, String nCode, String infoSource, String imgFileName,int type,int color) {
		{
			
			String time = TimeStamp.getTime(14);
			StringBuffer buf = new StringBuffer(100);
			buf.append(SysManager.getInstance().getItemPath());
			buf.append("/4/");
			buf.append(time.substring(0, 6));
			buf.append("/");
			buf.append(time.substring(6, 8));
			buf.append("/");
			buf.append(time.substring(8, 14));
			buf.append("/");
			
			String outputPath = buf.toString();
			buf.setLength(0);
			// 获取模板
			PlayListItemInfo model = SysManager.getInstance()
					.getPlayListItemInfo(screenType);
			// param
			buf.append("param=").append(model.getParam().toString())
			.append("\n");
			
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (int i = 0; i < cont.length; i++) {
				if (cont[i] != null && cont[i] != "") {
					JSONObject json = new JSONObject();
					json.put("content", cont[i]);
					json.put("color", color);
					json.put("fontStyle", ""
							+ model.getTxt().get(0).getFontStyle());
					json.put("fontType", ""
							+ model.getTxt().get(0).getFontType());
					json.put("fontFamily", model.getTxt().get(0)
							.getFontBackColor());
					json.put("fontSize", ""
							+ model.getTxt().get(0).getFontSize());
					jsonList.add(json);
				}
				
			}
			String imgName = "";
			if (imgFileName == null) {
				return null;
			}
			// img
			imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
			ImgInfo img = model.getImg();
			img.setFileName(imgName);
			buf.append("img1=").append(img.toString()).append("\n");
			buf.append("imgparam1=").append(img.getImgParam().toString())
			.append("\n");
			ImgsInfo imgs = new ImgsInfo();
			imgs.setImgName(imgName);
			imgs.setImgPath(outputPath);
			imgs.setIndex(0);
//			imgs.setStayTime("" + img.getImgParam().getStopTime());
			imgs.setStayTime(""+(color==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)
			ChartGraphics chartGraphics = new ChartGraphics();
			chartGraphics.graphicsGenerationFour(jsonList, screenType,outputPath,imgName,color);
			
			List<TextsInfo> txts = new ArrayList<TextsInfo>();
			List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
			List<ImgsInfo> imgLis = new ArrayList<ImgsInfo>();
			imgLis.add(imgs);
			List<VideosInfo> video = new ArrayList<VideosInfo>();
			List<TimesInfo> tim = new ArrayList<TimesInfo>();
			
			String times = TimeStamp.getTime(14);
			String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
			String playTime = times.substring(8, 12) + "-"
					+ endTime.substring(8, 12);
			
			TimesInfo t = new TimesInfo();
			t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
			t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
			tim.add(t);
			String json = createModelItemJson(txts, txtImg, imgLis, video, tim);
			
			try {
				
				saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
						+ screenType.replace("*", "_") + "_"+type + ".lst");
				
				// String times = TimeStamp.getTime(14);
				// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
				// 14);
				// String playTime = times.substring(8,
				// 12)+"-"+endTime.substring(8, 12);
				DeviceItemInfo item = new DeviceItemInfo();
				item.setCREATE_TIME(TimeStamp.getTime(14));
				item.setEND_TIME(endTime);
				// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
				item.setINFO_SOURCE("4");
				item.setINFO_TYPE("2");
				item.setITEM_PATH(outputPath + nCode + "_"
						+ screenType.replace("*", "_") + "_"+type + ".lst");
				item.setPLAY_TIME(playTime);
				item.setSTART_TIME(times);
				item.setDEVICE_CODE(nCode);
				return item;
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

//	private int getColor(int color) {
//		// 1-红 2-绿 3-蓝 4-黄 5-紫 6-青 7-白 8黑
//		int co = 2;
//		switch (color) {
//		case 1:
//			co = 2;
//			break;
//		case 2:
//			co = 4;
//			break;
//		default:
//			co = 1;
//			break;
//		}
//		return co;
//	}
	private int getColor(int color) {
		
		return color;
	}

	protected DeviceItemInfo WriteSixItem(String screenType, String nCode,
			String imgFileName, String infoSource, int type,int color) {
		String time = TimeStamp.getTime(14);
		StringBuffer buf = new StringBuffer(100);
		buf.append(SysManager.getInstance().getItemPath());
		buf.append("/6/");
		buf.append(type);
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
//		PlayListItemInfo model = SysManager.getInstance().getPlayListItemInfo(
//				screenType);
		// param
//		buf.append("param=").append(model.getParam().toString()).append("\n");
//		// txt
		String imgName = "";
		if (imgFileName == null) {
			return null;
		}
		// img
		imgName = imgFileName.substring(imgFileName.lastIndexOf("/") + 1);
//		ImgInfo img = model.getImg();
//		img.setFileName(imgName);
//		buf.append("img1=").append(img.toString()).append("\n");
//		buf.append("imgparam1=").append(img.getImgParam().toString())
//				.append("\n");

		String times = TimeStamp.getTime(14);
		String endTime = TimeStamp.getAddTime(null, TimeStamp.HOUR, 1, 14);
		String playTime = times.substring(8, 12) + "-"
				+ endTime.substring(8, 12);

		ImgsInfo imgs = new ImgsInfo();
		imgs.setImgName(imgName);
		imgs.setImgPath(outputPath);
		imgs.setIndex(0);
//		imgs.setStayTime("" + img.getImgParam().getStopTime());
		imgs.setStayTime(""+(color==1?5:10));//畅通为5秒，缓行为10秒，拥堵为覆盖10秒(无实际意义)

		List<TextsInfo> txt = new ArrayList<TextsInfo>();
		List<TextImgsInfo> txtImg = new ArrayList<TextImgsInfo>();
		List<ImgsInfo> imglis = new ArrayList<ImgsInfo>();
		imglis.add(imgs);
		List<VideosInfo> video = new ArrayList<VideosInfo>();
		List<TimesInfo> tim = new ArrayList<TimesInfo>();
		TimesInfo t = new TimesInfo();
		t.setStart(times.substring(8, 10) + ":" + times.substring(10, 12));
		t.setEnd(endTime.substring(8, 10) + ":" + endTime.substring(10, 12));
		tim.add(t);

		String json = createModelItemJson(txt, txtImg, imglis, video, tim);

		try {

			saveFile(json.getBytes("utf-8"), outputPath, nCode + "_"
					+ screenType.replace("*", "_") + "_" + type + ".lst");
			if (imgFileName != null) {
				// 拷贝图片资源文件
				FileTools.copy(imgFileName, outputPath + imgName);
			}

			// String times = TimeStamp.getTime(14);
			// String endTime = TimeStamp.getAddTime(null,TimeStamp.HOUR, 1,
			// 14);
			// String playTime = times.substring(8, 12)+"-"+endTime.substring(8,
			// 12);
			DeviceItemInfo item = new DeviceItemInfo();
			item.setCREATE_TIME(TimeStamp.getTime(14));
			item.setEND_TIME(endTime);
			// 自动信息来源 如果为自动信息，则有5个来源，1气象信息；2能见度信息；3事故信息；4路况信息；5养护信息
			item.setINFO_SOURCE("4");
			//info_type 1 手工消息 2自动消息
			item.setINFO_TYPE("2");
			item.setITEM_PATH(outputPath + nCode + "_"
					+ screenType.replace("*", "_") + "_" + type + ".lst");
			item.setPLAY_TIME(playTime);
			item.setSTART_TIME(times);
			item.setDEVICE_CODE(nCode);
			return item;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void saveFile(byte[] bytes, String path, String filename)
			throws Exception {
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
	
	
	public int getColor(String city_code, String road_code) {
		try {
			int color = 0;
			float tempColor=0f;
			float all = 0;
			InduceStateRequest req = new InduceStateRequest();
			req.setCity(city_code);
			req.setRoadId(road_code);
			InduceStateResponse stateResp = GetInduceStateByAmap.getInduceState(req);
			if (stateResp != null) {
				logger.debug("调用高德接口! req:" + req.toString() + " 结果：" + stateResp.toString());
				if (stateResp.getStatus().getCode() == 0) {
					// 计算系数
					float num = 0;
					for (DataInfo d : stateResp.getData()) {
//						if (d.getStatus() >= 2) {
							num += d.getLength() * d.getStatus();
							all += d.getLength();
//						}
					}
					if (num > 0 && all > 0)
						tempColor = num / all;
				} else {
					logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp.toString());
				}
			} else {
				logger.error("调用高德接口返回结果异常!req:" + req.toString() + " 结果：" + stateResp);
			}
			color=isCt(tempColor);
			return color;
		} catch (Exception e) {
			logger.error("请求接口异常:", e);
		}
		return 0;
	}
	
	/***
	 * 根据配置文件自定义拥堵细数计算拥堵状态
	 * @param num
	 * @return
	 */
	public int isCt(float num){
		float unimpeded=SysManager.getInstance().getUnimpeded();
		float amble=SysManager.getInstance().getAmble();
		float congestion=SysManager.getInstance().getCongestion();
		
		if(num>=unimpeded&&num<amble){
			return 1;
		}
		if(num>=amble&&num<congestion){
			return 2;
		}
		if(num>=congestion){
			return 3;
		}
		
		return 0;
	}
	
	
	public static void main(String[] args) {
		float a=10;
		
		float b=25;
		float c=b/a;
		float d =b/a;
		System.out.println(c);
		System.out.println(d);
	}
	
	public int getTime(String city_code, String start_point, String end_point) {
		try {
			int allTime = 0;
			InduceTimeRequest treq = new InduceTimeRequest();
			treq.setCity(city_code);
			treq.setEndpos(start_point);
			treq.setStartpos(end_point);
			treq.setStrategy("0");

			InduceTimeResponse tresp = GetInduceTimeByAmap.getInduceState(treq);
			if (tresp != null) {
				logger.debug("调用高德接口2! req:" + treq.toString() + " 结果：" + tresp.toString());
				if (tresp.getStatus().getCode() == 0) {
					TimeDataInfo td = tresp.getData();
					RouteInfo rinfo = td.getRoute();
					if (rinfo != null) {
						List<PathsInfo> path = rinfo.getPaths();
						if (path != null && (!path.isEmpty())) {
							for (PathsInfo pinfo : path) {
								pinfo.getDistance();
								allTime += Integer.valueOf(pinfo.getDuration());
							}

						}
					}
				} else {
					logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" + tresp.toString());
				}
			} else {
				logger.debug("调用高德接口2 异常! req:" + treq.toString() + " 结果：" + tresp);

			}
			return allTime;
		} catch (Exception e) {
			logger.error("请求接口异常:", e);
		}
		return 0;
	}
}
