import axios from './axiosConfig'
import { User } from '@/types/tmsTypes'

const API_URL = process.env.VUE_APP_API_URL + '/Users'

export const userService = {
  async getUsers (): Promise<User[]> {
    try {
      const response = await axios.get<User[]>(API_URL)
      return response.data
    } catch (error) {
      console.error('Error fetching users:', error)
      throw error
    }
  },

  async getUser (id: number | string): Promise<User> {
    try {
      const response = await axios.get<User>(`${API_URL}/${id}`)
      return response.data
    } catch (error) {
      console.error('Error fetching users:', error)
      throw error
    }
  },

  async createUser (user: { username: string; password?: string }): Promise<User> {
    try {
      const response = await axios.post<User>(API_URL, null, { // Use generic for response
        params: {
          username: user.username,
          password: user.password
        }
      })
      return response.data
    } catch (error) {
      console.error('Error creating user:', error)
      throw error
    }
  },

  async changePassword (id: string, oldPassword: string, newPassword: string): Promise<void> {
    try {
      await axios.post(`${API_URL}/changePassword`, null, {
        params: {
          id: id,
          oldPassword: oldPassword,
          newPassword: newPassword
        }
      })
    } catch (error) {
      console.error('Error changing password:', error)
      throw error
    }
  },

  async generateToken (): Promise<string> {
    try {
      const response = await axios.post(`${API_URL}/token`, null)
      return response.data.token
    } catch (error) {
      console.error('Error generating token:', error)
      throw error
    }
  },

  async getToken (): Promise<string> {
    try {
      const response = await axios.get(`${API_URL}/token`)
      return response.data.token
    } catch (error) {
      console.error('Error getting token:', error)
      throw error
    }
  },

  async deleteToken (): Promise<void> {
    try {
      await axios.delete(`${API_URL}/token`)
    } catch (error) {
      console.error('Error deleting token:', error)
      throw error
    }
  },

  async deleteUser (id: number): Promise<void> {
    try {
      await axios.delete(`${API_URL}/${id}`)
    } catch (error) {
      console.error('Error deleting user:', error)
      throw error
    }
  }
}
