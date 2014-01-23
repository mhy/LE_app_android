package com.mhy.lefinder.fragment.board;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mhy.lefinder.MainActivity;
import com.mhy.lefinder.R;
import com.mhy.lefinder.fragment.search.Request;
import com.mhy.lefinder.fragment.search.SearchAsyncTask.Category;
import com.mhy.lefinder.result.Result;
import com.mhy.lefinder.webview.ViewerActivity;

public class FgmtBoard extends Fragment {

	private ReadPostingAsyncTask mReadPostAsync;
	private Category mCategory;
	private ArrayList<Result> mResults;
	private BoardAdapter mAdaptor;
	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_board, container, false);
		
		char keyCategory = getArguments().getChar("category");
		switch(keyCategory){
			case MainActivity.CATEGORY_MV :
				mCategory = Category.MV;
				break;
			case MainActivity.CATEGORY_LYRICS :
				mCategory = Category.LYRICS;
				break;
		}

		Button btnRefresh = (Button) mRootView.findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mReadPostAsync = new ReadPostingAsyncTask(FgmtBoard.this, mCategory);
				mReadPostAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
			}
		});
		
		mAdaptor = new BoardAdapter();
		mReadPostAsync = new ReadPostingAsyncTask(this, mCategory);
		
		return mRootView;
	}

	public void setResults(ArrayList<Result> results){
		String currentDateandTime = SimpleDateFormat.getTimeInstance().format(new Date());
		TextView tvNoti = (TextView) mRootView.findViewById(R.id.tvBoardNoti);
		
		if(results != null){
			mResults = results;
			
			ListView lvBoard = (ListView) mRootView.findViewById(R.id.listBoard);
			lvBoard.setAdapter(mAdaptor);
			lvBoard.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					Intent i = new Intent();
					
					i.setClass(getActivity(), ViewerActivity.class);
					i.putExtra("result", mResults.get(pos));
					i.putExtra("request", new Request("", mCategory));
					
					startActivity(i);				
				}
			});
	
			tvNoti.setText(getActivity().getString(R.string.board_sync_success) + " : " + currentDateandTime);
			
			mAdaptor.notifyDataSetChanged();
		}else{
			tvNoti.setText(getActivity().getString(R.string.board_sync_fail));	
		}
			
	}
	
	public void readPosting(){
		Status status = mReadPostAsync.getStatus();
		if(status != Status.RUNNING && status != Status.FINISHED)
			mReadPostAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
	}
	
	
	private class BoardAdapter extends BaseAdapter{
		LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		@Override
		public int getCount() {
			return mResults.size();
		}

		@Override
		public Object getItem(int position) {
			return mResults.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null)
				convertView = mInflater.inflate(R.layout.item_result, parent, false);
			
			TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			tvTitle.setText(mResults.get(position).getTitle());
			
			return convertView;
		}
		
	}
}
