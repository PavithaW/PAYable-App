package com.mpos.connection;

import android.content.Context;
import android.util.Log;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import com.cba.payable.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;

public class MposHttpClient extends DefaultHttpClient {

    private static Context appContext = null;
    private static HttpParams params = null;
    private static SchemeRegistry schmReg = null;
    private static Scheme httpsScheme = null;
    private static Scheme httpScheme = null;
    private static String TAG = "MyHttpClient";

    public MposHttpClient(Context myContext) {

        appContext = myContext;

        //if (httpScheme == null || httpsScheme == null) {
            //httpScheme = new Scheme("http", PlainSocketFactory.getSocketFactory(), 80);
            httpsScheme = new Scheme("https", mySSLSocketFactory(), 8080);
        //}
            
            Log.d(TAG, "here");

        //getConnectionManager().getSchemeRegistry().register(httpScheme);
        getConnectionManager().getSchemeRegistry().register(httpsScheme);

    }

    private SSLSocketFactory mySSLSocketFactory() {
        SSLSocketFactory ret = null;
        try {
            final KeyStore ks = KeyStore.getInstance("BKS");

            final InputStream inputStream = appContext.getResources().openRawResource(R.raw.clienttruststore);
           // final InputStream inputStream = appContext.getResources().openRawResource(R.raw.client);

            ks.load(inputStream, appContext.getString(R.string.store_pass).toCharArray());
            inputStream.close();
            
            Log.d(TAG, "here");

            ret = new SSLSocketFactory(ks);
        } catch (UnrecoverableKeyException ex) {
            Log.d(TAG, ex.getMessage() + "1");
        } catch (KeyStoreException ex) {
            Log.d(TAG, ex.getMessage() + "2");
        } catch (KeyManagementException ex) {
            Log.d(TAG, ex.getMessage()+ "3");
        } catch (NoSuchAlgorithmException ex) {
            Log.d(TAG, ex.getMessage()+ "4");
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage()+ "5");
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage()+ "6");
        } 
        
        return ret;

    }
    
}
