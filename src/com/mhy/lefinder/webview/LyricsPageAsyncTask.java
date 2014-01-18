package com.mhy.lefinder.webview;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mhy.lefinder.R;
import com.mhy.lefinder.Request;
import com.mhy.lefinder.SearchAsyncTask.Category;
import com.mhy.lefinder.result.Result;
import com.mhy.lefinder.util.BaseAsyncTask;


/**
 * write Lyrics content on the custom viewer
 * @author mhy
 *
 */
public class LyricsPageAsyncTask extends BaseAsyncTask<String, Void, Article> {
	private final String FILENAME = "templyrics.html";
	private WebView webview;
	private HttpClient mClient;

	public LyricsPageAsyncTask(Activity act) {
		super(act);
		webview = (WebView) mAct.findViewById(R.id.wbPage);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mClient = new DefaultHttpClient();
	}

	@Override
	protected Article doInBackground(String... arg0) {
		HttpEntity resEntity = null;
		try {
			HttpGet get = new HttpGet(arg0[0]);
			HttpResponse response = mClient.execute(get);
			resEntity = response.getEntity();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (resEntity != null) {
			try {
				Article article = new Article(EntityUtils.toString(resEntity));
				writeHtmlPage(article);
				return article;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}

	@Override
	protected void onPostExecute(Article result) {
		super.onPostExecute(result);

		if (result != null) {
			TextView tvTitle = (TextView) mAct.findViewById(R.id.tvTitle_lyrics);
			TextView tvAuthor = (TextView) mAct.findViewById(R.id.tvAuthor_lyrics);

			tvTitle.setText(result.getTitle());
			tvAuthor.setText("by " + result.getAuthor());
			
			//put marquee effect on Title
			tvTitle.setSelected(true);
			tvTitle.setMarqueeRepeatLimit(-1);
			tvTitle.setSingleLine();
			tvTitle.setEllipsize(TruncateAt.MARQUEE);
			setMarquee(tvTitle);

			webview.getSettings().setRenderPriority(RenderPriority.HIGH);
			webview.getSettings().setDefaultTextEncodingName("utf-8");
			webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // for scaling huge-imgs on webview
			
			webview.getSettings().setPluginState(PluginState.ON);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setBuiltInZoomControls(true);
//	        webview.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.36 (KHTML, like Gecko) Chrome/13.0.766.0 Safari/534.36");
			
			webview.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					try{
						if (url.contains("hiphople.com/lyrics/")) { // case : lyrics of LE(전곡모음 핸들링용) //TODO url이 다음과 같지 않은 경우도있더라... (e.g.Yeezus 전곡모음) 이건 통일시켜달라고 건의해야 할듯
							url = url.substring(url.indexOf("lyrics/")+"lyrics/".length(), url.length());
							
							Request req = new Request("", Category.LYRICS);
							Result	res = new Result("", url);
							
							Intent i = new Intent();
							i.setClass(mAct, ViewerActivity.class);
							i.putExtra("request", req);
							i.putExtra("result", res);
							mAct.startActivity(i);
							
//							new LyricsPageAsyncTask(mAct).execute(url);
						} else { // case 2 : any url other than LE's lyrics  //content(참고자료용으로 네이버백과사전 링크를 걸어놨다든지), 2013.09.17 Article에서 youtube link를 핸들링함으로써 유투브 동영상url도 가능해짐.
							Intent browserI = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							mAct.startActivity(browserI);
						}
					}catch(Exception e){
						Log.e("MHY", "error on shouldOverrideUrlLoading");
					}
					
					return true;
				}
			});
			
			webview.loadUrl("file://" + mAct.getFilesDir() + "/" + FILENAME);
		}
	}

	private void writeHtmlPage(Article article) {
		// TODO should make background watermarked
		File dir = new File(mAct.getFilesDir().toString());
		if (!dir.exists())
			dir.mkdir();

		try {
			File htmlfile = new File(dir, FILENAME);
			htmlfile.createNewFile();
			FileWriter fw = new FileWriter(htmlfile);
			BufferedWriter bf = new BufferedWriter(fw);
			bf.write(article.getContent());
			bf.close();
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setMarquee(TextView tv){
		try {
	        Field f = tv.getClass().getDeclaredField("mMarquee");
	        f.setAccessible(true);
	        Object marquee = f.get(tv);
	        if (marquee != null) {
	            Field mf = marquee.getClass().getDeclaredField("mScrollUnit");
	            mf.setAccessible(true);
	            
	            float newSpeed = 8f;
	            mf.setFloat(marquee, newSpeed);
	        }
	    } catch (Exception e) {
	        // ignore, not implemented in current API level
	    }
	}
}
