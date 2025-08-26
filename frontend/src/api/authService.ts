import axios from 'axios'
import router from '../router'
import { ref } from 'vue'

const API_URL = process.env.VUE_APP_API_URL + '/Auth'
export const isLoggedIn = ref<boolean>(!!localStorage.getItem('token'))

export const authService = {
  async login (username: string, password: string): Promise<string> {
    try {
      const response = await axios.post(`${API_URL}/login`, null, { params: { username, password } })
      const token: string = response.data.token
      localStorage.setItem('token', token)
      isLoggedIn.value = true
      return token
    } catch (error) {
      console.error('Error logging in:', error)
      localStorage.removeItem('token')
      throw error
    }
  },

  logout (): void {
    localStorage.removeItem('token')
    isLoggedIn.value = false
    router.push('/login')
  },

  getToken (): string | null {
    return localStorage.getItem('token')
  },

  isLoggedIn (): boolean {
    return !!localStorage.getItem('token')
  },

  isTokenExpired (): boolean {
    const token = this.getToken()
    if (!token) return true

    try {
      const base64Url = token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      }).join(''))
      const parsedToken = JSON.parse(jsonPayload)
      const expiry: number = parsedToken.exp
      const now = Math.floor(Date.now() / 1000)
      return expiry < now
    } catch (error) {
      console.error('Error decoding token:', error)
      return true
    }
  }
}
