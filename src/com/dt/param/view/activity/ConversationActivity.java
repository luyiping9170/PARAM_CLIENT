package com.dt.param.view.activity;

import com.dt.param.R;

import android.os.Bundle;
import android.os.Message;

public class ConversationActivity extends ParamView{

	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_activity);
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
