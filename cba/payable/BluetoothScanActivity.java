package com.cba.payable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.pojo.APIData;
import com.mpos.util.AnimatedGifImageView;
import com.mpos.util.BluetoothPref;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

public class BluetoothScanActivity extends DSBaseTxActivity {

    String login_device_name = "";
    // BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
    BluetoothDevice device1, device2;
    private ProgressDialog mProgressDlg;
    private BluetoothAdapter mBluetoothAdapter;
    TextView tv_status, txtHeader;
    Button btnScan, btnCancel;
    ProgressBar pgScan;

    protected MsgClient m_client;
    protected MsgClientAmex m_client_amex;

    private int status = 0;

    private AnimatedGifImageView animatedGifImageView;

    BluetoothPref bluepref;

    private boolean boolDeviceFound = false, boolScanFinished = false;

    private static final int REQUEST_ENABLE_BT = 100;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    private String actionType = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);


        InitViews();

        //get action type
        Intent blueIntent = getIntent();
        actionType = blueIntent.getStringExtra("ACTION");

        boolDeviceFound = false;
        boolScanFinished = false;

        int stVisa = Config.getState(this);
        int stAmex = Config.getAmexState(this);

        if (stVisa == Config.STATUS_LOGIN) {
            login_device_name = BluetoothPref.generateValidDeviceName(getApplicationContext());
        } else if (stAmex == Config.STATUS_LOGIN) {
            login_device_name = BluetoothPref.generateValidDeviceNameAmex(getApplicationContext());
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mProgressDlg = new ProgressDialog(this);

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();

            } else {
                showDisabled();
            }
        }

        // ============If bluetooth is not supported, then show the
        // message========//

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        btnScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                blutoothFunctions();
            }
        });
    }

    @Override
    protected void onInitActivity(APIData... data) {

    }

    // @Override
    /*
     * public void onPause() { if (mBluetoothAdapter != null) { if
	 * (mBluetoothAdapter.isDiscovering()) {
	 * mBluetoothAdapter.cancelDiscovery(); } }
	 * 
	 * super.onPause(); }
	 */
    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void blutoothFunctions() {

        // If bluetooth is enabled then start the discovery
        if (lang_status == LangPrefs.LAN_EN) {
            tv_status.setText("Scanning Devices, Please wait...");
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.apply_scan_tv_status(tv_status, 0);

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.apply_scan_tv_status(tv_status, 0);
        } else {
            // do nothing
            tv_status.setText("Scanning Devices, Please wait...");
        }

        tv_status.setTextColor(Color.parseColor("#4786C6"));

        mBluetoothAdapter.startDiscovery();

        pgScan.setVisibility(View.VISIBLE);

        btnScan.setBackgroundColor(Color.parseColor("#B2AEAD"));
        btnScan.setEnabled(false);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    // showToast("Enabled");

                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                // mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // mProgressDlg.dismiss();

                boolScanFinished = true;

                if (!boolDeviceFound) {

                    if (lang_status == LangPrefs.LAN_EN) {
                        tv_status.setText("Reader not found.Please turn on your reader");
                    } else if (lang_status == LangPrefs.LAN_TA) {
                        tamlang_select.apply_scan_tv_status(tv_status, 2);

                    } else if (lang_status == LangPrefs.LAN_SIN) {
                        sinlang_select.apply_scan_tv_status(tv_status, 2);
                    } else {
                        // do nothing
                        tv_status.setText("Reader not found.Please turn on your reader");
                    }

                    tv_status.setTextColor(Color.RED);

                    btnScan.setVisibility(View.VISIBLE);
                    btnScan.setEnabled(true);
                    btnScan.setBackgroundDrawable(getResources().getDrawable(R.drawable.sign_in_btn_selector));

                }

                pgScan.setVisibility(View.GONE);

                // startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // The bluetooth device which is stored when login.

                String devicename_main = device.getName();
                /*
                 * if(devicename_main.contains("MPOS")) { devicename_main =
				 * device.getName().replace("MPOS", "").trim(); }
				 */

                if (login_device_name.equals(devicename_main)) {

                    device1 = device;

                    BluetoothPref.setDeviceReaderStatus(getApplicationContext(), devicename_main, device.getAddress());
                    boolDeviceFound = true;

                    if (lang_status == LangPrefs.LAN_EN) {
                        tv_status.setText("Reader is registered with app successfully");
                    } else if (lang_status == LangPrefs.LAN_TA) {
                        tamlang_select.apply_scan_tv_status(tv_status, 1);

                    } else if (lang_status == LangPrefs.LAN_SIN) {
                        sinlang_select.apply_scan_tv_status(tv_status, 1);
                    } else {
                        // do nothing
                        tv_status.setText("Reader is registered with app successfully");
                    }

                    Toast.makeText(getApplicationContext(), "Reader is registered with app successfully",
                            Toast.LENGTH_SHORT).show();

                    if (Home.isHomeActive) {
                        finish();
                    } else {
//                        Intent in = new Intent(getApplicationContext(), Home.class);
//                        startActivity(in);
//                        finish();

                        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                        ComponentName cn = homeIntent.getComponent();
                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                        startActivity(mainIntent);
                    }

                }

            }
        }
    };

    private void showEnabled() {

        blutoothFunctions();

    }

    private void showDisabled() {

        if (lang_status == LangPrefs.LAN_EN) {
            tv_status.setText("Bluetooth is turned off.Please turn it on");
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.apply_scan_tv_status(tv_status, 3);

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.apply_scan_tv_status(tv_status, 3);
        } else {
            // do nothing
            tv_status.setText("Bluetooth is turned off.Please turn it on");
        }

        tv_status.setTextColor(Color.RED);
        btnScan.setVisibility(View.VISIBLE);

        pgScan.setVisibility(View.GONE);

        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enabler, REQUEST_ENABLE_BT);

    }

    private void showUnsupported() {

        if (lang_status == LangPrefs.LAN_EN) {
            tv_status.setText("Bluetooth is unsupported by this device");
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.apply_scan_tv_status(tv_status, 4);

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.apply_scan_tv_status(tv_status, 4);
        } else {
            // do nothing
            tv_status.setText("Bluetooth is unsupported by this device");
        }

        tv_status.setTextColor(Color.RED);
    }

    private void fn_ProgressDialog() {
        mProgressDlg = new ProgressDialog(this);

        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });
    }

    private void InitViews() {
        // login_device_name = Config.getReaderId(getApplicationContext());

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        int reader = Config.getActiveReader(getApplicationContext());

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_status.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnScan = (Button) findViewById(R.id.btnScan);

        btnCancel = (Button) findViewById(R.id.btnCancel);

        pgScan = (ProgressBar) findViewById(R.id.pgScan);

        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.gifView));

        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);


        if (Config.getReaderType(getApplicationContext()) == 3) {

            animatedGifImageView.setAnimatedGif(R.raw.pair_pinpad, AnimatedGifImageView.TYPE.FIT_CENTER);

        }else if(getIntent().getBooleanExtra("isAmex", false)){

            animatedGifImageView.setAnimatedGif(R.raw.pair_ntb, AnimatedGifImageView.TYPE.FIT_CENTER);

        }else if(getIntent().getStringExtra("BANK_CODE") != null){

            String bankCode = getIntent().getStringExtra("BANK_CODE");
            if (bankCode.equals("hnb")) {
                animatedGifImageView.setAnimatedGif(R.raw.pair_hnb, AnimatedGifImageView.TYPE.FIT_CENTER);
            } else if (bankCode.equals("seylan")) {
                animatedGifImageView.setAnimatedGif(R.raw.pair_seylan, AnimatedGifImageView.TYPE.FIT_CENTER);
            } else if (bankCode.equals("commercial")) {
                animatedGifImageView.setAnimatedGif(R.raw.pair_combank, AnimatedGifImageView.TYPE.FIT_CENTER);
            } else if (Config.getBankCode(getApplicationContext()).equals("boc")) {
                animatedGifImageView.setAnimatedGif(R.raw.pair_boc, AnimatedGifImageView.TYPE.FIT_CENTER);
            }else {
                animatedGifImageView.setAnimatedGif(R.raw.pair_ntb, AnimatedGifImageView.TYPE.FIT_CENTER);
            }

        }else {
            //if not pin pad, set the anim relevant to bank
            if ((m_client.isLoggedIn() && m_client_amex.isLoggedIn()) || m_client.isLoggedIn()) {
                if (Config.getBankCode(getApplicationContext()).equals("hnb")) {
                    animatedGifImageView.setAnimatedGif(R.raw.pair_hnb, AnimatedGifImageView.TYPE.FIT_CENTER);
                } else if (Config.getBankCode(getApplicationContext()).equals("seylan")) {
                    animatedGifImageView.setAnimatedGif(R.raw.pair_seylan, AnimatedGifImageView.TYPE.FIT_CENTER);
                } else if (Config.getBankCode(getApplicationContext()).equals("commercial")) {
                    animatedGifImageView.setAnimatedGif(R.raw.pair_combank, AnimatedGifImageView.TYPE.FIT_CENTER);
                } else if (Config.getBankCode(getApplicationContext()).equals("boc")) {
                    animatedGifImageView.setAnimatedGif(R.raw.pair_boc, AnimatedGifImageView.TYPE.FIT_CENTER);
                }else {
                    animatedGifImageView.setAnimatedGif(R.raw.pair_ntb, AnimatedGifImageView.TYPE.FIT_CENTER);
                }
            }else if ( m_client_amex.isLoggedIn()){
                animatedGifImageView.setAnimatedGif(R.raw.pair_ntb, AnimatedGifImageView.TYPE.FIT_CENTER);
            }
        }

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (com.setting.env.Environment.is_demo_version) {
            animatedGifImageView.setVisibility(View.GONE);
        } else {
            animatedGifImageView.setVisibility(View.VISIBLE);
        }

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.apply_bluetoothscan(txtHeader, btnScan, btnCancel);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.apply_bluetoothscan(txtHeader, btnScan, btnCancel);
        } else {
            // do nothing
        }

    }

    @Override
    protected POS_TYPE getPosType() {
        return null;
    }

    @Override
    protected void statusCallBack(int status, String message) {

    }

    @Override
    protected String getAmount() {
        return null;
    }

    @Override
    protected String getCurrency() {
        return null;
    }

    @Override
    protected void swipeCallBack(String ksn, String cardHolder, String track2, String maskedPan) {

    }

    protected void nfcCallBack(String ksn, String cardHolder, String track2, String maskedPan,String nfcdata){

    }


    @Override
    protected void emvCallBack(String data) {

    }

    @Override
    protected void iccErrCallBack(String data) {

    }

    @Override
    protected void iccIssuerScriptSuccessCallBack(String data) {

    }

    @Override
    protected void iccIssuerScriptFailCallBack(String data) {

    }

    @Override
    protected void navigateToSigScreen() {

    }

    @Override
    public void onBackPressed() {
//        if (actionType.equals("settings")) {
//            finish();
//        } else if (actionType.equals("amexlogin")) {
//            Setting.isSettingsClose = true;
//            finish();
//        } else {
//
//            if (Home.isHomeActive) {
//                finish();
//            } else {
//                Intent in = new Intent(getApplicationContext(), Home.class);
//                startActivity(in);
//                finish();
//            }
//        }

        if (actionType.equals("settings")) {
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            startActivity(mainIntent);
        }
    }

}
