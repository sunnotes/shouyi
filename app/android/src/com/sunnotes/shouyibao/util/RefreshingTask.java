//package com.sunnotes.shouyibao.util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.sunnotes.shouyibao.model.FundInfo;
//
//public class RefreshingTask {
//
//	public static List<FundInfo> updateFundInfos(String url) {
//		List<FundInfo> results = new ArrayList<FundInfo>();
//		//String url = "http://shouyibao.sinaapp.com/testjson1.php";
//		JSONObject jsonObject = null;
//		try {
//			jsonObject = JsonUtils.getJson(url);
//		} catch (JSONException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		} catch (Exception e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		FundInfo fundInfo;
//		JSONArray funds = null;
//		
//		try {
//			funds = jsonObject.getJSONArray("data");
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		for (int i = 0; i < funds.length();i++) {
//			JSONObject jsonItem;
//			try {
//				jsonItem = funds.getJSONObject(i);
//				String code = (String) jsonItem.get("code");
//				String name = (String) jsonItem.get("name");
//				String date = (String) jsonItem.get("date");
//				String profit = (String) jsonItem.get("profit");
//				String rate = (String) jsonItem.get("rate");
//
//				fundInfo = new FundInfo(code, name, date, profit, rate);
//				results.add(fundInfo);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//		return results;
//	}
//
//}
