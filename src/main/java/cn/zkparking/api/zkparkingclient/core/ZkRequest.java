package cn.zkparking.api.zkparkingclient.core;

import java.util.Map;

public interface ZkRequest<T> {
    public String getApiMethodName();
    public Map<String, Object> getParams();
    public Map<String, String> getHeaders();
    public Class<T> getResponseClass();
}
