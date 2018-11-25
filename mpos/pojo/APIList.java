package com.mpos.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class APIList<T extends APIData> extends APIData implements
		Parcelable {

	protected List<T> listObj = null;

	protected abstract ClassLoader onGetClassLoader();

	public APIList() {

	}

	public APIList(Parcel in) {
		if (in != null) {
			List<T> values = new ArrayList<T>();
			in.readList(values, onGetClassLoader());
			if (values != null && values.size() > 0) {
				listObj = values;
			}
		}
	}

	public int getSize() {
		if (listObj == null) {
			return 0;
		}
		return listObj.size();
	}

	public T getData(int index) {
		if (listObj == null) {
			return null;
		}

		if (index < 0 || index >= listObj.size()) {
			return null;
		}

		return listObj.get(index);
	}

	public void addData(T elem) {

		if (listObj == null) {
			listObj = new ArrayList<T>();
		}
		listObj.add(elem);

	}
	
	abstract public void setList(String json) ;
	
	public String getJson() {
		
		if (listObj == null){
			return null ;
		}
		
		final GsonBuilder builder = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation();
		;
		final Gson gson = builder.create();
		//

		return gson.toJson(listObj);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(listObj);

	}

}
