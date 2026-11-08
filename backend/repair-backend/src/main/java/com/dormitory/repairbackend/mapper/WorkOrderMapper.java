package com.dormitory.repairbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dormitory.repairbackend.entity.WorkOrder;
import com.dormitory.repairbackend.entity.WorkOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {

    // 手写多表联查：查工单的同时，把设备名称和编号一起查出来
    @Select("SELECT w.id, w.equipment_id, w.description, w.status, w.create_time, " +
            "e.name AS equipmentName, e.code AS equipmentCode " +
            "FROM work_order w " +
            "LEFT JOIN equipment e ON w.equipment_id = e.id " +
            "ORDER BY w.create_time DESC")
    List<WorkOrderVO> selectWorkOrderVOList();
}