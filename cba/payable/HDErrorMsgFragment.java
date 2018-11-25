package com.cba.payable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

@SuppressLint("ValidFragment")
public class HDErrorMsgFragment extends DialogFragment {
	private String alertText , alertTitle ;
	private int callerId ;
	private HDErrorCallBack callBack ;
	
	HDErrorMsgFragment(String title, String msg ){
		super();
		this.alertText=msg;
		alertTitle = title ;
		callBack = null ;
		callerId = -1 ;
	}
	
	HDErrorMsgFragment(String title, String msg , HDErrorCallBack cb , int id){
		super();
		this.alertText=msg;
		alertTitle = title ;
		callBack = cb ;
		callerId = id ;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setMessage(alertText)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //Action for ok button
                	   
                	   if(callBack != null){
                		   callBack.onMSGDlgBtnClick(callerId) ;
                	   }
                   }
               });
        if(alertTitle != null){
        	builder.setTitle(alertTitle) ;
        }
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
