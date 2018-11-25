package com.cba.payable;



import com.mpos.util.TypeFaceUtils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HDPayableSuccess_dlg extends DialogFragment {

	private String alertTitle, alertText;
	private HDErrorCallBack callBack;
	private int callerId;

	public HDPayableSuccess_dlg(String title, String msg) {
		super();
		this.alertText = msg;
		this.alertTitle = title;
		callBack = null;
		callerId = -1;
	}

	public HDPayableSuccess_dlg(String alertTitle, String alertText, HDErrorCallBack callBack, int callerId) {
		super();
		this.alertTitle = alertTitle;
		this.alertText = alertText;
		this.callBack = callBack;
		this.callerId = callerId;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		final Dialog dialog =new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_success);
		dialog.setCanceledOnTouchOutside(false);
		//dialog.setCancelable(false);
		
		dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                
                return true;
            }
        });
		
		ImageView imgMessage =(ImageView)dialog.findViewById(R.id.imgMessage);
		
		TextView txtMesage =(TextView)dialog.findViewById(R.id.txtMesage);
		txtMesage.setTypeface(TypeFaceUtils.setRobotoMedium(getActivity()));
		txtMesage.setText(alertText);
		
		Button btnOk =(Button)dialog.findViewById(R.id.btnOk);
		btnOk.setTypeface(TypeFaceUtils.setLatoRegular(getActivity()));
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (callBack != null) {
					callBack.onMSGDlgBtnClick(callerId);
				}

				dialog.dismiss();

			}
		});
	
		
		return dialog;
	}
	
	public void onBackPressed() {

	}

}
