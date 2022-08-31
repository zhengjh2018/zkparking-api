package cn.zkparking.api.zkparkingclient.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZkResponse<T> implements Serializable {
    private int  code;
    private String  codeMsg;
    private T data;
}
