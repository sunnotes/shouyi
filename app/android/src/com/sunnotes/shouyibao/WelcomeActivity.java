package com.sunnotes.shouyibao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.sunnotes.shouyibao.service.SyncService;
import com.sunnotes.shouyibao.util.DBImporter;

public class WelcomeActivity extends Activity {

	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟5秒
	private static final long SPLASH_DELAY_MILLIS = Constants.SPLASH_DELAY_MILLIS;
	private static final String SHAREDPREFERENCES_NAME = Constants.SHAREDPREFERENCES_NAME;

	// Log
	private static final String TAG = "WelcomeActivity";
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		Log.i(TAG, "welcome init====");
		Intent service = new Intent(this, SyncService.class);
		startService(service);
		init();
	}

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，几秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);

		} else {
			//mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
			//copy table
			DBImporter importer = new DBImporter(this);
	        importer.copyDB();
			// 赋值
			Editor editor = preferences.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();
		}
	}

	private void goHome() {
		Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}
}
