package cn.zkparking.api.zkparkingclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseModel<T> implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long        id;

}
