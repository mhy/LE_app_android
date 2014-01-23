package com.mhy.lefinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhy.lefinder.fragment.board.FgmtBoard;
import com.mhy.lefinder.fragment.search.FgmtSearch;
import com.mhy.lefinder.fragment.search.SearchAsyncTask.Category;

public class FgmtPageAdapter extends FragmentPagerAdapter {

	FgmtSearch fgmtSearch;
	FgmtBoard fgmtSubtitleMv;
	FgmtBoard fgmtLyrics;
	
	public FgmtPageAdapter(FragmentManager fm) {
		super(fm);
		
		fgmtSearch = new FgmtSearch();
		
		fgmtSubtitleMv = new FgmtBoard();
		fgmtSubtitleMv.setArguments(setCategory(Category.MV));
		
		fgmtLyrics = new FgmtBoard();
		fgmtLyrics.setArguments(setCategory(Category.LYRICS));
	}

	@Override
	public Fragment getItem(int id) {
		switch(id){
			case 1 : 
				return fgmtSubtitleMv;
			case 2 : 
				return fgmtLyrics;
			default:
				return fgmtSearch;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	private Bundle setCategory(Category category){
		Bundle bundle = new Bundle();
		
		if(category==Category.LYRICS)
			bundle.putChar("category", 'l');
		else if(category==Category.MV)
			bundle.putChar("category", 'm');
		
		return bundle;
	}
}
