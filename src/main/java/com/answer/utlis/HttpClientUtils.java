package com.answer.utlis;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author zhaolingzhi
 * @date 2020/7/21 10:52 AM
 */
public class HttpClientUtils {


    public static final String CHARSET = "UTF-8";

    private static CloseableHttpClient httpClient = createSSLInsecureClient();

    public static final Integer HTTP_OK = 200;

    public static final String CONTENT_TYPE_NAME = "Content-Type";

    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    public static final String CONTENT_TYPE_XML = "text/xml;charset=UTF-8";

    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

    public static final String ACCEPT_NAME = "Accept";
    public static final String ACCEPT = "application/json;charset=UTF-8";

    //这个要弄长点
    public static final  int TIMEOUT = 60000;



    public static String getUrl(String url, Map<String, Object> params) {
        String result = null;
        //返回结果,释放链接
        CloseableHttpResponse response = null;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        try {


            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT * 1000)
                    .setConnectTimeout(TIMEOUT * 1000)
                    .setConnectionRequestTimeout(TIMEOUT * 1000)
                    .build();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
            httpGet.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_FORM);
            httpGet.addHeader(ACCEPT_NAME, ACCEPT);
            httpGet.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            httpGet.setConfig(defaultRequestConfig);

            //建立链接得到返回结果
            response = httpClient.execute(httpGet);
            //返回的结果码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                return null;
            }
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                return null;
            } else {
                result = EntityUtils.toString(httpEntity, CHARSET);
            }
            //按照官方文档的说法：二者都释放了才可以正常的释放链接
            EntityUtils.consume(httpEntity);
            response.close();
            return result;
        } catch (Exception e) {
            return null;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
                    new TrustStrategy() {

                        public boolean isTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return HttpClients.custom().setMaxConnTotal(100).setMaxConnPerRoute(10)
                    .setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

}
