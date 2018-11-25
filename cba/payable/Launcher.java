package com.cba.payable;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.mpos.pojo.Merchant;
import com.mpos.util.BluetoothPref;
import com.setting.env.Config;

import java.util.Arrays;

public class Launcher extends Activity {

    private static final String LOG_TAG = "payable " + Launcher.class.getSimpleName();
    public static boolean isRooted = false;
    private static int PERMISSION_REQUESTS = 1011;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate : ");
        setContentView(R.layout.activity_splash);

    }

    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume : ");

        //if marschmallow or greater
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUESTS);
            } else {
                _startTimer();
            }
        }else{
            _startTimer();
        }


    }


    //-1 is returned in grantResults[] if any one of the permission is DENIED
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(contains(grantResults, -1)){
            Toast.makeText(this, "You must allow both permissions", Toast.LENGTH_LONG).show();
        }else{
            _startTimer();
        }
    }

    public boolean contains(final int[] array, final int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    private void _startTimer() {
        Log.d(LOG_TAG, "_startTimer : ");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Thread.sleep(1000);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            fireActivity();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void fireActivity() {
        Log.d(LOG_TAG, "fireActivity : ");
        isRooted = RootUtil.isDeviceRooted();
        int st = Config.getState(this);
        int stAmex = Config.getAmexState(this);

        Log.d(LOG_TAG, "fireActivity : App Status : " + st);
        Log.d(LOG_TAG, "fireActivity : PWD_FLAG Visa: " + Config.getPwdFlag(this));
        Log.d(LOG_TAG, "fireActivity : PWD_FLAG AMEX : " + Config.getPwdFlagAmex(this));

        if (Config.getPwdFlag(this) == Merchant.PWD_FLAG_PORTAL
                || (st == Config.STATUS_LOGOUT && stAmex == Config.STATUS_LOGOUT)) {
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }


        //IF AMEX NOT CONFIRM CHANGE PW NEED TO RELOAD AGAIN CHANGE PW SCREEN
        if (Config.getPwdFlagAmex(this) == Merchant.PWD_FLAG_PORTAL) {
            Home.isAmexCardSelected = true;
            Intent intent = new Intent(this, ChangePwd.class);
            intent.putExtra("fromLauncher", true);
            startActivity(intent);
            finish();
            return;
        }


        if (st == Config.STATUS_LOGIN || stAmex == Config.STATUS_LOGIN) {

            boolean isRegDevice = false;

            if (st == Config.STATUS_LOGIN) {
                isRegDevice = BluetoothPref.isRegiesterRequired(getApplicationContext());
            } else if (stAmex == Config.STATUS_LOGIN) {
                isRegDevice = BluetoothPref.isRegiesterRequiredAmex(getApplicationContext());
            }

            if (isRegDevice) {
                Intent intent = new Intent(this, BluetoothScanActivity.class);
                intent.putExtra("ACTION", "normal");
                startActivity(intent);
                finish();
                return;
            }

            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
            return;
        }

		/*if(st == Config.STATUS_LOGOUT ||Config.getPwdFlag(this) == Merchant.PWD_FLAG_PORTAL){
            Log.d(LOG_TAG, "fireActivity : PWD_FLAG : " + Config.getPwdFlag(this));
			startActivity(new Intent(this, Login.class));
			//startActivity(new Intent(this, LoginSecondary.class));
			finish() ;
			return ;
		}
		
		if(st == Config.STATUS_LOGIN){
			
			
			if(BluetoothPref.isRegiesterRequired(getApplicationContext())){
				Intent intent = new Intent(this, BluetoothScanActivity.class);
				intent.putExtra("ACTION", "normal");
				startActivity(intent);
				finish();
				return ;
			}
			
			Intent intent = new Intent(this, Home.class);
			startActivity(intent);
			finish() ;
			return ;
		}*/
    }

}
