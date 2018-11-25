package com.mpos.connection;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.mpos.util.PLog;

public class MposTrustManager implements X509TrustManager {
	
	private static final String TAG = "JEYLOGS";

	private static String PUB_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100df9d233e0475dd71740bbb705f36d7e0e6d1828efd4dc9f6dc4bf4cbda8af9c1831e5b20763faaeda021ef696bb00dc929864499f10b63ae1190a9beb037ef3a63dd6c4d09ed15bffd0ee3a136b5665b39aff7697a840dfd56a18565c0eef69bd4bc17f9ecee136d3e6145470ad2131a1e63e91b569ebf2fe23ca12d1208271aeeffc2997908c5e80d8293de5c52ed4650552fa6a18ac36d8345a575fc7dfa28082da1f75b4c34da66e17f231852cee0dbec7b48060d3ebf618a656b84725ffbf5bac63bfda4f4bbfe4d5c438023d6cfaf6c18c789f62081b2291c7b9ec4fa344d65116a225d3f0fde5cc10f52ffaa4126c53658ee9b773f3baf3371f24b829d0203010001";

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		if (chain == null) {
			PLog.e(TAG, "checkServerTrusted: X509Certificate array is null") ;
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate array is null");
		}
		
		if (!(chain.length > 0)) {
			PLog.e(TAG, "checkServerTrusted: X509Certificate is empty") ;
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate is empty");
		}
		
		if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
			PLog.e(TAG, "checkServerTrusted: AuthType is not RSA") ;
			throw new CertificateException(
					"checkServerTrusted: AuthType is not RSA");
		}
		
		TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance("X509");
			tmf.init((KeyStore) null);

			for (TrustManager trustManager : tmf.getTrustManagers()) {
				((X509TrustManager) trustManager).checkServerTrusted(
						chain, authType);
			}

		} catch (Exception e) {
			throw new CertificateException(e);
		}
		
		RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
		String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
		
		final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
		
		
		if (!expected) {
			PLog.e(TAG, "checkServerTrusted: Certificate mismatched.") ;
			throw new CertificateException("checkServerTrusted: Certificate mismatched.");
		}
	}

	public void checkClientTrusted(X509Certificate[] xcs, String string) {
		// throw new
		// UnsupportedOperationException("checkClientTrusted: Not supported yet.");
	}

	public X509Certificate[] getAcceptedIssuers() {
		// throw new
		// UnsupportedOperationException("getAcceptedIssuers: Not supported yet.");
		return null;
	}

}
