package com.mhy.lefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mhy.lefinder.util.BaseDialog;

public class AboutDialog extends BaseDialog {
	private Context mContext;
	
	public AboutDialog(Context context) {
		super(context, R.layout.dialog_about);
		mContext = context;
		
		final ImageButton closeBtn = (ImageButton)findViewById(R.id.btnClose);
		closeBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN :
					closeBtn.setBackgroundResource(R.drawable.close_clicked);
					break;
				case MotionEvent.ACTION_UP :
					closeBtn.setBackgroundResource(R.drawable.close_normal);
					dismiss();
					break;
			}
				return false;
			}
		});
		
		TextView tvAbout = (TextView) findViewById(R.id.tvAbout);
		tvAbout.setText(Html.fromHtml(readRawTextFile(R.raw.about)));
		Linkify.addLinks(tvAbout, Linkify.ALL);
		
		getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
	}
	
	public String readRawTextFile(int id) {
		InputStream inputStream = mContext.getResources().openRawResource(id);


		InputStreamReader in = new InputStreamReader(inputStream);
		BufferedReader buf = new BufferedReader(in);
		String line;


		StringBuilder text = new StringBuilder();
		try {
			while (( line = buf.readLine()) != null)
				text.append(line);
		}catch (IOException e) {
				return null;
		}finally{
			try {
				buf.close();
				in.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text.toString();
	}
}
