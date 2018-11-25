package com.mpos.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.mpos.util.Crypto;
import com.mpos.util.PLog;
import com.setting.env.Config;
import com.setting.env.Environment;
import com.setting.env.Environment.Env;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import javax.net.ssl.SSLContext;

public class Connector {

	private static final String LOG_TAG = "payable " + Connector.class.getSimpleName();

	protected static final String TAG = "JEYLOGS";

	public static String callApi(CallParams params ,  Context context) throws ApiException {

		Log.i(LOG_TAG, "UserId: " + params.getStrUserId()) ;
		Log.i(LOG_TAG, "StrLogin: " + params.getStrLogin()) ;
		Log.i(LOG_TAG, "AuthId1: " + params.getStrAuthId1()) ;
		Log.i(LOG_TAG, "AuthId2: " + params.getStrAuthId2()) ;
		Log.i(LOG_TAG, "DeviceId: " + params.getDeviceId()) ;
		Log.i(LOG_TAG, "SimId: " + params.getSimId()) ;
		Log.i(LOG_TAG, "Endpoint: " + params.getEndpoint().toString());


		EnumApi endpoint = params.getEndpoint();


		if (endpoint == null)
			throw new ApiException(ApiException.INTERNAL_ERROR,
					"Unknown API-endpoint");

		// BufferedReader reader = null;
		// android.net.Uri uri = null;

		try {

			/*
			 * HttpRequestBase request = endpoint.getHttpRequest(params
			 * .getPayLoadStr());
			 */

			String strPayLoad = params.getPayLoadStr();

			Log.i(LOG_TAG, "PayLoad: " + strPayLoad) ;

			// String strMD5 = getMD5Token(params.getPayLoadStr());

			String strSignature = "";

			HttpRequestBase request = endpoint.getHttpRequest(strPayLoad , context , params.getServerIndex());

			if (strPayLoad != null && strPayLoad.length() > 0) {
				// strSignature = Crypto.generateSHA1(strPayLoad) ;

				strSignature = Crypto.generateMD5(strPayLoad);

				Log.i(LOG_TAG, "strSignature = " + strSignature);
			}

			request.setHeader("User-Agent", "MPOS-ANDROID-Client");
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Accept", "application/json");
			// request.setHeader("Accept-Encoding", "gzip");

			request.setHeader("Ver-Sig",
					Integer.toString(Environment.getVersionId()));
			request.setHeader("Type-Sig",
					Integer.toString(Environment.getAppTypeId()));
			request.setHeader("Content-Sig", strSignature);

			request.setHeader("mpos-userid", params.getStrUserId());
			request.setHeader("mpos-login", params.getStrLogin());
			request.setHeader("mpos-auth1", params.getStrAuthId1());
			request.setHeader("mpos-auth2", params.getStrAuthId2());

			request.setHeader("mpos-deviceid", params.getDeviceId());
			request.setHeader("mpos-simid", params.getSimId());


			SchemeRegistry schemeRegistry = new SchemeRegistry();


			//Uncomment this if the release is a LIVE (Google Play) release..
			if (Config.getBankCode(context).equals("hnb") || Config.getBankCode(context).equals("commercial") || Config.getBankCode(context).equals("ntb") || params.getServerIndex() == 2 || Config.getBankCode(context).equals("cargills")) {
				if (android.os.Build.VERSION.SDK_INT > 20) {
					schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
				}else{
					schemeRegistry.register(new Scheme("https", new TlsSniSocketFactory(), 443));
				}

			} else {
				if (Environment.getEnvironment() == Environment.Env.DEV) {
					schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
				} else {

					schemeRegistry.register(new Scheme("https", MposSSLSocketFactoryV2.getSSLSocketFactory(params .getContext()), 8080));
					Log.d(LOG_TAG, "SSL");
				}
			}



			//Uncomment this if the release is a DEV or DEMO release
//			if (Config.getBankCode(context).equals("hnb") ) {
//				if (android.os.Build.VERSION.SDK_INT > 20) {
//					schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//				}else{
//					schemeRegistry.register(new Scheme("https", new TlsSniSocketFactory(), 443));
//				}
//
//			} else {
//				if (Environment.getEnvironment() == Environment.Env.DEV) {
//					schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
//				} else {
//
//					schemeRegistry.register(new Scheme("https", MposSSLSocketFactoryV2.getSSLSocketFactory(params .getContext()), 8080));
//					Log.d(LOG_TAG, "SSL");
//				}
//			}


			HttpParams httpParameters = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
			HttpConnectionParams.setSoTimeout(httpParameters, 30000);

			ClientConnectionManager cm = new ThreadSafeClientConnManager(
					httpParameters, schemeRegistry);





			HttpClient httpclient = new DefaultHttpClient(cm, httpParameters);

			// code for routing
			//((AbstractHttpClient) httpclient).setRoutePlanner(new DefaultHttpRoutePlanner(schemeRegistry));

			HttpResponse httpResponse = httpclient.execute(request);

			//PLog.i("jeylogs", "after execute ..");
			StatusLine status = httpResponse.getStatusLine();
			Log.i(LOG_TAG, "Status Code: " + status.toString()) ;
			int statusCode = status.getStatusCode();

			//Log.i("jeylogs", "status code:" + statusCode);
			//Log.i("jeylogs", "point 30");
			if (statusCode == 200) {
				return extractData(httpResponse);

			}

			String strMsg = null;
			String strCode = null;

			Header headers[] = httpResponse.getHeaders("mpos-expMsg");

			if (headers != null && headers.length > 0) {
				Header h1 = headers[0];
				strMsg = h1.getValue();
			}

			if (strMsg == null) {
				strMsg = status.getReasonPhrase();
			}

			Header headers2[] = httpResponse.getHeaders("mpos-expCode");

			if (headers2 != null && headers2.length > 0) {
				Header h1 = headers2[0];
				strCode = h1.getValue();
			}





			if (strMsg != null) {

				ApiException apiException = null;

				if (strCode != null) {
					apiException = new ApiException(Integer.parseInt(strCode), strMsg);
				}else{
					apiException =  new ApiException(status.getStatusCode(), strMsg);
				}
				//throw new ApiException(status.getStatusCode(), strMsg);

				Header isheaders1[] = httpResponse.getHeaders("emv-is-status");

				if (isheaders1 != null && isheaders1.length > 0){
					Header h = isheaders1[0];
					String res = h.getValue() ;

					if(res != null && res.equals("1")){
						Header isheaders2[] = httpResponse.getHeaders("emv-is");

						Header h1 = isheaders2[0];
						String strIS = h1.getValue() ;

						if(strIS != null){
							apiException.setIsState(1);
							apiException.setIsMessage(strIS);
						}
					}
				}

				throw  apiException ;
			}

			throw new ApiException(status.getStatusCode(),
					"Unexpacted HTTP response code");

			// throw new
			// ApiException("Unexpacted HTTP response code: "+status.getStatusCode());

		} catch (ApiException ae) {
			//PLog.i("JEYLOGS", "Connector:ApiException::" + ae.toString());
			Log.i(LOG_TAG, "ApiException:" + ae.toString()) ;
			throw ae;
		}

		catch (SocketTimeoutException e) {
			//PLog.i("JEYLOGS", "Timeout exception :: " + e.toString());
			Log.i(LOG_TAG, "SocketTimeoutException: " + e.toString()) ;

			throw new ApiException(ApiException.TIMEOUT_ERROR, e.toString());
		}catch (SocketException e){
//			Log.i("JEYLOGS", "inside SocketException block :: " + e.toString());
			Log.i(LOG_TAG, "SocketException: " + e.toString()) ;
			throw new ApiException(ApiException.SCOCKET_EXCEPTION, e.toString());
		}
		catch (Exception e) {
			//PLog.i("JEYLOGS", "Connector:Exception :: " + e.toString());
			// throw new ApiException(ApiException.INTERNAL_ERROR,
			// e.getMessage());
//			Log.i("JEYLOGS", "inside exception block :: " + e.toString());
			Log.i(LOG_TAG, "Exception:" + e.toString()) ;

			throw new ApiException(ApiException.SCOCKET_EXCEPTION, e.toString());
		}

	}

	public static String extractData(HttpResponse res) throws IOException {
		String strdata = null;

		InputStream in = res.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		StringBuilder str = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null) {
			str.append(line + "\n");
		}

		in.close();
		strdata = str.toString();

		return strdata;
	}

}
