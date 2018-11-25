package com.mpos.connection;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.message.BufferedHeader;

import com.cba.payable.R;
import com.mpos.util.PLog;
import com.setting.env.Config;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class MposSSLSocketFactoryV2 {

	public static SSLSocketFactory getSSLSocketFactory(Context context) {

		SSLSocketFactory ret = null;

		try {

			final KeyStore ks = KeyStore.getInstance("BKS");

			InputStream inputStream = null;


			//Uncomment this if the release is a LIVE (Google Play) release

			if (Config.getBankCode(context).equals("seylan")) {
				inputStream = context.getResources().openRawResource(R.raw.clienttruststore_seylan);
			}  else if (Config.getBankCode(context).equals("boc")) {
				inputStream = context.getResources().openRawResource(R.raw.clienttruststore_boc);
			} else {
				inputStream = context.getResources().openRawResource(R.raw.clienttruststore_demo);
			}


			//Uncomment this if the release is a DEV or DEMO release
			// inputStream = context.getResources().openRawResource(R.raw.clienttruststore_demo);


			ks.load(inputStream, context.getString(R.string.store_pass).toCharArray());

			inputStream.close();
			ret = new SSLSocketFactory(ks);

		} catch (Exception ex) {

			PLog.e(PLog.TAG, ex.toString());
		}

		return ret;

	}

}
