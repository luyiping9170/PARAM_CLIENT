package com.dt.bean;

import java.io.Serializable;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String broadCastFlag;
	private Pack  pack = new Pack();
	
	public Message(String broadCastFlag){
		this.broadCastFlag = broadCastFlag;
	}
	
	public Pack getPack(){
		return pack;
	}
	
	public void setPack(Pack pack){
		this.pack = pack;
	}
}
