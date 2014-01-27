package com.mhy.lefinder.fragment.search;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mhy.lefinder.R;
import com.mhy.lefinder.fragment.search.SearchAsyncTask.Category;

public class Request implements Parcelable{	
	private String key;
	private Category category;
	
	public Request(Activity act){
		setKey(act);
		setCategory(act);
	}
	
	public Request(String key, Category caterory){
		this.key = key;
		this.category = caterory;
	}
	
	public Request(Parcel in){
		key = in.readString();
		category = (Category) in.readSerializable();
	}
	
	public String getKey() {
		return key;
	}
	
	public Category getCategory() {
		return category;
	}
	
	private void setKey(Activity act){
		EditText etKey = (EditText)act.findViewById(R.id.etKeyword);
		key = etKey.getText().toString();
		key = key.replace(' ', '+');
	}
	
	private void setCategory(Activity act){
		 RadioGroup rbtnCategory = (RadioGroup)act.findViewById(R.id.rbtnCategory);
		 int selectedId = rbtnCategory.getCheckedRadioButtonId();
		 
		 switch(selectedId){
		 	case R.id.rbtnMv :
		 		category = Category.MV;
		 		break;
		 	case R.id.rbtnLyrics :
		 		category = Category.LYRICS;
		 		break;
		 }
		return;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeSerializable(category);		
	}
	
	public static final Parcelable.Creator<Request> CREATOR = new Creator<Request>() {
		
		@Override
		public Request[] newArray(int size) {
			return new Request[size];
		}
		
		@Override
		public Request createFromParcel(Parcel source) {
			return new Request(source);
		}
	};

}
