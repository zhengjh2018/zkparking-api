package cn.zkparking.api.zkparkingclient.utils.request.okhttp3;


import cn.zkparking.api.zkparkingclient.utils.request.IServiceRequest;
import cn.zkparking.api.zkparkingclient.utils.request.okhttp.DefaultCallback;
import cn.zkparking.api.zkparkingclient.utils.request.okhttp.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * description: OkHttp3 请求
 *
 * @author : zhanjj
 * @version : 1.0
 * @since : 2017-09-09
 */
public class OkHttpRequest implements IServiceRequest {

    @Override
    public String sendPost(String apiUrl, Object xmlObj) throws Exception {

        Map<String, String> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addXmlObject(xmlObj).xml().post().build().doRequestSync(new DefaultCallback<String>(){
            @Override
            public void onSuccess(String s) {
                retMap.put("retStr", s);
            }
        });
        return retMap.get("retStr");
    }

    @Override
    public Map<String, Object> doGetASync(String apiUrl, Map<String, Object> reqParams) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).get().build().doRequestASync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Map<String, Object> doGetSync(String apiUrl, Map<String, Object> reqParams) {
        return doGetSync(apiUrl, reqParams, null);
    }

    @Override
    public Map<String, Object> doGetSync(String apiUrl, Map<String, Object> reqParams, Map<String, String> headers) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).addHeaders(headers).get().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Map<String, Object> doPostSync(String apiUrl, Map<String, Object> reqParams, Map<String, String> headers) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).addHeaders(headers).json().post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Map<String, Object> doPostSync(String apiUrl, String postData, Map<String, String> headers) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().addHeaders(headers).url(apiUrl).addPostData(postData).jsonPostData().post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }
    @Override
    public Map<String, Object> doPostASync(String apiUrl, Map<String, Object> reqParams) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).json().post().build().doRequestASync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }
    @Override
    public Map<String, Object> doPostASyncWithHeads(String apiUrl, Map<String, Object> reqParams,Map<String, String> headsParam) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().addHeaders(headsParam).url(apiUrl).addParams(reqParams).json().post().build().doRequestASync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Map<String, Object> doPostUrlEncode(String apiUrl, Map<String, Object> reqParams, Map<String, String> headers) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).addHeaders(headers).urlencode().post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Map<String, Object> doPostSync(String apiUrl, Map<String, Object> reqParams) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).json().post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    public Map<String, Object> doPostSyncWithHeads(String apiUrl, Map<String, Object> reqParams,Map<String, String> headsParam) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().addHeaders(headsParam).url(apiUrl).addParams(reqParams).json().post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }
    @Override
    public Map<String, Object> doPostSyncWithPostData(String apiUrl, Map<String, Object> reqParams) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addParams(reqParams).post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Map<String, Object> doPostSyncWithPostData(String apiUrl, String postData) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).addPostData(postData).post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }
    public Map<String, Object> doPostSync(String apiUrl) {
        Map<String, Object> retMap = new HashMap<>();
        SimpleHttpClient.newBuilder().url(apiUrl).post().build().doRequestSync(new DefaultCallback<Map>() {

            @Override
            public void onSuccess(Map map) {
                retMap.putAll(map);
            }
        });
        return retMap;
    }

    @Override
    public Object request2Object(String apiUrl, String postData) {
        return SimpleHttpClient.newBuilder().url(apiUrl).addPostData(postData).post().build().doRequest2Object();
    }
}
