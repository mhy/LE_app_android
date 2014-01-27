package com.mhy.lefinder.fragment.search;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mhy.lefinder.R;

public class FgmtSearch extends Fragment {
	private SearchAsyncTask mSearchAsync;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		
		final ImageButton btnSubmit = (ImageButton)rootView.findViewById(R.id.btnSubmit);
		btnSubmit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN :
						btnSubmit.setBackgroundResource(R.drawable.search_clicked);
						break;
					case MotionEvent.ACTION_UP :
						btnSubmit.setBackgroundResource(R.drawable.search_normal);
						
						Request req = new Request(getActivity());
						if(!req.getKey().isEmpty()){
		 					mSearchAsync = new SearchAsyncTask(getActivity(), req);
							mSearchAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
						} else
							Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.nokeyword), Toast.LENGTH_SHORT).show();
						break;
				}
				return false;
			}
		});
				
		return rootView;		
	}
	
}
