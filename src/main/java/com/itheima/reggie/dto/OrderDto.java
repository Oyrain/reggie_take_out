package com.itheima.reggie.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;


@Data
public class OrderDto {
    private String number;//订单号
    private String beginTime;//开始时间
    private String endTime;//结束时间
}
