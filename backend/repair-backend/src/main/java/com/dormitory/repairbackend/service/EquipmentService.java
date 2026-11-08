package com.dormitory.repairbackend.service;

import com.dormitory.repairbackend.entity.Equipment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface EquipmentService {
    List<Equipment> getList();
    void addEquipment(Equipment equipment);
    Page<Equipment> getPage(Integer pageNum, Integer pageSize);
}