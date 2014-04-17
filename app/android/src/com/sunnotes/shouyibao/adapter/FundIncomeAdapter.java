package com.sunnotes.shouyibao.adapter;

import java.util.List;


import com.sunnotes.shouyibao.R;
import com.sunnotes.shouyibao.model.FundIncome;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FundIncomeAdapter extends BaseAdapter {
	private Context context;
	private List<FundIncome> list;

	public FundIncomeAdapter(Context context, List<FundIncome> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		FundIncome income = list.get(position);
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.fund_income_list_item,
					parent, false);
			viewHolder.profit = (TextView) view.findViewById(R.id.income_profit);
			viewHolder.rate = (TextView) view.findViewById(R.id.income_rate);
			viewHolder.income = (TextView) view.findViewById(R.id.income_income);
			viewHolder.name = (TextView) view.findViewById(R.id.income_name);
			viewHolder.money = (TextView) view.findViewById(R.id.income_money);
			viewHolder.date = (TextView) view.findViewById(R.id.income_date);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.profit.setText(income.getProfit());
		viewHolder.rate.setText(income.getRate()+"%");
		viewHolder.income.setText(income.getIncome());
		viewHolder.name.setText(income.getName());
		viewHolder.money.setText(income.getMoney());
		viewHolder.date.setText(income.getDate()+"收益");
		return view;
	}

	
	public List<FundIncome> getList() {
		return list;
	}

	public void setList(List<FundIncome> list) {
		this.list = list;
	}
	
	private static class ViewHolder {
		private TextView profit;
		private TextView rate;
		private TextView name;
		private TextView money;
		private TextView income;
		private TextView date;
	}
}
