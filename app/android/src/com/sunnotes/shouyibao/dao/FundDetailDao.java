package com.sunnotes.shouyibao.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sunnotes.shouyibao.Constants;
import com.sunnotes.shouyibao.model.FundDetail;
import com.sunnotes.shouyibao.util.DBHelper;
import com.sunnotes.shouyibao.util.JsonUtils;

public class FundDetailDao {
	// 基金详情
	private final static String details_url = Constants.URL_DETAILS;
	// 辅助类属性
	private DBHelper dbHelper;
	// Log
	private static final String TAG = "FundDetailDao";
	/**
	 * 带参构造方法，传入context
	 * 
	 * @param context
	 */
	public FundDetailDao(Context context) {
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
		db.delete("fundinfo", "code in (" + sb.toString() + ")", c);
		db.close();
	}

	/**
	 * 添加纪录
	 * 
	 * @param FundInfo
	 */
	public void save(FundDetail detail) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String insert = "INSERT INTO `fund_info`(`code`,`name`,`nickname`,"
				+ "`type`,`date`,`profit`,`rate`,`buy_info`,`start_date`, "
				+ "`custodian_bank`, `investment_range`,`investment_aim`,"
				+ "`benchmark`, `risk`,`memo`) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(
				insert,
				new Object[] { detail.getCode(), detail.getName(),
						detail.getNickname(), detail.getType(),
						detail.getDate(), detail.getProfit(), detail.getRate(),
						detail.getBuyinfo(), detail.getStartdate(),
						detail.getCustodianbank(),
						detail.getInverstmentrange(),
						detail.getInverstmentaim(), detail.getBenchmark(),
						detail.getRisk(), detail.getMemo() });
		db.close();
	}

	/**
	 * 查找所有的记录
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public FundDetail getFundDetail(String code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		FundDetail detail = null;
		Cursor cursor = db
				.rawQuery(
						"select `code`,`name`,`nickname`,"
								+ "`date`,`profit`,`rate`,`type`,`buy_info`,`start_date`, "
								+ "`custodian_bank`, `investment_range`,`investment_aim`,"
								+ "`benchmark`, `risk`,`memo` ,`updatetime` from fund_info where code = ? ",
						new String[] { code });

		while (cursor.moveToNext()) {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = format1.parse(cursor.getString(3));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			detail = new FundDetail(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), date, cursor.getString(4),
					cursor.getString(5), cursor.getString(6),
					cursor.getString(7), cursor.getString(8),
					cursor.getString(9), cursor.getString(10),
					cursor.getString(11), cursor.getString(12),
					cursor.getString(13), cursor.getString(14),
					cursor.getString(15));
			break;
		}
		db.close();
		return detail;
	}

	@SuppressLint("SimpleDateFormat")
	public List<FundDetail> getAllFundDetailsFromNet(long begintime) {
		String url = details_url + "?timestamp='" + begintime + "'";
		List<FundDetail> results = new ArrayList<FundDetail>();
		JSONObject jsonObject = null;
		FundDetail detail;
		JSONArray details = null;
		int count = 0;
		try {
			jsonObject = JsonUtils.getJson(url);
			count = jsonObject.getInt("num");		
		} catch (JSONException e1) {
			Log.e(TAG, e1.getMessage());
		} catch (Exception e1) {
			Log.e(TAG, e1.getMessage());
		}
		if (count == 0) {
			return null;
		}
		try {
			details = jsonObject.getJSONArray("data");
		} catch (JSONException e1) {
			Log.e(TAG, e1.getMessage());
		}
		if (details == null) {
			return null;
		}
		JSONObject jsonItem;
		for (int i = 0; i < details.length(); i++) {	
			try {
				jsonItem = details.getJSONObject(i);
				String code = (String) jsonItem.get("code");
				String name = (String) jsonItem.get("name");
				String nickname = (String) jsonItem.get("nickname");
				// 日期转换
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date = format1.parse(jsonItem.get("date").toString());
				} catch (ParseException e) {
					Log.e(TAG, e.getMessage());
				}
				String profit = (String) jsonItem.get("profit");
				String rate = (String) jsonItem.get("rate");
				String type = (String) jsonItem.get("type");
				String buyinfo = (String) jsonItem.get("buy_info");
				String startdate = (String) jsonItem.get("start_date");
				String custodianbank = (String) jsonItem.get("custodian_bank");
				String investmentrange = (String) jsonItem
						.get("investment_range");
				String investmentaim = (String) jsonItem.get("investment_aim");
				String benchmark = (String) jsonItem.get("benchmark");
				String risk = (String) jsonItem.get("risk");
				String memo = (String) jsonItem.get("memo");
				String updatetime = (String) jsonItem.get("updatetime");
				detail = new FundDetail(code, name, nickname, date, profit,
						rate, type, buyinfo, startdate, custodianbank,
						investmentrange, investmentaim, benchmark, risk, memo,
						updatetime);
				results.add(detail);
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
			}

		}
		return results;
	}

	@SuppressLint("SimpleDateFormat")
	public List<FundDetail> getAllFundDetailsFromNet() {
		return getAllFundDetailsFromNet(0);
	}

	@SuppressLint("SimpleDateFormat")
	public List<FundDetail> updateAllFromNet(String url) {
		List<FundDetail> results = new ArrayList<FundDetail>();
		JSONObject jsonObject = null;
		try {
			jsonObject = JsonUtils.getJson(url);
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		FundDetail detail;
		JSONArray detailaArray = null;

		try {
			detailaArray = jsonObject.getJSONArray("data");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < detailaArray.length(); i++) {
			JSONObject jsonItem;
			try {
				jsonItem = detailaArray.getJSONObject(i);
				String code = (String) jsonItem.get("code");
				String name = (String) jsonItem.get("name");
				String nickname = (String) jsonItem.get("nickname");
				// 日期转换
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date = format1.parse(jsonItem.get("date").toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Date date = new Date(jsonItem.get("date").toString());
				String profit = (String) jsonItem.get("profit");
				String rate = (String) jsonItem.get("rate");
				String type = (String) jsonItem.get("type");
				String buyinfo = (String) jsonItem.get("buy_info");
				String startdate = (String) jsonItem.get("start_date");
				String custodianbank = (String) jsonItem.get("custodian_bank");
				String investmentrange = (String) jsonItem
						.get("investment_range");
				String investmentaim = (String) jsonItem.get("investment_aim");
				String benchmark = (String) jsonItem.get("benchmark");
				String risk = (String) jsonItem.get("risk");
				String memo = (String) jsonItem.get("memo");
				String updatetime = (String) jsonItem.get("updatetime");

				detail = new FundDetail(code, name, nickname, date, profit,
						rate, type, buyinfo, startdate, custodianbank,
						investmentrange, investmentaim, benchmark, risk, memo,
						updatetime);
				results.add(detail);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return results;
	}

	public void saveFundDetailsList(List<FundDetail> records) {
		for (FundDetail detail : records) {
			save(detail);
		}
	}

	// /**
	// * 查询全部
	// *
	// * @return 游标
	// */
	// public Cursor getAllFundInfo() {
	// SQLiteDatabase db = helper.getReadableDatabase();
	// // ListView 里的id是有个下划线的，所以这里要给个别名_id
	// Cursor cursor = db.rawQuery(
	// "select FundInfoid as _id, name,age from FundInfo", null);
	// // 这里数据库不能关闭
	// return cursor;
	// }

}
