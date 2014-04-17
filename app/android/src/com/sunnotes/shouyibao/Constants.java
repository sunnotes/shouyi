package com.sunnotes.shouyibao;

public class Constants {
	// 共享
	public static String SHAREDPREFERENCES_NAME = "shouyibao";
	// 更新基金详细信息
	public static String URL_DETAILS = "http://shouyibao.sinaapp.com/app/data/upalldetails.php";
	// 更新收益信息
	public static String URL_VALUES = "http://shouyibao.sinaapp.com/app/data/upallvalues.php";
	//反馈建议
	public static String URL_FEEDBACK = "http://shouyibao.sinaapp.com/app/data/feedback.php";
	// 历史收益和
	public static String URL_HISTORY = "http://weishouyi.sinaapp.com/m/history.php";
	//净值比较
	public static String URL_COMPARE = "http://weishouyi.sinaapp.com/m/compare.php";
	// 间隔时间5分钟
	public static long SYNC_PERIOD = 5 * 60 * 1000;
	// 开屏时间
	public static long SPLASH_DELAY_MILLIS = 1 * 1000;
}
