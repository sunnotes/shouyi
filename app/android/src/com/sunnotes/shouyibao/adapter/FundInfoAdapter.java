package com.sunnotes.shouyibao.adapter;

import java.util.List;

import com.sunnotes.shouyibao.R;
import com.sunnotes.shouyibao.model.FundValue;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FundInfoAdapter extends BaseAdapter {
	private Context context;
	private List<FundValue> list;

	public FundInfoAdapter(Context context, List<FundValue> list) {
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
					R.layout.fund_list_item, parent, false);
			viewHolder.image = (ImageView) view.findViewById(R.id.image);
			viewHolder.code = (TextView) view.findViewById(R.id.code);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.date = (TextView) view.findViewById(R.id.date);
			viewHolder.profit = (TextView) view.findViewById(R.id.profit);
			viewHolder.rate = (TextView) view.findViewById(R.id.rate);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// String img = "icon_" + value.getCode() + ".png";
		// int resID = getResourceByReflect(img);
		int resID = getResourceByCode(value.getCode());
		Drawable image = this.context.getResources().getDrawable(resID);
		viewHolder.image.setImageDrawable(image);
		viewHolder.code.setText(value.getCode());
		viewHolder.name.setText(value.getName());
		viewHolder.date.setText(value.getDate());
		viewHolder.profit.setText(value.getProfit());
		viewHolder.rate.setText(value.getRate()+"%");
		return view;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		super.registerDataSetObserver(observer);
	}



	public List<FundValue> getList() {
		return list;
	}

	public void setList(List<FundValue> list) {
		this.list = list;
	}

	private static class ViewHolder {
		private ImageView image;
		private TextView code;
		private TextView name;
		private TextView profit;
		private TextView date;
		private TextView rate;
	}

	/**
	 * 获取图片名称获取图片的资源id的方法
	 * 
	 * @param imageName
	 * @return
	 */
	private int getResourceByReflect(String imageName) {
		int r_id;
		try {
			r_id = this.context.getResources().getIdentifier(imageName,
					"drawable", this.context.getPackageName());
		} catch (Exception e) {
			r_id = R.drawable.icon_000000;
			Log.e("ERROR", "PICTURE NOT　FOUND！");
		}
		if (r_id == 0) {
			return R.drawable.icon_000000;
		}
		return r_id;
	}

	private int getResourceByCode(String code) {
		if (code.equals("000198")) {
			return R.drawable.icon_000198;
		} else if (code.equals("000009")) {
			return R.drawable.icon_000009;
		} else if (code.equals("003003")) {
			return R.drawable.icon_003003;
		} else if (code.equals("000330")) {
			return R.drawable.icon_000330;
		} else if (code.equals("000343")) {
			return R.drawable.icon_000343;
		} else if (code.equals("000389")) {
			return R.drawable.icon_000389;
		} else if (code.equals("000397")) {
			return R.drawable.icon_000397;
		} else if (code.equals("000464")) {
			return R.drawable.icon_000464;
		} else if (code.equals("000509")) {
			return R.drawable.icon_000509;
		} else {
			return R.drawable.icon_000000;
		}
	}
}
