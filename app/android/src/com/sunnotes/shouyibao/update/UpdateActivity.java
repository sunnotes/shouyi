package com.sunnotes.shouyibao.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sunnotes.shouyibao.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class UpdateActivity extends Activity {
	private static final String TAG = "UpdateActivity";
	public ProgressDialog pBar;
	private Handler handler = new Handler();

	private boolean done = false;
	private int newVerCode = 0;
	private String newVerName = "";
	private String newApkName = "";

	// 进度条
	private ProgressDialog verDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		/* 显示App icon左侧的back键 */
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		pBar = new ProgressDialog(this);
		verDialog = new ProgressDialog(this);
		try {
			if (new CheckVerTask().execute().get()) {
				int vercode = Config.getVerCode(this);
				if (newVerCode > vercode) {
					doNewVersionUpdate();
				} else {
					notNewVersionShow();
				}
			}
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage());
		} catch (ExecutionException e) {
			Log.e(TAG, e.getMessage());
		}

	}

	private void notNewVersionShow() {
		String verName = Config.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(",\n已是最新版,无需更新!");
		Dialog dialog = new AlertDialog.Builder(UpdateActivity.this)
				.setTitle("软件更新").setMessage(sb.toString())// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void doNewVersionUpdate() {
		String verName = Config.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(", 发现新版本:");
		sb.append(newVerName);
		sb.append(", 是否更新?");
		Dialog dialog = new AlertDialog.Builder(UpdateActivity.this)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// pBar = new
								// ProgressDialog(UpdateActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(Config.UPDATE_SERVER + newApkName);
							}

						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(
								Environment.getExternalStorageDirectory(),
								newApkName);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					Log.e(TAG, e.getMessage());
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}

		}.start();

	}

	private void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	private void update() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), newApkName)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
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

	public class CheckVerTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String verjson = NetworkTool.getContent(Config.UPDATE_SERVER
						+ Config.UPDATE_VER_PHP);

				JSONTokener jsonParser = new JSONTokener(verjson);
				// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
				// 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
				JSONObject obj = (JSONObject) jsonParser.nextValue();
				// 接下来的就是JSON对象的操作了
				newVerCode = Integer.parseInt(obj.getString("verCode"));
				newVerName = obj.getString("verName");
				newApkName = obj.getString("apkname");
			} catch (Exception e) {
				newVerCode = -1;
				newVerName = "";
				newApkName = "";
				Log.e(TAG, e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			verDialog.setTitle("提示信息");
			verDialog.setMessage("正在检查最新版本，请稍候...");
			verDialog.setCancelable(false);
			verDialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			verDialog.dismiss();
			super.onPostExecute(result);

		}
	}

}