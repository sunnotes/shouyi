package com.sunnotes.shouyibao.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sunnotes.shouyibao.Constants;
import com.sunnotes.shouyibao.model.FundValue;
import com.sunnotes.shouyibao.util.DBHelper;
import com.sunnotes.shouyibao.util.JsonUtils;

public class FundValueDao {
	// 收益历史
	private final static String value_url = Constants.URL_VALUES;
	// Log
	private static final String TAG = "WelcomeActivity";
	// 辅助类属性
	private DBHelper dbHelper;

	/**
	 * 带参构造方法，传入context
	 * 
	 * @param context
	 */
	public FundValueDao(Context context) {
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
	 * 根据id查找
	 * 
	 * @param id
	 * @return
	 */
	// public FundValue find(Integer code) {
	// SQLiteDatabase db = helper.getWritableDatabase();
	// Cursor cursor = db.rawQuery(
	// "select * from FundValue where FundValueid=?",
	// new String[] { String.valueOf(id) });
	// if (cursor.moveToNext()) {
	// return new FundValue(cursor.getInt(0), cursor.getString(1),
	// cursor.getInt(2));
	// }
	// return null;
	// }

	/**
	 * 查找指定code记录
	 * 
	 * @return
	 */
	public List<FundValue> getFundValues(String code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<FundValue> values = new ArrayList<FundValue>();
		Cursor cursor = db.rawQuery(
				"select code,name,date,updatetime,profit,rate from fund where code = '"
						+ code + "' order by date desc limit 30 ", null);
		while (cursor.moveToNext()) {
			values.add(new FundValue(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5)));
		}
		return values;
	}

	/**
	 * 查找所有的记录
	 * 
	 * @return
	 */
	public List<FundValue> getAllFundValues() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<FundValue> values = new ArrayList<FundValue>();
		Cursor cursor = db
				.rawQuery(
						"select code,name,date,updatetime,profit,rate from fund_info  ORDER BY  `date` DESC , profit DESC",
						null);
		while (cursor.moveToNext()) {
			values.add(new FundValue(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5)));
		}
		return values;
	}

	public List<FundValue> getMyFundValues() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<FundValue> values = new ArrayList<FundValue>();
		Cursor cursor = db
				.rawQuery(
						"select my_fund.code,name,date,updatetime,profit,rate from my_fund left join fund_info where my_fund.code = fund_info.code",
						null);
		while (cursor.moveToNext()) {
			values.add(new FundValue(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5)));
		}
		return values;
	}

	public List<FundValue> getAllFromNet(long timestamp) {
		String url = value_url + "?timestamp='" + timestamp + "'";
		List<FundValue> values = new ArrayList<FundValue>();
		FundValue value;
		JSONArray valueArray = null;
		JSONObject jsonObject = null;
		int count = 0;
		try {
			jsonObject = JsonUtils.getJson(url);
			count = jsonObject.getInt("num");
		} catch (JSONException e2) {
			Log.e(TAG, url + "==" + e2.getMessage());
		} catch (Exception e2) {
			Log.e(TAG, url + "==" + e2.getMessage());
		}
		if (count == 0) {
			return null;
		}
		try {
			valueArray = jsonObject.getJSONArray("data");
		} catch (JSONException e1) {
			Log.e(TAG, e1.getMessage());
		}
		if (valueArray == null) {
			return null;
		}
		JSONObject jsonItem;
		for (int i = 0; i < valueArray.length(); i++) {
			try {
				jsonItem = valueArray.getJSONObject(i);
				String code = (String) jsonItem.get("code");
				String name = (String) jsonItem.get("name");
				String date = (String) jsonItem.get("date");
				String updatetime = (String) jsonItem.get("updatetime");
				String profit = (String) jsonItem.get("profit");
				String rate = (String) jsonItem.get("rate");
				value = new FundValue(code, name, date, updatetime, profit,
						rate);
				values.add(value);
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return values;
	}

	/**
	 * 批量插入
	 * 
	 * @param records
	 */
	public void saveFundValuesList(List<FundValue> records) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		for (FundValue value : records) {
			db.execSQL(
					"insert into fund (code,name,date,updatetime,profit,rate) values(?,?,?,?,?,?)",
					new Object[] { value.getCode(), value.getName(),
							value.getDate(), value.getUpdatetime(),
							value.getProfit(), value.getRate() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void UpdateFundInfo() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql1 = "UPDATE fund_info SET  DATE  =  (SELECT DATE FROM fund  WHERE fund.code = fund_info.code ORDER BY DATE DESC LIMIT 1 )";
		String sql2 = "UPDATE fund_info SET  profit  =  (SELECT profit FROM fund  WHERE fund.code = fund_info.code ORDER BY DATE DESC LIMIT 1 )";
		String sql3 = "UPDATE fund_info SET  rate  =  (SELECT rate FROM fund  WHERE fund.code = fund_info.code ORDER BY DATE  DESC LIMIT 1 )";
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
	}
}
