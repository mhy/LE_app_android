package com.mhy.lefinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends Activity {
	AlertDialog.Builder adBuilder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		adBuilder = new AlertDialog.Builder(this);
		adBuilder.setTitle(R.string.alert_title);
		adBuilder.setCancelable(false);
		
		new CheckNetworkAsync().execute();
		
      }

	class CheckNetworkAsync extends AsyncTask<Void, Void, Integer>{


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			ConnectivityManager cMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
			
			NetworkInfo infoWifi = cMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo infoData = cMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			int scoreWifi = (infoWifi.isConnected() ? 1 : 0);
			int scoreData = (infoData.isConnected() ? 2 : 0);
			int scoreTotal = scoreWifi + scoreData;

			try {
				Thread.sleep(1900);	//sleep code been written just to meet HiphopLE's request 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return scoreTotal;
		}

		@Override
		protected void onPostExecute(Integer score) {
			MyAlertListener listener = new MyAlertListener();
			switch(score){
				case 0:	//there's no connection set up
					adBuilder.setMessage(getResources().getString(R.string.alert_no_connection));
					
					adBuilder.setNegativeButton(getResources().getString(R.string.no), listener);
					adBuilder.setNeutralButton(getResources().getString(R.string.mobiledata), listener);
					adBuilder.setPositiveButton(getResources().getString(R.string.wifi), listener);
					adBuilder.create().show();
					break;
				case 2:	//only Data (ask if the user doesn't mind running this app without wifi)
					adBuilder.setMessage(getResources().getString(R.string.alert_no_wifi));
					
					adBuilder.setNegativeButton(getResources().getString(R.string.no), listener);
					adBuilder.setPositiveButton(getResources().getString(R.string.yes), listener);
					adBuilder.create().show();
					break;
				case 1: // good to go
				case 3:
					startActivity(new Intent(getApplicationContext(), MainActivity.class));	// 액티비티 종료, 메인 메뉴 화면으로
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					finish();
					break;
			}
			
			super.onPostExecute(score);
		}

		class MyAlertListener implements DialogInterface.OnClickListener{
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));	// 액티비티 종료, 메인 메뉴 화면으로
				
				if(which == DialogInterface.BUTTON_POSITIVE)
						startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
				else if(which == DialogInterface.BUTTON_NEUTRAL)
					startActivity(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
				else
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				
				finish();
			}
		} 
	}//Async
}
