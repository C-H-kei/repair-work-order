package com.dormitory.repairbackend.controller;

import com.dormitory.repairbackend.common.Result;
import com.dormitory.repairbackend.entity.Equipment;
import com.dormitory.repairbackend.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // 调用带有 Redis 缓存的普通列表查询
    @GetMapping
    public Result<List<Equipment>> list() {
        return Result.success(equipmentService.getList());
    }

    // 顺便保留一个分页接口（可选测试，前端暂时不调）
    @GetMapping("/page")
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<Equipment>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(equipmentService.getPage(pageNum, pageSize));
    }

    @PostMapping
    public Result<Void> create(@RequestBody Equipment equipment) {
        equipmentService.addEquipment(equipment);
        return Result.success(null);
    }
}