package com.cba.payable;

import com.mpos.util.TypeFaceUtils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HDPayableFailure_dlg extends DialogFragment {

	private String alertTitle, alertText;
	private HDErrorCallBack callBack;
	private int callerId;

	public HDPayableFailure_dlg(String title, String msg) {
		super();
		this.alertText = msg;
		this.alertTitle = title;
		callBack = null;
		callerId = -1;
	}

	public HDPayableFailure_dlg(String alertTitle, String alertText,
			HDErrorCallBack callBack, int callerId) {
		super();
		this.alertTitle = alertTitle;
		this.alertText = alertText;
		this.callBack = callBack;
		this.callerId = callerId;
	}

	

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_failure);
		dialog.setCanceledOnTouchOutside(false);
		//dialog.setCancelable(false);
		
		dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                
                return true;
            }
        });

		ImageView imgMessage = (ImageView) dialog.findViewById(R.id.imgMessage);

		TextView txtMesage = (TextView) dialog.findViewById(R.id.txtMesage);
		txtMesage.setTypeface(TypeFaceUtils.setRobotoMedium(getActivity()));
		txtMesage.setText(alertText);

		Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
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

		/*Display display = getActivity().getWindowManager().getDefaultDisplay();

		WindowManager.LayoutParams lp;

		lp = new WindowManager.LayoutParams();
		lp.copyFrom(getActivity().getWindow().getAttributes());
		lp.width = (int) (display.getWidth() * 0.9);
		lp.height = (int) (display.getHeight() * 0.9);

		getActivity().getWindow().setAttributes(lp);*/

		return dialog;
	}

}
