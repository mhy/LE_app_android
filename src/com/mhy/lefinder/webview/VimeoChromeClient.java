package com.mhy.lefinder.webview;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import com.mhy.lefinder.R;

public class VimeoChromeClient extends WebChromeClient {
	private Activity mAct;
	private FrameLayout mFrame;
	
	private final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
    		new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	private CustomViewCallback mCallback;
	
	public VimeoChromeClient(Activity act) {
		mAct = act;
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		Log.d("MHY","show");
		mCallback = callback;
		super.onShowCustomView(view, callback);
		mFrame = new FrameLayout(mAct);
		mFrame.setLayoutParams(COVER_SCREEN_PARAMS);
		mFrame.setBackgroundColor(Color.BLACK);
		mFrame.addView(view);
		
		WebView webview = (WebView) mAct.findViewById(R.id.wbPageOriginal);
		webview.setVisibility(View.GONE);
		
		FrameLayout mainFrame = (FrameLayout) mAct.findViewById(R.id.frmWebViewer);
		mainFrame.addView(mFrame);
		
	}

	public void hideCustomView(){
		if(mCallback!=null)
			mCallback.onCustomViewHidden();
	}
	
}
