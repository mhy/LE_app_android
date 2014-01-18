package com.mhy.lefinder.util;

import com.mhy.lefinder.R;
import android.app.Activity;
import android.os.AsyncTask;

public class BaseAsyncTask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {
	protected Activity mAct;
	protected BaseDialog mDialog;
	
	public BaseAsyncTask(Activity act) {
		mAct = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = new BaseDialog(mAct, R.layout.dialog_progress);
		mDialog.setCancelable(false);
		mDialog.show();
	}

	@Override
	protected T3 doInBackground(T1... arg0) {
		
		return null;
	}

	@Override
	protected void onPostExecute(T3 result) {
		super.onPostExecute(result);
		if(mDialog.isShowing())
			mDialog.dismiss();
	}	

}
