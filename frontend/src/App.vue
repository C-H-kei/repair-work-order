<script setup lang="ts">
import { onMounted, ref } from 'vue'
import request from './utils/request'
import { ElMessage } from 'element-plus'

// ====== 登录模块 ======
const isLogin = ref(!!localStorage.getItem('token'))
const loginForm = ref({ username: '', password: '' })

async function handleLogin() {
  if (!loginForm.value.username || !loginForm.value.password) return ElMessage.warning('请输入账号密码')
  try {
    const res = await request.post('/auth/login', loginForm.value)
    if (res.data.code === 200) {
      localStorage.setItem('token', res.data.data.token)
      isLogin.value = true
      ElMessage.success('登录成功')
      loadEquipments()
      loadWorkOrders()
    }
  } catch (e) { }
}

function logout() {
  localStorage.removeItem('token')
  isLogin.value = false
}

// ====== 业务模块（换成 request 即可） ======
interface Equipment { id?: number; code: string; name: string; status?: string }
const equipments = ref<Equipment[]>([])
const equipmentForm = ref<Equipment>({ code: '', name: '' })

async function loadEquipments() {
  const res = await request.get('/equipments')
  if (res.data.code === 200) {
    // 因为我们改回了普通查询，直接拿 data 数组
    equipments.value = res.data.data
  }
}
async function addEquipment() {
  if (!equipmentForm.value.code || !equipmentForm.value.name) return ElMessage.warning('请输入完整信息')
  await request.post('/equipments', equipmentForm.value)
  ElMessage.success('设备添加成功')
  equipmentForm.value = { code: '', name: '' }
  await loadEquipments()
}

interface WorkOrder { id?: number; equipmentId: number; description: string; status?: string; createTime?: string }
const workOrders = ref<WorkOrder[]>([])
const orderForm = ref<any>({ equipmentId: null, description: '' })
async function loadWorkOrders() {
  const res = await request.get('/work-orders')
  if (res.data.code === 200) workOrders.value = res.data.data
}

async function createWorkOrder() {
  if (!orderForm.value.equipmentId || !orderForm.value.description) return ElMessage.warning('请选择设备并填写故障描述')
  await request.post('/work-orders', orderForm.value)
  ElMessage.success('报修提交成功')
  orderForm.value = { equipmentId: 0, description: '' }
  await loadWorkOrders()
}

async function changeStatus(id: number, status: string) {
  await request.put(`/work-orders/${id}/status?status=${status}`)
  ElMessage.success('状态更新成功')
  await loadWorkOrders()
}

function getStatusType(status: string) {
  if (status === 'PENDING') return 'danger'
  if (status === 'PROCESSING') return 'warning'
  if (status === 'COMPLETED') return 'success'
  return 'info'
}
function translateStatus(status: string) {
  if (status === 'PENDING') return '待处理'
  if (status === 'PROCESSING') return '维修中'
  if (status === 'COMPLETED') return '已完成'
  return status
}

onMounted(() => {
  if (isLogin.value) {
    loadEquipments()
    loadWorkOrders()
  }
})
</script>

<template>
  <div style="background-color: #f4f6f8; min-height: 100vh; padding: 20px;">

    <!-- 登录页面 -->
    <div v-if="!isLogin" style="display: flex; justify-content: center; align-items: center; height: 80vh;">
      <el-card style="width: 400px;">
        <template #header>
          <div style="text-align: center; font-size: 20px; font-weight: bold;">园区设备报修系统 - 登录</div>
        </template>
        <el-form label-width="80px">
          <el-form-item label="账号">
            <el-input v-model="loginForm.username" placeholder="请输入账号 (admin)" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码 (123456)" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%;" @click="handleLogin">登 录</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 业务主页 -->
    <div v-else style="max-width: 1000px; margin: 0 auto;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h1 style="color: #333; margin: 0;">园区设备报修运维系统</h1>
        <el-button type="danger" plain @click="logout">退出登录</el-button>
      </div>

      <!-- 工单区域 (代码和之前一样，已折叠) -->
      <el-card shadow="hover" style="margin-bottom: 20px;">
        <template #header>
          <div style="font-weight: bold; color: #e6a23c; font-size: 18px;">🚨 提交与处理报修工单</div>
        </template>
        <el-form :inline="true" style="margin-bottom: 20px;">
          <el-form-item label="选择设备">
            <el-select v-model="orderForm.equipmentId" placeholder="请选择故障设备" style="width: 200px;" clearable>
              <el-option v-for="eq in equipments" :key="eq.id" :label="`${eq.code} - ${eq.name}`" :value="eq.id!" />
            </el-select>
          </el-form-item>
          <el-form-item label="故障描述">
            <el-input v-model="orderForm.description" placeholder="请简述故障情况" style="width: 300px;" />
          </el-form-item>
          <el-form-item><el-button type="warning" @click="createWorkOrder">提交报修</el-button></el-form-item>
        </el-form>
        <el-table :data="workOrders" style="width: 100%" border stripe>
          <el-table-column prop="id" label="工单ID" width="100" align="center" />
          <el-table-column prop="equipmentCode" label="设备编号" width="120" align="center" />
          <el-table-column prop="equipmentName" label="故障设备" width="150" />
          <el-table-column prop="description" label="故障描述" />
          <el-table-column label="状态" width="120" align="center">
            <template #default="scope"><el-tag :type="getStatusType(scope.row.status)">{{
              translateStatus(scope.row.status) }}</el-tag></template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template #default="scope">
              <el-button v-if="scope.row.status === 'PENDING'" size="small" type="primary"
                @click="changeStatus(scope.row.id, 'PROCESSING')">接单维修</el-button>
              <el-button v-if="scope.row.status === 'PROCESSING'" size="small" type="success"
                @click="changeStatus(scope.row.id, 'COMPLETED')">维修完成</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 设备区域 (代码和之前一样，已折叠) -->
      <el-card shadow="hover">
        <template #header>
          <div style="font-weight: bold; color: #409eff; font-size: 18px;">🖥️ 设备管理台账</div>
        </template>
        <el-form :inline="true" style="margin-bottom: 20px;">
          <el-form-item label="设备编号"><el-input v-model="equipmentForm.code" placeholder="如 EQ-003" /></el-form-item>
          <el-form-item label="设备名称"><el-input v-model="equipmentForm.name" placeholder="如 空调" /></el-form-item>
          <el-form-item><el-button type="primary" @click="addEquipment">新增设备</el-button></el-form-item>
        </el-form>
        <el-table :data="equipments" style="width: 100%" border stripe>
          <el-table-column prop="id" label="ID" width="80" align="center" />
          <el-table-column prop="code" label="设备编号" />
          <el-table-column prop="name" label="设备名称" />
          <el-table-column prop="status" label="状态" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>