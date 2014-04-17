package com.sunnotes.shouyibao.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sunnotes.shouyibao.util.DBHelper;

public class MyFundDao {

	// 辅助类属性
	private DBHelper dbHelper;


	/**
	 * 带参构造方法，传入context
	 * 
	 * @param context
	 */
	public MyFundDao(Context context) {
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
		db.delete("my_fund", "code in (" + sb.toString() + ")", c);
		db.close();
	}

	/**
	 * 添加自选
	 * 
	 * @param FundInfo
	 */
	public void save(String code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String insert = "INSERT INTO `my_fund`(`code`,`isbuy`,`money`) "
				+ "VALUES (?,?,?)";
		db.execSQL(insert, new Object[] { code, 0, 0 });
		db.close();
	}

	/**
	 * 删除自选
	 * 
	 * @param FundInfo
	 */
	public int delete(String code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] args = { code };
		return db.delete("my_fund", "code=?", args);
	}

	@SuppressLint("SimpleDateFormat")
	public int updateMoney(String code, float money) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("isbuy", "1");
		cv.put("money", money);
		String[] args = { String.valueOf(code) };
		return db.update("my_fund", cv, "code=?", args);
	}
}
