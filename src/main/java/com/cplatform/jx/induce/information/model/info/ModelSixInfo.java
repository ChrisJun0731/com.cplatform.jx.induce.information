package com.cplatform.jx.induce.information.model.info;

import java.awt.Color;

import com.alibaba.fastjson.JSON;

public class ModelSixInfo {

	private int ID;
	private int PID;
	private String START_POINT;
	private int UP_LEFT_X;
	private int UP_LEFT_Y;
	private int UNDER_RIGHT_X;
	private int UNDER_RIGHT_Y;
	private String END_POINT;
	private String ROAD_CODE;
	private int TEXT_PX_X;
	private int TEXT_PX_Y;
	private int TEXT_SIZE;
	private int TEXT_COLOR;
	
	private int IMG_COLOR;
	
	public int getIMG_COLOR() {
		return IMG_COLOR;
	}
	public void setIMG_COLOR(int iMG_COLOR) {
		IMG_COLOR = iMG_COLOR;
	}
	private Color color;
	private String text;
	
	public int getID() {
		return ID;
	}
	public int getPID() {
		return PID;
	}
	public String getSTART_POINT() {
		return START_POINT;
	}
	
	public String getEND_POINT() {
		return END_POINT;
	}
	public String getROAD_CODE() {
		return ROAD_CODE;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	public void setPID(int pID) {
		PID = pID;
	}
	public void setSTART_POINT(String sTART_POINT) {
		START_POINT = sTART_POINT;
	}
	
	public void setEND_POINT(String eND_POINT) {
		END_POINT = eND_POINT;
	}
	public void setROAD_CODE(String rOAD_CODE) {
		ROAD_CODE = rOAD_CODE;
	}
	
	public Color getColor() {
		return color;
	}
	public String getText() {
		return text;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	public int getUP_LEFT_X() {
		return UP_LEFT_X;
	}
	public int getUP_LEFT_Y() {
		return UP_LEFT_Y;
	}
	public int getUNDER_RIGHT_X() {
		return UNDER_RIGHT_X;
	}
	public int getUNDER_RIGHT_Y() {
		return UNDER_RIGHT_Y;
	}
	public int getTEXT_PX_X() {
		return TEXT_PX_X;
	}
	public int getTEXT_PX_Y() {
		return TEXT_PX_Y;
	}
	public void setUP_LEFT_X(int uP_LEFT_X) {
		UP_LEFT_X = uP_LEFT_X;
	}
	public void setUP_LEFT_Y(int uP_LEFT_Y) {
		UP_LEFT_Y = uP_LEFT_Y;
	}
	public void setUNDER_RIGHT_X(int uNDER_RIGHT_X) {
		UNDER_RIGHT_X = uNDER_RIGHT_X;
	}
	public void setUNDER_RIGHT_Y(int uNDER_RIGHT_Y) {
		UNDER_RIGHT_Y = uNDER_RIGHT_Y;
	}
	public void setTEXT_PX_X(int tEXT_PX_X) {
		TEXT_PX_X = tEXT_PX_X;
	}
	public void setTEXT_PX_Y(int tEXT_PX_Y) {
		TEXT_PX_Y = tEXT_PX_Y;
	}
	
	public int getTEXT_SIZE() {
		return TEXT_SIZE;
	}
	public void setTEXT_SIZE(int tEXT_SIZE) {
		TEXT_SIZE = tEXT_SIZE;
	}
	
	
	public int getTEXT_COLOR() {
		return TEXT_COLOR;
	}
	public void setTEXT_COLOR(int tEXT_COLOR) {
		TEXT_COLOR = tEXT_COLOR;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	
}
