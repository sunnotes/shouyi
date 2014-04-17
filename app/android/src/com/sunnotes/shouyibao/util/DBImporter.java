package com.sunnotes.shouyibao.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sunnotes.shouyibao.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBImporter {
	public static final String PACKAGE_NAME = "com.sunnotes.shouyibao";
	public static final String DB_NAME = "shouyibao.db";
	public static String DB_PATH = "/data/data/" + PACKAGE_NAME + "/databases";
	private Context context;
	// Log
	private static final String TAG = "DBImport";

	public DBImporter(Context mContext) {
		this.context = mContext;
	}

	public SQLiteDatabase openDataBase() {
		return SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME,
				null);
	}

	public void copyDB() {
		File file = new File(DB_PATH + "/" + DB_NAME);
		File dir = new File(DB_PATH);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(file);
				int buffer = 400000;
				// 读取数据库并保存到data/data/packagename/databases/xx.db...
				InputStream ins = context.getResources().openRawResource(
						R.raw.shouyibao);
				byte[] bts = new byte[buffer];
				int length;
				while ((length = ins.read(bts)) > 0) {
					out.write(bts, 0, bts.length);
				}
				out.close();
				ins.close();
				Log.i(TAG, "==copy database");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
