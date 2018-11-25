package com.cba.payable;


import com.mpos.pojo.APIData;
import com.mpos.pojo.TxVoidRes;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VoidReceiptActivity extends GenericActivity {

	TextView txtTitle, txtVoidComplete, txtlike;
	Button btnEmail, btnText, btnNoReceipt;

	LangPrefs langPrefs;
	Sinhala_LangSelect sinlang_select;
	Tamil_LangSelect tamlang_select;

	int lang_status = 0;
	
	private RecSale m_recSale;
	private TxVoidRes m_voidRes;

	@Override
	protected void onInitActivity(APIData... data) {
		setContentView(R.layout.activity_void_receipt);
		
		if (data != null){
			for (int i = 0; i < data.length; i++){
				
				if(data[i] instanceof RecSale){
					m_recSale = (RecSale) data[i] ;
				} 
				
				if(data[i] instanceof TxVoidRes){
					m_voidRes = (TxVoidRes) data[i] ;
				} 
			}
		}

		InitViews();
	}

	private void InitViews() {

		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

		txtVoidComplete = (TextView) findViewById(R.id.txtVoidComplete);
		txtVoidComplete.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtlike = (TextView) findViewById(R.id.txtlike);
		txtlike.setTypeface(TypeFaceUtils.setLatoLight(getApplicationContext()));

		btnEmail = (Button) findViewById(R.id.btnEmail);
		btnEmail.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));
		btnEmail.setOnClickListener(onClickListener);

		btnText = (Button) findViewById(R.id.btnText);
		btnText.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));
		btnText.setOnClickListener(onClickListener);

		btnNoReceipt = (Button) findViewById(R.id.btnNoReceipt);
		btnNoReceipt.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));
		btnNoReceipt.setOnClickListener(onClickListener);

		sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

		tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

		lang_status = LangPrefs.getLanguage(getApplicationContext());

		if (lang_status == LangPrefs.LAN_EN) {

		}

		else if (lang_status == LangPrefs.LAN_SIN) {
			sinlang_select.Apply_VoidReceipt(txtTitle, txtVoidComplete, txtlike, btnEmail, btnText, btnNoReceipt);
		} else if (lang_status == LangPrefs.LAN_TA) {
			tamlang_select.Apply_VoidReceipt(txtTitle, txtVoidComplete, txtlike, btnEmail, btnText, btnNoReceipt);
		} else {
			// do nothing
		}

		Config.RECEIPT_STATE = Config.RECEIPT_STATE_NORMAL;

	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btnText: {
				onSMS() ;
				break;
			}

			case R.id.btnEmail: {
				onEmail() ;
				break;
			}
			
			case R.id.btnNoReceipt: {
				gotoHome() ;
				break;
			}

			}
		}
	};
	
	public void onBackPressed(){
		
	}
	
	public void onNoReceipt(){
		gotoHome();
	}
	
	public void onEmail(){
		if(m_recSale == null || m_voidRes == null){
			return ;
		}
		this.pushActivity(SendEmailActivity.class,  m_recSale,m_voidRes);
	}
	
	public void onSMS(){
		
		if(m_recSale == null || m_voidRes == null){
			return ;
		}
		
		/*if(m_recSale.getAmount() < 200){
			showDialog("E-Receipt", "Cannot send sms receipt, if amount is less than Rs.200");
			return ;
		}*/

		String currencyType = UtilityFunction.getCurrencyTypeString(m_recSale.getCurrencyType());

		if (currencyType.equals("LKR") && m_recSale.getAmount() < 200) {
			showDialog("E-Receipt", "Cannot send sms receipt, if amount is less than " + currencyType + " 200");
			return;
		}
		
		
		this.pushActivity(SendTextActivity.class, m_recSale,m_voidRes);
	}
}
