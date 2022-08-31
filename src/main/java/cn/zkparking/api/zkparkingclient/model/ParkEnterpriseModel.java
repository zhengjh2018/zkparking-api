package cn.zkparking.api.zkparkingclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.List;





@Data
public class ParkEnterpriseModel  extends BaseModel{

    @ApiModelProperty(value = "机构id",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long businessesid;

    @ApiModelProperty(value = "车场id",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long enterpriseid;

    @ApiModelProperty(value = "车场规则id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long ruleid;

    @ApiModelProperty("停车场名称")
    private String parkname;

    @ApiModelProperty("停车场地址")
    private String parkaddress;

    @ApiModelProperty("本地服务器IP")
    private String localip;

    @ApiModelProperty("本地服务器Port")
    private int localport;

    @ApiModelProperty("只支持月租车")
    private byte onlyrent;

    @ApiModelProperty("外部预约")
    private byte allowreservation;

    @ApiModelProperty("最大停车数")
    private int maxmoto;

    @ApiModelProperty("报警接收人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long alertid;

    @ApiModelProperty("图片url")
    private String iconurl;

    @ApiModelProperty("gps")
    private String gps;

    @ApiModelProperty("=1车场=2路内")
    private byte road;

    @ApiModelProperty("1园区")
    private int type;

    @ApiModelProperty("0默认不分区 1东区 2西区")
    private int part;

    @ApiModelProperty("业务属性 1bot 2非bot")
    private int botType;
    @ApiModelProperty("业务属性")
    private String botName;
    @ApiModelProperty("业主")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long ownerId;
    @ApiModelProperty("业主")
    private String ownerName;

    @ApiModelProperty("是否包含充电桩")
    private Byte electric;

    @ApiModelProperty("车场图片")
    private byte[] parkcon;

    @ApiModelProperty("图片扩展名")
    private String extent;

    @ApiModelProperty("已停车数")
    private int used;

    @ApiModelProperty("存表 区域 有传则按区域查询 不传按经纬度查询")
    private String addressSet;

    @ApiModelProperty("基本时长")
    private int basicTime;

    @ApiModelProperty("基本费率")
    private int basicFee;

    @ApiModelProperty(value = "提前出场时间",required = false)
    private int advanceTime;

    @ApiModelProperty("免费时长")
    private int freeTime;

    @ApiModelProperty("阶梯时长")
    private int stepTime;

    @ApiModelProperty("阶梯费率")
    private int stepFee;

    @ApiModelProperty("阶梯精度")
    private int stepZero;

    @ApiModelProperty("每日封顶费用")
    private int dayMax;

    @ApiModelProperty("超长停车额外收费")
    private int dayAdd;

    //
    @ApiModelProperty(value = "区域 有传则按区域查询 不传按经纬度查询",required = false)
    private String[] addrSet;

    @ApiModelProperty("经营方式(1:自营, 2:代运营, 3:承包...)")
    private int agent;

    @ApiModelProperty("运营主体名称")
    private String agentName;


    @ApiModelProperty("机构名字")
    private String enterpriseName;

    private int qrType;
    @ApiModelProperty("特殊车场标志")
    private int special;

    @ApiModelProperty("距离")
    private double distance;

    @ApiModelProperty("是否可预约")
    private int order;

    @ApiModelProperty("0旧规则 1新规则")
    private int charge;

    //ex
    @ApiModelProperty("linkids")
    private String linkids;

    @ApiModelProperty("机构ids")
    private List<Long> businessesIds;

}