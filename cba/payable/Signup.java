package com.cba.payable;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Merchant;

public class Signup extends GenericActivity{
	
	private EditText txtEmail , txtPwd , txtConfirmPwd , txtMobile ;
	private EditText txtSurename , txtFirstName , txtBusinessName ;
	private EditText txtAddress , txtCity , txtIMEI ;
	private EditText txtSimId , txtCRId  ;
	
	protected void onInitActivity(APIData... data){
		setContentView(R.layout.layout_signup) ;
		
		txtEmail = (EditText) findViewById(R.id.txtUsername);
		txtPwd = (EditText) findViewById(R.id.txtPwd);
		txtConfirmPwd = (EditText) findViewById(R.id.txtConfirmPwd);
		txtMobile = (EditText) findViewById(R.id.txtMobile);
		txtSurename = (EditText) findViewById(R.id.txtSurename);
		txtFirstName = (EditText) findViewById(R.id.txtFirstname);
		txtBusinessName = (EditText) findViewById(R.id.txtBusinessname);
		
		txtAddress = (EditText) findViewById(R.id.txtAddress);
		txtCity = (EditText) findViewById(R.id.txtCity);
		
		txtIMEI = (EditText) findViewById(R.id.txtIMEI);
		txtIMEI.setKeyListener(null) ;
		txtSimId = (EditText) findViewById(R.id.txtSimId);
		txtSimId.setKeyListener(null) ;
		txtCRId = (EditText) findViewById(R.id.txtCRId);
		txtCRId.setKeyListener(null) ;
		_fetchSignatures() ;
	}
	
	private void _fetchSignatures(){
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		txtIMEI.setText(mTelephonyMgr.getDeviceId()) ;
		txtSimId.setText(mTelephonyMgr.getSubscriberId()) ;
		
	}
	
	public void onSignUp(View v){
		String strEmail = txtEmail.getText().toString() ;
		String strPwd = txtPwd.getText().toString() ;
		String strConfirmPwd = txtConfirmPwd.getText().toString() ;
		String strMobile = txtMobile.getText().toString() ;
		String strSureName = txtSurename.getText().toString() ;
		
		String strFirstName = txtFirstName.getText().toString() ;
		String strBusinessName = txtBusinessName.getText().toString() ;
		String strAddress = txtAddress.getText().toString() ;
		String strCity = txtCity.getText().toString() ;
		String strIMEI = txtIMEI.getText().toString() ;
		String strSimId = txtSimId.getText().toString() ;
		String strCRId = txtCRId.getText().toString() ;
		
		if(strEmail == null || strEmail.length() == 0){
			showDialog("Error" ,"Please enter your email address." ) ;
			return ;
		}
		
		if(strPwd == null || strPwd.length() == 0){
			showDialog("Error" ,"Please enter your password.") ;
			return ;
		}
		
		if(strConfirmPwd == null || strConfirmPwd.length() == 0){
			showDialog("Error" ,"Please enter the Confirm password.") ;
			return ;
		}
		
		if(! strPwd.equals(strConfirmPwd)){
			showDialog("Error" ,"Password and Confirm password doesn't match.") ;
		}
		
		if(strMobile == null || strMobile.length() == 0){
			showDialog("Error" ,"Please enter your mobile number.") ;
			return ;
		}
		
		if(strSureName == null || strSureName.length() == 0){
			showDialog("Error" ,"Please enter your surename.") ;
			return ;
		}
		
		if(strFirstName == null || strFirstName.length() == 0){
			showDialog("Error" ,"Please enter your First name.") ;
			return ;
		}
		
		if(strBusinessName == null || strBusinessName.length() == 0){
			showDialog("Error" ,"Please enter your Business name.") ;
			return ;
		}
		
		if(strAddress == null || strAddress.length() == 0){
			showDialog("Error" ,"Please enter your Address.") ;
			return ;
		}
		
		if(strCity == null || strCity.length() == 0){
			showDialog("Error" ,"Please enter your City.") ;
			return ;
		}
		
		if(strIMEI == null || strIMEI.length() == 0){
			showDialog("Error" ,"Couldn't detect the IMEI. please check your device.") ;
			return ;
		}
		
		if(strSimId == null || strSimId.length() == 0){
			showDialog("Error" ,"Couldn't detect the IMSI.") ;
			return ;
		}
		
		if(strCRId == null || strCRId.length() == 0){
			showDialog("Error" ,"Couldn't access your CardReader.please check") ;
			return ;
		}
		
		Merchant m = new Merchant() ;
		m.setUserName(strEmail) ;
		m.setPwd(strPwd) ;
		m.setMobileNumber(strMobile);
		m.setSureName(strSureName) ;
		m.setFirstName(strFirstName);
		m.setBusinessName(strBusinessName);
		m.setAddress(strAddress);
		m.setCity(strCity);
		m.setDeviceId(strIMEI);
		m.setSimId(strSimId);
		m.setCardReaderId(strCRId);
		
		m_client.signUp(m) ;
		
	}
	
	public void onCancel(View v){
		finish() ;
	}
	
	public void onCallSuccess(EnumApi api ,int callerId , APIData data  ){
		if(data != null){
			
		}
		
	}

}
