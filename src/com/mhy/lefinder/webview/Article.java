package com.mhy.lefinder.webview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class Article {
	private String title;
	private String author;
	private String content;
		
	public Article(String data){
		setData(data);
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}
	
	private void setData(String data){		
		//author
		String tokenAuthor = "<meta name=\"author\" content=";
		data = data.substring(data.indexOf(tokenAuthor+"\"")+tokenAuthor.length()+1 , data.length());
		this.author = data.substring(0, data.indexOf("\""));
		
		//title
		String tokenTitle = "<meta property=\"og:title\" content=";
		data = data.substring(data.indexOf(tokenTitle+"\"")+tokenTitle.length()+1 , data.length());
		this.title =  data.substring(0, data.indexOf("\""));
		
		//content
		String tokenStarter = "<!--BeforeDocument";	//<!--BeforeDocument(219058,104933)-->
		String tokenFinilizer = "<!--AfterDocument";	//<!--AfterDocument(219058,104933)-->
		
		int start = data.indexOf(tokenStarter);
		int last = data.indexOf(tokenFinilizer);		
		this.content = data.substring(start, last);
	
		convertYoutubeUrl();
	}

	private void convertYoutubeUrl(){
		//loop while it meets youtube iframe. turn iframe into simple url link
		Pattern pYoutube = Pattern.compile("//www.youtube.com/embed/([^?^\"]*)");
		
		Pattern pIframe = Pattern.compile("<iframe(.*)//www.youtube.com/[^>]*(></iframe>)");
		Matcher mIframe = pIframe.matcher(content);
		if(mIframe.find()){
			String iframe = mIframe.group(0);
			Log.d("MHY", "catch iframe statement ++ " + iframe);
			
			Matcher mYoutube = pYoutube.matcher(iframe);
			if(mYoutube.find()){
				//http://www.youtube.com/watch?v=359na4NeaVA
				String href = "<p align=center><a href=\"http:" + mYoutube.group(0) + "\">"
						+ "<img src=\"http://img.youtube.com/vi/" + mYoutube.group(1) + "/1.jpg\"/>"
						+ "</br>watch this on Youtube!"
						+ "</a></p>";
//				Log.d("MHY", href);
				
				content = content.replace(iframe, href);
			}
		}
	}
	
}
