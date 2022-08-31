package cn.zkparking.api.zkparkingclient.core;

public interface ZkClient {
    /**
     * @param <T>
     * @param request
     * @return
     */
    public <T> ZkResponse execute(ZkRequest<T> request) ;
}
