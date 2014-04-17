package com.sunnotes.shouyibao;

import java.util.List;

import com.sunnotes.shouyibao.adapter.FundHistoryAdapter;
import com.sunnotes.shouyibao.dao.FundDetailDao;
import com.sunnotes.shouyibao.dao.FundValueDao;
import com.sunnotes.shouyibao.model.FundDetail;
import com.sunnotes.shouyibao.model.FundValue;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class FundHistoryActivity extends Activity {

	private static String fundcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fund_history);
		/* 显示App icon左侧的back键 */
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// 读取code
		fundcode = this.getIntent().getStringExtra("fundcode");
		// 显示头部
		TextView text = (TextView) findViewById(R.id.fund_history_head);
		FundDetailDao dao = new FundDetailDao(this);
		FundDetail detail = dao.getFundDetail(fundcode);
		text.setText(detail.getName() + "（" + detail.getNickname() + "）");
		// 绑定XML中的ListView，作为Item的容器
		ListView list = (ListView) findViewById(R.id.fund_history_list_view);
		FundHistoryAdapter adapter = new FundHistoryAdapter(this,
				getRecords(fundcode));
		list.setAdapter(adapter);
		list.setCacheColorHint(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common, menu);
		return true;
	}

	private List<FundValue> getRecords(String code) {
		FundValueDao dao = new FundValueDao(this);
		return dao.getFundValues(fundcode);
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
}
