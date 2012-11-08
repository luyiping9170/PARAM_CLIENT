package com.dt.param.view.activity;

import java.util.Date;

import com.dt.bean.Message;
import com.dt.bean.Multipart;
import com.dt.bean.Pack;
import com.dt.param.R;
import com.dt.param.controller.MyService;
import com.dt.param.dao.Dao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends ParamView implements KeyListener{
	
	private Button login;
	
	private BroadcastReceiver loginReceiver;
	private NetworkReceiver   netReceiver;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_activity);
		
		login = (Button) this.findViewById(R.id.login_activity_login_button);
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = ""; //TODO get username from edittext
				password = ""; //TODE get password from edittext
				login(username,password);
			}
			
		});
		
		//Register intent filter
		IntentFilter loginFilter = new IntentFilter(MyService.BROADCAST_LOGIN);
		loginReceiver = new LoginReceiver();
		this.registerReceiver(loginReceiver, loginFilter);
		
		IntentFilter netFilter = new IntentFilter(MyService.BROADCAST_NETWORD_STATE);
		netReceiver = new NetworkReceiver();
		this.registerReceiver(netReceiver, netFilter);
		
		//Start the background service
		Intent serviceIntent = new Intent(LoginActivity.this,MyService.class);
		this.startService(serviceIntent);
		
		dao = Dao.instance(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		this.unregisterReceiver(loginReceiver);
		this.unregisterReceiver(netReceiver);
	}
	
	/**
	 * Send a Login message to the background service by calling start service from the context
	 * @param username
	 * @param password
	 * @return
	 */
	private void login(String username, String password){
		Message message = new Message(MyService.BROADCAST_LOGIN);
		message.setPack(buildLoginPack(username,password));
		startService(newIntent(this,MyService.class,MyService.CMD_SEND_MSG, message));
	}
	
	private class LoginReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			Message msg   = (Message) bundle.get(MyService.MESSAGE);
			Multipart mp  = msg.getPack().getData().get(0);
			switch(mp.getType()){
			case Multipart.TYPE_TEXT:{
				// Data received is token
				Intent dIntent = new Intent(LoginActivity.this, MyService.class);
				dIntent.putExtra(MyService.UNAME, username);
				dIntent.putExtra(MyService.TOKEN, mp.getData());
				dIntent.putExtra(MyService.COMMAND, MyService.CMD_SET_ACCOUNT);
				startService(dIntent);// Send account info to background service
				
				Intent sIntent = new Intent(LoginActivity.this, MsgCenterActivity.class);
				sIntent.putExtra(INTENT_FROM, FROM_LOGIN);
				sIntent.putExtra(INTENT_ACCOUNT, username);
				startActivity(sIntent);
				LoginActivity.this.finish();
				break;
			}
			case Multipart.TYPE_ERROR:{
				// Data received is error code
				showToast(LoginActivity.this, "error:" + mp.getData(), Toast.LENGTH_LONG);
				break;
			}
			}
		}
	}

	private class NetworkReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String status = intent.getStringExtra(MyService.NETWORK);
			showToast(LoginActivity.this,"network unavailable",Toast.LENGTH_LONG);
		}
		
	}
	
	@Override
	public boolean handleMessage(android.os.Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Compose a package containing encoded username and password
	 * information. The username and password stings are encoded using
	 * BASE64. 
	 * @param username
	 * @param password
	 * @return
	 */
	private Pack buildLoginPack(String username,String password){
		Pack pack = new Pack();
		pack.setPkgType(Pack.HANDLER_LOGIN);
		Date time = new Date();
		Multipart uname = new Multipart(Multipart.TYPE_TEXT,Multipart.ENCODING_BASE64,username,time);
		Multipart paswd = new Multipart(Multipart.TYPE_PASSWD,Multipart.ENCODING_BASE64,password,time);
		pack.addData(uname);
		pack.addData(paswd);
		return pack;
	}

	@Override
	public void clearMetaKeyState(View arg0, Editable arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInputType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onKeyDown(View arg0, Editable arg1, int arg2, KeyEvent arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyOther(View view, Editable text, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			LoginActivity.this.stopService(new Intent(LoginActivity.this,MyService.class));
			LoginActivity.this.finish();
			System.exit(0);
			return true;
		}
		
		return false;
	}
	
	
	
}
