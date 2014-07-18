package com.flowerworld.app.tool.http;

import android.text.TextUtils;
import com.flowerworld.app.tool.util.LOG;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class HttpRequestHelper {
    private static final String TAG = HttpRequestHelper.class.getSimpleName();

    public static String requestHttpGet(String url, Map<String, Object> parameters) {
        String resultStr = null;
        try {
            if (null != parameters && !parameters.isEmpty()) {
                url += getUrlParameter(parameters);
            }
            LOG.i(TAG, "===request=get=url: " + url);
            HttpClient client = SSLSocketFactoryEx.getNewHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = null;
            response = client.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultStr = EntityUtils.toString(response.getEntity());
            } else {
                resultStr = HttpResultStatus.ERROR_CONNECT_SERVER_FAILED;
            }

            if (TextUtils.isEmpty(resultStr)) {
                resultStr = HttpResultStatus.ERROR_RESPONSE_EMPTY;
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_TIME_OUT;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_REQUEST_PARAMETER;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_ILLEGAL_URL;
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_DEFAULT;
        }
        return resultStr;
    }

    /**
     * 拼接url参数，get方式时使用
     */
    public static String getUrlParameter(Map<String, Object> parameters) {
        String urlParamete = "?";
        if (null != parameters && !parameters.isEmpty()) {
            Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                LOG.d(entry.getKey() + "", entry.getValue() + "");
                urlParamete += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString()) + "&";
            }
            if (urlParamete.length() > 1) {
                urlParamete = urlParamete.substring(0, urlParamete.length() - 1);
            }
        }
        return urlParamete;
    }

    public static String requestHttpPost(String url, Map<String, Object> parameters) {
        LOG.i(TAG, "===request=post=url: " + url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (null != parameters && !parameters.isEmpty()) {
            Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                LOG.d(entry.getKey() + "", entry.getValue() + "");
                params.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
        }

        String resultStr = null;
        try {
            HttpClient client = SSLSocketFactoryEx.getNewHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("charset", HTTP.UTF_8);
            HttpEntity he = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(he);
            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultStr = EntityUtils.toString(response.getEntity());
            } else {
                resultStr = HttpResultStatus.ERROR_CONNECT_SERVER_FAILED;
            }

            if (TextUtils.isEmpty(resultStr)) {
                resultStr = HttpResultStatus.ERROR_RESPONSE_EMPTY;
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_TIME_OUT;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_REQUEST_PARAMETER;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_ILLEGAL_URL;
        } catch (HttpHostConnectException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_CONNECT_SERVER_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_DEFAULT;
        }
        return resultStr;
    }

    public static String requestHttpPostWithFile(String url, Map<String, Object> parameters) {
        LOG.i(TAG, "===requestHttpPostWithFile=url: " + url);

        MultipartEntity mpEntity = new MultipartEntity();

        if (null != parameters && !parameters.isEmpty()) {
            Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
            Entry<String, Object> entry = null;
            String key = null;
            Object value = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                key = entry.getKey();
                value = entry.getValue();

                try {
                    if (value instanceof File) {
                        mpEntity.addPart(key, new FileBody((File) value));
                    } else if (value instanceof String) {
                        mpEntity.addPart(key, new StringBody((String) value, Charset.forName("utf-8")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String resultStr = null;
        try {
            HttpClient client = SSLSocketFactoryEx.getNewHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("charset", HTTP.UTF_8);
            httpPost.setEntity(mpEntity);
            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultStr = EntityUtils.toString(response.getEntity());
            } else {
                resultStr = HttpResultStatus.ERROR_CONNECT_SERVER_FAILED;
            }

            if (TextUtils.isEmpty(resultStr)) {
                resultStr = HttpResultStatus.ERROR_RESPONSE_EMPTY;
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_TIME_OUT;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_REQUEST_PARAMETER;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_ILLEGAL_URL;
        } catch (HttpHostConnectException e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_CONNECT_SERVER_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = HttpResultStatus.ERROR_DEFAULT;
        }
        return resultStr;
    }
}
