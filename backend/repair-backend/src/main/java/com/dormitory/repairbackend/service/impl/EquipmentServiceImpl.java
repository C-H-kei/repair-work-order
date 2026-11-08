package com.dormitory.repairbackend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dormitory.repairbackend.entity.Equipment;
import com.dormitory.repairbackend.mapper.EquipmentMapper;
import com.dormitory.repairbackend.service.EquipmentService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentMapper equipmentMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    // 构造函数注入
    public EquipmentServiceImpl(EquipmentMapper equipmentMapper, RedisTemplate<String, Object> redisTemplate) {
        this.equipmentMapper = equipmentMapper;
        this.redisTemplate = redisTemplate;
    }

    // 1. 缓存查询（旁路缓存模式 Cache-Aside）
    @Override
    public List<Equipment> getList() {
        String cacheKey = "equipment:list";

        // 先查 Redis
        List<Equipment> list = (List<Equipment>) redisTemplate.opsForValue().get(cacheKey);

        if (list != null) {
            System.out.println("========== 走了 Redis 缓存 ==========");
            return list;
        }

        // Redis 没有，查 MySQL
        System.out.println("========== 走了 MySQL 数据库 ==========");
        list = equipmentMapper.selectList(null);

        // 查完之后存入 Redis，设置 10 分钟过期（防止雪崩可以加随机数，这里简单设为10分钟）
        redisTemplate.opsForValue().set(cacheKey, list, 10, TimeUnit.MINUTES);

        return list;
    }

    @Override
    public Page<Equipment> getPage(Integer pageNum, Integer pageSize) {
        Page<Equipment> page = new Page<>(pageNum, pageSize);
        return equipmentMapper.selectPage(page, null);
    }

    // 2. 新增设备时，必须删掉缓存，保证数据一致性
    @Override
    public void addEquipment(Equipment equipment) {
        if (equipment.getStatus() == null) {
            equipment.setStatus("IN_USE");
        }
        equipmentMapper.insert(equipment);

        // 重点：数据发生变动，立刻清除 Redis 里的旧缓存
        redisTemplate.delete("equipment:list");
        System.out.println("========== 新增设备，已清除 Redis 缓存 ==========");
    }
}