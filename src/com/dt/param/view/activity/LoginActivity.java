package com.dt.param.view.activity;

import com.dt.param.R;

import android.os.Bundle;
import android.os.Message;
import android.widget.Button;

public class LoginActivity extends ParamView{
	
	private Button login;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_activity);
		
		login = (Button) this.findViewById(R.id.login_activity_login_button);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
