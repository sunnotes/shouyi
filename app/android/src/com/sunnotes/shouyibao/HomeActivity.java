package com.sunnotes.shouyibao;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunnotes.shouyibao.adapter.FundIncomeAdapter;
import com.sunnotes.shouyibao.adapter.FundInfoAdapter;
import com.sunnotes.shouyibao.dao.FundIncomeDao;
import com.sunnotes.shouyibao.dao.FundValueDao;
import com.sunnotes.shouyibao.dao.MyFundDao;
import com.sunnotes.shouyibao.model.FundIncome;
import com.sunnotes.shouyibao.model.FundValue;
import com.sunnotes.shouyibao.update.UpdateActivity;
import com.sunnotes.shouyibao.view.MyScrollLayout;

public class HomeActivity extends Activity implements OnViewChangeListener,
		OnClickListener {

	// 上次更新时间
	private long lastUpdateTime;
	// 进度条
	private ProgressDialog dialog;
	private MyScrollLayout mScrollLayout;
	private LinearLayout[] mImageViews;
	private int mViewCount;
	private int mCurSel;
	private TextView zixuan, shouyi, quanbu;
	// 自选
	private FundInfoAdapter myFundAdapter;
	// 全部
	private FundInfoAdapter allFundAdapter;
	// 收益
	private FundIncomeAdapter fundIncomeAdapter;
	// DAO
	private FundValueDao fundValueDao = new FundValueDao(this);
	private FundIncomeDao fundIncomeDao = new FundIncomeDao(this);
	// ItemList
	private List<FundValue> myFunds, allFunds;
	private List<FundIncome> incomes;

	private ListView my_fund_list, all_fund_list, fund_income_list;

	private static final String SHAREDPREFERENCES_NAME = Constants.SHAREDPREFERENCES_NAME;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		dialog = new ProgressDialog(this);
		init();
	}

	private void init() {
		zixuan = (TextView) findViewById(R.id.zixuan);
		shouyi = (TextView) findViewById(R.id.shouyi);
		quanbu = (TextView) findViewById(R.id.quanbu);

		my_fund_list = (ListView) this.findViewById(R.id.my_fund_list);
		all_fund_list = (ListView) this.findViewById(R.id.all_fund_list);
		fund_income_list = (ListView) this.findViewById(R.id.fund_income_list);

		// 自选
		myFunds = fundValueDao.getMyFundValues();
		myFundAdapter = new FundInfoAdapter(this, myFunds);
		my_fund_list.setAdapter(myFundAdapter);
		my_fund_list.setCacheColorHint(0);
		// 注册上下文菜单
		this.registerForContextMenu(my_fund_list);
		my_fund_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView fundcodeTextView = (TextView) view
								.findViewById(R.id.code);
						String fundcode = fundcodeTextView.getText().toString();
						Intent intent = new Intent(HomeActivity.this,
								FundHistoryActivity.class);
						intent.putExtra("fundcode", fundcode);
						startActivity(intent);
					}
				});
		// 收益
		incomes = fundIncomeDao.getMyFundIncomes();
		fundIncomeAdapter = new FundIncomeAdapter(this, incomes);
		fund_income_list.setAdapter(fundIncomeAdapter);
		fund_income_list.setCacheColorHint(0);

		// 全部
		allFunds = fundValueDao.getAllFundValues();
		allFundAdapter = new FundInfoAdapter(this, allFunds);
		all_fund_list.setAdapter(allFundAdapter);
		all_fund_list.setCacheColorHint(0);
		// 注册上下文菜单
		this.registerForContextMenu(all_fund_list);
		all_fund_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView fundcodeTextView = (TextView) view
								.findViewById(R.id.code);
						String fundcode = fundcodeTextView.getText().toString();
						Intent intent = new Intent(HomeActivity.this,
								FundDetailActivity.class);
						intent.putExtra("fundcode", fundcode);
						startActivity(intent);
					}
				});

		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lllayout);

		mViewCount = mScrollLayout.getChildCount();
		mImageViews = new LinearLayout[mViewCount];
		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (LinearLayout) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.actionbar_refresh:
			new UpdateTask().execute();
			return true;
//		case R.id.actionbar_share:
//			intent = new Intent(this, HistoryActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
		case R.id.actionbar_history:
			intent = new Intent(this, HistoryActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.actionbar_compare:
			intent = new Intent(this, CompareActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
//		case R.id.actionbar_guide:
//			intent = new Intent(this, GuideActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
//		case R.id.actionbar_drink:
//			intent = new Intent(this, DrinkActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
//		case R.id.actionbar_settings:
//			intent = new Intent(this, GuideActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
		case R.id.actionbar_feedback:
			intent = new Intent(this, FeedbackActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.actionbar_update:
			intent = new Intent(this, UpdateActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * 生成上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// 此方法在每次调用上下文菜单时都会被调用一次
		if (v == all_fund_list) {
			menu.setHeaderTitle("操作"); // 设置操作名字
			// 添加 menu item
			menu.add(0, 1, Menu.NONE, "添加自选");

		} else if (v == my_fund_list) {
			menu.setHeaderTitle("操作"); // 设置操作名字
			// 添加 menu item
			// menu.add(0, 2, Menu.NONE, "置顶");
			menu.add(0, 3, Menu.NONE, "设置金额");
			menu.add(0, 4, Menu.NONE, "删除自选");
		} else {
			menu.setHeaderTitle("操作");
		}

	}

	/*
	 * 响应上下文菜单项
	 */

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo(); // 得到当前被选中的item的信息
		final String code = ((TextView) menuInfo.targetView
				.findViewById(R.id.code)).getText().toString();
		final MyFundDao dao = new MyFundDao(this);
		Log.i("MainActivity", "取当前选中item的id:" + menuInfo.id); // 获取当前选中item的id

		// 进行操作
		switch (item.getItemId()) {
		case 1:
			// 添加自选操作

			dao.save(code);
			Toast.makeText(HomeActivity.this, "恭喜您，您成功将（" + code + "）加入自选！",
					Toast.LENGTH_LONG).show();
			refresh();
			break;
		// case 2:
		// // 置顶操作
		// Toast.makeText(MainActivity.this, "您选中的是置顶操作", Toast.LENGTH_LONG)
		// .show();
		// break;
		case 3:
			// 购买操作
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("请输入金额");
			// Set an EditText view to get user input
			final EditText input = new EditText(this);

			alert.setView(input);
			alert.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							float money = Float.parseFloat(input.getText()
									.toString());
							dao.updateMoney(code, money);
							Toast.makeText(HomeActivity.this,
									"您设置金额为：" + money, Toast.LENGTH_LONG)
									.show();
							refresh();
						}
					});
			alert.show();
			break;
		case 4:
			// 删除操作
			// 添加自选操作
			dao.delete(code);
			Toast.makeText(HomeActivity.this, "您成功删除了" + code,
					Toast.LENGTH_LONG).show();
			refresh();
			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
	}

	/**
	 * 刷新列表数据
	 */
	private void refresh() {
		// 刷新数据
		myFunds.clear();
		myFunds.addAll(fundValueDao.getMyFundValues());
		myFundAdapter.notifyDataSetChanged();

		allFunds.clear();
		allFunds.addAll(fundValueDao.getAllFundValues());
		allFundAdapter.notifyDataSetChanged();

		incomes.clear();
		incomes.addAll(fundIncomeDao.getMyFundIncomes());
		fundIncomeAdapter.notifyDataSetChanged();
	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;
		if (index == 0) {
			zixuan.setTextColor(0xff228B22);
			shouyi.setTextColor(Color.BLACK);
			quanbu.setTextColor(Color.BLACK);
		} else if (index == 1) {
			zixuan.setTextColor(Color.BLACK);
			shouyi.setTextColor(0xff228B22);
			quanbu.setTextColor(Color.BLACK);
		} else {
			zixuan.setTextColor(Color.BLACK);
			shouyi.setTextColor(Color.BLACK);
			quanbu.setTextColor(0xff228B22);
		}
	}

	@Override
	public void OnViewChange(int view) {
		setCurPoint(view);
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class UpdateTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// 记录刷新时间
			// 当前时间
			long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
			// 一个周前
			long weekBefore = currentTime - 7 * 24 * 60 * 60;
			// 读取SharedPreferences中需要的数据
			SharedPreferences preferences = getSharedPreferences(
					SHAREDPREFERENCES_NAME, MODE_PRIVATE);
			lastUpdateTime = preferences.getLong("lastUpdateTime", weekBefore);
			/*
			 * 添加基金收益信息
			 */
			fundValueDao = new FundValueDao(HomeActivity.this);
			List<FundValue> values = fundValueDao.getAllFromNet(lastUpdateTime);
			if (values != null) {
				// 更新收益历史
				fundValueDao.saveFundValuesList(values);
				// 更新基金信息
				fundValueDao.UpdateFundInfo();
				// 实例化Editor对象
				Editor editor = preferences.edit();
				// 存入数据
				editor.putLong("lastUpdateTime", currentTime);
				// 提交修改
				editor.commit();
				return values.size();
			}else {
				return 0;
			}		
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("提示信息");
			dialog.setMessage("正在更新，请稍候...");
			dialog.setCancelable(false);
			dialog.show();
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
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			refresh();	
			super.onPostExecute(result);
			if (result == 0) {
				Toast.makeText(HomeActivity.this, "数据已是最新，暂不用更新！",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(HomeActivity.this,
						"恭喜您，本次更新共更新" + result + "条记录，您的数据已更新到最新！",
						Toast.LENGTH_LONG).show();
			}		
		}
	}
}
