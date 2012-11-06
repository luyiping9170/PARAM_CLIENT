package com.dt.param.view.activity;

import com.dt.param.controller.Controller;
import com.dt.param.dao.Dao;

import android.app.Activity;
import android.os.Handler;
import android.os.Handler.Callback;

public abstract class ParamView extends Activity implements Callback{
	protected static Controller controller;
	protected static Dao dao;
	protected Handler handler;
	
	public ParamView(){
		controller = new Controller();
		dao = new Dao(this);
	}
}
