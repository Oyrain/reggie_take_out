package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmaelService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> ids);
    public void haltSales(List<String> ids);
    public void startSales(List<String> ids);

    //根据id查询套餐信息和对应的菜品信息
    SetmealDto getByIdWithDish(Long id);
    //修改套餐信息
    public void updateWithDish(SetmealDto setmealDto);
}
