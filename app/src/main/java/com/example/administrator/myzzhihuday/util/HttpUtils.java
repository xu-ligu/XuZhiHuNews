package com.example.administrator.myzzhihuday.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.ResponseHandlerInterface;

import java.security.KeyStore;

import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * 定义全局的httpclient
 */
public class HttpUtils {


    private static AsyncHttpClient client = new AsyncHttpClient(getSchemeRegistry());

    public static void get(String url, ResponseHandlerInterface responseHandlerInterface) {

        client.get(Constant.BASEURL + url, responseHandlerInterface);
    }

    public static void getImage(String url, ResponseHandlerInterface responseHandlerInterface) {
        client.get(url, responseHandlerInterface);
    }


    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;

    }

    public static SchemeRegistry getSchemeRegistry() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);//允许所有主机的验证
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            return registry;
        } catch (Exception e) {
            return null; }
    }


}