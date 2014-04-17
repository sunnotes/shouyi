package com.sunnotes.shouyibao.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sunnotes.shouyibao.model.FundIncome;
import com.sunnotes.shouyibao.model.FundValue;
import com.sunnotes.shouyibao.util.DBHelper;

public class FundIncomeDao {

	// 辅助类属性
	private DBHelper dbHelper;

	/**
	 * 带参构造方法，传入context
	 * 
	 * @param context
	 */
	public FundIncomeDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 使用不同的方法删除记录1到多条记录
	 * 
	 * @param ids
	 */
	public void delete(Integer... ids) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] c = new String[ids.length];
		StringBuffer sb = new StringBuffer();
		if (ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				sb.append('?').append(',');
				// 把整数数组转换哼字符串数组
				c[i] = ids[i].toString();
			}
			// 删除最后一个元素
			sb.deleteCharAt(sb.length() - 1);
		}
		db.delete("fund", "code in (" + sb.toString() + ")", c);
		db.close();
	}

	/**
	 * 添加纪录
	 * 
	 * @param FundValue
	 */
	public void save(FundValue value) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into fund (code,name,date,updatetime,profit,rate) values(?,?,?,?,?,?)",
				new Object[] { value.getCode(), value.getName(),
						value.getDate(), value.getUpdatetime(),
						value.getProfit(), value.getRate() });
		db.close();
	}

	/**
	 * 查找所有的记录
	 * 
	 * @return
	 */
	public List<FundValue> getAllFundValues() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<FundValue> values = new ArrayList<FundValue>();
		Cursor cursor = db.rawQuery(
				"select code,name,date,updatetime,profit,rate from fund", null);
		while (cursor.moveToNext()) {
			values.add(new FundValue(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5)));
		}
		return values;
	}

	/*
	 * 读取所有设置过金额的自选基金
	 */
	public List<FundIncome> getMyFundIncomes() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<FundIncome> values = new ArrayList<FundIncome>();
		Cursor cursor = db
				.rawQuery(
						"select my_fund.code,name,date,updatetime,profit,rate,money from my_fund left join fund_info where my_fund.code =   fund_info.code  and isbuy=1",
						null);
		while (cursor.moveToNext()) {
			Float income = Float.parseFloat(cursor.getString(4))
					* Float.parseFloat(cursor.getString(6))/10000;
			DecimalFormat decimalFormat = new DecimalFormat("##0.00");
			String dd = decimalFormat.format(income);
			values.add(new FundIncome(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), dd,cursor.getString(6), cursor.getString(4), cursor
							.getString(5)));
		}
		return values;
	}

	public void saveFundValuesList(List<FundValue> records) {
		for (FundValue value : records) {
			save(value);
		}
	}
}
