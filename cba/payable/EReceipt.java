package com.cba.payable;

import java.text.DecimalFormat;
import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mpos.storage.RecSale;
import com.mpos.util.EmailValidator;
import com.mpos.util.LangPrefs;
import com.mpos.util.PLog;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.SMSValidator;
import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.SaleReceiptReq;
import com.mpos.pojo.SalesReceipt;
import com.mpos.pojo.TxSaleRes;
import com.mpos.pojo.TxVoidRes;

public class EReceipt extends GenericActivity {

	private EditText txtEmail, txtSMS;

	private TxSaleRes m_salesRes;
	private RecSale m_recSale;

	private TxVoidRes m_voidRes;

	private boolean isSalesReceipt;

	protected void onInitActivity(APIData... data) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_ereceipt);

		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtSMS = (EditText) findViewById(R.id.txtSms);

		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] instanceof TxSaleRes) {
					m_salesRes = (TxSaleRes) data[i];
				}

				if (data[i] instanceof RecSale) {
					m_recSale = (RecSale) data[i];
				}

				if (data[i] instanceof TxVoidRes) {
					m_voidRes = (TxVoidRes) data[i];
				}
			}
		}

		if (m_salesRes != null) {
			DecimalFormat df = new DecimalFormat("####0.00");
			TextView txtAmount = (TextView) findViewById(R.id.txtAmount);
			TextView txtName = (TextView) findViewById(R.id.txtName);
			txtName.setText(m_salesRes.getCardHolder());
			txtAmount.setText(df.format(m_salesRes.getAmount()));

			TextView txtApprovalCode = (TextView) findViewById(R.id.txtCode);
			// txtApprovalCode.setText("1234x") ;

			// txtApprovalCode.setText(Integer.toString(m_salesRes.getApprovalCode()))
			// ;
			txtApprovalCode.setText(m_salesRes.getApprovalCode());

			isSalesReceipt = true;
		}

		if (m_recSale != null) {
			DecimalFormat df = new DecimalFormat("####0.00");
			TextView txtAmount = (TextView) findViewById(R.id.txtAmount);
			TextView txtName = (TextView) findViewById(R.id.txtName);
			txtName.setText(m_recSale.getCardHolder());
			txtAmount.setText(df.format(m_recSale.getAmount()));

			TextView txtApprovalCode = (TextView) findViewById(R.id.txtCode);
			// txtApprovalCode.setText(Integer.toString(m_recSale.getApprovalCode()))
			// ;
			txtApprovalCode.setText(m_recSale.getApprovalCode());

			TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
			txtTitle.setText("Void Amount:");

			TextView txtExit = (TextView) findViewById(R.id.txtERecepExit);
			txtExit.setText("Home");

			isSalesReceipt = false;
		}

	}

	public void onSendSms(View v) {

		String strMobile = txtSMS.getText().toString();
		if (strMobile == null || strMobile.length() == 0) {
			showDialog("Error", "Please enter the mobile Number");
			return;
		}

		if (strMobile.length() < 5 || strMobile.length() > 15) {
			showDialog("Error", "Please enter valid mobile number.");
			return;
		}

		if (!SMSValidator.validatePhoneNumber(strMobile)) {
			showDialog("Error", "Please enter valid mobile number.");
			return;
		}

		SaleReceiptReq req = new SaleReceiptReq();

		if ((m_salesRes == null) && (m_recSale == null)) {
			return;
		}

		if (m_salesRes != null) {
			req.setTxId(m_salesRes.getTxId());
			req.setCardHolder(m_salesRes.getCardHolder());
			req.setCcLast4(m_salesRes.getCcLast4());
			req.setAmount(m_salesRes.getAmount());
			req.setCardType(m_salesRes.getCardType());

			Log.i("DATE JEYLOGS" , "Date to send server : " + m_salesRes.getServerTime());
			// create  date object from this string
			// log date.tostring
			DateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.ENGLISH);
			Date date = null;
			try{
				date = format.parse(m_salesRes.getServerTime());
			}catch (Exception e){

			}
			req.setServerTime(date);


			req.setApprovalCode(m_salesRes.getApprovalCode());
			req.setReceiptType(SaleReceiptReq.RECEIPT_SALE);
			// req.setEmail(strEmail) ;
			req.setMobile(strMobile);

			req.setBatchNo(m_salesRes.getBatchNo());
			req.setStn(m_salesRes.getStn());
			req.setRrn(m_salesRes.getRrn());

		}

		if (m_recSale != null) {
			req.setTxId(m_recSale.getTxId());
			req.setCardHolder(m_recSale.getCardHolder());
			req.setCcLast4(m_recSale.getCcLast4());
			req.setAmount(m_recSale.getAmount());
			req.setCardType(m_recSale.getCardType());
			req.setServerTime(m_recSale.getVoidTs());
			req.setApprovalCode(m_recSale.getApprovalCode());
			req.setReceiptType(SaleReceiptReq.RECEIPT_VOID);
			// req.setEmail(strEmail) ;
			req.setMobile(strMobile);

			// Log.i(TAG, "BATCH : " + m_recSale.getBatchNo());
			req.setBatchNo(m_recSale.getBatchNo());
			// Log.i(TAG, "RRN : " + m_voidRes.getRrn());
			req.setStn(m_voidRes.getStn());
			req.setRrn(m_voidRes.getRrn());
		}
		
		m_client.smsReceipt(100, ProgressDialogMessagesUtil.eReceiptTranslation(lang_status), req);

	/*	if (lang_status == LangPrefs.LAN_EN) {

			m_client.smsReceipt(100, "Sending E-Receipt", req);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.smsReceipt(100, ",ÿm; hjñka mj;S'", req);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.smsReceipt(100, "gw;Wr;rPl;L mDg;ggLfpd;wJ", req);

		} else {
			// do nothing
		}*/

		

	}

	public void onBackPressed() {

	}

	public void onSendMail(View v) {

		String strEmail = txtEmail.getText().toString();
		if (strEmail == null || strEmail.length() == 0) {
			showDialog("Error", "Please enter the email address");
			return;
		}

		if (!EmailValidator.validate(strEmail)) {
			showDialog("Error", "Please enter valid email.");
			return;
		}

		SaleReceiptReq req = new SaleReceiptReq();

		if ((m_salesRes == null) && (m_recSale == null)) {
			return;
		}

		if (m_salesRes != null) {
			req.setTxId(m_salesRes.getTxId());
			req.setCardHolder(m_salesRes.getCardHolder());
			req.setCcLast4(m_salesRes.getCcLast4());
			req.setAmount(m_salesRes.getAmount());
			req.setCardType(m_salesRes.getCardType());
			//req.setServerTime(m_salesRes.getServerTime());

			Log.i("DATE JEYLOGS" , "Date to send server : " + m_salesRes.getServerTime());
			// create  date object from this string
			// log date.tostring
			DateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.ENGLISH);
			Date date = null;
			try{
				date = format.parse(m_salesRes.getServerTime());
			}catch (Exception e){

			}
			req.setServerTime(date);

			
			req.setApprovalCode(m_salesRes.getApprovalCode());
			req.setReceiptType(SaleReceiptReq.RECEIPT_SALE);
			req.setRrn(m_salesRes.getRrn());
			req.setBatchNo(m_salesRes.getBatchNo());
			req.setStn(m_salesRes.getStn());
			req.setAid(m_salesRes.getAid());
			req.setExpDate(m_salesRes.getExpDate());
			req.setAppName(m_salesRes.getAppName());
			req.setEmail(strEmail);
		}

		if (m_recSale != null) {
			req.setTxId(m_recSale.getTxId());
			req.setCardHolder(m_recSale.getCardHolder());
			req.setCcLast4(m_recSale.getCcLast4());
			req.setAmount(m_recSale.getAmount());
			req.setCardType(m_recSale.getCardType());
			req.setServerTime(m_recSale.getVoidTs());
			req.setApprovalCode(m_recSale.getApprovalCode());
			req.setReceiptType(SaleReceiptReq.RECEIPT_VOID);
			req.setEmail(strEmail);

			req.setBatchNo(m_recSale.getBatchNo());
			req.setStn(m_voidRes.getStn());
			req.setRrn(m_voidRes.getRrn());
		}

		m_client.emailReceipt(100, "Sending E-Receipt", req);

	}

	public void onSendMail2(View v) {

		String strEmail = txtEmail.getText().toString();
		if (strEmail == null || strEmail.length() == 0) {
			showDialog("Error", "Please enter the email address");
			return;
		}

		if (!EmailValidator.validate(strEmail)) {
			showDialog("Error", "Please enter valid email.");
			return;
		}

		SalesReceipt sr = new SalesReceipt();
		sr.setClientTs(200);
		sr.setTransToken(10002);
		sr.setEmail(strEmail);

		m_client.sendEReceipt(100, "Sending E-Receipt", sr);

	}

	public void onNewSale(View v) {
		

		if (isSalesReceipt) {
			// gotoSale() ;
			SalePad.isNewSale = true;
			finish();
		} else {
			gotoHome();
		}

	}

	public void onCallSuccess(EnumApi api, int callerId, APIData data) {

		if (callerId == 100) {
			showDialog("E-Receipt", "Bill was sent successfully");
		}
	}

}
