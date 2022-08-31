package cn.zkparking.api.zkparkingclient.utils.request;

import java.io.File;
import java.util.Map;

/**
 * description: 这里定义服务层需要请求器标准接口
 *
 * @author : zhanjj
 * @version : 1.0
 * @since : 2017-09-09
 */
public interface IServiceRequest {

    // Service依赖的底层https请求器必须实现这么一个接口
    String sendPost(String apiUrl, Object xmlObj) throws Exception;

    /**
     * Get 异步请求
     * 
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @return Map
     */
    Map<String, Object> doGetASync(String apiUrl, Map<String, Object> reqParams);

    /**
     * Get 同步请求
     * 
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @return Map
     */
    Map<String, Object> doGetSync(String apiUrl, Map<String, Object> reqParams);

    /**
     * Get 同步请求
     *
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @param headers 自定义请求头
     * @return Map
     */
    Map<String, Object> doGetSync(String apiUrl, Map<String, Object> reqParams, Map<String, String> headers);



    /**
     * POST 同步请求 , JSON提交方式
     *
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @param headers 自定义请求头
     * @return Map
     */
    Map<String, Object> doPostSync(String apiUrl, Map<String, Object> reqParams, Map<String, String> headers);

    /**
     * POST 同步请求 , JSON提交方式
     *
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @param headers 自定义请求头
     * @return Map
     */
    Map<String, Object> doPostSync(String apiUrl, String reqParams, Map<String, String> headers);

    /**
     * POST 同步请求, JSON提交方式
     *
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @return Map
     */
     Map<String, Object> doPostSync(String apiUrl, Map<String, Object> reqParams);

    /**
     * POST 异步请求, JSON提交方式
     *
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @return Map
     */
    Map<String, Object> doPostASync(String apiUrl, Map<String, Object> reqParams);


    /**
     * POST 异步请求, JSON提交方式
     *
     * @param apiUrl API URL
     * @param reqParams 请求参数集合
     * @param headsParams 请求头部
     * @return Map
     */
    Map<String, Object> doPostASyncWithHeads(String apiUrl, Map<String, Object> reqParams,Map<String, String> headsParams);



    Map<String, Object> doPostUrlEncode(String apiUrl, Map<String, Object> reqParams, Map<String, String> headers);
    /**
     * POST 同步请求
     * @param apiUrl API URL
     * @param postData  请求体数据
     * @return  Map
     */
    Map<String, Object> doPostSyncWithPostData(String apiUrl, String postData);

    Map<String, Object> doPostSyncWithPostData(String apiUrl, Map<String, Object> reqParams);

    Object request2Object(String apiUrl, String postData);
}
