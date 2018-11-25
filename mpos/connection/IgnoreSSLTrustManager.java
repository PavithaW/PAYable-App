package com.mpos.connection;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by "Don" on 6/5/2018.
 * Class Functionality :-
 */

public class IgnoreSSLTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Perform no check whatsoever on the validity of the SSL certificate
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Perform no check whatsoever on the validity of the SSL certificate
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
