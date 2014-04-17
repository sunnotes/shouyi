package com.sunnotes.shouyibao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	//private static final String TAG = "JSONUtil";

	/**
	 * 获取json内容
	 * 
	 * @param url
	 * @return JSONArray
	 * @throws JSONException
	 * @throws ConnectionException
	 */
	public static JSONObject getJson(String url) throws JSONException,
			Exception {

		// return new JSONObject(getRequest(url));
		return new JSONObject(downloadUrl(url).toString());
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	public static StringBuffer downloadUrl(String myurl) {
		// Only display the first 500 characters of the retrieved
		// web page content.
		StringBuffer sb = null;
		URL url = null;
		try {
			url = new URL(myurl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (url != null) {
			try {
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();// 打开连接，此处只是创建一个实力，并没有真正的连接
				urlConn.setDoInput(true);
				urlConn.setDoOutput(true);
				urlConn.connect();// 连接
				InputStream input = urlConn.getInputStream();
				InputStreamReader inputReader = new InputStreamReader(input);
				BufferedReader reader = new BufferedReader(inputReader);
				String inputLine = null;
				sb = new StringBuffer();
				while ((inputLine = reader.readLine()) != null) {
					sb.append(inputLine).append("\n");
				}
				reader.close();
				inputReader.close();
				input.close();
				urlConn.disconnect();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;

	}
}
