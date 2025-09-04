import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios'
import { authService } from './authService'

// axios.interceptors.request.use(
//   (config: InternalAxiosRequestConfig) => { // Changed type to InternalAxiosRequestConfig
//     if (config.url && config.url.toLowerCase().indexOf('/auth/login') === -1) {
//       if (authService.isTokenExpired()) {
//         authService.logout()
//         return Promise.reject(new Error('Token has expired.')) as any // Explicitly cast to any to satisfy type checker
//       }
//     }

//     const token = authService.getToken()
//     if (token && config.headers) {
//       config.headers.Authorization = `Bearer ${token}`
//     }
//     return config
//   },
//   (error: AxiosError) => {
//     return Promise.reject(error)
//   }
// )

export default axios
