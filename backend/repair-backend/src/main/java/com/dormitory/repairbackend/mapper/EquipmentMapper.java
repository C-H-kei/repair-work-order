package com.dormitory.repairbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dormitory.repairbackend.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
}