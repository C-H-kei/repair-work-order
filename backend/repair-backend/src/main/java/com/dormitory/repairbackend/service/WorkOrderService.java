package com.dormitory.repairbackend.service;

import com.dormitory.repairbackend.entity.WorkOrder;
import com.dormitory.repairbackend.entity.WorkOrderVO;
import java.util.List;

public interface WorkOrderService {
    // 注意：这里的返回值变成了 WorkOrderVO 的集合
    List<WorkOrderVO> getAllOrders();
    void createOrder(WorkOrder order);
    void updateStatus(Long id, String status);
}