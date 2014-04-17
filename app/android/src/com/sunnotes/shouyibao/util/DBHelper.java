package com.sunnotes.shouyibao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "shouyibao.db";
	private static final int VERSION = 4;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	/**
	 * 第一次运行的时候创建
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
//		String creat_fund_sql = "CREATE  TABLE  IF NOT EXISTS  'fund' "
//				+ "('id' INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL ,'code' VARCHAR NOT NULL  DEFAULT (000) ,'name' VARCHAR NOT NULL  DEFAULT (000) ,'date' DATE DEFAULT (CURRENT_DATE) ,'updatetime' DATETIME DEFAULT (CURRENT_TIMESTAMP) ,'profit' decimal(10,4) DEFAULT (0.0) ,'rate' decimal(10,4) DEFAULT (0.0) )";
//		db.execSQL(creat_fund_sql);
//
//		String creat_fund_info_sql = "CREATE  TABLE  IF NOT EXISTS 'fund_info' "
//				+ "('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'code' VARCHAR DEFAULT 000, 'name' VARCHAR DEFAULT 000, 'nickname' VARCHAR DEFAULT 000, 'type' VARCHAR DEFAULT 000, 'date' DATE DEFAULT CURRENT_DATE, 'profit' NUMERIC(10,4) DEFAULT 0.0, 'rate' NUMERIC(10,4) DEFAULT 0.0, 'buy_info' VARCHAR DEFAULT 000, 'start_date' VARCHAR DEFAULT 000, 'custodian_bank' VARCHAR DEFAULT 000, 'investment_range' VARCHAR DEFAULT 000, 'investment_aim' VARCHAR DEFAULT 000, 'benchmark' VARCHAR DEFAULT 000, 'risk' VARCHAR DEFAULT 000, 'memo' VARCHAR DEFAULT 000, 'updatetime' DATETIME DEFAULT CURRENT_TIMESTAMP)";
//		db.execSQL(creat_fund_info_sql);
//
//		String creat_my_fund_sql = "CREATE TABLE 'my_fund' ('code' varchar DEFAULT (null) ,'isbuy' varchar DEFAULT (null) ,'money' varchar DEFAULT (null) ) ";
//
//		db.execSQL(creat_my_fund_sql);
	}

	/**
	 * 更新的时候
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS fund");
//		db.execSQL("DROP TABLE IF EXISTS fund_info");
//		onCreate(db);
	}

}