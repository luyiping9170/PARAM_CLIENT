package com.dt.param.view.activity;

import com.dt.bean.Message;
import com.dt.param.controller.Controller;
import com.dt.param.controller.MyService;
import com.dt.param.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.widget.Toast;

public abstract class ParamView extends Activity implements Callback{
	protected static Controller controller;
	protected static Dao dao;
	protected Handler handler;
	
	protected static final String INTENT_FROM    = "from";
	protected static final String FROM_LOGIN     = "from_login";
	protected static final String FROM_SERVICE   = "from_service";
	protected static final String INTENT_ACCOUNT = "account";
	
	protected String username = "";
	protected String password = "";
	
	protected Intent newIntent(Context context, Class c,int command , Message message){
		Intent intent = new Intent(context,c);
		Bundle bundle = new Bundle();
		intent.putExtra(MyService.COMMAND, command);
		bundle.putSerializable(MyService.MESSAGE, message);
		intent.putExtras(bundle);
		return intent;
	}
	
	protected void showToast(Context context,String text,int duration){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	protected void showToast(Context context, int resid, int duration){
		Toast.makeText(context, resid, Toast.LENGTH_LONG).show();
	}
}
