package com.sunnotes.shouyibao;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class DrinkActivity extends Activity {
	private WebView webView; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drink);		
        webView = (WebView) findViewById(R.id.webView_drink);  
        /*显示App icon左侧的back键*/
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本  
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放  

        webView.loadUrl("https://me.alipay.com/eason8");  
        webView.setWebViewClient(new WebViewClient(){  
            @Override  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {   
                view.loadUrl(url);// 使用当前WebView处理跳转  
                return true;//true表示此事件在此处被处理，不需要再广播  
            }  
            @Override   //转向错误时的处理  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
                // TODO Auto-generated method stub  
                Toast.makeText(DrinkActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();  
            }  
        });  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.common, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	        case android.R.id.home:
	            this.finish();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
