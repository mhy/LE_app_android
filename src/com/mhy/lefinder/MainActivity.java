package com.mhy.lefinder;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.mhy.lefinder.fragment.FgmtPageAdapter;
import com.mhy.lefinder.fragment.board.FgmtBoard;
import com.mhy.lefinder.result.Result;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{
	public ArrayList<Result> results;
	
	public final float NUMBER_TABS = 3f;
	public final static char CATEGORY_MV = 'm';
	public final static char CATEGORY_LYRICS = 'l';
	
	private ActionBar mActionBar;
	private ViewPager mPager;
	private FgmtPageAdapter mAdapter;
	private TabStripView mStrip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//width of current screen to set up strip tab-indicator
		DisplayMetrics currMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(currMetrics);
		final float widthTab = (currMetrics.widthPixels / NUMBER_TABS) + 1f;	//+1f because it makes a better appearance
		
		//set action bar and tab w/ interactive strip tab-indicator
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//initialize tab strip
		mStrip = (TabStripView) findViewById(R.id.tabStrip);
		mStrip.setStrip(0, widthTab);
		
		mAdapter = new FgmtPageAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.viewPager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(3);	//prevent fragments from being detached when switching tabs 
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);	
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //moving strip tab-indicator
				float x1 = (position * widthTab) + (positionOffset * widthTab);
                float x2 = x1 + widthTab;
                mStrip.setStrip(x1, x2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		
		//creating tabs
		for (int i = 0; i < mAdapter.getCount(); i++) {
            mActionBar.addTab(
					mActionBar.newTab()
                            .setIcon(R.drawable.close_normal)	//TODO TEST : putting custom icon
                            .setTabListener(this));
        }
		
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

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) { }
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) { }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		int pos = tab.getPosition();
		mPager.setCurrentItem(pos);
		switch(pos){
			case 0:
				mActionBar.setTitle(R.string.actionbar_title_search);
				break;
			case 1:
				mActionBar.setTitle(R.string.actionbar_title_subtitle);
				FgmtBoard fgmtSubtitle = (FgmtBoard) mAdapter.getItem(pos);
				fgmtSubtitle.readPosting();
				break;
			case 2:
				mActionBar.setTitle(R.string.actionbar_title_lyrics);
				FgmtBoard fgmtLyrics = (FgmtBoard) mAdapter.getItem(pos);
				fgmtLyrics.readPosting();
				break;
		}
	}	
}
