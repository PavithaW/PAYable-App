package com.cba.payable;

import android.app.Dialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dspread.xpos.QPOSService;
import com.dspread.xpos.QPOSService.CommunicationMode;
import com.dspread.xpos.QPOSService.Display;
import com.dspread.xpos.QPOSService.DoTradeResult;
import com.dspread.xpos.QPOSService.EmvOption;
import com.dspread.xpos.QPOSService.Error;
import com.dspread.xpos.QPOSService.QPOSServiceListener;
import com.dspread.xpos.QPOSService.TransactionResult;
import com.dspread.xpos.QPOSService.TransactionType;
import com.dspread.xpos.QPOSService.UpdateInformationResult;
import com.mpos.connection.ApiException;
import com.mpos.util.BluetoothPref;
import com.setting.env.Config;
import com.setting.env.Environment;
import com.setting.env.Environment.Advt;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;


public abstract class DSBaseTxActivity extends GenericActivity {

    private  static final String TAG = "JEYLOGS" ;

    protected static final int QPOS_CONNECTED = 1;
    protected static final int QPOS_DISCONNECTED = 2;
    protected static final int WAITING_FOR_CARD = 3;
    protected static final int CARD_NONE = 4;
    protected static final int CARD_NOT_ICC = 5;
    protected static final int CARD_BAD_SWIPE = 6;
    protected static final int CARD_NO_RESPONSE = 7;
    protected static final int QPOS_ERROR = 8;
    protected static final int QPOS_NOT_DECTECTED = 9;
    protected static final int CARD_SWIPE_SUCCESS = 10;
    protected static final int CARD_ICC = 11;
    protected static final int CARD_ICC_ONLINE = 12;
    protected static final int SELECT_APP_FAIL = 13;

    protected static final int BT_CONNECTING = 14;

    protected static final int BT_DIS_CONNECTED = 15;

    protected static final int CARD_NFC_DECLIENED = 16;

    protected static final int CARD_NFC_OFFLINE = 17;

    private static final int REQUEST_ENABLE_BT = 100;

    private BluetoothAdapter mBtAdapter;
    private String blueTootchAddress = "";

    private QPOSService pos;
    private MyPosListener listener;

    private SimpleDateFormat sdf;

    private POS_TYPE posType = POS_TYPE.BLUETOOTH;

    protected String deviceId;


    private Dialog appDialog;
    private ListView appListView;


    protected boolean isFallback = false;
    private boolean isICCErrLog = false;
    private boolean isICCOnline = false;
    private boolean isWaitingForUserAction = false;
    private boolean isUpdatingIssuerScript = false;

    private boolean isReacerBlConnected = false;

    private String strIccErrLog = null;
    private String strIccBatchLog = null;
    protected String strF39ErrMsg = null;

    protected int serverExceptionCode;


    protected boolean isICCOnlineTxSuccess = false;
    protected boolean isICCOnlineTxFail = false;
    protected boolean isBroadcastReceiverUnRegistered = false;

    protected static enum POS_TYPE {
        BLUETOOTH, AUDIO, UART
    }

    abstract protected POS_TYPE getPosType();

    abstract protected void statusCallBack(int status, String message);

    abstract protected String getAmount();

    abstract protected String getCurrency();

    abstract protected void swipeCallBack(String ksn, String cardHolder,
                                          String track2, String maskedPan);

    abstract protected void nfcCallBack(String ksn, String cardHolder,
                                          String track2, String maskedPan,String nfcData);

    abstract protected void emvCallBack(String data);

    abstract protected void iccErrCallBack(String data);

    abstract protected void iccIssuerScriptSuccessCallBack(String data);

    abstract protected void iccIssuerScriptFailCallBack(String data);


    abstract protected void navigateToSigScreen();

    public void dismissAppDialog() {
        if (appDialog != null) {
            appDialog.dismiss();
            appDialog = null;
        }
    }

    protected void setupCardReader() {
        sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        deviceId = null;

        posType = getPosType();


        if (posType == POS_TYPE.AUDIO) {
            _connectAudioDevice();
            return;
        }

        //Log.i(TAG, "DS base - point : 2000") ;

        _resetTxFlags();

        registerBT_BC_Receiver();

        if (posType == POS_TYPE.BLUETOOTH) {

            mBtAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBtAdapter != null) {
                if (mBtAdapter.isEnabled()) {
                    _connectBTDevice();
                } else {
                    Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enabler, REQUEST_ENABLE_BT);
                }
            }

            return;
        }


    }


    private void registerBT_BC_Receiver() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        registerReceiver(stateChangeReceiver, filter);

        isBroadcastReceiverUnRegistered = false;
    }

    protected void processSignature() {
        if (!isBroadcastReceiverUnRegistered) {
            unregisterReceiver(stateChangeReceiver);
            isBroadcastReceiverUnRegistered = true;
        }

        navigateToSigScreen();
    }

    private void _onTransactionCancel() {
        showToastMessage("Transaction canceled.");
        finish();
    }

    protected void _onTransactionDeclined() {

        if (serverExceptionCode == ApiException.SCOCKET_EXCEPTION || serverExceptionCode == ApiException.TIMEOUT_ERROR) {
            showToastMessage("Please check your internet connectivity.");
            finish();
            return;
        }

        if (strF39ErrMsg != null) {
            showToastMessage(strF39ErrMsg);
        } else {
            showToastMessage("Transaction Declined.");
        }
        //showToastMessage("Transaction Declined.") ;
        finish();
    }

    protected void _onTransactionFallbackErr() {
        showToastMessage("Not accepted.");
        finish();
    }

    private void _onCardBlocked() {
        showToastMessage("Card Blocked.");
        finish();
    }

    protected void writeIssuerScriptOnSuccess(String script) {

        isUpdatingIssuerScript = true;

        // 00 for approval
        // 05 for a decline
        // 02 for referral

        if (!isReacerBlConnected) {
            processSignature();
            return;
        }


        if (pos != null) {
            if (script.contains("8A023030")) {
                pos.sendOnlineProcessResult(script);
            } else {
                pos.sendOnlineProcessResult("8A023030" + script);
            }

        }

    }

    protected void writeIssuerScriptOnError(String script) {

        isUpdatingIssuerScript = true;

        if (!isReacerBlConnected) {
            _onTransactionDeclined();
            return;
        }

        if (pos != null) {
            if (script.contains("8A023035")) {
                pos.sendOnlineProcessResult(script);
            } else {
                pos.sendOnlineProcessResult("8A023035" + script);
            }

        }

    }

    protected void turnoffIcc() {
        if (pos != null) {
            pos.powerOffIcc();
        }

        //pos.lcdShowCustomDisplay(lcdModeAlign, lcdFont);

    }

    private void _connectAudioDevice() {
        _open(CommunicationMode.AUDIO);

        if (pos != null) {
            //pos.openAudio();
            sendMsg(1005);
        }

        // pos.getQposId() ;
    }

    private void _connectBTDevice() {
        statusCallBack(BT_CONNECTING, "Please wait.Trying to connect with bluetooth reader.");

        if (m_client.isLoggedIn()) {
            deviceId = Config.getReaderId(getApplicationContext());
        } else if (m_client_amex.isLoggedIn()) {
            deviceId = Config.getReaderIdAmex(getApplicationContext());
        }


        blueTootchAddress = BluetoothPref.getDeviceAddress(getApplicationContext());

        if (blueTootchAddress == null) {
            showToastMessage("Scanning device, please wait...");
            //finish();
            return;
        }


        _open(CommunicationMode.BLUETOOTH_2Mode);
        posType = POS_TYPE.BLUETOOTH;

        if (pos != null) {
            sendMsg(1001);
        }

    }

    private void _connectBTDevice2() {

        // BT_CONNECTING
        statusCallBack(BT_CONNECTING, "Please wait.Trying to connect with bluetooth reader.");

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if (pairedDevices == null || pairedDevices.size() == 0) {
            //Log.e(TAG, "There are no paired device");
            // need to throw error.
            return;
        }

        int testing = 1;

        if (testing == 2) {
            return;
        }

        if (testing == 1) {


            _open(CommunicationMode.BLUETOOTH_2Mode);

            posType = POS_TYPE.BLUETOOTH;
            blueTootchAddress = "8C:DE:52:4B:15:66";

            if (pos != null) {
                sendMsg(1001);
            }

            return;
        }


        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String strName = device.getName();
                String strAddress = device.getAddress();


                if (strName.startsWith("MPOS")) {
                    if (strName.length() == 14) {
                        // found the device. connect
                        _open(CommunicationMode.BLUETOOTH_2Mode);
                        //Log.i(TAG, "DS base - point : 3300") ;
                        posType = POS_TYPE.BLUETOOTH;
                        blueTootchAddress = strAddress;
                        //sendMsg(1001);
                        if (pos != null) {
                            //Log.i(TAG, "DS base - point : 3400") ;
                            //pos.connectBluetoothDevice(true, 25, blueTootchAddress);
                            sendMsg(1001);
                            //Log.i(TAG, "DS base - point : 3500") ;
                        }

                        return;
                    }
                }

            }

            // seems no device here.
            //Log.i(TAG, "No reader found") ;
            //showDialog("Error" , "Couldn't  find a paired reader.Please pair the reader first." ) ;
            sendMsg(1004);
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
                    // pos.connectBluetoothDevice(20, blueTootchAddress);
                    pos.connectBluetoothDevice(true, 25, blueTootchAddress);
                    // doTradeButton.setEnabled(true);
                    break;
                case 1003:
                    //Log.i(TAG, "Before calling doTrade.") ;
                    //pos.doCheckCard(30) ;
                    //pos.doTrade(30);
                    break;
                case 1004:
                    showDialog("Error", "Couldn't  find a paired reader.Please pair the reader first.", 222);
                    break;

                case 1005:
                    pos.openAudio();
                default:
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {

            if (resultCode == -1) {
                _connectBTDevice();
            }

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {

        if (isUpdatingIssuerScript) {
            return;
        }

        if (isWaitingForUserAction) {
            turnoffIcc();
        }


        finish();
    }

    public void onDestroy() {

        if (!isBroadcastReceiverUnRegistered) {
            unregisterReceiver(stateChangeReceiver);
            isBroadcastReceiverUnRegistered = true;
        }

        super.onDestroy();
        _close();

    }

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

    private void _close() {
        if (pos == null) {
            return;
        }
        if (posType == POS_TYPE.AUDIO) {
            pos.closeAudio();
        } else if (posType == POS_TYPE.BLUETOOTH) {
            pos.disconnectBT();
        } else if (posType == POS_TYPE.UART) {
            pos.closeUart();
        }


    }

    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 222) {
            finish();
            return;
        }

        if (callerId == 111) {
            finish();
            return;
        }

        if (callerId == 333) {
            finish();
            return;
        }

        if (callerId == 444) {
            statusCallBack(SELECT_APP_FAIL, "Please swipe the card.");
            pos.doCheckCard(30);
            return;
        }

        if (callerId == 555) {
            finish();
            return;
        }

        super.onMSGDlgBtnClick(callerId);

        //finish() ;

    }

    private void _resetTxFlags() {
        isICCErrLog = false;
        isICCOnline = false;
        strIccErrLog = null;
        strIccBatchLog = null;
        isICCOnlineTxSuccess = false;
        isICCOnlineTxFail = false;
        strF39ErrMsg = null;
    }


    private final BroadcastReceiver stateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //boolean isTurnedOff = false ;

            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {

                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                int stVisa = Config.getState(context);
                int stAmex = Config.getAmexState(context);
                String deviceName = null;

                if (stVisa == Config.STATUS_LOGIN) {
                    deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());
                } else if (stAmex == Config.STATUS_LOGIN) {
                    deviceName = BluetoothPref.generateValidDeviceNameAmex(getApplicationContext());
                }

                //String deviceName = BluetoothPref.generateValidDeviceNameAmex(getApplicationContext());

                // String deviceName = BluetoothPref.generateValidDeviceNameAmex(getApplicationContext());

                //Log.i(TAG, "Binded device name : " + deviceName) ;
                //Log.i(TAG, "BT name : " + device.getName()) ;

                if (deviceName != null && !deviceName.equals(device.getName())) {
                    return;
                }

                if (pos != null) {
                    pos.setPosExistFlag(true);
                    //Log.i(TAG, "Is device present :"+ pos.isQposPresent()) ;
                }

            }

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {

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

                if (deviceName != null && !deviceName.equals(device.getName())) {
                    return;
                }

                isReacerBlConnected = false;

                if (pos != null) {
                    pos.setPosExistFlag(false);
                    //Log.i(TAG, "Is device present :"+ pos.isQposPresent()) ;
                    pos.disconnectBT();

                    ///statusCallBack(BT_DIS_CONNECTED, "Disconnected with cardreader.");
                }

                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }

                //isICCOnlineTxFail

                if (isICCOnline && isICCOnlineTxFail) {
                    _onTransactionDeclined();
                    return;
                }

                //_onTransactionDeclined() ;
            }
        }
    };


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


    abstract  class MyPosListener_vo implements QPOSServiceListener {

        @Override
        public void onBluetoothBondFailed() {
        }

        @Override
        public void onBluetoothBondTimeout() {

        }

        @Override
        public void onBluetoothBonded() {
        }

        @Override
        public void onBluetoothBonding() {

        }

        @Override
        public void onCbcMacResult(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onConfirmAmountResult(boolean res) {
            //Log.i(TAG, "inside onConfirmAmountResult..");
            //Log.i(TAG, "Result = " + res);

        }

        @Override
        public void onDoTradeResult(DoTradeResult result,
                                    Hashtable<String, String> decodeData) {

            isWaitingForUserAction = false;
            isUpdatingIssuerScript = false;

            //	Log.i(TAG, "DecodeData : " + decodeData) ;

            if (result == DoTradeResult.NONE) {
                statusCallBack(CARD_NONE, "No valid card detected.");
                return;
            }

            if (result == DoTradeResult.NOT_ICC) {
                statusCallBack(CARD_NOT_ICC, "No valid card detected.");
                return;
            }

            if (result == DoTradeResult.BAD_SWIPE) {
                isWaitingForUserAction = true;
                statusCallBack(CARD_BAD_SWIPE, "Bad swipe.Please try again.");
                return;
            }

            if (result == DoTradeResult.ICC) {
                //Log.i(TAG, "inside result == DoTradeResult.ICC ");

                _resetTxFlags();

                if (isFallback) {
                    showDialog("Error", "Please remove the card and swipe.", 444);
                    //pos.p
                    return;
                }

                statusCallBack(CARD_ICC, "EMV card inserted.");
                pos.doEmvApp(EmvOption.START);
                return;
            }

            if (result == DoTradeResult.MCR) {

                statusCallBack(CARD_SWIPE_SUCCESS, "Successfull Swipe.");

                String formatID = decodeData.get("formatID");

                String ksn = null;
                String cardHolder = null;
                String track2 = null;
                String maskedPan = null;

                if (formatID.equals("31") || formatID.equals("40")
                        || formatID.equals("37") || formatID.equals("17")
                        || formatID.equals("11") || formatID.equals("10")) {

                    swipeCallBack(null, null, null, null);

                } else if (formatID.equals("FF")) {
                    swipeCallBack(null, null, null, null);
                } else {
                    cardHolder = decodeData.get("cardholderName");
                    maskedPan = decodeData.get("maskedPAN");
                    track2 = decodeData.get("encTrack2");
                    ksn = decodeData.get("trackksn");

                    swipeCallBack(ksn, cardHolder, track2, maskedPan);
                }

                return;
            }

            if (result == DoTradeResult.NO_RESPONSE) {
                statusCallBack(CARD_NO_RESPONSE, "No response from card.");
                return;
            }

            if (result == DoTradeResult.NFC_DECLINED) {
                statusCallBack(CARD_NFC_DECLIENED, "NFC is declined.");
                return;
            }

            if (result == DoTradeResult.NFC_OFFLINE) {
                statusCallBack(CARD_NFC_OFFLINE, "NFC offline transaction is not allowed.");
                return;
            }

            if (result == DoTradeResult.NFC_ONLINE){

                Log.i(TAG,"Decode data : " + decodeData.toString());

                String cardHolder = decodeData.get("cardholderName");
                String maskedPan = decodeData.get("maskedPAN");
                String track2 = decodeData.get("encTrack2");
                String ksn = decodeData.get("trackksn");

                Hashtable<String,String> h = pos.getNFCBatchData();
                String data = h.get("tlv");

                nfcCallBack(ksn,cardHolder,track2,maskedPan,data);

            }


        }

        @Override
        public void onEmvICCExceptionData(String res) {
            isICCErrLog = true;
            strIccErrLog = res;
            //Log.i(TAG, "Inside onEmvICCExceptionData");
            //Log.i(TAG, "Res:" + res);

        }

        @Override
        public void onError(Error err) {

            //Log.i(TAG, "inside onError") ;
            //Log.i(TAG,"Err:" +err.toString() ) ;


            storeErrorLog("inside cr onError. err :  " + err.toString());

            if (err == Error.CMD_TIMEOUT) {

                showDialog("Error", "No card insertion/swipe", 111);
                //showDialog("Error" , "No card insertion/swipe" ) ;
                return;
            }

            if (err == Error.TIMEOUT) {
                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }
            }

            if (err == Error.DEVICE_BUSY) {
                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }
            }

            statusCallBack(QPOS_ERROR, "Error with card reader.");
        }

        @Override
        public void onGetCardNoResult(String arg0) {

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
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
            deviceId = posIdTable.get("posId") == null ? "" : posIdTable
                    .get("posId");


            //pos.doCheckCard(30);
            String strId = "";
            if (m_client.isLoggedIn()) {
                strId = Config.getReaderId(getApplicationContext());
            } else if (m_client_amex.isLoggedIn()) {
                strId = Config.getReaderIdAmex(getApplicationContext());
            }

            if (strId.equalsIgnoreCase(deviceId)) {
                pos.setAmountIcon(getCurrency());
                pos.doCheckCard(30);
            } else {
                showDialog("Error", "Reader id  doesn't match.Please connect with valid  reader.", 555);
            }

        }

        @Override
        public void onQposInfoResult(Hashtable<String, String> arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReadBusinessCardResult(boolean arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRequestBatchData(String tlv) {
            //Log.i(TAG, "inside onRequestBatchData") ;
            //Log.i(TAG, " ************* ") ;
            //Log.i(TAG, tlv) ;
            strIccBatchLog = tlv;

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
            //Log.i(TAG, "inside onRequestFinalConfirm.");
            pos.finalConfirm(true);

        }

        @Override
        public void onRequestIsServerConnected() {
            pos.isServerConnected(true);

        }

        @Override
        public void onRequestNoQposDetected() {
            statusCallBack(QPOS_NOT_DECTECTED, "No cardreader detected.");
            showDialog("Error", "No cardreader detected.", 333);

        }

        @Override
        public void onRequestOnlineProcess(String tlv) {


            //pos.powerOffIcc() ;

//            Hashtable<String,String> h1 = pos.getICCTag(0, 1, "8F") ;
//            Log.i("JEYLOGS" , " INSIDE onRequestOnlineProcess") ;
//            Log.i("JEYLOGS" , " h1 : " + h1.toString()) ;


            _resetTxFlags();
            isICCOnline = true;

            statusCallBack(CARD_ICC_ONLINE, "Processing online.");
            emvCallBack(tlv);

        }

        @Override
        public void onRequestQposConnected() {
            /*statusCallBack(QPOS_CONNECTED,
                    "Connected with cardreader. Verifying reader .."); */

            statusCallBack(QPOS_CONNECTED,
                    "Connected with cardreader. initiating ...");

            isReacerBlConnected = true;


            //pos.getQposId();

           // pos.setCardTradeMode(QPOSService.CardTradeMode.SWIPE_TAP_INSERT_CARD_NOTUP);
            pos.setCardTradeMode(QPOSService.CardTradeMode.SWIPE_TAP_INSERT_CARD);

            pos.setAmountIcon(getCurrency());

            pos.doCheckCard(30);
           // pos.doTrade(30);
        }

        @Override
        public void onRequestQposDisconnected() {
            statusCallBack(QPOS_DISCONNECTED, "Disconnected with cardreader.");
        }

        @Override
        public void onRequestSelectEmvApp(ArrayList<String> appList) {
            //Log.i(TAG, "inside onRequestSelectEmvApp");

            String[] appNameList = new String[appList.size()];

            //Log.i(TAG, "nummber of apps:" + appList.size());

            for (int i = 0; i < appNameList.length; ++i) {
                //Log.i(TAG, "i=" + i + "," + appList.get(i));
                appNameList[i] = appList.get(i);
            }

            appDialog = new Dialog(DSBaseTxActivity.this);
            appDialog.setContentView(R.layout.emv_app_dialog);
            appDialog.setTitle("Please select the account");

            appListView = (ListView) appDialog.findViewById(R.id.appList);
            appListView.setAdapter(new ArrayAdapter<String>(DSBaseTxActivity.this,
                    android.R.layout.simple_list_item_1, appNameList));
            appListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    pos.selectEmvApp(position);
                    dismissAppDialog();
                }

            });
            appDialog.findViewById(R.id.cancelButton).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            pos.cancelSelectEmvApp();
                            dismissAppDialog();
                        }
                    });
            appDialog.show();


            //pos.selectEmvApp(0);

        }

        @Override
        public void onRequestSetAmount() {
            TransactionType transactionType = TransactionType.GOODS;
            pos.setAmount(getAmount(), "0", "144", transactionType);
            //pos.setCardTradeMode(CardTradeMode.UNALLOWED_LOW_TRADE);
        }

        @Override
        public void onRequestSetPin() {
            //Log.i(TAG, "inside onRequestSetPin");
            //pos.sendPin("1234");
            // pos.sendPin("1111");
            pos.cancelPin();

        }

        @Override
        public void onRequestSignatureResult(byte[] arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRequestTime() {
            // String terminalTime =
            // sdf.format(Calendar.getInstance().getTime());
            pos.sendTime(sdf.format(Calendar.getInstance().getTime()));

        }

        @Override
        public void onRequestTransactionLog(String strLog) {
            //Log.i(TAG, "inside onRequestTransactionLog");
            //Log.i(TAG, "Log :: " + strLog) ;

        }

        @Override
        public void onRequestTransactionResult(
                TransactionResult transactionResult) {
            //	Log.i(TAG, "inside onRequestTransactionResult");
            //Log.i(TAG, "Result: " + transactionResult.toString());

            storeMessageLog("inside final chip result :  " + transactionResult.toString());

            isUpdatingIssuerScript = false;

            // _onTransactionCancel

            if (transactionResult == TransactionResult.CANCEL) {
                _onTransactionCancel();
                return;
            }

            if (transactionResult == TransactionResult.APPROVED) {
                if (Environment.getADVTStatus() == Advt.ON) {
                    if (strIccBatchLog != null) {
                        iccIssuerScriptSuccessCallBack(strIccBatchLog);
                        return;
                    }
                }

                processSignature();
                return;
            }

            if (transactionResult == TransactionResult.DECLINED) {
                if (isICCErrLog) {
                    if (strIccErrLog != null) {
                        if (isICCOnline && Environment.getADVTStatus() == Advt.OFF) {
                            _onTransactionDeclined();
                            return;
                        }
                        iccErrCallBack(strIccErrLog);
                        return;
                    }
                }

                if (strIccBatchLog != null) {
                    iccIssuerScriptFailCallBack(strIccBatchLog);
                    return;
                }

                _onTransactionDeclined();
                return;
            }

            if (transactionResult == TransactionResult.TERMINATED) {
                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }
                _onTransactionDeclined();
                return;
            }

            //_onCardBlocked

            if (transactionResult == TransactionResult.CARD_BLOCKED_OR_NO_EMV_APPS) {
                _onCardBlocked();
                return;
            }

            if (transactionResult == TransactionResult.SELECT_APP_FAIL) {
                statusCallBack(SELECT_APP_FAIL, "Chip is not responding correctly.");
                showDialog("Error", "Please remove the card and click ok.", 444);
                isFallback = true;
                //pos.powerOffIcc() ;
                //pos.doCheckCard(30);
                return;
            }

            if (transactionResult == TransactionResult.FALLBACK) {
                _onTransactionFallbackErr();
                return;
            }




        }

        @Override
        public void onRequestUpdateWorkKeyResult(UpdateInformationResult arg0) {
            // TODO Auto-generated method stub
            //pos.udpateWorkKey(workKey, workKeyCheck);
            //pos.udpateWorkKey(pik, pikCheck, trk, trkCheck, mak, makCheck)

        }

        @Override
        public void onRequestWaitingUser() {
            isWaitingForUserAction = true;
            if (!isFallback) {
                statusCallBack(WAITING_FOR_CARD, "Waiting to swipe / insert Credit /Debit card.");
            } else {
                statusCallBack(SELECT_APP_FAIL, "Please swipe the card.");
            }


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
        public void onReturnGetPinResult(Hashtable<String, String> res) {
            //Log.i(TAG, "inside onReturnGetPinResult");
            //Log.i(TAG, "get pin result :" + res);
            // TODO Auto-generated method stub


        }

        @Override
        public void onReturnNFCApduResult(boolean arg0, String arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnPowerOffIccResult(boolean res) {
            //pos.doCheckCard(30);

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
                    Log.e("JEYLOGS", e.getMessage());
                }
            }
        }
    }

}
