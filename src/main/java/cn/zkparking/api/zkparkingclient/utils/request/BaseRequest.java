package cn.zkparking.api.zkparkingclient.utils.request;

import java.util.Map;

/**
 * description: 第三方api基类
 *
 * @author : zhanjj
 * @version : 1.0
 * @since : 2017-09-09
 */
public class BaseRequest {
    // API的地址
    private String          apiURL;

    // 发请求的HTTPS请求器
    private IServiceRequest serviceRequest;

    public BaseRequest(String api) throws Exception{
        apiURL = api;
        Class c = Class.forName("cn.zkparking.api.zkparkingclient.utils.request.okhttp3.OkHttpRequest");
        serviceRequest = (IServiceRequest) c.newInstance();
    }

    protected Map<String,Object> doGetSync()  {
        return serviceRequest.doGetSync(apiURL, null);
    }

    public Map<String, Object> doGetSync(Map<String, Object> reqParams, Map<String, String> headerParams) {
        return this.serviceRequest.doGetSync(apiURL, reqParams, headerParams);
    }


    public Map<String, Object> doPostSync(Map<String, Object> reqParams, Map<String, String> headerParams) {
        return this.serviceRequest.doPostSync(apiURL, reqParams, headerParams);
    }

    public String sendPost( Object xmlObj) throws Exception{
        return this.serviceRequest.sendPost(apiURL,xmlObj);
    }

    public Map<String, Object> doPostSync(Map<String, Object> reqParams) {
        return this.serviceRequest.doPostSync(apiURL, reqParams);
    }
    public Map<String, Object> doPostASync(Map<String, Object> reqParams) {
        return this.serviceRequest.doPostASync(apiURL, reqParams);
    }


    public Map<String, Object> doPostASyncWithHeads(Map<String, Object> reqParams,Map<String, String> headsParams) {
        return this.serviceRequest.doPostASyncWithHeads(apiURL, reqParams,headsParams);
    }


    public Map<String, Object> doPostUrlEncode(Map<String, Object> reqParams,Map<String, String> headsParams) {
        return this.serviceRequest.doPostUrlEncode(apiURL, reqParams,headsParams);
    }


    public Map<String, Object> sendPostData(String postData) {
        return this.serviceRequest.doPostSyncWithPostData(apiURL, postData);
    }

    public Map<String, Object> sendPostData(Map<String, Object> reqParams) {
        return this.serviceRequest.doPostSyncWithPostData(apiURL, reqParams);
    }

    public Object request2Object(String postData) {
        return this.serviceRequest.request2Object(apiURL, postData);
    }



}
