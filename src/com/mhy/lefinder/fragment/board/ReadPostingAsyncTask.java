package com.mhy.lefinder.fragment.board;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;

import com.mhy.lefinder.R;
import com.mhy.lefinder.fragment.board.ReadPostingAsyncTask.BoardParser;
import com.mhy.lefinder.fragment.search.Request;
import com.mhy.lefinder.fragment.search.SearchAsyncTask.Category;
import com.mhy.lefinder.fragment.search.SearchResultParser;
import com.mhy.lefinder.result.Result;
import com.mhy.lefinder.util.BaseAsyncTask;
import com.mhy.lefinder.util.LEFException;

public class ReadPostingAsyncTask extends BaseAsyncTask<Void, Void, BoardParser> {
	private String mUrl;
	private HttpClient mClient;
	private FgmtBoard mBoard;
	
	public ReadPostingAsyncTask(FgmtBoard board, Category category) {
		super(board.getActivity());
		mBoard = board;
		mClient = new DefaultHttpClient();
		
		if(category == Category.MV){
			mUrl = "http://www.hiphople.com/subtitle";
		}else{ //Category.Lyrics
			mUrl = "http://www.hiphople.com/lyrics";
		}
	}	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
				mBoard.setResults(null);
			}
		});
	}

	@Override
	protected BoardParser doInBackground(Void... arg0) {
		HttpEntity resEntity = null;
		try {
			if(isCancelled())
				return null;
			HttpGet get = new HttpGet(mUrl);
			HttpResponse response = mClient.execute(get);
			resEntity = response.getEntity(); 
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(resEntity != null && !(isCancelled())){
			try {
				String result = EntityUtils.toString(resEntity);
				
				int a = result.indexOf("<td class=\"title\">");
				int b = result.lastIndexOf("<td class=\"title\">");
				result = result.substring(a, b);
				
				BoardParser parser = new BoardParser(result, new Request("", null));	//passing null Request
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
	protected void onPostExecute(BoardParser parser) {
		super.onPostExecute(parser);
		
		if(parser!=null && !(isCancelled())){
			ArrayList<Result> results = parser.getParsedData();
			if(results.size()>0){
				mBoard.setResults(results);
			}else
				Toast.makeText(mAct, mAct.getResources().getString(R.string.noresult), Toast.LENGTH_SHORT).show();
		}else
			mBoard.setResults(null);
	}


	
	class BoardParser extends SearchResultParser{

		public BoardParser(String data, Request req) throws LEFException {
			super(data, req);
		}		
		
		@Override
		protected ArrayList<Result> parseData() throws LEFException {
			try{
				String tokenItem = "data-viewer";
				String tokenUrl_helper = "document_srl=";
				String tokenStyle = "<span style=";
				String tokenNext = "<td class=";
				String data = String.copyValueOf(getData().toCharArray());
				ArrayList<Result> result = new ArrayList<Result>();
				
				int index = data.indexOf(tokenItem);
				for(int i=0 ; i<5 ; i++){
					data = data.substring(index, data.length());
					data = data.substring(data.indexOf(tokenUrl_helper), data.length());

					String url = data.substring(tokenUrl_helper.length(), data.indexOf("&"));
									
//					//style-applied-title(bold, color, etc), remove them before parsing title
					int iStyle = data.indexOf(tokenStyle);
					if(iStyle != -1 && iStyle<100)
						data = data.substring(tokenStyle.length()+iStyle, data.length());
					
					//parsing title
					String title = data.substring(data.indexOf(">")+1, data.indexOf("<"));
					title = title.replaceAll("\t", "");
					title = title.replaceAll("amp;", "");
					title = title.replace("&quot;", "\"");
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

		@Override
		protected void setPageNumber(Request req) {	} //do nothing
	}
}
