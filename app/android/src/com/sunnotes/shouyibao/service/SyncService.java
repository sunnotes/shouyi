package com.sunnotes.shouyibao.service;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sunnotes.shouyibao.Constants;
import com.sunnotes.shouyibao.HomeActivity;
import com.sunnotes.shouyibao.R;
import com.sunnotes.shouyibao.dao.FundValueDao;
import com.sunnotes.shouyibao.model.FundValue;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service {

	// 上次更新时间
	private long lastUpdateTime;
	// long period
	private long period = Constants.SYNC_PERIOD;
	// share
	private static final String SHAREDPREFERENCES_NAME = Constants.SHAREDPREFERENCES_NAME;
	// Log
	private static final String TAG = "SyncService";
	// DAO
	private FundValueDao fundValueDao = new FundValueDao(this);
	private Context context;
	Timer timer;

	public SyncService() {

	}

	@Override
	public void onCreate() {
		Log.i(TAG, "==service start");
		this.context = getApplicationContext();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// updateWeather();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 记录刷新时间
				// 一周前
				long weekBefore = Calendar.getInstance().getTimeInMillis()
						/ 1000 - 7 * 24 * 60 * 60;
				// 当前时间
				long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
				// 读取SharedPreferences中需要的数据
				SharedPreferences preferences = getSharedPreferences(
						SHAREDPREFERENCES_NAME, MODE_PRIVATE);
				lastUpdateTime = preferences.getLong("lastUpdateTime",
						weekBefore);				
				 //添加基金收益信息				 
				fundValueDao = new FundValueDao(context);
				List<FundValue> values = fundValueDao
						.getAllFromNet(lastUpdateTime);
				if (values != null) {
					// 更新收益历史
					fundValueDao.saveFundValuesList(values);
					// 更新基金信息
					fundValueDao.UpdateFundInfo();
					// 是否需要产生通知
					notice(values);
					// 实例化Editor对象
					Editor editor = preferences.edit();
					// 存入数据
					editor.putBoolean("isFirstUse", false);
					editor.putLong("lastUpdateTime", currentTime);
					// 提交修改
					editor.commit();
				} 
				Log.d(TAG, "time==" + System.currentTimeMillis());
			}
		}, 0, period);
		return super.onStartCommand(intent, flags, startId);
	}

	
	

	@Override
	public void onDestroy() {
		Log.i(TAG, "==service destory");
		super.onDestroy();
	}

	/**
	 * 判断是否发通知
	 * 
	 * @param updates
	 */
	private void notice(List<FundValue> updates) {
		List<FundValue> selected = fundValueDao.getMyFundValues();
		if (selected == null) {
			return;
		} else {
			for (FundValue select : selected) {
				for (FundValue item : updates) {
					if (select.getCode().equals(item.getCode())) {
						// prepare intent which is triggered if the
						// notification is selected
						Intent intent = new Intent(this, HomeActivity.class);
						PendingIntent pi = PendingIntent.getActivity(this, 0,
								intent, 0);
						// 创建一个Notification
						Notification.Builder builder = new Notification.Builder(
								context)
								.setContentTitle(" "+item.getName()+" 收益更新")
								.setContentText(
										" "+ select.getDate() + "  万份收益："
												+ select.getProfit()+"元 七日年化："+select.getRate()+"%")
								.setSmallIcon(
										R.drawable.notification_small_icon)
								.setContentIntent(pi).setAutoCancel(true);
						Notification notification = builder.getNotification();
						//DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
				        //DEFAULT_LIGHTS  使用默认闪光提示
				        //DEFAULT_SOUNDS  使用默认提示声音
				        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
				        notification.defaults = Notification.DEFAULT_ALL; 
						// 显示多条Notification
						NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						nm.notify(0, notification);
						Log.i(TAG, "==new notice");
						return;
					}
				}
			}
		}
	}
}
