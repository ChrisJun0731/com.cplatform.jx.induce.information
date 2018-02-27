package com.cplatform.jx.induce.information.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cplatform.jx.induce.information.model.info.ModelSixConfigInfo;
import com.cplatform.jx.induce.information.model.info.ModelSixInfo;

public class ChangIMGColor {

	/** 日志记录器 */
	private static Logger logger = Logger.getLogger(ChangIMGColor.class);
	public static void main(String[] args) {
//		ChangIMGColor t =new ChangIMGColor();
//		try {
//			t.replaceImageColor("F:\\T\\12\\svgImg.png",Color.white,Color.yellow,0,0,20,20,"F:\\T\\12\\svgImg22.png");
//		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	
	public static boolean replaceImageColor(List<ModelSixInfo> sixInfos, String file, Color srcColor,String resultpath, String resultfile,ModelSixConfigInfo confInfo,String [] weaInfo) throws IOException{
        try{
		URL http = null;
        if(file.trim().startsWith("https")){
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else if(file.trim().startsWith("http")){
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else{
            http = new File(file).toURI().toURL();
        }
        BufferedImage bi = ImageIO.read(http.openStream());
        
        for(ModelSixInfo six :sixInfos){

        for (int i = six.getUP_LEFT_X(); i < six.getUNDER_RIGHT_X(); i++) {
            for (int j = six.getUP_LEFT_Y(); j < six.getUNDER_RIGHT_Y(); j++) {
//                  System.out.println(bi.getRGB(i, j));
            	try{
            		//如果基图上的该像素点的颜色为白色
                  	if(srcColor.getRGB()==bi.getRGB(i, j)){
//                      System.out.println(i+","+j+"  from:"+srcColor.getRGB()+"to"+six.getColor().getRGB());
                      	//将白色替换为该路段的颜色
						bi.setRGB(i, j, six.getColor().getRGB());
                  	}
            	}
            	catch(Exception e){
            		logger.error("转化异常！i:"+i+"j:"+j,e);
            	}
            }
        }
        if(!(six.getText()==null||six.getText().isEmpty()||six.getTEXT_PX_X()<0||six.getTEXT_PX_Y()<0)){
        	
        //写文字
        Graphics2D graphic = bi.createGraphics();

        //设置字体
        if(confInfo.getType()==12){
        	
        	graphic.setFont(new Font("黑体", Font.PLAIN, six.getTEXT_SIZE()));
        }else{
        	graphic.setFont(new Font(null, Font.BOLD, six.getTEXT_SIZE()));
        	
        }

        //设置字体颜色
        switch(six.getTEXT_COLOR()){
        
        case 1:
        	graphic.setColor(new Color(0xffffff));
        	break;
        case 2:
        	graphic.setColor(new Color(0xFFFF00));
        	break;
        case 3:
        	graphic.setColor(new Color(0xFF0000));
        	break;
        default:
        	graphic.setColor(new Color(0x00FF00));
        		break;
        	
        }
        

        // 绘制文本, 其中坐标参数指的是文本绘制后的 左下角 的位置
        // 首次绘制需要初始化字体, 可能需要较耗时

			// 这个地方绘制的字体是每个路段对应的文字，即config_param表中的配置
        graphic.drawString(six.getText(), six.getTEXT_PX_X(), six.getTEXT_PX_Y());
        graphic.dispose();
        }
        }
        if(confInfo.getType()==6){
        	
        	if(!(confInfo==null||StringUtils.isEmpty(confInfo.getTextName())||confInfo.getFontSize()<0||confInfo.getTextX()<0||confInfo.getTextY()<0)){
        		
        		//写文字
        		Graphics2D graphic = bi.createGraphics();
        		graphic.setFont(new Font(null, Font.BOLD, confInfo.getFontSize()));
        		switch(confInfo.getFontColor()){
        		
        		 case 1:
        	        	graphic.setColor(new Color(0xffffff));
        	        	break;
        	        case 2:
        	        	graphic.setColor(new Color(0xFFFF00));
        	        	break;
        	        case 3:
        	        	graphic.setColor(new Color(0xFF0000));
        	        	break;
        	        default:
        	        	graphic.setColor(new Color(0x00FF00));
        	        		break;
        		}
        		
        		
        		// 绘制文本, 其中坐标参数指的是文本绘制后的 左下角 的位置
        		// 首次绘制需要初始化字体, 可能需要较耗时
        		graphic.drawString(confInfo.getTextName(), confInfo.getTextX(), confInfo.getTextY());
        		graphic.dispose();
        	}
        	
        }else if(confInfo.getType()==9){

        	//模板9需要绘制两段文字

        	if(!(confInfo==null||StringUtils.isEmpty(confInfo.getTextName())||confInfo.getFontSize()<0||confInfo.getTextX()<0||confInfo.getTextY()<0)
        		||StringUtils.isEmpty(confInfo.getTextName1())||confInfo.getFontSize()<0||confInfo.getTextX1()<0||confInfo.getTextY1()<0){
        		
        		//写文字
        		Graphics2D graphic = bi.createGraphics();
        		graphic.setFont(new Font(null, Font.BOLD, confInfo.getFontSize()));
        		switch(confInfo.getFontColor()){
        		 case 1:
        	        	graphic.setColor(new Color(0x00FF00));
        	        	break;
        	        case 2:
        	        	graphic.setColor(new Color(0xFFFF00));
        	        	break;
        	        case 3:
        	        	graphic.setColor(new Color(0xFF0000));
        	        	break;
        	        default:
        	        	graphic.setColor(new Color(0x00FF00));
        	        		break;
        			
        		}
        		
        		
        		// 绘制文本, 其中坐标参数指的是文本绘制后的 左下角 的位置
        		// 首次绘制需要初始化字体, 可能需要较耗时
        		graphic.drawString(confInfo.getTextName(), confInfo.getTextX(), confInfo.getTextY());
//        		graphic.dispose();
        		switch(confInfo.getFontColor1()){
        		
        		 case 1:
        	        	graphic.setColor(new Color(0x00FF00));
        	        	break;
        	        case 2:
        	        	graphic.setColor(new Color(0xFFFF00));
        	        	break;
        	        case 3:
        	        	graphic.setColor(new Color(0xFF0000));
        	        	break;
        	        default:
        	        	graphic.setColor(new Color(0x00FF00));
        	        		break;
        			
        		}
        		graphic.drawString(confInfo.getTextName1(), confInfo.getTextX1(), confInfo.getTextY1());
        		graphic.dispose();
        	}
        	
        	if(weaInfo!=null&&weaInfo.length>0){
        		Graphics2D graphic = bi.createGraphics();
        		graphic.setFont(new Font(null, Font.BOLD, confInfo.getWeatherFontSize()));
        		graphic.setColor(new Color(0x00FF00));
        		graphic.drawString(weaInfo[0],confInfo.getWeatherXPoint(), confInfo.getWeatherYPoint());
        		graphic.drawString(weaInfo[1],confInfo.getWindXPoint(), confInfo.getWindYPoint());
        		graphic.dispose();
        	}
        	
        }
        
        
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = it.next();
        File f = new File(resultpath);
        if (!f.exists())
			f.mkdirs();
        f = new File(resultpath+"//"+resultfile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
        return  true;
        }
        catch(Exception e){
        	logger.error("生成图片异常！",e);
        }

        return false;
    }
    
    public void createImage(int width, int height) throws IOException{
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphic = bi.createGraphics();
        graphic.setColor(new Color(0.2f,0.3f,0.4f,0.4f));
        graphic.fillRect(0, 0, width, height);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                  //result[i][j] = bi.getRGB(i, j) & 0xFFFFFF;
                  System.out.println(bi.getRGB(i, j));
                 // bi.setRGB(i, j, 0xFFFFFF);
            }
       }
        
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = it.next();
        File f = new File("c://test02.png");
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        
        writer.write(bi);
    }
    
}
