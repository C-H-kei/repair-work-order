package com.dormitory.repairbackend.controller;

import com.dormitory.repairbackend.common.Result;
import com.dormitory.repairbackend.entity.WorkOrder;
import com.dormitory.repairbackend.entity.WorkOrderVO;
import com.dormitory.repairbackend.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public Result<List<WorkOrderVO>> list() {
        return Result.success(workOrderService.getAllOrders());
    }

    @PostMapping
    public Result<Void> create(@RequestBody WorkOrder order) {
        workOrderService.createOrder(order);
        return Result.success(null);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        workOrderService.updateStatus(id, status);
        return Result.success(null);
    }
}