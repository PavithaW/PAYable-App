package com.cba.payable;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.VFCode;

public class Verification extends GenericActivity{
	
	private EditText txtEmail , txtMobile ;
	
	protected void onInitActivity(APIData... data){
		
		setContentView(R.layout.layout_verification) ;
		txtEmail = (EditText) findViewById(R.id.txtEmailCode);
		txtMobile = (EditText) findViewById(R.id.txtMobileCode);
	}
	
	public void onSubmit(View v){
		String strEmailCode = txtEmail.getText().toString() ;
		String strMobileCode = txtMobile.getText().toString() ;
		
		if(strEmailCode == null || strEmailCode.length() == 0){
			showDialog("Error" ,"Please enter your email-verification code" ) ;
			return ;
		}
		
		if(strMobileCode == null || strMobileCode.length() == 0){
			showDialog("Error" ,"Please enter your mobile-verification code" ) ;
			return ;
		}
		
		VFCode vf = new VFCode() ;
		vf.setEmailCode(strEmailCode) ;
		vf.setMobilecode(strMobileCode) ;
		m_client.verify(vf) ;
	}
	
	public void onResend(View v){
		m_client.resendVerification() ;
	}
	
	public void onCallSuccess(EnumApi api ,int callerId , APIData data  ){
	}

}
