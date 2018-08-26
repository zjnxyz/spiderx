package app.bravo.zu.spiderx.http.client.okhttp;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类/接口注释
 *
 * @author jiangnan.zjn@alibaba-inc.com
 * @createDate 2018/7/13
 */
public class OkHttpClientBuilderBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpClientBuilderBox.class);

    private OkHttpClient.Builder okBuilder = null;

    public OkHttpClientBuilderBox() {
        okBuilder = new OkHttpClient.Builder();
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[] {
                x509TrustManager
            };
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager).hostnameVerifier(hostnameVerifier);
        } catch (Exception e) {
            LOGGER.error("ssl init fail.err={}", e.getMessage(), e);
        }
    }

    public OkHttpClient.Builder instance() {
        return this.okBuilder;
    }
}
