package com.cba.payable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.mpos.pojo.APIData;
import com.mpos.pojo.SignUpBean;
import com.mpos.util.EmailValidator;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends GenericActivity {

	ArrayList<SignUpBean> arr_bus;
	SignUpBean signbean;
	int LAN_EN = 0;
	int LAN_TA = 1;
	int LAN_SIN = 2;
	String lan = "en";

	Dialog spnDialog;
	ListView lstBusinessCat;
	ProgressDialog pDialog;
	private int success_code = 0;

	BusinessCatAdapter busadapter;
	Sinhala_LangSelect sin_select;
	Tamil_LangSelect tam_select;

	TextView txtTitle, txtSelectCategory, txtEnglish, txtSinhala, txtTamil;
	EditText edtFirstName, edtLasttName, edtMobileNumber, edtEmailAddress, edtCompanyName, edtBusinessDes;
	Button btnSignUp;
	RelativeLayout rlBusinessType;
	RelativeLayout rlCompany;

	String selected_text = null;
	boolean status_selected = false;
	int selected_index = -1;

	// ======== UI variables ===========//

	String Lead_Source = "Android App";
	String First_Name = "";
	String Last_Name = "";
	String email = "";
	String Phone = "";
	String Mobile = "";
	String Business_Type = "";
	String Company = "";
	String Business_des = "";
	String deviceId = "";
	String simId = "";

	// =======Location Variables========//

	String lat = "";
	String lon = "";
	LocationManager locationManager;
	double longitudeNetwork, latitudeNetwork;

	@Override
	protected void onInitActivity(APIData... data) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_sign_up);
		InitViews();
		TextChangeFunctions();
	}

	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_sign_up);
	 * 
	 * 
	 * }
	 */
	private void InitViews() {

		arr_bus = new ArrayList<SignUpBean>();
		sin_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
		tam_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);

		rlCompany = (RelativeLayout) findViewById(R.id.rlCompany);

		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

		txtEnglish = (TextView) findViewById(R.id.txtEnglish);
		txtEnglish.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtEnglish.setOnClickListener(onClickListener);

		txtSinhala = (TextView) findViewById(R.id.txtSinhala);
		txtSinhala.setText("isxy,");
		txtSinhala.setTextSize(17.0f);
		txtSinhala.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
		txtSinhala.setOnClickListener(onClickListener);

		txtTamil = (TextView) findViewById(R.id.txtTamil);
		txtTamil.setText("jkpo;");
		txtTamil.setTextSize(16.0f);
		txtTamil.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));
		txtTamil.setOnClickListener(onClickListener);

		txtSelectCategory = (TextView) findViewById(R.id.txtSelectCategory);
		txtSelectCategory.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtFirstName = (EditText) findViewById(R.id.edtFirstName);
		edtFirstName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtLasttName = (EditText) findViewById(R.id.edtLasttName);
		edtLasttName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);
		edtMobileNumber.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
		edtEmailAddress.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtCompanyName = (EditText) findViewById(R.id.edtCompanyName);
		edtCompanyName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtBusinessDes = (EditText) findViewById(R.id.edtBusinessDes);
		edtBusinessDes.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		rlBusinessType = (RelativeLayout) findViewById(R.id.rlBusinessType);
		rlBusinessType.setOnClickListener(onClickListener);

		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignUp.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		btnSignUp.setOnClickListener(onClickListener);

		Color_English_Change();

		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

	}

	private View.OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnSignUp:
				validateData();
				break;
			case R.id.rlBusinessType:
				BusinessCat_Dialog();
				break;

			case R.id.txtEnglish:
				lan = "en";
				Color_English_Change();
				break;

			case R.id.txtTamil:
				lan = "ta";
				Color_Tamil_Change();
				break;
			case R.id.txtSinhala:
				lan = "sin";
				Color_Sinhala_Change();
				break;
			}
		}
	};

	private void validateData() {

		First_Name = edtFirstName.getText().toString().trim();
		Last_Name = edtLasttName.getText().toString().trim();
		Mobile = edtMobileNumber.getText().toString().trim();
		email = edtEmailAddress.getText().toString().trim();
		Company = edtCompanyName.getText().toString().trim();
		Business_des = edtBusinessDes.getText().toString().trim();

		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = mTelephonyMgr.getDeviceId();
		simId = mTelephonyMgr.getSimSerialNumber();

		if (isNetworkConnected() == false) {
			Toast.makeText(getApplicationContext(), "Please Connect to the internet", Toast.LENGTH_SHORT).show();
		}

		else if (First_Name.isEmpty() || First_Name.length() == 0) {
			Toast.makeText(getApplicationContext(), "Please Enter First name", Toast.LENGTH_SHORT).show();
		}

		else if (Last_Name.isEmpty() || Last_Name.length() == 0) {
			Toast.makeText(getApplicationContext(), "Please Enter Last name", Toast.LENGTH_SHORT).show();
		}

		else if (Mobile.isEmpty() || Mobile.length() == 0) {
			Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
		} else if (Mobile.length() < 10) {
			Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
		}

		else if (email.isEmpty() || email.length() == 0) {
			Toast.makeText(getApplicationContext(), "Please Enter Email Address", Toast.LENGTH_SHORT).show();
		} else if (EmailValidator.validate(edtEmailAddress.getText().toString()) == false) {
			Toast.makeText(getApplicationContext(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show();

		} else {
			new Async_SignUp().execute();
		}

	}

	public class Async_SignUp extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			showProgressDialog("Please wait");

		}

		@Override
		protected String doInBackground(String... args) {
			try {

				if (status_selected == false) {
					Business_Type = "";
				}
				
				if (selected_index == 0) {
					Business_Type = "Individual";
				} else if (selected_index == 1) {
					Business_Type = "Sole Proprietor";
				} else if (selected_index == 2) {
					Business_Type = "Partnership";
				} else if (selected_index == 3) {
					Business_Type = "Private Limited";
				} else {
					Business_Type = "";
				}

				String data = URLEncoder.encode("xmlData", "UTF-8") + "="
						+ URLEncoder.encode("<Leads><row no='1'>" + "<FL val='Lead Source'>" + Lead_Source + "</FL>"
								+ "<FL val='First Name'>" + First_Name + "</FL>" + "<FL val='Last Name'>" + Last_Name
								+ "</FL>" + "<FL val='Email'>" + email + "</FL>" + "<FL val='Phone'>" + Phone + "</FL>"
								+ "<FL val='Mobile'>" + Mobile + "</FL> " + "<FL val='Lead Owner'>"
								+ "support@payable.lk" + "</FL>" + "<FL val='SIM Number'>" + simId + "</FL>"
								+ "<FL val='IMEI number'>" + deviceId + "</FL>" + "<FL val='Business Type'>"
								+ Business_Type + "</FL>" + "<FL val='Company'>" + Company + "</FL>"
								+ "<FL val='Business Description'>" + Business_des + "</FL>"
								+ "<FL val='LCTN- Latitude'>" + lat + "</FL>" + "<FL val='LCTN- Longitude'>" + lon
								+ "</FL>" + "<FL val='Send Notification Email'>true</FL>" + "</row></Leads>", "UTF-8");
				data += "&" + URLEncoder.encode("newFormat", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

				data += "&" + URLEncoder.encode("authtoken", "UTF-8") + "="
						+ URLEncoder.encode(getResources().getString(R.string.zoho_token), "UTF-8");

				data += "&" + URLEncoder.encode("scope", "UTF-8") + "=" + URLEncoder.encode("crmapi", "UTF-8");

				data += "&" + URLEncoder.encode("wfTrigger", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");

				String text = "";
				BufferedReader reader = null;

				try {

					// Defined URL where to send data
					java.net.URL url = new java.net.URL(getResources().getString(R.string.zoho_url));

					// Send POST data request

					URLConnection conn = url.openConnection();
					HttpURLConnection httpConnection = (HttpURLConnection) conn;
					httpConnection.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(data);
					wr.flush();

					// Get the server response
					int responseCode = httpConnection.getResponseCode();
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					if (responseCode == 200) {
						success_code = 1;

						StringBuilder sb = new StringBuilder();
						String line = null;

						// Read Server Response
						while ((line = reader.readLine()) != null) {
							// Append server response in string
							sb.append(line + "\n");
						}

						text = sb.toString();
					} else {
						success_code = 0;
					}

				} catch (Exception ex) {

				} finally {
					try {

						reader.close();
					}

					catch (Exception ex) {
					}
				}

				// Show response on activity

			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {

			dismissProgressDialog();

			if (success_code == 1) {
				
			
			//	showDialog("Error", "Something has gone wrong,Please try again later",555);
				showInfo("Success", "Your information has been saved.A customer representative will contact you soon.",200);
				// pushActivity(Login.class);
			}

			else if (success_code == 2)

			{
				showDialog("Error", "Something has gone wrong,Please try again later",555);
			}

		}

	}
	
	
	public void onMSGDlgBtnClick(int callerId){
		if(callerId == 200){
			pushActivity(Login.class);
			finish() ;
			return ;
		}
				
		if(callerId == 555){
			//finish() ;
			return ;
		}
		
		super.onMSGDlgBtnClick(callerId);
		
		//finish() ;
		
	}

	// ============Get Location from network==================//

	private final LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			longitudeNetwork = location.getLongitude();
			latitudeNetwork = location.getLatitude();

			lat = String.valueOf(latitudeNetwork);
			lon = String.valueOf(longitudeNetwork);

		}

		@Override
		public void onStatusChanged(String s, int i, Bundle bundle) {

		}

		@Override
		public void onProviderEnabled(String s) {

		}

		@Override
		public void onProviderDisabled(String s) {

		}
	};

	private void BusinessCat_Dialog() {

		try {

			spnDialog = new Dialog(SignUpActivity.this);
			spnDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			spnDialog.setContentView(R.layout.dialog_businesscategory);
			spnDialog.setCanceledOnTouchOutside(true);

			lstBusinessCat = (ListView) spnDialog.findViewById(R.id.lstBusinessCat);
			
			TextView txtTitle = (TextView)spnDialog.findViewById(R.id.txtTitle);
			
			if(lan.equals("en"))
			{
				txtTitle.setText("Business Categories");
				txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
			}
			else if(lan.equals("ta"))
			{
				txtTitle.setText("tpahghu gpupTfs;");
				txtTitle.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));
			}
			else if(lan.equals("sin"))
			{
				txtTitle.setText("jHdmdr leg.ß");
				txtTitle.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
			}
			else
			{
				
			}

			busadapter = new BusinessCatAdapter(SignUpActivity.this, arr_bus);
			lstBusinessCat.setAdapter(busadapter);

			lstBusinessCat.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub

					Business_Type = arr_bus.get(position).getBusiness_type();

					selected_index = position;
					status_selected = true;

					if (lan.equals("en")) {

						txtSelectCategory.setText(Business_Type);
						txtSelectCategory.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
					} else if (lan.equals("ta")) {
						setTamilList();
						txtSelectCategory.setText(Business_Type);
						txtSelectCategory.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));

					} else {
						setSinhalaList();
						txtSelectCategory.setText(Business_Type);
						txtSelectCategory.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
					}

					if (position == 1 || position == 2 || position == 3) {
						edtCompanyName.setVisibility(View.VISIBLE);
						rlCompany.setVisibility(View.VISIBLE);
					} else {
						edtCompanyName.setVisibility(View.GONE);
						rlCompany.setVisibility(View.GONE);
					}

					spnDialog.dismiss();
				}

			});

			spnDialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	private void Color_English_Change() {

		arr_bus.clear();

		txtEnglish.setTextColor(getResources().getColor(R.color.signin_btn));
		txtTamil.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
		txtSinhala.setTextColor(getResources().getColor(R.color.notregistered_signup_color));

		txtTitle.setText("Request Free Card Reader");
		txtTitle.setTextSize(14.0f);
		txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

		edtFirstName.setHint("Enter First Name *");
		edtFirstName.setTextSize(14.0f);
		edtFirstName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtLasttName.setHint("Enter Last Name *");
		edtLasttName.setTextSize(14.0f);
		edtLasttName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtMobileNumber.setHint("Enter Mobile No *");
		edtMobileNumber.setTextSize(14.0f);
		edtMobileNumber.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtEmailAddress.setHint("Enter Email Address *");
		edtEmailAddress.setTextSize(14.0f);
		edtEmailAddress.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtCompanyName.setHint("Enter Company");
		edtCompanyName.setTextSize(14.0f);
		edtCompanyName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		edtBusinessDes.setHint("Enter Business Description");
		edtBusinessDes.setTextSize(14.0f);
		edtBusinessDes.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtSelectCategory.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		txtSelectCategory.setTextSize(14.0f);

		signbean = new SignUpBean();
		BusinessCat_English("Individual", LAN_EN);
		BusinessCat_English("Sole Proprietor", LAN_EN);
		BusinessCat_English("Partnership", LAN_EN);
		BusinessCat_English("Private Limited", LAN_EN);

		if (status_selected == false) {
			txtSelectCategory.setText("Select Business Category");

		} else {
			if (selected_index == 0) {
				txtSelectCategory.setText("Individual");

			} else if (selected_index == 1) {
				txtSelectCategory.setText("Sole Proprietor");
			} else if (selected_index == 2) {
				txtSelectCategory.setText("Partnership");
			} else {
				txtSelectCategory.setText("Private Limited");
			}
		}
	}

	private void Color_Sinhala_Change() {

		arr_bus.clear();
		
		if(edtFirstName.getText().toString().length()>0)
		{
			edtFirstName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		else
		{
			sin_select.Apply_SignUp_FirstName(edtFirstName);
		}
		 if(edtLasttName.getText().toString().length()>0)
		{
			edtLasttName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 sin_select.Apply_SignUp_LastName(edtLasttName);;
		 }
		 
		 if(edtMobileNumber.getText().toString().length()>0)
		{
			edtMobileNumber.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 sin_select.Apply_SignUp_Mobile(edtMobileNumber);
		 }
		 if(edtEmailAddress.getText().toString().length()>0)
		{
			edtEmailAddress.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 sin_select.Apply_SignUp_Email(edtEmailAddress);
		 }
		 if(edtCompanyName.getText().toString().length()>0)
		{
			edtCompanyName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 sin_select.Apply_SignUp_Company(edtCompanyName);
		 }
		 if(edtBusinessDes.getText().toString().length()>0)
		{
			edtBusinessDes.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 sin_select.Apply_SignUp_BusinessDes(edtBusinessDes);
		 }
//		if(edt)
//			sin_select.Apply_SignUp_Sinhala(txtTitle, edtFirstName, edtLasttName, edtMobileNumber, edtEmailAddress,
//					edtCompanyName, edtBusinessDes, txtSelectCategory, btnSignUp);
		 sin_select.Apply_SignUp_Title(txtTitle);
		 sin_select.Apply_SignUp_Submit(btnSignUp);
		

		txtSinhala.setTextColor(getResources().getColor(R.color.signin_btn));
		txtEnglish.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
		txtTamil.setTextColor(getResources().getColor(R.color.notregistered_signup_color));

		BusinessCat_Sinhala("mqoa.,sl jHdmdr", LAN_SIN);
		BusinessCat_Sinhala(";ks mqoa., jHdmdr", LAN_SIN);
		BusinessCat_Sinhala("yjq,a jHdmdr", LAN_SIN);
		BusinessCat_Sinhala("mqoa.,sl iud.ï", LAN_SIN);

		if (status_selected == false) {
			txtSelectCategory.setText("jHdmdr leg.ßh f;darkak");
			txtSelectCategory.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));

		} else {
			if (selected_index == 0) {
				txtSelectCategory.setText("mqoa.,sl jHdmdr");
				txtSelectCategory.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));

			} else if (selected_index == 1) {
				txtSelectCategory.setText(";ks mqoa., jHdmdr");
				txtSelectCategory.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));

			} else if (selected_index == 2) {
				txtSelectCategory.setText("yjq,a jHdmdr");
				txtSelectCategory.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
			} else {
				txtSelectCategory.setText("mqoa.,sl iud.ï");
				txtSelectCategory.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
			}
		}
	}

	private void Color_Tamil_Change() {

		arr_bus.clear();
		
		if(edtFirstName.getText().toString().length()>0)
		{
			edtFirstName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		else
		{
			tam_select.Apply_SignUp_FirstName(edtFirstName);
		}
		 if(edtLasttName.getText().toString().length()>0)
		{
			edtLasttName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 tam_select.Apply_SignUp_LastName(edtLasttName);;
		 }
		 
		 if(edtMobileNumber.getText().toString().length()>0)
		{
			edtMobileNumber.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 tam_select.Apply_SignUp_Mobile(edtMobileNumber);
		 }
		 if(edtEmailAddress.getText().toString().length()>0)
		{
			edtEmailAddress.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 tam_select.Apply_SignUp_Email(edtEmailAddress);
		 }
		 if(edtCompanyName.getText().toString().length()>0)
		{
			edtCompanyName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 tam_select.Apply_SignUp_Company(edtCompanyName);
		 }
		 if(edtBusinessDes.getText().toString().length()>0)
		{
			edtBusinessDes.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
		}
		 else
		 {
			 tam_select.Apply_SignUp_BusinessDes(edtBusinessDes);
		 }

		 tam_select.Apply_SignUp_Title(txtTitle);
		 tam_select.Apply_SignUp_Submit(btnSignUp);
//		tam_select.Apply_SignUp_Tamil(txtTitle, edtFirstName, edtLasttName, edtMobileNumber, edtEmailAddress,
//				edtCompanyName, edtBusinessDes, txtSelectCategory, btnSignUp);

		txtTamil.setTextColor(getResources().getColor(R.color.signin_btn));
		txtSinhala.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
		txtEnglish.setTextColor(getResources().getColor(R.color.notregistered_signup_color));

		signbean = new SignUpBean();
		BusinessCat_Tamil("jdp egu;", LAN_TA);
		BusinessCat_Tamil("jdp cupikahsu;", LAN_TA);
		BusinessCat_Tamil("$l;Lg; gq;fhz;ik", LAN_TA);
		BusinessCat_Tamil("jdpahu; epWtdk;", LAN_TA);

		if (status_selected == false) {
			txtSelectCategory.setText("tpahghu gpupT Nju;e;njLf;f");
			txtSelectCategory.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));

		} else {
			if (selected_index == 0) {
				txtSelectCategory.setText("jdp egu;");
				txtSelectCategory.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));

			} else if (selected_index == 1) {
				txtSelectCategory.setText("jdp cupikahsu;");
				txtSelectCategory.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));

			} else if (selected_index == 2) {
				txtSelectCategory.setText("$l;Lg; gq;fhz;ik");
				txtSelectCategory.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));
			} else {
				txtSelectCategory.setText("jdpahu; epWtdk;");
				txtSelectCategory.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));
			}
		}

	}

	private void BusinessCat_English(String businesstype, int lang) {

		signbean = new SignUpBean();
		signbean.setBusiness_type(businesstype);
		signbean.setLanguage(lang);

		arr_bus.add(signbean);

	}

	private void BusinessCat_Tamil(String businesstype, int lang) {

		signbean = new SignUpBean();
		signbean.setBusiness_type(businesstype);
		signbean.setLanguage(lang);

		arr_bus.add(signbean);

	}

	private void BusinessCat_Sinhala(String businesstype, int lang) {

		signbean = new SignUpBean();
		signbean.setBusiness_type(businesstype);
		signbean.setLanguage(lang);

		arr_bus.add(signbean);

	}

	private void setTamilList() {

		if (selected_index == 0) {
			Business_Type = "jdp egu;";
		} else if (selected_index == 1) {
			Business_Type = "jdp cupikahsu;";
		} else if (selected_index == 2) {
			Business_Type = "$l;Lg; gq;fhz;ik";
		} else if (selected_index == 3) {
			Business_Type = "jdpahu; epWtdk;";
		} else {

		}

	}

	private void setSinhalaList() {

		if (selected_index == 0) {
			Business_Type = "mqoa.,sl jHdmdr";
		} else if (selected_index == 1) {
			Business_Type = ";ks mqoa., jHdmdr";
		} else if (selected_index == 2) {
			Business_Type = "yjq,a jHdmdr";
		} else if (selected_index == 3) {
			Business_Type = "mqoa.,sl iud.ï";
		} else {

		}

	}
	
	
	private void TextChangeFunctions() {
		edtFirstName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().startsWith(" ")) {

					edtFirstName.setText("");

				} else {

				}
				
				if (lan == "en") {

				}

				else if (lan == "ta") {
					if (s == null || s.length() == 0) {

						tamlang_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);
						tamlang_select.Apply_SignUp_FirstName(edtFirstName);
					} else {

						edtFirstName.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}

				}

				else if (lan =="sin") {
					if (s == null || s.length() == 0) {

						sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
						sinlang_select.Apply_SignUp_FirstName(edtFirstName);
					} else {

						edtFirstName.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}
				}

				else {
					// do nothing
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		edtLasttName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().startsWith(" ")) {

					edtLasttName.setText("");

				} else {

				}
				
				if (lan == "en") {

				}

				else if (lan == "ta") {
					if (s == null || s.length() == 0) {

						tamlang_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);
						tamlang_select.Apply_SignUp_LastName(edtLasttName);
					} else {

						edtLasttName.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}

				}

				else if (lan =="sin") {
					if (s == null || s.length() == 0) {

						sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
						sinlang_select.Apply_SignUp_LastName(edtLasttName);
					} else {

						edtLasttName.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}
				}

				else {
					// do nothing
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		edtMobileNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().startsWith(" ")) {

					edtMobileNumber.setText("");

				} else {

				}
				
				if (lan == "en") {

				}

				else if (lan == "ta") {
					if (s == null || s.length() == 0) {

						tamlang_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);
						tamlang_select.Apply_SignUp_Mobile(edtMobileNumber);
					} else {

						edtMobileNumber.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}

				}

				else if (lan =="sin") {
					if (s == null || s.length() == 0) {

						sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
						sinlang_select.Apply_SignUp_Mobile(edtMobileNumber);
					} else {

						edtMobileNumber.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}
				}

				else {
					// do nothing
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		edtEmailAddress.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().startsWith(" ")) {

					edtEmailAddress.setText("");

				} else {

				}
				
				if (lan == "en") {

				}

				else if (lan == "ta") {
					if (s == null || s.length() == 0) {

						tamlang_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);
						tamlang_select.Apply_SignUp_Email(edtEmailAddress);
					} else {

						edtEmailAddress.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}

				}

				else if (lan =="sin") {
					if (s == null || s.length() == 0) {

						sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
						sinlang_select.Apply_SignUp_Email(edtEmailAddress);
					} else {

						edtEmailAddress.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}
				}

				else {
					// do nothing
				}
				

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		
		edtCompanyName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().startsWith(" ")) {

					edtCompanyName.setText("");

				} else {

				}
				
				if (lan == "en") {

				}

				else if (lan == "ta") {
					if (s == null || s.length() == 0) {

						tamlang_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);
						tamlang_select.Apply_SignUp_Company(edtCompanyName);
					} else {

						edtCompanyName.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}

				}

				else if (lan =="sin") {
					if (s == null || s.length() == 0) {

						sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
						sinlang_select.Apply_SignUp_Company(edtCompanyName);
					} else {

						edtCompanyName.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}
				}

				else {
					// do nothing
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	edtBusinessDes.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().startsWith(" ")) {

					edtBusinessDes.setText("");

				} else {

				}
				
				if (lan == "en") {

				}

				else if (lan == "ta") {
					if (s == null || s.length() == 0) {

						tamlang_select = new Tamil_LangSelect(getApplicationContext(), SignUpActivity.this);
						tamlang_select.Apply_SignUp_BusinessDes(edtBusinessDes);
					} else {

						edtBusinessDes.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}

				}

				else if (lan =="sin") {
					if (s == null || s.length() == 0) {

						sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SignUpActivity.this);
						sinlang_select.Apply_SignUp_BusinessDes(edtBusinessDes);
					} else {

						edtBusinessDes.setTypeface(TypeFaceUtils.setLatoRegular(SignUpActivity.this));

					}
				}

				else {
					// do nothing
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void onNavBack(View v) {

		onNavBackPressed();
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			// Log.d("Activity", "Touch event " + event.getRawX() + "," +
			// event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() +
			// "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + "
			// coords " + scrcoords[0] + "," + scrcoords[1]);
			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		}
		return ret;
	}
}
