package com.cba.payable;

import java.util.ArrayList;

import com.mpos.connection.PaginationHandler;
import com.mpos.pojo.APIData;
import com.mpos.pojo.APIList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public abstract class MposPageAdapter<T extends APIData> extends BaseAdapter {

	public static final int ROW_LOADING = 0;
	public static final int ROW_DATA = 1;
	public static final int ROW_ERROR = 2;

	protected ArrayList<T> arrItems = null;
	private boolean isLoading = false;
	private boolean isDownloadable = true;
	private boolean isLoadingError = false;

	protected Context context ;
	private PaginationHandler pageHandler;
	private int pageSize;
	private int callerId ;
	
	//private int logVal = 0 ;

	public MposPageAdapter(Context c ,PaginationHandler ph, int pageSize , int cId) {
		context = c ;
		pageHandler = ph;
		this.pageSize = pageSize;
		callerId = cId ;
	}

	public void onError() {
		//logVal = 100 + ((logVal / 100) * 100) ;
		//Log.i("JEYLOGS", "Log value: " + logVal) ;
		isLoading = false;
		isLoadingError = true;
		notifyDataSetChanged();
	}
	
	public void onEmtyRecord(){
		isDownloadable = false;
		isLoading = false;
		notifyDataSetChanged() ;
	}

	@Override
	public int getCount() {

		int res = 0;

		if (arrItems != null) {
			res = arrItems.size();
		}

		if (isDownloadable) {
			res += 1;
		}

		return res;
	}

	public int getRowType(int pos) {

		if (pos == getCount() - 1) {
			if (isDownloadable) {

				if (isLoadingError) {
					return ROW_ERROR;
				}

				return ROW_LOADING;
			}

		}

		return ROW_DATA;

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		//logVal = logVal + 1 ;
		//Log.i("JEYLOGS", "Log value: " + logVal) ;
		
		//Log.i("JEYLOGS", "Row Type value: " + getRowType(position)) ;

		switch (getRowType(position)) {

		case ROW_ERROR: {
			
			//Log.i("JEYLOGS", "Constructing row error") ;
			
			if(convertView == null ){
				convertView = renderErrorRow(parent) ;
				convertView.setTag(new Integer(ROW_ERROR)) ;
				break ;
			}
			
			int tag = ((Integer)convertView.getTag()).intValue() ;
			
			if(tag != ROW_ERROR){
				convertView = renderErrorRow(parent) ;
				convertView.setTag(new Integer(ROW_ERROR)) ;
				
			}
			
			break ;

		}

		case ROW_LOADING: {
			
			//Log.i("JEYLOGS", "Constructing row loading") ;
			
			if(convertView == null ){
				convertView = renderLoadingRow(parent) ;
				convertView.setTag(new Integer(ROW_LOADING)) ;
			}else{
				int tag = ((Integer)convertView.getTag()).intValue() ;
				
				if(tag != ROW_LOADING){
					convertView = renderLoadingRow(parent) ;
					convertView.setTag(new Integer(ROW_LOADING)) ;
				}
				
			}
			
			if(! isLoading){
				if(pageHandler != null){
					int recs = 0;
					
					if(arrItems != null){
						recs = arrItems.size() ;
					}
					
					pageHandler.loadData(recs/pageSize, pageSize, callerId) ;
					isLoading = true ;
					
				}
			}
			
			break ;

			
		}
		
		default :{
			convertView  = renderListRow(getRecord(position),position,convertView,parent);
		}

		}

		return convertView;
	}
	
	private View renderErrorRow(ViewGroup parent){
		View v = LayoutInflater.from(context).inflate(R.layout.lstrowerror, parent, false);
		Button btn = (Button)v.findViewById(R.id.btnError);
		btn.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
              isLoadingError = false;
              notifyDataSetChanged();              
            }
          });  
		return v ;
	}
	
	private View renderLoadingRow(ViewGroup parent){
		View v = LayoutInflater.from(context).inflate(R.layout.lstrowloader, parent, false);
		
		return v ;
	}

	abstract protected View renderListRow(T data, int position,
			View convertView, ViewGroup parent);
	
	public T getRecord(int pos){
		
		if(arrItems == null){
			return null ;
		}
		
		if(arrItems.size() > pos){
			return arrItems.get(pos) ;
		}
		
		return null ;
	}

	public void insertRecords(APIList<T> rec) {

		if (isLoading) {
			if (arrItems == null) {
				arrItems = new ArrayList<T>();
			}

			for (int i = 0; i < rec.getSize(); i++) {
				arrItems.add(rec.getData(i));
			}

			if (pageSize > rec.getSize()) {
				isDownloadable = false;
			}

			isLoading = false;
			notifyDataSetChanged() ;
		}

	}

}
