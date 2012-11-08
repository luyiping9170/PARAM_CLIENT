package com.dt.param.view.activity;

import com.dt.param.R;
import com.dt.param.R.id;
import com.dt.param.R.layout;
import com.dt.param.controller.Controller;
import com.dt.param.controller.MyService;
import com.dt.param.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MsgCenterActivity extends ParamView implements OnKeyListener{
	
	private LinearLayout msg_center_container;
	private LinearLayout on_offline_info;
	private LinearLayout number_list;
	
	private TextView numbers;
	
	private EditText search_txt;
	
	private Button number_selector;
	private Button on_offline_info_closer;
	private Button config_button;
	private ImageButton new_msg;
	
	private ListView msg_list;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_center);
		
		msg_center_container   = (LinearLayout) this.findViewById(R.id.msg_center_container);
		on_offline_info 	   = (LinearLayout) this.findViewById(R.id.msg_center_on_offline_info);
		number_list     	   = (LinearLayout) this.findViewById(R.id.msg_center_number_list);
		
		number_selector 	   = (Button) this.findViewById(R.id.msg_center_number_selector);
		on_offline_info_closer = (Button) this.findViewById(R.id.msg_center_on_offline_closer);
		config_button	       = (Button) this.findViewById(R.id.msg_center_config);
		new_msg				   = (ImageButton) this.findViewById(R.id.msg_center_send_msg);
		
		search_txt 			   = (EditText) this.findViewById(R.id.msg_center_search_txt);
		search_txt.clearFocus();
		
		msg_list 			   = (ListView) this.findViewById(R.id.msg_center_msg_list);
		msg_list.setCacheColorHint(Color.TRANSPARENT);
		msg_list.requestFocus();
		
		//initInfrastructure();
		initListener();
		
		/// Init controller and dao
		initInfrastructure();
		
		/// download and init configuration
		
		///Init data
		
	}
	
	/**
	 * 
	 */
	private void initInfrastructure(){
		controller = new Controller();
		dao		   = Dao.instance(this);
	}

	private void initListener(){
		
		number_selector.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("click", "number selector");
				if(number_list.isShown())
					number_list.setVisibility(View.INVISIBLE);
				else
					number_list.setVisibility(View.VISIBLE);
			}
			
		});
		
		on_offline_info_closer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(on_offline_info!=null){
					msg_center_container.removeView(on_offline_info);
				}
			}
			
		});
		
		new_msg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MsgCenterActivity.this,ContactChooseActivity.class);
				MsgCenterActivity.this.startActivity(intent);
			}
			
		});
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		this.stopService(new Intent(this,MyService.class));
	}

	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ���غ��˼����Ϊ��֤Ӧ�ú�̨���С�
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:{
			break;
		}
		}
		return false;
	}
	
}
