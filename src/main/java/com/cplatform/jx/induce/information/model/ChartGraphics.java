package com.cplatform.jx.induce.information.model;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
 
import java.io.UnsupportedEncodingException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.text.StyledEditorKit;











import com.alibaba.fastjson.JSONObject;
import com.cplatform.jx.induce.information.model.info.TextsInfo;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.org.apache.bcel.internal.classfile.Code;
 
public class ChartGraphics {
    private BufferedImage image;
//    private int imageWidth = 640;  //图片的宽度
//    private int imageHeight = 320; //图片的高度
    //生成图片文件
    @SuppressWarnings("restriction")
    public void createImage(String fileLocation,String saveName) {
        BufferedOutputStream bos = null;
        if(image != null){
            try {
            	File file =new File(fileLocation);    
            	//如果文件夹不存在则创建    
            	if (file.getParentFile().exists()) {  
                    file.mkdir();  
                } else {  
                    mkDir(file.getParentFile());  
                    file.mkdir();    
                }  
            	
            	 
                FileOutputStream fos = new FileOutputStream(fileLocation+"/"+saveName);
                bos = new BufferedOutputStream(fos);
                ImageIO.write(image, "png", bos);//输出png图片
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(bos!=null){//关闭输出流
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static void mkDir(File file) {  
        if (file.getParentFile().exists()) {  
            file.mkdir();  
        } else {  
            mkDir(file.getParentFile());  
            file.mkdir();    
        }  
    }  
    public void graphicsGenerationEleven(List<JSONObject> list,String screenType,String savePath,String saveName) {
    	
    	
    	String [] hw=screenType.split("\\*");
    	Integer imageWidth=Integer.parseInt(hw[0]);
    	Integer imageHeight=Integer.parseInt(hw[1]);
    	
    	image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    	//设置图片的背景色
    	Graphics2D main = image.createGraphics();
//        main.setColor(new Color(0, 0, 0));//背景颜色
    	main.setColor(new Color(0x000000));//背景颜色
    	main.fillRect(0, 0, imageWidth, imageHeight);
    	main.dispose();
    	
    	for (int i = 0; i < list.size(); i++) {
    		String content=list.get(i).getString("content");
    		String minute=list.get(i).getString("minute");
    		Integer color=list.get(i).getInteger("color");
    		Integer fontStyle=list.get(i).getInteger("fontStyle");
    		String fontType=list.get(i).getString("fontType");
    		String fontFamily=list.get(i).getString("fontFamily");
    		String tempSize=list.get(i).getString("fontSize");
    		int fontSize=10;
    		if(tempSize.length()==3){
    			
    			fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 1));
    		}else{
    			fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
    		}
    		Integer fStyle=0;
    		Color c=new Color(0xFF0000);
    		switch (color) {
    		case 1:
    			c=new Color(0x00FF00);//绿
    			break;
    		case 2:
    			c=new Color(0xFFFF00);//黄色
    			break;
    		case 3:
    			c=new Color(0xFF0000);//红
    			break;
    		case 4:
    			c=new Color(0xFF0000);//红
    			break;
    			
    		default:
    			c=new Color(0x00FF00);//绿
    			break;
    		}
    		switch (fontType) {
    		case "1":
    			fontType="黑体";
    			break;
    		case "2":
    			fontType="楷体";
    			break;
    		case "3":
    			fontType="宋体";
    			break;
    		case "4":
    			fontType="仿宋";
    			break;
    		case "5":
    			fontType="隶书";
    			break;
    			
    		default:
    			fontType="宋体";
    			break;
    		}
    		switch (fontStyle) {
    		case 0:
    			fStyle=Font.PLAIN;
    			break;
    		case 1:
    			fStyle=Font.BOLD;
    			break;
    		case 2:
    			fStyle=Font.ITALIC;
    			break;
    		case 4:
    			fStyle=Font.PLAIN;
    			break;
    		case 8:
    			fStyle=Font.PLAIN;
    			break;
    		default:
    			fStyle=Font.PLAIN;
    			break;
    		}
    		Graphics2D gd=image.createGraphics();
    		gd.setColor(new Color(0x00FF00));
    		Font font = new Font(fontType, fStyle, fontSize);
    		gd.setFont(font);
    		if(fontStyle==4||fontStyle==8){
    			Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
    			if(fontStyle==4){
    				map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
    			}else{
    				map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
    			}
    			font = font.deriveFont( map );
    		}
    		
    		FontMetrics fm = gd.getFontMetrics(font);
    		
    		int textHight=fm.getHeight();
    		int h=(imageHeight-2*textHight)/2;
    		
    		
    		int textWidth = fm.stringWidth("分钟");
    		gd.drawString("分钟", imageWidth-textWidth, fontSize*(i+1)+h);//分钟
    		
//	        gd.drawString("分钟", imageWidth-"分钟".length()*fontSize, fontSize*(i+1));//分钟
    		
    		gd.dispose();
    		
    		Graphics2D gd2 = image.createGraphics();
    		gd2.setColor(c);
    		Font font1 = new Font(fontType, fStyle, fontSize);
    		gd2.setFont(font1);
    		
    		FontMetrics fm2 = gd.getFontMetrics(font1);
    		int textWidth2 = fm2.stringWidth(minute);
    		
    		gd2.drawString(minute, imageWidth-textWidth-textWidth2, fontSize*(i+1)+h);//分钟
    		
    		gd2.dispose();
    		
    		Graphics2D gd3 = image.createGraphics();
    		gd3.setColor(new Color(0x00FF00));
    		Font font3 = new Font(fontType, fStyle, fontSize);
    		gd3.setFont(font3);
    		
    		FontMetrics fm3 = gd.getFontMetrics(font3);
    		int textWidth3 = fm3.stringWidth(content+"1"+"111");//加上一个数字占用的宽度(空格)+加上三位数字占用的宽度(分钟数值)
    		
    		
    		
    		gd3.drawString(content, (imageWidth-textWidth-textWidth3)/2, fontSize*(i+1)+h);//分钟
    		
    		gd3.dispose();
    		
    	}
    	
    	createImage(savePath,saveName);
    	
    }
    public void graphicsGenerationTen2(List<JSONObject> list,String screenType,String savePath,String saveName) {
    	
    	
    	String [] hw=screenType.split("\\*");
    	Integer imageWidth=Integer.parseInt(hw[0]);
    	Integer imageHeight=Integer.parseInt(hw[1]);
    	
    	image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    	//设置图片的背景色
    	Graphics2D main = image.createGraphics();
//        main.setColor(new Color(0, 0, 0));//背景颜色
    	main.setColor(new Color(0x000000));//背景颜色
    	main.fillRect(0, 0, imageWidth, imageHeight);
    	main.dispose();
    	
    	for (int i = 0; i < list.size(); i++) {
    		String content=list.get(i).getString("content");
    		String minute=list.get(i).getString("minute");
    		Integer color=list.get(i).getInteger("color");
    		Integer fontStyle=list.get(i).getInteger("fontStyle");
    		String fontType=list.get(i).getString("fontType");
//    		String fontFamily=list.get(i).getString("fontFamily");
    		String tempSize=list.get(i).getString("fontSize");
    		int fontSize=10;
    		if(tempSize.length()==3){
    			
    			fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 1));
    		}else{
    			fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
    		}
    		Integer fStyle=0;
    		Color c=new Color(0xFF0000);
    		switch (color) {
    		case 1:
    			c=new Color(0x00FF00);//绿
    			break;
    		case 2:
    			c=new Color(0xFFFF00);//黄色
    			break;
    		case 3:
    			c=new Color(0xFF0000);//红
    			break;
    		case 4:
    			c=new Color(0xFF0000);//红
    			break;
    			
    		default:
    			c=new Color(0x00FF00);//绿
    			break;
    		}
    		switch (fontType) {
    		case "1":
    			fontType="黑体";
    			break;
    		case "2":
    			fontType="楷体";
    			break;
    		case "3":
    			fontType="宋体";
    			break;
    		case "4":
    			fontType="仿宋";
    			break;
    		case "5":
    			fontType="隶书";
    			break;
    			
    		default:
    			fontType="宋体";
    			break;
    		}
    		switch (fontStyle) {
    		case 0:
    			fStyle=Font.PLAIN;
    			break;
    		case 1:
    			fStyle=Font.BOLD;
    			break;
    		case 2:
    			fStyle=Font.ITALIC;
    			break;
    		case 4:
    			fStyle=Font.PLAIN;
    			break;
    		case 8:
    			fStyle=Font.PLAIN;
    			break;
    		default:
    			fStyle=Font.PLAIN;
    			break;
    		}
    		Graphics2D gd=image.createGraphics();
    		gd.setColor(c);
    		Font font = new Font(fontType, fStyle, fontSize);
    		gd.setFont(font);
    		if(fontStyle==4||fontStyle==8){
    			Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
    			if(fontStyle==4){
    				map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
    			}else{
    				map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
    			}
    			font = font.deriveFont( map );
    		}
    		
    		FontMetrics fm = gd.getFontMetrics(font);
    		int textWidth = fm.stringWidth(content);
    		int textHight=fm.getHeight();
    		int stringAscent = fm.getAscent(); 
    		int stringDescent = fm.getDescent (); 
    		if(list.size()==1){
    			
    			gd.drawString(content, (imageWidth-textWidth)/2, (imageHeight+stringAscent-stringDescent)/2);//分钟
    		}else if(list.size()==2){
    			if(i==0){
    				gd.drawString(content, (imageWidth-textWidth)/2, (imageHeight+stringAscent-stringDescent-textHight)/2);//分钟
    			}else if(i==1){
    				gd.drawString(content, (imageWidth-textWidth)/2, (imageHeight+stringAscent-stringDescent+textHight)/2);
    			}
    		}
    		
    		gd.dispose();
    		
    	}
    	
    	createImage(savePath,saveName);
    	
    }
    public void graphicsGenerationSenven(List<JSONObject> list,String screenType,String savePath,String saveName) {
    	
		
    	String [] hw=screenType.split("\\*");
    	Integer imageWidth=Integer.parseInt(hw[0]);
    	Integer imageHeight=Integer.parseInt(hw[1]);
    	
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        //设置图片的背景色
        Graphics2D main = image.createGraphics();
//        main.setColor(new Color(0, 0, 0));//背景颜色
        main.setColor(new Color(0x000000));//背景颜色
        main.fillRect(0, 0, imageWidth, imageHeight);
        main.dispose();
         
        for (int i = 0; i < list.size(); i++) {
        	String content=list.get(i).getString("content");
			String minute=list.get(i).getString("minute");
			Integer color=list.get(i).getInteger("color");
			Integer fontStyle=8;
			String fontType="3";
			String fontFamily=list.get(i).getString("fontFamily");
			String tempSize=list.get(i).getString("fontSize");
			int fontSize=10;
			if(tempSize.length()==3){
				
				 fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 1));
			}else{
				 fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
			}
			Integer fStyle=0;
			Color c=new Color(0xFF0000);
			switch (color) {
			case 1:
				c=new Color(0x00FF00);//绿
				break;
			case 2:
				c=new Color(0xFFFF00);//黄色
				break;
			case 3:
				c=new Color(0xFF0000);//红
				break;
			case 4:
				c=new Color(0xFF0000);//红
				break;

			default:
				c=new Color(0x00FF00);//绿
				break;
			}
			switch (fontType) {
			case "1":
				fontType="黑体";
				break;
			case "2":
				fontType="楷体";
				break;
			case "3":
				fontType="宋体";
				break;
			case "4":
				fontType="仿宋";
				break;
			case "5":
				fontType="隶书";
				break;

			default:
				fontType="宋体";
				break;
			}
			switch (fontStyle) {
			case 0:
				fStyle=Font.PLAIN;
				break;
			case 1:
				fStyle=Font.BOLD;
				break;
			case 2:
				fStyle=Font.ITALIC;
				break;
			case 4:
				fStyle=Font.PLAIN;
				break;
			case 8:
				fStyle=Font.PLAIN;
				break;
			default:
				fStyle=Font.PLAIN;
				break;
			}
			Graphics2D gd=image.createGraphics();
			gd.setColor(new Color(0x00FF00));
	        Font font = new Font(fontType, fStyle, fontSize);
	        gd.setFont(font);
	        if(fontStyle==4||fontStyle==8){
	          Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
	          if(fontStyle==4){
	        	  map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
	          }else{
	        	  map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
	          }
	          font = font.deriveFont( map );
	        }
	        
	        FontMetrics fm = gd.getFontMetrics(font);
			int textWidth = fm.stringWidth("分钟");
			int textHight=fm.getHeight();
			int h=(imageHeight+2*3-3*(textHight+2))/2;
			
    		gd.drawString("分钟", imageWidth-textWidth, (fontSize+2)*(i+1)+h);//分钟
	        
//	        gd.drawString("分钟", imageWidth-"分钟".length()*fontSize, fontSize*(i+1));//分钟
	        
	        gd.dispose();
	        
	        Graphics2D gd2 = image.createGraphics();
	        gd2.setColor(c);
	        Font font1 = new Font(fontType, fStyle, fontSize);
	        gd2.setFont(font1);
	        
	        FontMetrics fm2 = gd.getFontMetrics(font1);
			int textWidth2 = fm2.stringWidth(minute);
			
    		gd2.drawString(minute, imageWidth-textWidth-textWidth2, (fontSize+2)*(i+1)+h);//分钟
    		
	        gd2.dispose();
	        
	        Graphics2D gd3 = image.createGraphics();
	        gd3.setColor(new Color(0x00FF00));
	        Font font3 = new Font(fontType, fStyle, fontSize);
	        gd3.setFont(font3);
	        
	        FontMetrics fm3 = gd.getFontMetrics(font3);
			int textWidth3 = fm3.stringWidth(content+"1"+"111");//加上一个数字占用的宽度(空格)+加上三位数字占用的宽度(分钟数值)
			
			
			
    		gd3.drawString(content, (imageWidth-textWidth-textWidth3)/2, (fontSize+2)*(i+1)+h);//分钟
	        
	        gd3.dispose();
	        
		}
        
        createImage(savePath,saveName);
         
    }
 public void graphicsGenerationTen(List<JSONObject> list,String screenType,String savePath,String saveName) {
 	
		
 	String [] hw=screenType.split("\\*");
 	Integer imageWidth=Integer.parseInt(hw[0]);
 	Integer imageHeight=Integer.parseInt(hw[1]);
 	
     image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
     //设置图片的背景色
     Graphics2D main = image.createGraphics();
//     main.setColor(new Color(0, 0, 0));//背景颜色
     main.setColor(new Color(0x000000));//背景颜色
     main.fillRect(0, 0, imageWidth, imageHeight);
     main.dispose();
      
     for (int i = 0; i < list.size(); i++) {
     	String content=list.get(i).getString("content");
			String minute=list.get(i).getString("minute");
			Integer color=list.get(i).getInteger("color");
			Integer fontStyle=list.get(i).getInteger("fontStyle");
			String fontType=list.get(i).getString("fontType");
			String fontFamily=list.get(i).getString("fontFamily");
			String tempSize=list.get(i).getString("fontSize");
			int fontSize=10;
			if(tempSize.length()==3){
				
				 fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 1));
			}else{
				 fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
			}
			Integer fStyle=0;
			Color c=new Color(0xFF0000);
			switch (color) {
			case 1:
				c=new Color(0x00FF00);//绿
				break;
			case 2:
				c=new Color(0xFFFF00);//黄色
				break;
			case 3:
				c=new Color(0xFF0000);//红
				break;
			case 4:
				c=new Color(0xFF0000);//红
				break;

			default:
				c=new Color(0x00FF00);//绿
				break;
			}
			switch (fontType) {
			case "1":
				fontType="黑体";
				break;
			case "2":
				fontType="楷体";
				break;
			case "3":
				fontType="宋体";
				break;
			case "4":
				fontType="仿宋";
				break;
			case "5":
				fontType="隶书";
				break;

			default:
				fontType="宋体";
				break;
			}
			switch (fontStyle) {
			case 0:
				fStyle=Font.PLAIN;
				break;
			case 1:
				fStyle=Font.BOLD;
				break;
			case 2:
				fStyle=Font.ITALIC;
				break;
			case 4:
				fStyle=Font.PLAIN;
				break;
			case 8:
				fStyle=Font.PLAIN;
				break;
			default:
				fStyle=Font.PLAIN;
				break;
			}
			Graphics2D gd=image.createGraphics();
			gd.setColor(new Color(0x00FF00));
	        Font font = new Font(fontType, fStyle, fontSize);
	        gd.setFont(font);
	        if(fontStyle==4||fontStyle==8){
	          Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
	          if(fontStyle==4){
	        	  map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
	          }else{
	        	  map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
	          }
	          font = font.deriveFont( map );
	        }
	        
	        FontMetrics fm = gd.getFontMetrics(font);
	        
	        
    		int textHight=fm.getHeight();
    		int h=(imageHeight-2*textHight)/2;
			int textWidth = fm.stringWidth("分钟");
//			gd.drawString("分钟", imageWidth-textWidth, (imageHeight+stringAscent-stringDescent-textHight)/2);//分钟
 		   gd.drawString("分钟", imageWidth-textWidth, fontSize*(i+1)+h);//分钟
	        
//	        gd.drawString("分钟", imageWidth-"分钟".length()*fontSize, fontSize*(i+1));//分钟
	        
	        gd.dispose();
	        
	        Graphics2D gd2 = image.createGraphics();
	        gd2.setColor(c);
	        Font font1 = new Font(fontType, fStyle, fontSize);
	        gd2.setFont(font1);
	        
	        FontMetrics fm2 = gd.getFontMetrics(font1);
			int textWidth2 = fm2.stringWidth(minute);
			
			gd2.drawString(minute, imageWidth-textWidth-textWidth2, fontSize*(i+1)+h);//分钟
 		
	        gd2.dispose();
	        
	        Graphics2D gd3 = image.createGraphics();
	        gd3.setColor(new Color(0x00FF00));
	        Font font3 = new Font(fontType, fStyle, fontSize);
	        gd3.setFont(font3);
	        
	        FontMetrics fm3 = gd.getFontMetrics(font3);
			int textWidth3 = fm3.stringWidth(content+"1"+"111");//加上一个数字占用的宽度(空格)+加上三位数字占用的宽度(分钟数值)
			
			
			
			gd3.drawString(content, (imageWidth-textWidth-textWidth3)/2, fontSize*(i+1)+h);//分钟
			
			
			
	        
	        gd3.dispose();
	        
	        
	        
		}
     
     createImage(savePath,saveName);
      
 }
    /**
     * 生成图片
     * @param list
     * @param screenType
     * @param savePath
     * @param saveName
     */
    public void graphicsGenerationFour(List<JSONObject> list,String screenType,String savePath,String saveName,int color) {
    	
    	
    	String [] hw=screenType.split("\\*");
    	Integer imageWidth=Integer.parseInt(hw[0]);
    	Integer imageHeight=Integer.parseInt(hw[1]);
    	
    	image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    	//设置图片的背景色
    	Graphics2D main = image.createGraphics();
    	main.setColor(new Color(0x000000));//背景颜色
    	main.setStroke(new BasicStroke(1f));
    	
    	main.fillRect(0, 0, imageWidth, imageHeight);
    	main.dispose();
    	 
    	int j=1;
    	for (int i = 0; i < list.size(); i++) {
    		String content=list.get(i).getString("content");
    		Integer fontStyle=list.get(i).getInteger("fontStyle");
    		String fontType=list.get(i).getString("fontType");
    		String tempSize=list.get(i).getString("fontSize");
    		int fontSize=10;
			if(tempSize.length()==3){
				
				 fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 1));
			}else{
				 fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
			}
    		Integer fStyle=0;
    		Color c=new Color(0xFF0000);
			switch (color) {
			case 1:
				
				c=new Color(0x00FF00);//绿
				break;
			case 2:
				c=new Color(0xFFFF00);//黄色
				break;
			case 3:
				c=new Color(0xFF0000);//红
				break;
			case 4:
				c=new Color(0xFF0000);//红
				break;

			default:
				c=new Color(0x00FF00);//绿
				break;
			}
    		switch (fontType) {
    		case "1":
    			fontType="黑体";
    			break;
    		case "2":
    			fontType="楷体";
    			break;
    		case "3":
    			fontType="宋体";
    			break;
    		case "4":
    			fontType="仿宋";
    			break;
    		case "5":
    			fontType="隶书";
    			break;
    			
    		default:
    			fontType="宋体";
    			break;
    		}
    		switch (fontStyle) {
    		case 0:
    			fStyle=Font.PLAIN;
    			break;
    		case 1:
    			fStyle=Font.BOLD;
    			break;
    		case 2:
    			fStyle=Font.ITALIC;
    			break;
    		case 4:
    			fStyle=Font.PLAIN;
    			break;
    		case 8:
    			fStyle=Font.PLAIN;
    			break;
    		default:
    			fStyle=Font.PLAIN;
    			break;
    		}
    		
    		
    		int rowNum=imageWidth/fontSize;
    		if(content.length()>rowNum){//换行
    			String [] arrContent=slipRow(content, rowNum);
    			Graphics2D gd=image.createGraphics();
        		gd.setColor(c);
        		Font font = new Font(fontType, fStyle, fontSize);
        		gd.setFont(font);
        		gd.setStroke(new BasicStroke(1f));
        		if(fontStyle==4||fontStyle==8){
        			Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
        			if(fontStyle==4){
        				map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
        			}else{
        				map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
        			}
        			font = font.deriveFont( map );
        		}
    			for (String string : arrContent) {

    				FontMetrics fm = gd.getFontMetrics(font);
					int textWidth = fm.stringWidth(string);
            		gd.drawString(string, (imageWidth-textWidth)/2, fontSize*(j++));//分钟
				}
    			
    		}else{
    			Graphics2D gd=image.createGraphics();
        		gd.setColor(c);
        		Font font = new Font(fontType, fStyle, fontSize);
        		gd.setFont(font);
        		if(fontStyle==4||fontStyle==8){
        			Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
        			if(fontStyle==4){
        				map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
        			}else{
        				map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
        			}
        			font = font.deriveFont( map );
        		}
        		gd.setStroke(new BasicStroke(1f));
        		FontMetrics fm = gd.getFontMetrics(font);
				int textWidth = fm.stringWidth(content);
        		gd.drawString(content, (imageWidth-textWidth)/2, fontSize*(j++));//分钟
        		gd.dispose();
    		}
    		
    	}
    	
    	createImage(savePath,saveName);
    	
    }
    /**
     * 生成图片
     * @param list
     * @param screenType
     * @param savePath
     * @param saveName
     * @throws UnsupportedEncodingException 
     */
    public void graphicsGenerationOneAndTwo(List<JSONObject> list,String screenType,String savePath,String saveName,int color){
    	
    	
    	String [] hw=screenType.split("\\*");
    	Integer imageWidth=Integer.parseInt(hw[0]);
    	Integer imageHeight=Integer.parseInt(hw[1]);
    	
    	image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    	//设置图片的背景色
    	Graphics2D main = image.createGraphics();
//    	main.setColor(new Color(0, 0, 0));//背景颜色
    	main.setColor(new Color(0x000000));//背景颜色
    	main.fillRect(0, 0, imageWidth, imageHeight);
    	main.dispose();
    	int j=1;
    	for (int i = 0; i < list.size(); i++) {
    		String content=list.get(i).getString("content");
    		Integer fontStyle=list.get(i).getInteger("fontStyle");
    		String fontType=list.get(i).getString("fontType");
//			int fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
    		String tempSize=list.get(i).getString("fontSize");
    		int fontSize=16;
    		if(tempSize!=null) {
    		if(tempSize.length()==3){
    			
    			fontSize=Integer.parseInt(tempSize.substring(0, 1));
    		}else{
    			fontSize=Integer.parseInt(tempSize.substring(0, 2));
    		}
    		}
    		Integer fStyle=0;
    		Color c=new Color(0xFF0000);
			switch (color) {
			case 1:
				
				c=new Color(0x00FF00);//绿
				break;
			case 2:
				c=new Color(0xFFFF00);//黄色
				break;
			case 3:
				c=new Color(0xFF0000);//红
				break;
			case 4:
				c=new Color(0xFF0000);//红
				break;

			default:
				c=new Color(0x00FF00);//绿
				break;
			}
    		switch (fontType) {
    		case "1":
    			fontType="黑体";
    			break;
    		case "2":
    			fontType="楷体";
    			break;
    		case "3":
    			fontType="宋体";
    			break;
    		case "4":
    			fontType="仿宋";
    			break;
    		case "5":
    			fontType="隶书";
    			break;
    			
    		default:
    			fontType="宋体";
    			break;
    		}
    		switch (fontStyle) {
    		case 0:
    			fStyle=Font.PLAIN;
    			break;
    		case 1:
    			fStyle=Font.BOLD;
    			break;
    		case 2:
    			fStyle=Font.ITALIC;
    			break;
    		case 4:
    			fStyle=Font.PLAIN;
    			break;
    		case 8:
    			fStyle=Font.PLAIN;
    			break;
    		default:
    			fStyle=Font.PLAIN;
    			break;
    		}
    		
    		
    		int rowNum=imageWidth/fontSize;
    		if(content.length()>rowNum){//换行
    			String [] arrContent=slipRow(content, rowNum);
    			
    			for (String string : arrContent) {
    				System.out.println("string的长度"+string.length());
    				Graphics2D gd=image.createGraphics();
    				gd.setColor(c);
    				Font font = new Font(fontType, fStyle, fontSize);
    				gd.setFont(font);
    				if(fontStyle==4||fontStyle==8){
    					Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
    					if(fontStyle==4){
    						map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
    					}else{
    						map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
    					}
    					font = font.deriveFont( map );
    				}
    				FontMetrics fm = gd.getFontMetrics(font);
					int textWidth = fm.stringWidth(string);
					gd.drawString(string, (imageWidth-textWidth)/2, fontSize*(j++));
    				
    			}
    			
    		}else{
    			Graphics2D gd=image.createGraphics();
    			gd.setColor(c);
    			Font font = new Font(fontType, fStyle, fontSize);
    			gd.setFont(font);
    			if(fontStyle==4||fontStyle==8){
    				Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
    				if(fontStyle==4){
    					map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
    				}else{
    					map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
    				}
    				font = font.deriveFont( map );
    			}
    			FontMetrics fm = gd.getFontMetrics(font);
				int textWidth = fm.stringWidth(content);
				
				int textHight=fm.getHeight();
	    		int stringAscent = fm.getAscent(); 
	    		int stringDescent = fm.getDescent (); 
				
				gd.drawString(content, (imageWidth-textWidth)/2, (imageHeight+stringAscent-stringDescent)/2);
    			gd.dispose();
    		}
    		
    	}
    	
    	createImage(savePath,saveName);
    	
    }
    
    /**
     * 生成图片
     * @param list
     * @param screenType
     * @param savePath
     * @param saveName
     * @throws UnsupportedEncodingException 
     */
    public void graphicsGenerationTwelve(List<JSONObject> list,String screenType,String savePath,String saveName,int color){
    	
    	
    	String [] hw=screenType.split("\\*");
    	Integer imageWidth=Integer.parseInt(hw[0]);
    	Integer imageHeight=Integer.parseInt(hw[1]);
    	
    	image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    	//设置图片的背景色
    	Graphics2D main = image.createGraphics();
//    	main.setColor(new Color(0, 0, 0));//背景颜色
    	main.setColor(new Color(0x000000));//背景颜色
    	main.fillRect(0, 0, imageWidth, imageHeight);
    	main.dispose();
    	int j=1;
    	for (int i = 0; i < list.size(); i++) {
    		String content=list.get(i).getString("content");
    		Integer fontStyle=list.get(i).getInteger("fontStyle");
    		String fontType=list.get(i).getString("fontType");
//			int fontSize=Integer.parseInt(list.get(i).getString("fontSize").substring(0, 2));
    		String tempSize=list.get(i).getString("fontSize");
    		int fontSize=16;
    		if(tempSize.length()==3){
    			
    			fontSize=Integer.parseInt(tempSize.substring(0, 1));
    		}else{
    			fontSize=Integer.parseInt(tempSize.substring(0, 2));
    		}
    		Integer fStyle=0;
    		Color c=new Color(0xFF0000);
    		switch (color) {
    		case 1:
    			
    			c=new Color(0x00FF00);//绿
    			break;
    		case 2:
    			c=new Color(0xFFFF00);//黄色
    			break;
    		case 3:
    			c=new Color(0xFF0000);//红
    			break;
    		case 4:
    			c=new Color(0xFF0000);//红
    			break;
    			
    		default:
    			c=new Color(0x00FF00);//绿
    			break;
    		}
    		switch (fontType) {
    		case "1":
    			fontType="黑体";
    			break;
    		case "2":
    			fontType="楷体";
    			break;
    		case "3":
    			fontType="宋体";
    			break;
    		case "4":
    			fontType="仿宋";
    			break;
    		case "5":
    			fontType="隶书";
    			break;
    			
    		default:
    			fontType="宋体";
    			break;
    		}
    		switch (fontStyle) {
    		case 0:
    			fStyle=Font.PLAIN;
    			break;
    		case 1:
    			fStyle=Font.BOLD;
    			break;
    		case 2:
    			fStyle=Font.ITALIC;
    			break;
    		case 4:
    			fStyle=Font.PLAIN;
    			break;
    		case 8:
    			fStyle=Font.PLAIN;
    			break;
    		default:
    			fStyle=Font.PLAIN;
    			break;
    		}
    		
    		
    		int rowNum=imageWidth/fontSize;
    		if(content.length()>rowNum){//换行
    			String [] arrContent=slipRow(content, rowNum);
    			
    			for (String string : arrContent) {
    				System.out.println("string的长度"+string.length());
    				Graphics2D gd=image.createGraphics();
    				gd.setColor(c);
    				Font font = new Font(fontType, fStyle, fontSize);
    				gd.setFont(font);
    				if(fontStyle==4||fontStyle==8){
    					Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
    					if(fontStyle==4){
    						map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
    					}else{
    						map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
    					}
    					font = font.deriveFont( map );
    				}
    				FontMetrics fm = gd.getFontMetrics(font);
    				int textHight=fm.getHeight();
    	    		int h=(imageHeight-arrContent.length*textHight)/2;
//    	    		
//    	    		int textWidth = fm.stringWidth(string);
//    	    		int textHight=fm.getHeight();
//    	    		int stringAscent = fm.getAscent(); 
//    	    		int stringDescent = fm.getDescent (); 
    	    		
    				int textWidth = fm.stringWidth(string);
//    				gd.drawString(string, (imageWidth-textWidth)/2, (imageHeight+stringAscent-stringDescent)/2);
    				gd.drawString(string, (imageWidth-textWidth)/2, fontSize*(j++)+h);
    				
    			}
    			
    		}else{
    			Graphics2D gd=image.createGraphics();
    			gd.setColor(c);
    			Font font = new Font(fontType, fStyle, fontSize);
    			gd.setFont(font);
    			if(fontStyle==4||fontStyle==8){
    				Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
    				if(fontStyle==4){
    					map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);//下划线
    				}else{
    					map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);//中划线
    				}
    				font = font.deriveFont( map );
    			}
    			FontMetrics fm = gd.getFontMetrics(font);
    			int textWidth = fm.stringWidth(content);
    			
    			
//    			int textWidth = fm.stringWidth(content);
	    		int textHight=fm.getHeight();
	    		int stringAscent = fm.getAscent(); 
	    		int stringDescent = fm.getDescent (); 
    			
    			
//    			int textHight=fm.getHeight();
//	    		int h=(imageHeight-2*textHight)/2;
    			gd.drawString(content, (imageWidth-textWidth)/2, (imageHeight+stringAscent-stringDescent)/2);
    			gd.dispose();
    		}
    		
    	}
    	
    	createImage(savePath,saveName);
    	
    }
    
    
    
    
    public final BufferedImage getSharperPicture(BufferedImage originalPic){  
        int imageWidth = originalPic.getWidth();  
        int imageHeight = originalPic.getHeight();  
  
        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,  
                BufferedImage.TYPE_3BYTE_BGR);  
        float[] data =  
        { -1.0f, -1.0f, -1.0f, -1.0f, 10.0f, -1.0f, -1.0f, -1.0f, -1.0f };  
  
        Kernel kernel = new Kernel(3, 3, data);  
        ConvolveOp co = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
        co.filter(originalPic, newPic);  
        return newPic;  
    }  
 
    /**
     * 分行
     */
    public String [] slipRow(String str,int num){
    		StringBuffer s1=new StringBuffer(str);

    		int index;

    		for(index=num;index<s1.length();index+=num+1){

    		s1.insert(index, ',');

    		}

    		System.out.println(s1);
    		return s1.toString().split(",");

    }
    
    public static void main(String[] args) {
    	
//    	ChartGraphics graphics=new ChartGraphics();
//    	graphics.slipRow("畅通时间颜色为绿色缓行时为黄色拥堵时为红色",3);
    	
    	JSONObject json=new JSONObject();
		json.put("content", "距象山40公里 20分钟");
		json.put("minute", 9);
		json.put("color", 2);
		json.put("fontStyle",0);
		json.put("fontType",3);
		json.put("fontSize", "1010");
		JSONObject json2=new JSONObject();
		json2.put("content", "象山北");
		json2.put("minute", 45);
		json2.put("color", 4);
		json2.put("fontStyle",0);
		json2.put("fontType",3);
		json2.put("fontSize", "1010");
//		JSONObject json3=new JSONObject();
//		json3.put("content", "象山");
//		json3.put("minute", 155);
//		json3.put("color", 1);
//		json3.put("fontStyle",0);
//		json3.put("fontType",3);
//		json3.put("fontSize", "9090");
		List<JSONObject> list=new ArrayList<JSONObject>();
		list.add(json);
//		list.add(json2);
//		list.add(json3);
		ChartGraphics graphics=new ChartGraphics();
//		graphics.graphicsGenerationFour(list, "64*32","e:\\123","hehe.png");
		graphics.graphicsGenerationOneAndTwo(list, "64*32","e:\\123","hehe.png",4);
//		ChartGraphics graphics=new ChartGraphics();
//		graphics.graphicsGeneration(list, "640*320","e:\\123","hehe.jpg");
////		graphics.graphicsGeneration(list, "640*320","e:\\data\\traffic\\img","0x2122D24026_64_32_7.png");
//		graphics.graphicsGeneration(list, "640*320","e:\\data\\traffic\\img\\7\\201709\\22\\174230\\img","0x2122D24026_64_32_7.png");
//		//f:\data\traffic\img\7\201709\22\174230\0x2122D24026_64_32_7.png
    }
}