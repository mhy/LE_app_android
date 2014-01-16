package com.mhy.lefinder.webview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.mhy.lefinder.Request;
import com.mhy.lefinder.SearchAsyncTask.Category;
import com.mhy.lefinder.result.Result;

public class ViewerActivity extends FragmentActivity {
	private final static String URL = "http://www.hiphople.com/";
	private final static String MV = "subtitle/";
	private final static String LYRICS = "lyrics/";
	private WebView webview;
	private Category mCategory;
	
	private ViewerChromeClient mClient;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
				
		Result result = getIntent().getParcelableExtra("result");
		Request request = getIntent().getParcelableExtra("request");
		
		mCategory = request.getCategory();
		switch(mCategory){
			case MV :
				setContentView(com.mhy.lefinder.R.layout.activity_viewer_mv);
				webview = (WebView)findViewById(com.mhy.lefinder.R.id.wbPageOriginal);
				
				webview.getSettings().setJavaScriptEnabled(true);
		        webview.getSettings().setPluginState(PluginState.OFF);
		        webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		        webview.getSettings().setBuiltInZoomControls(false);
		        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		        webview.getSettings().setLoadWithOverviewMode(true);
		        webview.getSettings().setUseWideViewPort(true);
		        webview.clearHistory();
		        webview.clearFormData();
		        webview.clearCache(true);
		        webview.getSettings().setAllowFileAccess(true);
		        webview.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
				
				
				mClient = new ViewerChromeClient(this);
				webview.setWebChromeClient(mClient);
				
				webview.setWebViewClient(new WebViewClient(){
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						Toast.makeText(getApplicationContext(), "web browsing is not supported", Toast.LENGTH_SHORT).show();
						return true;
					}
				});
				
				if(arg0!=null){
					
				}else{
					webview.loadUrl(URL+MV+result.getUrl());
				}
				break;
			case LYRICS :
				new ContentAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL+LYRICS+result.getUrl());
				break;
		}
	}
	
	
/**
 * TODO implementing custom webivew that can play Vimeo.
 * TODO implementing graceful-close for custom webview playing video
 */
	
//	@Override
//	public void onBackPressed() {
//		if(mCategory == Category.MV){
//			if(webview.getVisibility()==View.GONE)
//				mClient.onHideCustomView();
//			else{
//	//			mClient.onCloseClient();
//				Log.d("MHY", "onBackPressed KILL MYSELF");
//				android.os.Process.killProcess(android.os.Process.myPid());
//	//			super.onBackPressed();
//			}
//		}else
//			super.onBackPressed();
//	}
//
//
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		
//		if(mCategory == Category.MV)
//			webview.saveState(outState);
//	}
//
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		
//		if(mCategory == Category.MV)
//			webview.restoreState(savedInstanceState);
//	}
//
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		if(mCategory==Category.MV){
//			if(hasFocus){
//				try {
//					Class.forName("android.webkit.WebView")
//					.getMethod("onResume", (Class[]) null)
//					            .invoke(webview, (Object[]) null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				webview.resumeTimers();
//			}else{
//				try {
//					Class.forName("android.webkit.WebView")
//					.getMethod("onPause", (Class[]) null)
//					            .invoke(webview, (Object[]) null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				webview.pauseTimers();
//			}
//		}
//		
//		super.onWindowFocusChanged(hasFocus);
//	}
	
	
}
