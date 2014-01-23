package com.mhy.lefinder.fragment.search;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.widget.Toast;

import com.mhy.lefinder.R;
import com.mhy.lefinder.result.Result;
import com.mhy.lefinder.result.ResultDialog;
import com.mhy.lefinder.util.BaseAsyncTask;

public class SearchAsyncTask extends BaseAsyncTask<String, Void, SearchResultParser> {
	public enum Category{MV, LYRICS}
	
	protected final String BASEURL = "http://www.hiphople.com/?mid=";
	protected final String SEARCH_KEYWORD = "&search_keyword=";
	protected final String SEARCH_TARGET = "&search_target=title";
	
	private Request request; 
	
	protected ArrayList<Result> mResults;
	private HttpClient mClient;
	private HttpGet mGet;
	
	public String actionUrl;

	public SearchAsyncTask(Activity act, Request req) {
		super(act);
		request = req;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		mClient = new DefaultHttpClient();
		actionUrl = getActionUrl(request);
		
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
			}
		});
	}

	@Override
	protected SearchResultParser doInBackground(String... arg0) {
		HttpEntity resEntity = null;
		try {
			mGet = new HttpGet(actionUrl);
			HttpResponse response = mClient.execute(mGet);
			resEntity = response.getEntity(); 
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(resEntity != null){
			try {
				String result = EntityUtils.toString(resEntity);
				
				int a = result.indexOf("<td class=\"title\">");
				int b = result.lastIndexOf("<td class=\"title\">");
				result = result.substring(a, b);
				
				SearchResultParser parser = new SearchResultParser(result, request);
				return parser;
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(mAct, "ERROR : "+e.getMessage(), Toast.LENGTH_SHORT).show();
				return null;
			}
		}else
			return null;
	}

	@Override
	protected void onPostExecute(SearchResultParser parser) {
		super.onPostExecute(parser);
		
		if(parser!=null){
			mResults = parser.getParsedData();
			if(mResults.size()>0){
				Bundle args = new Bundle();
				args.putParcelableArrayList("result", mResults);
				args.putInt("lastpage", parser.getLastIndex());	//if there's only one page, last index is set to 1
				args.putString("division", parser.getDivision());
				args.putParcelable("request", request);
				
				showResult(args);
			}else
				Toast.makeText(mAct, mAct.getResources().getString(R.string.noresult), Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void showResult(Bundle args){
		ResultDialog frag = new ResultDialog();
		frag.setArguments(args);
		frag.show(mAct.getFragmentManager(), "");
	}
	
	protected String getActionUrl(Request req){
		String url = "";
		switch (req.getCategory()) {
			case MV:
				url = BASEURL+"subtitle"+SEARCH_KEYWORD+req.getKey()+SEARCH_TARGET;
				break;
			case LYRICS:
				url = BASEURL+"lyrics"+SEARCH_KEYWORD+req.getKey()+SEARCH_TARGET;
				break;
		}
		return url;
	}
}
