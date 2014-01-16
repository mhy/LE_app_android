package com.mhy.lefinder.webview;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.MediaController;
import com.mhy.lefinder.R;

public class ViewerChromeClient extends WebChromeClient{
	private Activity mActivity;
    
    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
    		new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public ViewerChromeClient(Activity act){
    	mActivity = act;
    }
    
    
    private View mProgress;
	@Override
	public View getVideoLoadingProgressView() {
		Log.d("MHY", "getVideoLoadingProgressView");
		if(mProgress==null){
			LayoutInflater inf = LayoutInflater.from(mActivity);
			mProgress = inf.inflate(R.layout.dialog_progress, null);
		}
			
		return mProgress;
	}

	@Override
	public void onHideCustomView() {
		Log.d("MHY", "onHideCustomView");
		
		myFrm.setVisibility(View.GONE);
		
		WebView wV = (WebView)mActivity.findViewById(R.id.wbPageOriginal);
		wV.setVisibility(View.VISIBLE);
		
		mCallBack.onCustomViewHidden();
//		mActivity.finish();
		
		FrameLayout frmV = (FrameLayout) mActivity.findViewById(R.id.frmWebViewer);
		if(myFrm!=null)
			frmV.removeView(myFrm);
	}
	
	private MediaPlayer mp;
	private FrameLayout myFrm;
	private WebChromeClient.CustomViewCallback mCallBack;
	@Override
	public void onShowCustomView(final View view, CustomViewCallback callback) {
		Log.d("MHY", "onShowCustomView 2");
		mCallBack = callback;
		
		FrameLayout frmV = (FrameLayout) mActivity.findViewById(R.id.frmWebViewer);

		myFrm = new FrameLayout(mActivity);
		myFrm.setLayoutParams(COVER_SCREEN_PARAMS);
		myFrm.setBackgroundColor(Color.BLACK);
		myFrm.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				FrameLayout frm = (FrameLayout)v;
				FrameLayout frm2 = (FrameLayout)frm.getChildAt(0);
				SurfaceView sv = (SurfaceView)frm2.getChildAt(0);
				
				try{
					mc = getSurfaceView(view);
//					vv.setOnTouchListener(new OnTouchListener() {
//						
//						@Override
//						public boolean onTouch(View v, MotionEvent event) {
//							Log.d("MHY", "onTouch 2");
//							return false;
//						}
//					});
				}catch(Exception e){	}
				
//				FrameLayout fl = (FrameLayout)vv;
				if(mc.isShowing()){
					Log.d("MHY", "isShowing " + event.getAction() );
					
//					mp.start();
					
					if(event.getAction() == MotionEvent.ACTION_UP){
						new DelayedPlay().execute();
					}else if (event.getAction() == MotionEvent.ACTION_DOWN){
						mp.start();
					}
					return true;
				}
				return false;
			}
		});
		myFrm.addView(view);
		
		frmV.addView(myFrm);

		WebView wV = (WebView)mActivity.findViewById(R.id.wbPageOriginal);
		wV.setVisibility(View.GONE);
		
		try
        {
            @SuppressWarnings("rawtypes")
            Class _VideoSurfaceView_Class_ = Class.forName("android.webkit.HTML5VideoFullScreen$VideoSurfaceView");
            java.lang.reflect.Field _HTML5VideoFullScreen_Field_ = _VideoSurfaceView_Class_.getDeclaredField("this$0");
            _HTML5VideoFullScreen_Field_.setAccessible(true);
            Object _HTML5VideoFullScreen_Instance_ = _HTML5VideoFullScreen_Field_.get(((FrameLayout) view).getFocusedChild());
            
            @SuppressWarnings("rawtypes")
            Class _HTML5VideoView_Class_ = _HTML5VideoFullScreen_Field_.getType().getSuperclass();
            java.lang.reflect.Field _mPlayer_Field_ = _HTML5VideoView_Class_.getDeclaredField("mPlayer");
            _mPlayer_Field_.setAccessible(true);
            mp =  (MediaPlayer) _mPlayer_Field_.get(_HTML5VideoFullScreen_Instance_);

     		Log.d("MHY", "onShowCustomView MP "+mp);
            
        }
        catch (Exception ex) { }
		
//		try
//		{
//			@SuppressWarnings("rawtypes")
//	        Class _VideoSurfaceView_Class_ = Class.forName("android.webkit.HTML5VideoFullScreen$VideoSurfaceView");
//	        java.lang.reflect.Field _HTML5VideoFullScreen_Field_ = _VideoSurfaceView_Class_.getDeclaredField("this$0");
//	        _HTML5VideoFullScreen_Field_.setAccessible(true);
//	        Object _HTML5VideoFullScreen_Instance_ = _HTML5VideoFullScreen_Field_.get(((FrameLayout) view).getFocusedChild());
//	        
//	        @SuppressWarnings("rawtypes")
//	        Class _HTML5VideoView_Class_ = _HTML5VideoFullScreen_Field_.getType(); //getType().getSuperclass();
//			java.lang.reflect.Field _mVsv_Field_ = _HTML5VideoView_Class_.getDeclaredField("mVideoSurfaceView");
//			_mVsv_Field_.setAccessible(true);
//			View sv = (View)_mVsv_Field_.get(_HTML5VideoFullScreen_Instance_);
//			sv.setOnTouchListener(new OnTouchListener() {
//					
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						Log.d("MHY", "onTouch 2");
//					return false;
//				}
//			});
//		}catch(Exception ex2){
//			Log.e("MHY","ERR");
//		}		

		
		
//		super.onShowCustomView(view, callback);
	}
	
	MediaController mc;
	public MediaController getSurfaceView(View view) throws Exception{
		try{
			@SuppressWarnings("rawtypes")
            Class _VideoSurfaceView_Class_ = Class.forName("android.webkit.HTML5VideoFullScreen$VideoSurfaceView");
            java.lang.reflect.Field _HTML5VideoFullScreen_Field_ = _VideoSurfaceView_Class_.getDeclaredField("this$0");
            _HTML5VideoFullScreen_Field_.setAccessible(true);
            Object _HTML5VideoFullScreen_Instance_ = _HTML5VideoFullScreen_Field_.get(((FrameLayout) view).getFocusedChild());
			
            @SuppressWarnings("rawtypes")
            Class _HTML5VideoView_Class_ = _HTML5VideoFullScreen_Field_.getType();//.getSuperclass();
            java.lang.reflect.Field _mPlayer_Field_ = _HTML5VideoView_Class_.getDeclaredField("mMediaController");//("mVideoSurfaceView");
			_mPlayer_Field_.setAccessible(true);
			MediaController mc =  (MediaController) _mPlayer_Field_.get(_HTML5VideoFullScreen_Instance_);
			return mc;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	class DelayedPlay extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(50);
				Log.w("MHY", mp.isPlaying()+"");
				while(!mp.isPlaying()){
					Log.w("MHY", mp.isPlaying()+" start");
					Thread.sleep(50);
					mp.start();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				return null;
		}
	}
	
}
