package com.sunnotes.shouyibao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.sunnotes.shouyibao.util.JsonUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends Activity {
	private static final String TAG = "FeedbackActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		/* 显示App icon左侧的back键 */
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// button
		Button button = (Button) findViewById(R.id.feedback_send);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// text
				EditText editText = (EditText) findViewById(R.id.feedback);
				String feedback = editText.getText().toString();
				
				if (feedback == "") {
					
				} else {
					/*
					 * 转码解决中文乱码的问题
					 * 2014-03-30
					 */	
					try {
						feedback = URLEncoder.encode(feedback, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, e.getMessage());
					}
					new FeedbackTask().execute(feedback);
					dialog();
				}
			}
		});
	}

	private void dialog() {
		AlertDialog.Builder builder = new Builder(FeedbackActivity.this);
		builder.setMessage("已收到，感谢您的反馈。");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				FeedbackActivity.this.finish();
			}
		});
		builder.create().show();
	}

//	private boolean submit(String text) {
//		final TelephonyManager tm = (TelephonyManager) getBaseContext()
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		final String tmDevice = "" + tm.getDeviceId();
//		String url = Constants.URL_FEEDBACK + "?deviceid=" + tmDevice
//				+ "?text=" + text;
//		JsonUtils.downloadUrl(url);
//		return true;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.common, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public class FeedbackTask extends AsyncTask<String,Integer, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			String text = params[0];
			final TelephonyManager tm = (TelephonyManager) getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			final String tmDevice = "" + tm.getDeviceId();
			String url = Constants.URL_FEEDBACK + "?deviceid=" + tmDevice
					+ "&text=" + text;
			JsonUtils.downloadUrl(url);
			return null;
		}
		
	}
}
