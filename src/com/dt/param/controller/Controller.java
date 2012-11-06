package com.dt.param.controller;

import android.os.Handler;

import com.dt.bean.Message;

public class Controller {

	private static final String SERVER_IP   = "127.0.0.1";
	private static final int    SERVER_PORT = 9192;
	
	public Controller(){
		init();
	}
	
	public void init(){
		
	}
	
	/**
	 * 初始化配置项
	 */
	public void loadConfig(){
		
	}
	
	/**
	 * 启动后台服务
	 */
	public void startServices(){
		
	}
	
	/**
	 * 发送消息的对外接口，所有发往服务器的消息都由该接口发出，该方法开启一个线程处理该消息并且将反馈消息
	 * 发还给消息注册的handler，由handler负责处理反馈消息。
	 * @param msg
	 * @param handler
	 */
	public void sendMessage(Message msg, Handler handler){
		new Thread(new MsgSender(msg,handler)).start();
	}
	
	private class MsgSender implements Runnable{

		private Message msg;
		private Handler handler;
		
		public MsgSender(Message msg, Handler handler){
			this.msg = msg;
			this.handler = handler;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
