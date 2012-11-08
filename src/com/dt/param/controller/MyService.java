package com.dt.param.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.dom4j.DocumentException;

import com.dt.bean.Account;
import com.dt.bean.Message;
import com.dt.bean.Pack;
import com.dt.param.R;
import com.dt.param.dao.Dao;
import com.dt.param.util.PackParser;
import com.dt.param.view.activity.MsgCenterActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyService extends Service {

	private NotificationManager nm;
	private Handler handler;
	private int notification_id = 0;
	private String IMEI = ""; // Device IMEI
	private Account account = new Account("","","");
	private static int pid; // package id, increased with every msg sent
	private static Object pid_lock = new Object();
	private static Dao dao;
 
	private static final String SERVER_IP = "10.0.2.2";
	private static final int SERVER_PORT = 9291;

	/** Command flags */
	public static final String COMMAND = "cmd";
	public static final String BUNDLE = "data";
	public static final String MESSAGE = "msg";
	public static final String TOKEN   = "token";
	public static final String UNAME   = "uname";
	public static final String NETWORK = "netword";
	/** Command flags */
	public static final int CMD_DO_NOTHING = 0x0000000;
	public static final int CMD_SEND_MSG = 0x0000001;
	public static final int CMD_SET_ACCOUNT = 0x0000003;

	/** Broadcast string flags */
	public static final String BROADCAST_LOGIN         = "login_activity"; // / Broadcast flag for login action
	public static final String BROADCAST_MSG_CENTER    = "center_activity";
	public static final String BROADCAST_CONVERSATION  = "convers_activity";
	public static final String BROADCAST_NETWORD_STATE = "network_state";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);	
		IMEI = tm.getDeviceId();
		account.setIMEI(IMEI);
		dao = Dao.instance(this);
	}

	@Override
	public void onStart(Intent intent, int startID) {
		super.onStart(intent, startID);
		handleCommand(intent);
		Log.d("onstart", "service started");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dao.finish();
	}

	@Override
	public int onStartCommand(Intent intent, int arg0, int arg1) {
		handleCommand(intent);
		Log.d("onstart", "intent received in onstartcommand");
		return START_STICKY;
	}

	private void handleCommand(Intent intent) {
		switch (intent.getIntExtra(COMMAND, 0)) {
		case CMD_SEND_MSG: {
			Log.d("Service", "send msg request");
			Bundle data = intent.getExtras();
			Message msg = (Message) data.get(MESSAGE);
			new Thread(new MessageSender(msg)).start(); // Start a new thread to send message to server
			break;
		}
		case CMD_SET_ACCOUNT: {
			/// Complete account information
			account.setToken(intent.getStringExtra(TOKEN));
			account.setAccountName(intent.getStringExtra(UNAME));
			break;
		}
		}
	}

	/**
	 * send a new notification to the system status bar 
	 * @param icon
	 * @param tickerText
	 * @param when
	 * @param intentFlags
	 * @param title
	 * @param msg
	 * @param target
	 */
	private void newNotification(int icon, CharSequence tickerText, long when,
			int intentFlags, CharSequence title, CharSequence msg, Class target) {
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // �Զ���ֹ
		notification.defaults |= Notification.DEFAULT_SOUND; // Ĭ������
		Intent intent = new Intent(this, target);
		intent.setFlags(intentFlags);
		PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, title, msg, pending);
		nm.notify(notification_id++, notification);
	}

	/**
	 * A runnable responsible for sending message
	 * @author HP
	 * 
	 */
	private class MessageSender implements Runnable {

		private Message msg;

		public MessageSender(Message msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			if(isConnectedToInternet()){
				try {
					/** Fill the package with necessary info before sending */
					msg.getPack().setAccount(account);
					synchronized(pid_lock){
						msg.getPack().setPID(pid++);
					}
					//TODO Connect to server and send package
					Socket socket = new Socket(SERVER_IP,SERVER_PORT);
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					writer.write(msg.getPack().toString()+"\n");
					writer.write("EOF\n"); //Add EOF to the end of the stream
					writer.flush();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line = "";
					String pack = "";
					while((line = reader.readLine())!=null&&!line.equals("EOF"))
						pack += line;
					///Parse the pack sent back and broadcast it as
					///a message to the activity receiving the specific
					///broadcast flag specified in the message sent
					Message message = new Message(msg.broadCastFlag);
					message.setPack(PackParser.parseXML(pack));
					Bundle bundle = new Bundle();
					bundle.putSerializable(MESSAGE, message);
					Intent intent = new Intent(msg.broadCastFlag);
					intent.putExtras(bundle);
					MyService.this.sendBroadcast(intent);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				// Network is unavailable, send a broadcast to related activities
				Intent intent = new Intent(MyService.BROADCAST_NETWORD_STATE);
				intent.putExtra(MyService.NETWORK, "network unavailable");
				MyService.this.sendBroadcast(intent);
				Log.d("Service", "network unavailable");
			}
		}
	}

	/**
	 * Check internet connectivity
	 * @return
	 */
	public boolean isConnectedToInternet() {

		ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
		}
		return false;
	}

}