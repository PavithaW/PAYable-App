package com.mpos.connection;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.mpos.pojo.APIData;
import com.mpos.pojo.BatchTxHistoryReq;
import com.mpos.pojo.BatchlistReq;
import com.mpos.pojo.CRSale;
import com.mpos.pojo.DSICCOflineErrReq;
import com.mpos.pojo.DeviceInforReq;
import com.mpos.pojo.KeyEntryTxReq;
import com.mpos.pojo.Merchant;
import com.mpos.pojo.ModelSignature;
import com.mpos.pojo.OpenTxHistoryReq;
import com.mpos.pojo.PwdChangeReq;
import com.mpos.pojo.ResendSaleReceiptReq;
import com.mpos.pojo.SaleReceiptReq;
import com.mpos.pojo.SalesReceipt;
import com.mpos.pojo.SimpleReq;
import com.mpos.pojo.TXDsNfcReq;
import com.mpos.pojo.TXHistoryReq;
import com.mpos.pojo.TxDSSaleReq;
import com.mpos.pojo.TxDsEmvReq;
import com.mpos.pojo.TxEmvReq;
import com.mpos.pojo.TxSettlementReq;
import com.mpos.pojo.TxVoidReq;
import com.mpos.pojo.VFCode;
import com.setting.env.Config;

public class MsgClientAmex {
	
	private MsgListener m_listener = null;

	private static String strUserId = null;
	private static String strLogin = null ;
	private static String strAuthId1 = null;
	private static String strAuthId2 = null;
	
	private static String deviceId = null;
	private static String simId = null;

	private static boolean isLoggedIn = false;
	
	private Context appContext ;
	
	private static CallParams getCallParams(EnumApi api , Context context) {
		CallParams params = null;

		params = new CallParams(strUserId,strLogin, strAuthId1, strAuthId2, deviceId,simId,api,context);

		return params;
	}

	public boolean isLoggedIn(){
		return isLoggedIn;
	}

	public MsgClientAmex(MsgListener listener) {
		m_listener = listener;
	}
	
	public void loadCredentials(Context context){
		Merchant m = Config.getAmexUser(context) ;
		strUserId = Long.toString(m.getId());
		strLogin = m.getUserName() ;
		strAuthId1 = Long.toString(m.getAuth1());
		strAuthId2 = Long.toString(m.getAuth2());
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = mTelephonyMgr.getDeviceId(); 
		simId = mTelephonyMgr.getSimSerialNumber();
		
		appContext = context ;

		if (Config.getAmexState(appContext) == 0) {
			isLoggedIn = false;
		} else if (Config.getAmexState(appContext) == 1) {
			isLoggedIn = true;
		}
		
		//PLog.i("JEYLOGS", "Value of simid:" + simId) ;
		
		if(deviceId == null){
			deviceId = "" ;
		}
		
		if(simId == null){
			simId = "" ;
		}
	}

	public void sampleCall(int callerId, String msg, APIData reqData) {
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.SAMPLE_CALL , appContext);
		params.setServerIndex(2);
		params.setPayLoad(reqData.getJson());
		con.initiateCall(params);
	}

	public void signIn(Merchant reqData){
		signIn(0,null,reqData) ;
	}

	public void signIn(int callerId ,Merchant reqData){
		signIn(callerId,null,reqData) ;
	}
	
	public void signIn(int callerId, String msg, Merchant reqData){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.SignIn , appContext);
		params.setServerIndex(2);
		params.setPayLoad(reqData.getJson());
		con.initiateCall(params);
	}

	public void signUp(Merchant reqData){
		signUp(0,null,reqData) ;
	}

	public void signUp(int callerId ,Merchant reqData){
		signUp(callerId,null,reqData) ;
	}


	public void signUp(int callerId, String msg, Merchant reqData){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.SignUp , appContext);
		params.setServerIndex(2);
		params.setPayLoad(reqData.getJson());
		con.initiateCall(params);
	}

	public void verify(VFCode vfc){
		verify(0,null,vfc) ;
	}

	public void verify(int callerId ,VFCode vfc){
		verify(callerId,null,vfc) ;
	}

	public void verify(int callerId, String msg ,VFCode vfc){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.Verification , appContext);
		params.setServerIndex(2);
		params.setPayLoad(vfc.getJson());
		con.initiateCall(params);
	}

	public void resendVerification(){
		resendVerification(0,null) ;
	}

	public void resendVerification(int callerId ){
		resendVerification(callerId,null) ;
	}

	public void resendVerification(int callerId, String msg){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.VCResend , appContext);
		con.initiateCall(params);
	}

	public void sendEReceipt(SalesReceipt sr){
		sendEReceipt(0,null,sr) ;
	}

	public void sendEReceipt(int callerId ,SalesReceipt sr){
		sendEReceipt(callerId,null,sr) ;
	}

	public void sendEReceipt(int callerId, String msg ,SalesReceipt sr){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.EReceiptSales , appContext);
		params.setServerIndex(2);
		params.setPayLoad(sr.getJson());
		con.initiateCall(params);
	}

	public void sale(CRSale sale){
		sale(0,null,sale) ;
	}

	public void sale(int callerId ,CRSale sale){
		sale(callerId,null,sale) ;
	}

	public void sale(int callerId ,String msg,CRSale sale){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.Sales , appContext);
		params.setServerIndex(2);
		params.setPayLoad(sale.getJson());
		con.initiateCall(params);
	}

	public void emvSale(TxEmvReq sale){
		emvSale(0,null,sale) ;
	}

	public void emvSale(int callerId ,TxEmvReq sale){
		emvSale(callerId,null,sale) ;
	}

	public void emvSale(int callerId ,String msg,TxEmvReq sale){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.EmvSales,appContext);
		params.setServerIndex(2);
		params.setPayLoad(sale.getJson());
		con.initiateCall(params);
	}

	public void dsSwipeSale(TxDSSaleReq sale){
		dsSwipeSale(0,null,sale) ;
	}

	public void dsSwipeSale(int callerId ,TxDSSaleReq sale){
		dsSwipeSale(callerId,null,sale) ;
	}

	public void dsSwipeSale(int callerId ,String msg,TxDSSaleReq sale){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.DsSale,appContext);
		params.setServerIndex(2);
		params.setPayLoad(sale.getJson());
		con.initiateCall(params);
	}

	public void dsEmvSale(TxDsEmvReq sale){
		dsEmvSale(0,null,sale) ;
	}

	public void dsEmvSale(int callerId ,TxDsEmvReq sale){
		dsEmvSale(callerId,null,sale) ;
	}

	public void dsNfcSale(int callerId , String msg, TXDsNfcReq sale){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.DSNfcSale,appContext);
		params.setServerIndex(2);
		params.setPayLoad(sale.getJson());
		con.initiateCall(params);
	}

	public void dsEmvSale(int callerId ,String msg,TxDsEmvReq sale){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.DSNfcSale,appContext);
		params.setServerIndex(2);
		params.setPayLoad(sale.getJson());
		con.initiateCall(params);
	}

	public void DSICCOflineErr(DSICCOflineErrReq req){
		DSICCOflineErr(0,null,req) ;
	}

	public void DSICCOflineErr(int callerId ,DSICCOflineErrReq req){
		DSICCOflineErr(callerId,null,req) ;
	}

	public void DSICCOflineErr(int callerId ,String msg,DSICCOflineErrReq req){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.DSICCOflineErr,appContext);
		params.setServerIndex(2);
		params.setPayLoad(req.getJson());
		con.initiateCall(params);
	}

	public void BatchEmvLogs(DSICCOflineErrReq req){
		BatchEmvLogs(0,null,req) ;
	}

	public void BatchEmvLogs(int callerId ,DSICCOflineErrReq req){
		BatchEmvLogs(callerId,null,req) ;
	}

	public void BatchEmvLogs(int callerId ,String msg,DSICCOflineErrReq req){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.BatchEmvLogs,appContext);
		params.setServerIndex(2);
		params.setPayLoad(req.getJson());
		con.initiateCall(params);
	}

	public void signature(ModelSignature sig){
		signature(0,null,sig) ;
	}

	public void signature(int callerId ,ModelSignature sig){
		signature(callerId,null,sig) ;
	}

	public void signature(int callerId ,String msg ,ModelSignature sig){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.Signature,appContext);
		params.setServerIndex(2);
		params.setPayLoad(sig.getJson());
		con.initiateCall(params);
	}

	public void closeTxHistory(TXHistoryReq request){
		closeTxHistory(0,null,request) ;
	}

	public void closeTxHistory(int callerId ,TXHistoryReq request){
		closeTxHistory(callerId,null,request) ;
	}

	public void closeTxHistory(int callerId , String msg , TXHistoryReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.CloseTxHistory,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void openTxHistory(OpenTxHistoryReq request){
		openTxHistory(0,null,request) ;
	}

	public void openTxHistory(int callerId ,OpenTxHistoryReq request){
		openTxHistory(callerId,null,request) ;
	}

	public void openTxHistory(int callerId , String msg , OpenTxHistoryReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.OpenTxHistory,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void settlement(TxSettlementReq request){
		settlement(0,null,request) ;
	}

	public void settlement(int callerId ,TxSettlementReq request){
		settlement(callerId,null,request) ;
	}

	public void settlement(int callerId , String msg , TxSettlementReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.Settlement,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void settlementSummary(SimpleReq request){
		settlementSummary(0,null,request) ;
	}

	public void settlementSummary(int callerId ,SimpleReq request){
		settlementSummary(callerId,null,request) ;
	}

	public void settlementSummary(int callerId , String msg ,SimpleReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.SettlementSummary,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void settlementSummaryV2(int callerId , String msg ,SimpleReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.SettlementSummaryV2,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void voidRequest(TxVoidReq request){
		voidRequest(0,null,request) ;
	}

	public void voidRequest(int callerId  ,TxVoidReq request){
		voidRequest(callerId,null,request) ;
	}

	public void voidRequest(int callerId , String msg ,TxVoidReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.voidTx,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void emailReceipt(SaleReceiptReq request){
		emailReceipt(0,null,request) ;
	}

	public void emailReceipt(int callerId ,SaleReceiptReq request){
		emailReceipt(callerId,null,request) ;
	}

	public void emailReceipt(int callerId , String msg ,SaleReceiptReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.ReceiptEmail,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void emailReceiptManual(int callerId, String msg, SaleReceiptReq request) {
		ConThread con = new ConThread(callerId, m_listener, msg, appContext);
		CallParams params = getCallParams(EnumApi.ResendReceiptEmail, appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void smsReceipt(SaleReceiptReq request){
		smsReceipt(0,null,request) ;
	}

	public void smsReceipt(int callerId ,SaleReceiptReq request){
		smsReceipt(callerId,null,request) ;
	}


	public void smsReceipt(int callerId , String msg ,SaleReceiptReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.ReceiptSMS , appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void modifyPassword(PwdChangeReq request){
		modifyPassword(0,null,request) ;
	}

	public void modifyPassword(int callerId ,PwdChangeReq request){
		modifyPassword(callerId,null,request) ;
	}

	public void modifyPassword(int callerId , String msg ,PwdChangeReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.ModifyPassword , appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void sendDeviceInfo(DeviceInforReq request){
		sendDeviceInfo(0,null,request) ;
	}

	public void sendDeviceInfo(int callerId ,DeviceInforReq request){
		sendDeviceInfo(callerId,null,request) ;
	}

	public void sendDeviceInfo(int callerId , String msg ,DeviceInforReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.DeviceInfo , appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void fetchBatchList(BatchlistReq request){
		fetchBatchList(0,null,request) ;
	}

	public void fetchBatchList(int callerId ,BatchlistReq request){
		fetchBatchList(callerId,null,request) ;
	}

	public void fetchBatchList(int callerId , String msg ,BatchlistReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.Batchlist , appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void fetchBatchTxHistory(BatchTxHistoryReq request){
		fetchBatchTxHistory(0,null,request) ;
	}

	public void fetchBatchTxHistory(int callerId ,BatchTxHistoryReq request){
		fetchBatchTxHistory(callerId,null,request) ;
	}

	public void fetchBatchTxHistory(int callerId , String msg ,BatchTxHistoryReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.BatchTxHistory , appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void echo(SimpleReq request){
		echo(0,null,request) ;
	}

	public void echo(int callerId ,SimpleReq request){
		echo(callerId,null,request) ;
	}

	public void echo(int callerId , String msg ,SimpleReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.Echo,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	public void keyEntryTx(KeyEntryTxReq request){
		keyEntryTx(0,null,request) ;
	}

	public void keyEntryTx(int callerId ,KeyEntryTxReq request){
		keyEntryTx(callerId,null,request) ;
	}

	public void keyEntryTx(int callerId , String msg ,KeyEntryTxReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.KeyEntryTx,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

	//API call for resend Email
	public void resendEmailReceipt(ResendSaleReceiptReq request) {
		resendEmailReceipt(0, null, request);
	}

	public void resendEmailReceipt(int callerId, ResendSaleReceiptReq request) {
		resendEmailReceipt(callerId, null, request);
	}

	public void resendEmailReceipt(int callerId, String msg, ResendSaleReceiptReq request) {
		ConThread con = new ConThread(callerId, m_listener, msg, appContext);
		CallParams params = getCallParams(EnumApi.ResendReceiptEmail, appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}
	// end resend email


	//API call for resend SMS
	public void resendSmsReceipt(ResendSaleReceiptReq request) {
		resendSmsReceipt(0, null, request);
	}

	public void resendSmsReceipt(int callerId, ResendSaleReceiptReq request) {
		resendSmsReceipt(callerId, null, request);
	}

	public void resendSmsReceipt(int callerId, String msg, ResendSaleReceiptReq request) {
		ConThread con = new ConThread(callerId, m_listener, msg, appContext);
		CallParams params = getCallParams(EnumApi.ResendReceiptSMS, appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}
	// end resend SMS

	public void fetchUserInfo(SimpleReq request){
		fetchUserInfo(0,null,request) ;
	}

	public void fetchUserInfo(int callerId ,SimpleReq request){
		fetchUserInfo(callerId,null,request) ;
	}

	public void fetchUserInfo(int callerId , String msg ,SimpleReq request){
		ConThread con = new ConThread(callerId, m_listener, msg,appContext);
		CallParams params = getCallParams(EnumApi.UserDetail,appContext);
		params.setServerIndex(2);
		params.setPayLoad(request.getJson());
		con.initiateCall(params);
	}

}
