package com.dormitory.repairbackend.service.impl;

import com.dormitory.repairbackend.entity.WorkOrder;
import com.dormitory.repairbackend.entity.WorkOrderVO;
import com.dormitory.repairbackend.exception.BusinessException;
import com.dormitory.repairbackend.mapper.WorkOrderMapper;
import com.dormitory.repairbackend.service.WorkOrderService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper workOrderMapper;

    public WorkOrderServiceImpl(WorkOrderMapper workOrderMapper) {
        this.workOrderMapper = workOrderMapper;
    }

    @Override
    public List<WorkOrderVO> getAllOrders() {
        // 调用 Mapper 里我们手写的连表查询方法
        return workOrderMapper.selectWorkOrderVOList();
    }

    @Override
    public void createOrder(WorkOrder order) {
        order.setStatus("PENDING");
        order.setCreateTime(LocalDateTime.now());
        workOrderMapper.insert(order);
    }

    @Override
    public void updateStatus(Long id, String newStatus) {
        WorkOrder order = workOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        String currentStatus = order.getStatus();

        // 状态机流转规则校验
        if ("PENDING".equals(currentStatus) && !"PROCESSING".equals(newStatus)) {
            throw new BusinessException("【越权拦截】待处理工单只能流转为维修中");
        }
        if ("PROCESSING".equals(currentStatus) && !"COMPLETED".equals(newStatus)) {
            throw new BusinessException("【越权拦截】维修中工单只能流转为已完成");
        }
        if ("COMPLETED".equals(currentStatus)) {
            throw new BusinessException("工单已完成，无法再次修改状态");
        }

        order.setStatus(newStatus);
        workOrderMapper.updateById(order);
    }
}