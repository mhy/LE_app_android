package com.mhy.lefinder.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
import com.mhy.lefinder.Request;
import com.mhy.lefinder.SearchAsyncTask.Category;
import com.mhy.lefinder.result.Result;

public class ResultParser {	
	private String mData;
	private ArrayList<Result> mParsedData;
	private int mLast;
	private String mDiv;
	
	public ResultParser(String data, Request req) throws LEFException{
		mData = data;
		mParsedData = parseData(req);
		setPageNumber(req);
	}
	
	public ArrayList<Result> getParsedData(){
		return mParsedData;
	}
	
	public ArrayList<Result> getParsedData(int pageNumber){
		return mParsedData;
	}
	
	public int getLastIndex(){
		return mLast;
	}
	
	public String getDivision(){
		return mDiv;
	}
	
	private ArrayList<Result> parseData(Request req) throws LEFException{
		try{
			String tokenItem = "data-viewer";
			String tokenUrl_helper = "document_srl=";
			String tokenStyle = "<span style=";
			String tokenNext = "<td class=";
			String data = String.copyValueOf(mData.toCharArray());
			ArrayList<Result> result = new ArrayList<Result>();
			
			int index = data.indexOf(tokenItem);
			while(index != -1){
				data = data.substring(index, data.length());
				data = data.substring(data.indexOf(tokenUrl_helper), data.length());

				String url = data.substring(tokenUrl_helper.length(), data.indexOf("&"));
								
//				//When it comes to style-applied-title(bold, color, etc), remove it before parsing title
				int iStyle = data.indexOf(tokenStyle);
				if(iStyle != -1 && iStyle<100)
					data = data.substring(tokenStyle.length()+iStyle, data.length());
				
				//parsing title
				String title = data.substring(data.indexOf(">")+1, data.indexOf("<"));
				title = title.replaceAll("\t", "");
				title = title.replaceAll("amp;", "");
				title = title.replaceAll("\n", "");
				
				result.add(new Result(title, url));

				data = data.substring(data.indexOf(tokenNext), data.length());
				index = data.indexOf(tokenItem);
			}
			
			return result;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new LEFException("error while parsing data");
		}
	}
	
	private void setPageNumber(Request req){
		switch(req.getCategory()){
			case MV :{
				String data = String.copyValueOf(mData.toCharArray());
				data = data.substring((data.length() * (2/3)) , data.length());
				
				String div = ";division=";
				int iDiv = data.indexOf(div);
				if(iDiv!=-1){
					String temp = data.substring(iDiv, iDiv+30);
					Pattern pattern = Pattern.compile("[^=]*.[0-9]");
					Matcher matcher = pattern.matcher(temp);
					
					if(matcher.find())
						mDiv = matcher.group();
				}
				
				String ggud = "끝 페이지\">";
				int iLast = data.indexOf(ggud)+ggud.length();
				if(iLast!=6){
					data = data.substring(iLast, data.length());
					mLast = Integer.parseInt(data.substring(0, data.indexOf("<")));
				}else
					mLast = 1;
				break;
			}
			case LYRICS :{
				String data = String.copyValueOf(mData.toCharArray());
				data = data.substring((data.length() * (1/3)) , data.length());
				
				String div = ";division=";
				int iDiv = data.indexOf(div);
				if(iDiv!=-1){
					String temp = data.substring(iDiv, iDiv+30);
					Pattern pattern = Pattern.compile("[^=]*.[0-9]");
					Matcher matcher = pattern.matcher(temp);
					
					if(matcher.find())
						mDiv = matcher.group();
				}
				
				String ggud = "계속";
				if(data.indexOf(ggud)==-1)
					mLast = 1;
				else
					mLast = 2;
				
				break;
			}
		}
	}

}
