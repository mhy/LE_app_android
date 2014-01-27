package com.mhy.lefinder.webview;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mhy.lefinder.R;
import com.mhy.lefinder.util.BaseAsyncTask;


public class MvPageAsyncTask extends BaseAsyncTask<String, Void, String> {
	private WebView mWebview;
	private HttpClient mClient;
	private String mUrl;
	
	public MvPageAsyncTask(Activity act) {
		super(act);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mClient = new DefaultHttpClient();
		
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
				mAct.finish();
			}
		});
	}

	@Override
	protected String doInBackground(String... url) {
		mUrl = url[0];
		HttpEntity resEntity = null;
		try {
			HttpGet get = new HttpGet(url[0]);
			HttpResponse resp = mClient.execute(get);
			resEntity = resp.getEntity();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(resEntity != null){
			String vimeoId = null;
			try {
				vimeoId = getVimeoUrl(EntityUtils.toString(resEntity));
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return vimeoId;
		}else
			return null;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onPostExecute(String vId) {
		if(vId != null){
			mWebview = (WebView)mAct.findViewById(R.id.wbPageOriginal);	
			mWebview.getSettings().setJavaScriptEnabled(true);
			mWebview.loadUrl("http://hiphople.com/lyrics/266181");	//just a random page to obtain privilege to play vimeo. lighter a random page is, more performance we get. 
			final String javascript = String.format("javascript:window.open(\"http://player.vimeo.com/video/%s\",\"_self\")", vId);
			
			mWebview.setWebViewClient(new WebViewClient(){
				int triggerLoading = -5;
				
				@Override
				public void onLoadResource(WebView view, String url) {
					super.onLoadResource(view, url);
					triggerLoading++;
					if(triggerLoading == 0){
						mWebview.stopLoading();
						mWebview.loadUrl(javascript);
					}
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					if(url.contains("vimeo"))
						mDialog.dismiss();
				}
			});
		}else{
			super.onPostExecute(vId);
			Intent i = new Intent();
			i.setData(Uri.parse(mUrl));
			mAct.startActivity(i);
			mAct.finish();
		}
	}
	
	private String getVimeoUrl(String content){
		//content
		String tokenStarter = "<!--BeforeDocument";	//<!--BeforeDocument(219058,104933)-->
		String tokenFinilizer = "<!--AfterDocument";	//<!--AfterDocument(219058,104933)-->
		
		int start = content.indexOf(tokenStarter);
		int last = content.indexOf(tokenFinilizer);		
		content = content.substring(start, last);
		
		Pattern vimeoPattern = Pattern.compile("//player.vimeo.com/video/([0-9]*)[^>]*></iframe>.*"); //http://player.vimeo.com/video/36535874?
		Matcher match = vimeoPattern.matcher(content);
		
		if(match.find()){
			String vId = match.group(1);

			content = content.substring( content.indexOf(match.group(1)), content.length() );	//find if there's another vimeo url
			match = vimeoPattern.matcher(content);
			
			if(match.find())	//vimeo url should exists only one. if more than two urls exist, return null
				return null;
			else
				return vId;
		}else
			return null;
	}

	
}
