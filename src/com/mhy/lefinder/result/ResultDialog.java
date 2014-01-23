package com.mhy.lefinder.result;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mhy.lefinder.R;
import com.mhy.lefinder.fragment.search.Request;
import com.mhy.lefinder.fragment.search.SearchAsyncTask;
import com.mhy.lefinder.fragment.search.SearchResultParser;
import com.mhy.lefinder.webview.ViewerActivity;

public class ResultDialog extends DialogFragment {
	private ArrayList<Result> mResult;
	private FragmentActivity mActivity;
	private Dialog mDialog;
	private int mLastIndex;
	private static int mCurrentIndex;
	private String mDivision;
	private Request mRequest;
	private ResultAdapter mAdapter;
		
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Bundle args = getArguments();
		
		mActivity = (FragmentActivity) getActivity();
		mResult = args.getParcelableArrayList("result");
		mAdapter = new ResultAdapter();
		
		mRequest = args.getParcelable("request");
		mLastIndex = args.getInt("lastpage");
		mCurrentIndex = 1;
		mDivision = args.getString("division");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog =  super.onCreateDialog(savedInstanceState);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_result);
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		setDialogHeight();
		
		ListView lvResult = (ListView)mDialog.findViewById(R.id.listResult);
		lvResult.setAdapter(mAdapter);
		lvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent();
				
				i.setClass(mActivity, ViewerActivity.class);
				i.putExtra("result", mResult.get(position));
				i.putExtra("request", mRequest);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				
				startActivity(i);
			}
		});
		
		
		final ImageButton btnClose = (ImageButton)mDialog.findViewById(R.id.btnClose);
		btnClose.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN :
						btnClose.setBackgroundResource(R.drawable.close_clicked);
						break;
					case MotionEvent.ACTION_UP :
						btnClose.setBackgroundResource(R.drawable.close_normal);
						mDialog.dismiss();
						break;
				}
				return false;
			}
		});
		
		refreshButton();
		
		return mDialog;
	}
	
	private void refreshButton(){
		ImageButton btnPrev = (ImageButton)mDialog.findViewById(R.id.btnPrev);
		ImageButton btnNext = (ImageButton)mDialog.findViewById(R.id.btnNext);
		
		if(mCurrentIndex==1)
			btnPrev.setVisibility(Button.INVISIBLE);
		else
			btnPrev.setVisibility(Button.VISIBLE);
		
		if(mCurrentIndex==mLastIndex)
			btnNext.setVisibility(Button.INVISIBLE);
		else
			btnNext.setVisibility(Button.VISIBLE);
			
		NavBtnClickListner btnListner = new NavBtnClickListner();
		btnPrev.setOnTouchListener(btnListner);
		btnNext.setOnTouchListener(btnListner);
	}
	
	private void setDialogHeight(){
		Point res = new Point();
		mActivity.getWindowManager().getDefaultDisplay().getSize(res);
		
		int x = (int) (res.x * 0.9);		
		int y =  (mResult.size()<8 ? LayoutParams.WRAP_CONTENT : (int)(res.y * 0.9));	//a better height value for good-looking dialog
		
		mDialog.getWindow().setLayout(x, y);
	}
	
	class NavBtnClickListner implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int btnImg=0;
			int btnId = v.getId();
			switch(event.getAction()){
				case MotionEvent.ACTION_UP :
					if(btnId==R.id.btnNext){
						mCurrentIndex++;
						btnImg = R.drawable.next_normal;
					}else{
						mCurrentIndex--;
						btnImg = R.drawable.prev_normal;
					}
					new SearchOtherPageAsyncTask(mCurrentIndex, mDivision).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
					break;
				case MotionEvent.ACTION_DOWN :
					if(btnId==R.id.btnNext)
						btnImg = R.drawable.next_clicked;
					else
						btnImg = R.drawable.prev_clicked;
					break;
			}
			
			if(btnImg!=0){
				ImageButton btnSelected = (ImageButton)mDialog.findViewById(btnId);
				btnSelected.setBackgroundResource(btnImg);
			}
			
			return false;
		}
	}

	class ResultAdapter extends BaseAdapter{
		LayoutInflater mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		@Override
		public int getCount() {
			return mResult.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mResult.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null)
				convertView = mInflater.inflate(R.layout.item_result, parent, false);
			
			TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			tvTitle.setText(mResult.get(position).getTitle());
			
			return convertView;
		}
		
	}
	
	class SearchOtherPageAsyncTask extends SearchAsyncTask {
		private String ACTION; // = "&division=" + mDivision + "&last_division=0&page="; 
//		private int mPage;
		
		public SearchOtherPageAsyncTask(int pageNumber, String division) {
			super(mActivity, mRequest);
			switch(mRequest.getCategory()){
				case LYRICS :
					if(pageNumber==1)
						ACTION = "";
					else
						ACTION = "&division=" + division + "&last_division=0&page=" + (pageNumber - 1);
					break;
				default :
					ACTION = "&division=" + division + "&last_division=0&page=" + pageNumber;
					break;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionUrl = getActionUrl(mRequest) + ACTION; // + mPage;
		}
		
		@Override
		protected void onPostExecute(SearchResultParser parser) {
			super.onPostExecute(parser);
			if(mResults.size()>0){
				mResult = mResults;
			}
			
			setDialogHeight();
		}

		@Override
		protected void showResult(Bundle args) {
			mAdapter.notifyDataSetChanged();

			ListView lvResult = (ListView)ResultDialog.this.mDialog.findViewById(R.id.listResult);
			lvResult.setSelectionAfterHeaderView();
			
			refreshButton();
		}
	}
}
