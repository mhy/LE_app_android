package com.mhy.lefinder.result;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable{
	
	private String title;
	private String url;
	
	public Result(String title, String url){
		this.title = title;
		this.url = url;
	}
	
	public Result(Parcel in){
		this.title = in.readString();
		this.url = in.readString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(url);
	}
	
	public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {

		@Override
		public Result createFromParcel(Parcel source) {
			return new Result(source);
		}

		@Override
		public Result[] newArray(int size) {
			return new Result[size];
		}
	}; 
	

}
