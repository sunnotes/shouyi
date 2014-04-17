package com.sunnotes.shouyibao;

import com.sunnotes.shouyibao.dao.FundDetailDao;
import com.sunnotes.shouyibao.model.FundDetail;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class FundDetailActivity extends Activity {
	private static String fundcode;
	private FundDetailDao dao= new FundDetailDao(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fund_detail);
		/*显示App icon左侧的back键*/
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		fundcode = this.getIntent().getStringExtra("fundcode");// 得到一个对象		
		FundDetail detail = dao.getFundDetail(fundcode);
		((TextView) this.findViewById(R.id.detail_name)).setText(detail
				.getName());
		((TextView) this.findViewById(R.id.detail_nickname)).setText(detail
				.getNickname());
		((TextView) this.findViewById(R.id.detail_buy_info)).setText(detail
				.getBuyinfo());
		((TextView) this.findViewById(R.id.detail_fund_code)).setText(detail
				.getCode());
		((TextView) this.findViewById(R.id.detail_fund_type)).setText(detail
				.getType());
		((TextView) this.findViewById(R.id.detail_start_date)).setText(detail
				.getStartdate());
		((TextView) this.findViewById(R.id.detail_custodian_bank))
				.setText(detail.getCustodianbank());
		((TextView) this.findViewById(R.id.detail_investment_range))
				.setText(detail.getInverstmentrange());
		((TextView) this.findViewById(R.id.detail_investment_aim))
				.setText(detail.getInverstmentaim());
		((TextView) this.findViewById(R.id.detail_benchmark)).setText(detail
				.getBenchmark());
		((TextView) this.findViewById(R.id.detail_risk)).setText(detail
				.getRisk());
		((TextView) this.findViewById(R.id.detail_memo)).setText(detail
				.getMemo());

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

}
