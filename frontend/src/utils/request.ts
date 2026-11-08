import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000
})

// 请求拦截器：每次请求前，自动带上 Token
request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
})

// 响应拦截器：统一处理报错信息
request.interceptors.response.use(
    response => {
        const res = response.data
        // 如果后端返回的业务 code 不是 200，说明出错了（比如触发了全局异常拦截）
        if (res.code && res.code !== 200) {
            ElMessage.error(res.message || '操作失败')
            return Promise.reject(new Error(res.message || 'Error'))
        }
        return response
    },
    error => {
        if (error.response && error.response.status === 401) {
            ElMessage.error('登录已过期，请重新登录')
            localStorage.removeItem('token')
            setTimeout(() => window.location.reload(), 1000)
        } else {
            ElMessage.error(error.response?.data?.message || '网络请求失败')
        }
        return Promise.reject(error)
    }
)

export default request