package com.cba.payable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cba.payable.DSBaseTxActivity.MyPosListener;
import com.cba.payable.DSBaseTxActivity.POS_TYPE;
import com.dspread.xpos.QPOSService;
import com.dspread.xpos.QPOSService.CommunicationMode;
import com.dspread.xpos.QPOSService.Display;
import com.dspread.xpos.QPOSService.DoTradeResult;
import com.dspread.xpos.QPOSService.Error;
import com.dspread.xpos.QPOSService.QPOSServiceListener;
import com.dspread.xpos.QPOSService.TransactionResult;
import com.dspread.xpos.QPOSService.UpdateInformationResult;
import com.mpos.util.BluetoothPref;
import com.setting.env.Config;

public abstract class DSBaseActivityNew extends GenericActivity{
	
	private static final String LOG_TAG = "payable " + DSBaseActivityNew.class.getSimpleName();
	
	protected static final int REQUEST_ENABLE_BT = 100;
	
	protected static final int QPOS_CONNECTED = 1;
	protected static final int QPOS_DISCONNECTED = 2;
	protected static final int QPOS_NOT_DECTECTED = 9;
	protected static final int BT_CONNECTING = 14;

	protected static final int BT_DIS_CONNECTED = 15;
	
	private BluetoothAdapter mBtAdapter;
	private POS_TYPE posType = POS_TYPE.BLUETOOTH;
	private QPOSService pos;
	private MyPosListener listener;
	
	private String blueTootchAddress = "";
	protected String deviceId ;
	
	private boolean is_reader_connected = false ;
	private boolean isSameReader = true;
	private boolean fired_setup_reader; 
	
	protected  boolean navaigateReadingScr = false ;
	protected boolean isBroadcastReceiverUnRegistered = false ;
	
	
	abstract protected void statusCallBack(int status, String message);
	abstract protected void batteryStatusCallBack(String batteryLevel) ;
	
	protected void cardReaderSetup(){
		
		fired_setup_reader = true;
		registerBT_BC_Receiver() ;
		
		is_reader_connected = false ;
		
		if (posType == POS_TYPE.BLUETOOTH) {

			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			
			if(mBtAdapter != null){
				if(mBtAdapter.isEnabled()){
					 _connectBTDevice() ;
				}else{
					Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enabler,REQUEST_ENABLE_BT);
					
					statusCallBack(REQUEST_ENABLE_BT, "Reader not Connected.");
				}
			}

			return;
		}
		
	}
	
	private void registerBT_BC_Receiver(){
		IntentFilter filter = new IntentFilter();

		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		
		registerReceiver(stateChangeReceiver, filter);
		isBroadcastReceiverUnRegistered = false;
	}
	
	protected void reconnect(){
		Thread thread = new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(500) ;
					runOnUiThread(new Runnable() {
						public void run() {
							_tryAgain();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}) ;
		thread.start();
	}
	
	protected void batteryLeavelThread(){
		Thread thread = new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(100) ;
					pos.getQposInfo(30) ;
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}) ;
		thread.start();

	}
	
	private void _tryAgain(){
		if(pos != null && super.isCallBackSafe()){
			sendMsg(1001);
		}
	}
	
	protected void getQposInfo(){
		if(pos != null && super.isCallBackSafe() && isSameReader){
			//pos.getQposInfo() ;
			//sendMsg(1010);
			batteryLeavelThread() ;
		}else{
			statusCallBack(BT_DIS_CONNECTED, "No cardreader detected.");
		}
	}
	
	protected void refereshConnection(){
		registerBT_BC_Receiver() ;
		//_connectBTDevice() ;
		Thread thread = new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(300) ;
					runOnUiThread(new Runnable() {
						public void run() {
							_connectBTDevice() ;
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}) ;
		thread.start();
	}
	
	private void _connectBTDevice(){
		
		statusCallBack(BT_CONNECTING, "Please wait.Trying to connect with bluetooth reader.");

		if (m_client.isLoggedIn()) {
			deviceId = Config.getReaderId(getApplicationContext());
		} else if (m_client_amex.isLoggedIn()) {
			deviceId = Config.getReaderIdAmex(getApplicationContext());
		}

		//deviceId = Config.getReaderId(getApplicationContext()) ;
		
		blueTootchAddress = BluetoothPref.getDeviceAddress(getApplicationContext()) ;
		
		if(blueTootchAddress == null){
			showToastMessage("No registered card reader.") ;
			finish() ;
			return ;
		}
				
		
		
		_open(CommunicationMode.BLUETOOTH_2Mode);
		
		if(pos != null){
			sendMsg(1001);
		}
		
	}
	
	private void sendMsg(int what) {
		Message msg = new Message();
		msg.what = what;
		mHandler.sendMessage(msg);
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				sendMsg(1002);
				break;
			case 1002:
				is_reader_connected = false ;
				//pos.connectBT(blueTootchAddress);
				pos.connectBluetoothDevice(true, 25, blueTootchAddress);
				//pos.connectBluetoothDevice(true, 5, blueTootchAddress);
				
				// doTradeButton.setEnabled(true);
				break;
			case 1003:
				//Log.i(TAG, "Before calling doTrade.") ;
				//pos.doCheckCard(30) ;
				//pos.doTrade(30);
				break;
			case 1004:
				showDialog("Error" , "Couldn't  find a paired reader.Please pair the reader first." , 222) ;
				break ;
				
			case 1010:
				pos.getQposInfo(30) ;
				break ;
				
			default:
				break;
			}
		}
	};

	
	private void _open(CommunicationMode mode) {
		listener = new MyPosListener();
		pos = QPOSService.getInstance(mode);
		if (pos == null) {
			return;
		}

		pos.setConext(getApplicationContext());
		Handler handler = new Handler(Looper.myLooper());
		pos.initListener(handler, listener);
		
	}
	
	
	private void _close(){
		if (pos == null) {
			return;
		}
		pos.disconnectBT();
	}
	
	protected void disConnectPos(){
		//unregisterReceiver(stateChangeReceiver);
		
		if(! isBroadcastReceiverUnRegistered){
			unregisterReceiver(stateChangeReceiver);
			isBroadcastReceiverUnRegistered = true ;
		}
		
		if (pos == null) {
			return;
		}
		pos.disconnectBT();
	}
	
	
	public void onDestroy() {
		//unregisterReceiver(stateChangeReceiver);
		
		if(! isBroadcastReceiverUnRegistered && fired_setup_reader){
			unregisterReceiver(stateChangeReceiver);
			isBroadcastReceiverUnRegistered = true ;
		}
		_close();
		if (pos != null) {
			pos.onDestroy();
		}
		super.onDestroy();
		//stateChangeReceiver
		
	}
	
	public boolean isReaderConnected(){
		return is_reader_connected ;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {

			if (resultCode == -1) {
				_connectBTDevice();
			}

			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private final BroadcastReceiver stateChangeReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			
			if(BluetoothDevice.ACTION_ACL_CONNECTED == action){
				
				BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//String deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());

				int stVisa = Config.getState(context);
				int stAmex = Config.getAmexState(context);
				String deviceName = null;

				if (stVisa == Config.STATUS_LOGIN) {
					deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());
				} else if (stAmex == Config.STATUS_LOGIN) {
					deviceName = BluetoothPref.generateValidDeviceNameAmex(getApplicationContext());
				}
				
				//Log.i(TAG, "Binded device name : " + deviceName) ;
				//Log.i(TAG, "BT name : " + device.getName()) ;
								
				if (deviceName == null || !deviceName.equals(device.getName())){
					Log.e("", deviceName + "-" + device.getName());
					isSameReader = false;
					return ;
				}
				
				if(pos != null){
					pos.setPosExistFlag(true);
					//Log.i(TAG, "Is device present :"+ pos.isQposPresent()) ;
				}
				
			}
			
			if(BluetoothDevice.ACTION_ACL_DISCONNECTED == action){
				
				BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//String deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());

				int stVisa = Config.getState(context);
				int stAmex = Config.getAmexState(context);
				String deviceName = null;

				if (stVisa == Config.STATUS_LOGIN) {
					deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());
				} else if (stAmex == Config.STATUS_LOGIN) {
					deviceName = BluetoothPref.generateValidDeviceNameAmex(getApplicationContext());
				}
				
			//	Log.i(TAG, "Binded device name : " + deviceName) ;
				//Log.i(TAG, "BT name : " + device.getName()) ;
								
				if (deviceName == null || !deviceName.equals(device.getName())){
					isSameReader = false;
					return ;
				}
				
				if(pos != null){
					pos.setPosExistFlag(false);
					//Log.i(TAG, "Is device present :"+ pos.isQposPresent()) ;
					if(! navaigateReadingScr){
						pos.disconnectBT() ;
					}
					
					statusCallBack(BT_DIS_CONNECTED, "Disconnected with cardreader.");
				}
			}
		}
	} ;

	class MyPosListener extends MyPosListener_vo {


		@Override
		public void onQposKsnResult(Hashtable<String, String> hashtable) {

		}

		@Override
		public void onQposIsCardExist(boolean b) {

		}

		@Override
		public void onRequestDeviceScanFinished() {

		}

		@Override
		public void onQposGenerateSessionKeysResult(Hashtable<String, String> hashtable) {

		}

		@Override
		public void onQposDoSetRsaPublicKey(boolean b) {

		}

		@Override
		public void onSearchMifareCardResult(Hashtable<String, String> hashtable) {

		}

		@Override
		public void onFinishMifareCardResult(boolean b) {

		}

		@Override
		public void onVerifyMifareCardResult(boolean b) {

		}

		@Override
		public void onReadMifareCardResult(Hashtable<String, String> hashtable) {

		}

		@Override
		public void onWriteMifareCardResult(boolean b) {

		}

		@Override
		public void onOperateMifareCardResult(Hashtable<String, String> hashtable) {

		}

		@Override
		public void getMifareCardVersion(Hashtable<String, String> hashtable) {

		}

		@Override
		public void getMifareReadData(Hashtable<String, String> hashtable) {

		}

		@Override
		public void getMifareFastReadData(Hashtable<String, String> hashtable) {

		}

		@Override
		public void writeMifareULData(String s) {

		}

		@Override
		public void verifyMifareULData(Hashtable<String, String> hashtable) {

		}

		@Override
		public void transferMifareData(String s) {

		}

		@Override
		public void onRequestNoQposDetectedUnbond() {

		}

		@Override
		public void onRequestUpdateKey(String s) {

		}

		@Override
		public void onReturnUpdateIPEKResult(boolean b) {

		}

		@Override
		public void onReturnRSAResult(String s) {

		}

		@Override
		public void onReturnUpdateEMVResult(boolean b) {

		}

		@Override
		public void onReturnGetQuickEmvResult(boolean b) {

		}

		@Override
		public void onReturnGetEMVListResult(String s) {

		}

		@Override
		public void onReturnUpdateEMVRIDResult(boolean b) {

		}

		@Override
		public void onDeviceFound(BluetoothDevice bluetoothDevice) {

		}

		@Override
		public void onWaitingforData(String s) {

		}

		@Override
		public void onBluetoothBoardStateResult(boolean b) {

		}

		@Override
		public void onSetManagementKey(boolean b) {

		}

		@Override
		public void onSetSleepModeTime(boolean b) {

		}

		@Override
		public void onGetSleepModeTime(String s) {

		}

		@Override
		public void onGetShutDownTime(String s) {

		}

		@Override
		public void onEncryptData(String s) {

		}

		@Override
		public void onAddKey(boolean b) {

		}

		@Override
		public void onSetBuzzerResult(boolean b) {

		}

		@Override
		public void onSetBuzzerTimeResult(boolean b) {

		}

		@Override
		public void onSetBuzzerStatusResult(boolean b) {

		}

		@Override
		public void onGetBuzzerStatusResult(String s) {

		}

		@Override
		public void onQposDoTradeLog(boolean b) {

		}

		@Override
		public void onQposDoGetTradeLogNum(String s) {

		}

		@Override
		public void onQposDoGetTradeLog(String s, String s1) {

		}
	}
	
	
	abstract class MyPosListener_vo implements QPOSServiceListener{

		@Override
		public void onBluetoothBondFailed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBluetoothBondTimeout() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBluetoothBonded() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBluetoothBonding() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCbcMacResult(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConfirmAmountResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDoTradeResult(DoTradeResult arg0,
				Hashtable<String, String> arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEmvICCExceptionData(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(Error arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetCardNoResult(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetInputAmountResult(boolean arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPosComm(int arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLcdShowCustomDisplay(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPinKey_TDES_Result(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onQposIdResult(Hashtable<String, String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onQposInfoResult(Hashtable<String, String> posInfoData) {
			
			String batteryPercentage = posInfoData.get("batteryPercentage") == null ? ""
					: posInfoData.get("batteryPercentage");
			batteryStatusCallBack(batteryPercentage);
		}

		@Override
		public void onReadBusinessCardResult(boolean arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestBatchData(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestCalculateMac(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestDisplay(Display arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestFinalConfirm() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestIsServerConnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestNoQposDetected() {
			is_reader_connected = false ;
			statusCallBack(QPOS_NOT_DECTECTED, "No cardreader detected.");
			
		}

		@Override
		public void onRequestOnlineProcess(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestQposConnected() {
			is_reader_connected = true ;
			statusCallBack(QPOS_CONNECTED,
					"Connected with cardreader. initiating ...");
			
		}

		@Override
		public void onRequestQposDisconnected() {
			is_reader_connected = false ;
			statusCallBack(QPOS_DISCONNECTED, "Disconnected with cardreader.");
		}

		@Override
		public void onRequestSelectEmvApp(ArrayList<String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestSetAmount() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestSetPin() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestSignatureResult(byte[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestTime() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestTransactionLog(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestTransactionResult(TransactionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestUpdateWorkKeyResult(UpdateInformationResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestWaitingUser() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnApduResult(boolean arg0, String arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnBatchSendAPDUResult(
				LinkedHashMap<Integer, String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnCustomConfigResult(boolean arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnDownloadRsaPublicKey(HashMap<String, String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnGetPinResult(Hashtable<String, String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnNFCApduResult(boolean arg0, String arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnPowerOffIccResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnPowerOffNFCResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnPowerOnIccResult(boolean arg0, String arg1,
				String arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnPowerOnNFCResult(boolean arg0, String arg1,
				String arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnReversalData(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnSetMasterKeyResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnSetSleepTimeResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturniccCashBack(Hashtable<String, String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSetParamsResult(boolean arg0,
				Hashtable<String, Object> arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpdateMasterKeyResult(boolean arg0,
				Hashtable<String, String> arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpdatePosFirmwareResult(UpdateInformationResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onWriteBusinessCardResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	public void unpairBluetoothDevices(){
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				try {
					if(device.getName().startsWith("MPOS")){
						Method m = device.getClass().getMethod("removeBond", (Class[]) null);
						m.invoke(device, (Object[]) null);
					}
				} catch (Exception e) {
					Log.e("Removing has been failed.", e.getMessage());
				}
			}
		}
	}

}
