package com.dt.bean;

import java.io.Serializable;

public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String token;
	private String imei;
	
	public Account(String name,String token,String imei){
		this.name = name;
		this.token = token;
		this.imei = imei;
	}
	
	public String getAccountName(){
		return this.name;
	}
	
	public void setAccountName(String name){
		this.name = name;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public String getIMEI(){
		return this.imei;
	}
	
	public void setIMEI(String imei){
		this.imei = imei;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	@Override
	public String toString(){
		String string = "<account>";
		string += "<name>" + name + "</name><imei>" + imei + "</imei>";
		if(token!=null)
			string += "<token>" + token + "</token>";
		string += "</account>";
		
		return string;
	}

}
