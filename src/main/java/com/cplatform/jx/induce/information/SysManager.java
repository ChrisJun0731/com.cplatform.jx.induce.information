package com.cplatform.jx.induce.information;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import com.cplatform.config.Config;
import com.cplatform.config.XMLConfig;
import com.cplatform.jx.induce.information.device.PlayListItemInfo;
import com.cplatform.jx.induce.information.item.ImgInfo;
import com.cplatform.jx.induce.information.item.ImgParamInfo;
import com.cplatform.jx.induce.information.item.ParamInfo;
import com.cplatform.jx.induce.information.item.TxtInfo;
import com.cplatform.jx.induce.information.item.TxtParamInfo;

/**
 * 系统管理器. <br>
 * 提供对系统运行状态的监控和对配置文件的访问，配置文件位置为config/sys_config.xml.
 */
public class SysManager {

	/** 对象实例 */
	private static SysManager instance;

	/** 获取对象实例 */
	public static SysManager getInstance() {
		return instance;
	}

	/**
	 * 初始化对象实例
	 * 
	 * @throws Exception
	 */
	synchronized static void init() throws Exception {
		if (instance != null) {
			return;
		}
		instance = new SysManager();
		instance.loadItemInfo();
	}

	/** Logger. */
	private Logger logger;

	/** 系统运行状态. */
	private boolean run = true;

	/** 系统配置. */
	private Config sysConfig;

	private Map<String, PlayListItemInfo> playListItemInfo;

	private static int sequence = 1;

	/**
	 * 对象创建
	 */
	private SysManager() throws Exception {
		logger = Logger.getLogger(SysManager.class);
		loadSysConfig();
		// 启动一个定时检查停止标记的线程（停止标记的文件名.stop）
		new Thread(new Runnable() {

			@Override
			public void run() {
				doStop();
			}

		}, "CheckStopThread").start();

	}

	/**
	 * 停止操作
	 */
	private void doStop() {
		File file = new File(".stop");
		while (true) {
			// 如果文件存在表示要停止系统
			if (file.exists()) {
				run = false;

				// 删除标记文件，退出程序
				file.delete();

				if (logger.isInfoEnabled()) {
					logger.info("系统将要退出");
				}
				break;

			} else {
				// 5秒检查一次
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static synchronized int getSequence() {
		if (sequence++ >= 0x7fffffff) {
			sequence = 1;
		}
		return sequence;
	}

	/**
	 * 获得高德请求地址
	 * 
	 * @return
	 */
	public String getAmapUrl() {
		return sysConfig.getValue("amap_induce_url");
	}

	/**
	 * 获取城市编码
	 * 
	 * @return
	 */
	public String getCityCode() {
		return sysConfig.getValue("amap_city_code");
	}

	/**
	 * 获取序列号
	 * 
	 * @return
	 */
	public String getServiceKey() {
		return sysConfig.getValue("amap_service_key");
	}

	public String getTimeAmapUrl() {
		return sysConfig.getValue("amap_time_url");
	}

	/**
	 * 取得系统的字符集
	 * 
	 * @return 系统的字符集
	 */
	public String getEncoding() {
		return sysConfig.getValue("encoding");
	}

	/**
	 * 能见度阈值
	 * 
	 * @return
	 */
	public int getVisibilityLimit() {
		return sysConfig.getInteger("visibility_limit", 200);
	}

	/**
	 * 雨量阈值
	 * @return
	 */
	public int getRainfallLimit(){
		return sysConfig.getInteger("rainfall_limit", 5);
	}
	
	/**
	 * 自动消息ITEM 路径
	 * @return
	 */
	public String getItemPath(){
		return sysConfig.getValue("item_path");
	}
	
	/**
	 * 模板修改基图 处理路径
	 * @return
	 */
	public String getTempImagePath(){
		return sysConfig.getValue("model_img_path");
	}
	
	/**
	 * 资源图片路径
	 * @return
	 */
	public String getSourceImgPath(){
		return sysConfig.getValue("source_img_path");
	}
	/**
	 * 能见度图片名称
	 * @return
	 */
	public String getVisibImgFile() {
		return sysConfig.getValue("img_visib_filename");
	}
	
	/**
	 * 下雨图片名称
	 * @return
	 */
	public String getRainfalImgFile1() {
		return sysConfig.getValue("img_rain1_filename");
	}
	
	/**
	 * 下雨 限速图片名称
	 * @return
	 */
	public String getRainfalImgFile2() {
		return sysConfig.getValue("img_rain2_filename");
	}
	
	/**
	 * 下雨 冰冻图片名称
	 * @return
	 */
	public String getIceImgFile() {
		return sysConfig.getValue("img_ice_filename");
	}
	/**
	 * 通过类型查询item配置信息
	 * 
	 * @param account
	 * @return
	 */
	public PlayListItemInfo getPlayListItemInfo(String screenType) {
		if (playListItemInfo != null) {
			for (Map.Entry<String, PlayListItemInfo> entry : playListItemInfo.entrySet()) {
				PlayListItemInfo temp = (PlayListItemInfo) entry.getValue();
				if (screenType.contains(entry.getKey()))
					return temp;
			}
		}
		return null;
	}
	
	
	
	/**
	 * 畅通最小值
	 * 
	 * @return
	 */
	public float getUnimpeded() {
		return sysConfig.getFloat("unimpeded", 1);
	}
	
	/**
	 * 缓行最小值
	 * 
	 * @return
	 */
	public float getAmble() {
		return sysConfig.getFloat("amble", 1.5f);
	}
	/**
	 * 拥堵最小值
	 * 
	 * @return
	 */
	public float getCongestion() {
		return sysConfig.getFloat("congestion", 2.0f);
	}
	
	
	

	/**
	 * 得到XML输出格式.<br>
	 * (根据配置文件中的encoding产生，默认为GB18030)
	 * 
	 * @param encoding
	 *            编码格式
	 * @return XML输出格式
	 */
	public Format getXmlFormat() {
		Format format = Format.getPrettyFormat();
		String encoding = getEncoding();
		if ((encoding == null) || (encoding.trim().length() == 0)) {
			// 默认使用GB18030编码
			encoding = "GB18030";
		}
		format.setEncoding(encoding);
		return format;
	}

	/**
	 * 获取系统运行状态
	 * 
	 * @return 系统运行状态
	 */
	public boolean isRun() {
		return run;
	}

	/**
	 * 本地请求发送
	 * 
	 * @return
	 */
	public int getMaxSendTimes() {
		return sysConfig.getInteger("maxsendtime", 1);
	}

	/**
	 * 加载自动消息模板配置信息
	 * 
	 * @throws Exception
	 */
	private void loadItemInfo() throws Exception {
		SAXBuilder builder = new SAXBuilder();

		// 读取当前服务配置文件的路径并作为XML读入
		Document serviceInfoDoc = builder.build(new File(sysConfig.getValue("playlist_model")));

		Element root = serviceInfoDoc.getRootElement();

		if (root != null) {
			playListItemInfo = new HashMap<String, PlayListItemInfo>();
			List<?> serviceList = root.getChildren("Item");
			Element serviceInfoElement;

			for (Object object : serviceList) {
				serviceInfoElement = (Element) object;

				PlayListItemInfo info = new PlayListItemInfo();
				// 类路径
				info.setScreenType(serviceInfoElement.getChildText("screen-type").trim());
				info.setScreenDesc(serviceInfoElement.getChildText("screen-desc").trim());

				//param
				Element param = serviceInfoElement.getChild("param");
				if(param != null){
					ParamInfo paramInfo = new ParamInfo();
					paramInfo.setStayTime(Integer.valueOf(param.getChildText("stayTime").trim()));
					paramInfo.setFlashingNum(Integer.valueOf(param.getChildText("flashingNum").trim()));
					paramInfo.setFlashingSpeed(Integer.valueOf(param.getChildText("flashingSpeed").trim()));
					paramInfo.setInScreenMode(Integer.valueOf(param.getChildText("inScreenMode").trim()));
					paramInfo.setInScreenSpeed(Integer.valueOf(param.getChildText("inScreenSpeed").trim()));
					paramInfo.setOutScreenMode(Integer.valueOf(param.getChildText("outScreenMode").trim()));
					paramInfo.setPlayNum(Integer.valueOf(param.getChildText("playNum").trim()));
					info.setParam(paramInfo);
				}
				// img
				Element img = serviceInfoElement.getChild("img");
				if (img != null) {
					ImgInfo imgInfo = new ImgInfo();
					imgInfo.setFileName(img.getChildText("fileName").trim());
					imgInfo.setParam(0);
					imgInfo.setShowHight(Integer.valueOf(img.getChildText("showHight").trim()));
					imgInfo.setShowWidth(Integer.valueOf(img.getChildText("showWidth").trim()));
					imgInfo.setX(Integer.valueOf(img.getChildText("x").trim()));
					imgInfo.setY(Integer.valueOf(img.getChildText("y").trim()));
					ImgParamInfo imgParam = new ImgParamInfo();
					imgParam.setStopTime(Integer.valueOf(img.getChildText("stopTime").trim()));
					imgInfo.setImgParam(imgParam);
					info.setImg(imgInfo);
				}

				// txt
				Element txt = serviceInfoElement.getChild("txt");

				if (txt != null) {
					List<?> txtList = txt.getChildren("txtInfo");
					List<TxtInfo> txts = new ArrayList<TxtInfo>();
					for (Object obj : txtList) {
						Element txtElement = (Element) obj;
						TxtInfo txtInfo = new TxtInfo();
						txtInfo.setContent(txtElement.getChildText("content").trim());
						txtInfo.setFlashing(0);
						txtInfo.setShowHight(Integer.valueOf(txtElement.getChildText("showHight").trim()));
						txtInfo.setShowWidth(Integer.valueOf(txtElement.getChildText("showWidth").trim()));
						txtInfo.setX(Integer.valueOf(txtElement.getChildText("x").trim()));
						txtInfo.setY(Integer.valueOf(txtElement.getChildText("y").trim()));
						txtInfo.setFontBackColor(Integer.valueOf(txtElement.getChildText("fontBackColor").trim()));
						txtInfo.setFontColor(Integer.valueOf(txtElement.getChildText("fontColor").trim()));
						txtInfo.setFontSize(Integer.valueOf(txtElement.getChildText("fontSize").trim()));
						txtInfo.setFontStyle(Integer.valueOf(txtElement.getChildText("fontStyle").trim()));
						txtInfo.setFontType(Integer.valueOf(txtElement.getChildText("fontType").trim()));
						TxtParamInfo txtParam = new TxtParamInfo();
						txtParam.setOrderType(Integer.valueOf(txtElement.getChildText("orderType").trim()));
						txtParam.setSpace(Integer.valueOf(txtElement.getChildText("space").trim()));
						txtInfo.setTxtParam(txtParam);
						txts.add(txtInfo);
					}

					info.setTxt(txts);
				}
				playListItemInfo.put(info.getScreenType(), info);

			}
		}
	}

	
	/**
	 * 获得自动发布地址
	 * 
	 * @return
	 */
	public String getAutoIssuePath() {
		return sysConfig.getValue("auto_issue_path");
	}
	/**
	 * 加载系统配置
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void loadSysConfig() throws Exception {
		// 基本系统配置
		sysConfig = new XMLConfig("基本系统配置", "config/sys_config.xml", true);
	}


	/**
	 * 代理地址
	 * 
	 * @return
	 */
	public String getProxyHost() {
		return sysConfig.getValue("http_proxyHost");
	}
	
	/**
	 * 代理端口
	 * 
	 * @return
	 */
	public String getProxyPort() {
		return sysConfig.getValue("http_proxyPort");
	}
	
	/**
	 * 墨迹天气APPCODE
	 * 
	 * @return
	 */
	public String getAliAppCode() {
		return sysConfig.getValue("ali_weather_appcode");
	}

	/**
	 * 墨迹天气URL
	 * @return
	 */
	public String getAliWeatherUrl() {
		return sysConfig.getValue("ali_weather_url");
	}
	
	/**
	 * 墨迹天气CITYID
	 * @return
	 */
	public String getAliCityId(){
		return sysConfig.getValue("ali_weather_cityid");
	}

	/**
	 * 交通事件高德Channel
	 * @return
	 */
	public String getEventGMapChannel(){
		return sysConfig.getValue("gmap_event_channel");
	}

	/**
	 * 交通事件高德SourceId
	 * @return
	 */
	public String getEventGMapSourceId(){
		return sysConfig.getValue("gmap_event_sourceId");
	}

	/**
	 * 交通事件高德Key
	 * @return
	 */
	public String getEventGMapKey(){
		return sysConfig.getValue("gmap_event_key");
	}

	public String getEventGMapURL() {
		return sysConfig.getValue("gmap_event_url");
	}

	/**
	 * 获取需要启动的模板编号
	 * @return
	 */
	public String getRunModelNum() {
		return sysConfig.getValue("run_model_num");
	}

}
