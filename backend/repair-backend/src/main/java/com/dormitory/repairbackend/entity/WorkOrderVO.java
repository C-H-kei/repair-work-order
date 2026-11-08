package com.dormitory.repairbackend.entity;

import java.time.LocalDateTime;

// 继承 WorkOrder，可以直接复用原来的字段
public class WorkOrderVO extends WorkOrder {
    // 多出来的字段：设备名称
    private String equipmentName;
    // 多出来的字段：设备编号
    private String equipmentCode;

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public String getEquipmentCode() { return equipmentCode; }
    public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }
}