package com.sunnotes.shouyibao.adapter;

import java.util.List;

import com.sunnotes.shouyibao.R;
import com.sunnotes.shouyibao.model.FundValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FundHistoryAdapter extends BaseAdapter {
	private Context context;
	private List<FundValue> list;

	public FundHistoryAdapter(Context context, List<FundValue> list) {
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

		FundValue value = list.get(position);
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.fund_history_item, parent, false);
			viewHolder.date = (TextView) view.findViewById(R.id.fund_history_date);
			viewHolder.profit = (TextView) view
					.findViewById(R.id.fund_history_profit);
			viewHolder.rate = (TextView) view.findViewById(R.id.fund_history_rate);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.date.setText(value.getDate());
		viewHolder.profit.setText(value.getProfit());
		viewHolder.rate.setText(value.getRate()+"%");
		return view;
	}
	
	public List<FundValue> getList() {
		return list;
	}

	public void setList(List<FundValue> list) {
		this.list = list;
	}



	private static class ViewHolder {
		private TextView date;
		private TextView profit;
		private TextView rate;
	}
}
