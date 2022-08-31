package cn.zkparking.api.zkparkingclient.core;

import cn.zkparking.api.zkparkingclient.model.ParkEnterpriseModel;

import java.util.Map;

public class ZkEnterpriseRequest implements ZkRequest {
    private String apiMethodMane;
    private Map<String, Object> params;
    private Map<String, String> headers;

    public void setApiMethodMane(String apiMethodMane) {
        this.apiMethodMane = apiMethodMane;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String getApiMethodName() {
        return this.apiMethodMane;
    }

    @Override
    public Map<String, Object> getParams() {
        return this.params;
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public Class<ParkEnterpriseModel> getResponseClass() {
        return ParkEnterpriseModel.class;
    }
}
