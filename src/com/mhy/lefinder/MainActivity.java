package com.mhy.lefinder;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;
import com.mhy.lefinder.result.Result;
import com.mhy.lefinder.util.BaseDialog;

public class MainActivity extends FragmentActivity {

	public ArrayList<Result> results;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		
		final ImageButton btnSubmit = (ImageButton)findViewById(R.id.btnSubmit);
		btnSubmit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN :
						btnSubmit.setBackgroundResource(R.drawable.search_clicked);
						break;
					case MotionEvent.ACTION_UP :
						btnSubmit.setBackgroundResource(R.drawable.search_normal);
						
						Request req = new Request(MainActivity.this);
						if(!req.getKey().isEmpty())
		 					new SearchAsyncTask(MainActivity.this, req).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
						else
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.nokeyword), Toast.LENGTH_SHORT).show();
						break;
				}
				return false;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0,0,0,"About");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0 :
				AboutDialog aboutDialog = new AboutDialog(MainActivity.this);
				aboutDialog.show();
				break;
			default :
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
