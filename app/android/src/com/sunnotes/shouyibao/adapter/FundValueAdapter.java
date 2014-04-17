package com.sunnotes.shouyibao.adapter;

import java.util.ArrayList;

import com.sunnotes.shouyibao.R;
import com.sunnotes.shouyibao.model.FundValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FundValueAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<FundValue> list;
	
	public FundValueAdapter(Context context,ArrayList<FundValue> arrayList){
		this.context = context;
		this.list = arrayList;
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
		FundValue hh = list.get(position);
		Record h = null;
		if(view==null){
			h = new Record();
			view = LayoutInflater.from(context).inflate(R.layout.fund_list_item, parent, false);
			//h.pic = (ImageView)view.findViewById(R.id.tx1);
			//h.name = (TextView)view.findViewById(R.id.tx2);
			
			view.setTag(h);
		}else{
			h = (Record)view.getTag();
		}
		
		h.code.setText(hh.getCode());
		//h.date.setText(hh.getDate());	
		return view;
	}

	
	public ArrayList<FundValue> getList() {
		return list;
	}

	public void setList(ArrayList<FundValue> list) {
		this.list = list;
	}


	private class Record{
		TextView code;
		TextView date;
		TextView profit;
		TextView rate;
	}
}
