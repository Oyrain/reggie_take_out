package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrderDto;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据:{},",orders.toString());
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 查询订单数据
     * @param page
     * @param pageSize
     * @param orderDto
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, OrderDto orderDto){
        log.info("page:{},pageSize:{},orderDto:{}",page,pageSize,orderDto);

        Page<Orders> pageInfo = new Page<>(page,pageSize);
        //设置分页条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        String number = orderDto.getNumber();
        String beginTime = orderDto.getBeginTime();
        String endTime = orderDto.getEndTime();
        if(StringUtils.isNotEmpty(number)){
            queryWrapper.like(Orders::getNumber,number);
        }
        if(StringUtils.isNotEmpty(beginTime)){
            queryWrapper.ge(Orders::getOrderTime,beginTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            queryWrapper.le(Orders::getOrderTime, endTime);
        }

        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改订单状态
     * @param order
     * @return
     */
    @PutMapping
    public R<String> sendOrder(@RequestBody Orders order){
        log.info("order:{}",order);
        orderService.updateById(order);
        return R.success("修改订单状态完成");
    }

    /**
     * 获取用户的订单信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){
        Page<Orders> pageInfo = new Page<>(page,pageSize);

        //设置分页条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

}
