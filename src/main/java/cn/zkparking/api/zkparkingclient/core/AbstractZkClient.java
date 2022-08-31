package cn.zkparking.api.zkparkingclient.core;

import cn.zkparking.api.zkparkingclient.utils.json.JsonHelper;
import cn.zkparking.api.zkparkingclient.utils.lang.MD5Util;
import cn.zkparking.api.zkparkingclient.utils.lang.StringUtils;
import cn.zkparking.api.zkparkingclient.utils.request.okhttp3.OkHttpRequest;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractZkClient implements ZkClient {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractZkClient.class);


    private String serverUrl;
    private String appId;
    private String appSecret;


    public AbstractZkClient(String serverUrl,  String appId, String appSecret) {
        this.serverUrl = serverUrl;
        this.appId = appId;
        this.appSecret = appSecret;
    }


    @Override
    public <T> ZkResponse execute(ZkRequest<T> request) {
        Map<String, Object> rt = doPost(request);
        if (rt == null) {
            return null;
        }

        ZkResponse tRsp = null;
        try {
            String responseBody = rt.get("res").toString();
            if (StringUtils.isEmpty(responseBody)) return tRsp;
            tRsp = new ZkResponse<>();
            Map map = JsonHelper.fromJson(responseBody, Map.class);
            int code = MapUtils.getIntValue(map, "code");
            String codeMsg = MapUtils.getString(map, "codeMsg");
            String data = MapUtils.getString(map, "data");
            T t = JsonHelper.fromJson(data, request.getResponseClass());
            tRsp.setCode(code);
            tRsp.setCodeMsg(codeMsg);
            tRsp.setData(t);
        } catch (Exception e) {
            LOG.info("error:"+e.getMessage());
        }
        return tRsp;
    }

    /**
     *
     *
     * @param request
     * @return
     */
    private <T> Map<String, Object> doPost(ZkRequest<T> request)  {
        Map<String, Object> params = request.getParams();
        Map<String, String> headers = request.getHeaders();
        String apiMethodName = request.getApiMethodName();
        String sign = MD5Util.sign(appSecret, params);
        params.put("sign", sign);
        OkHttpRequest okHttpRequest = new OkHttpRequest();
        Map<String, Object> result = okHttpRequest.doPostSync(serverUrl + apiMethodName, params, headers);
        return result;
    }
}
