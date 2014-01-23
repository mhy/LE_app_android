package com.mhy.lefinder.webview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;

import com.mhy.lefinder.R;
import com.mhy.lefinder.fragment.search.Request;
import com.mhy.lefinder.fragment.search.SearchAsyncTask.Category;
import com.mhy.lefinder.result.Result;

public class ViewerActivity extends FragmentActivity {
	private final static String URL = "http://www.hiphople.com/";
	private final static String MV = "subtitle/";
	private final static String LYRICS = "lyrics/";
	private Category mCategory;
	private WebView mWebview;
	private VimeoChromeClient vcc;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
				
		Result result = getIntent().getParcelableExtra("result");
		Request request = getIntent().getParcelableExtra("request");
		
		
		mCategory = request.getCategory();
		switch(mCategory){
			case MV :
				setContentView(R.layout.activity_viewer_mv);
				vcc = new VimeoChromeClient(this);
				mWebview = (WebView)findViewById(R.id.wbPageOriginal);	
				mWebview.setWebChromeClient(vcc);
				new MvPageAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL+MV+result.getUrl());
				break;
			case LYRICS :
				setContentView(R.layout.activity_viewer_lyrics);
				new LyricsPageAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL+LYRICS+result.getUrl());
				break;
		}
	}
	
	@Override
	public void onBackPressed() {
		Log.d("MHY", "onBackPressed");
		if(mCategory == Category.MV){
			Log.d("MHY", "onBackPressed KILL PROCESS calling for resource return");
			vcc.hideCustomView();
		}
		super.onBackPressed();
	}
	
}
