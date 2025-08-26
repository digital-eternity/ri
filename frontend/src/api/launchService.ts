import axios from './axiosConfig' // Import configured axios
import { ExecutingTest, Launch } from '@/types/tmsTypes'

const API_URL = process.env.VUE_APP_API_URL + '/Launches'

export const launchService = {
  async getLaunches (projectId?: string | number): Promise<Launch[]> {
    try {
      const params = projectId ? { projectId: projectId } : {}
      const response = await axios.get<Launch[]>(API_URL, { params })
      return response.data
    } catch (error) {
      console.error('Error fetching launches:', error)
      throw error
    }
  },

  async getLaunch (launchId?: string | number): Promise<Launch> {
    try {
      const response = await axios.get<Launch>(`${API_URL}/${launchId}`)
      return response.data
    } catch (error) {
      console.error('Error fetching project:', error)
      throw error
    }
  },

  async createLaunch (launch: Launch): Promise<Launch> {
    try {
      const response = await axios.post<Launch>(API_URL, launch)
      return response.data
    } catch (error) {
      console.error('Error creating launch:', error)
      throw error
    }
  },

  async deleteLaunch (id: string): Promise<void> {
    try {
      await axios.delete(`${API_URL}/${id}`)
    } catch (error) {
      console.error('Error deleting launch:', error)
      throw error
    }
  },

  async getLaunchTests (launchId?: string | number): Promise<ExecutingTest[]> {
    try {
      const params = launchId ? { launchId: launchId } : {}
      const response = await axios.get<ExecutingTest[]>(`${API_URL}/tests`, { params })
      return response.data
    } catch (error) {
      console.error('Error fetching project:', error)
      throw error
    }
  },

  async getLaunchTest (testId?: string | number): Promise<ExecutingTest> {
    try {
      const response = await axios.get<ExecutingTest>(`${API_URL}/tests/${testId}`)
      return response.data
    } catch (error) {
      console.error('Error fetching project:', error)
      throw error
    }
  },

  async updateLaunchTest (testId: string | number, test: ExecutingTest): Promise<ExecutingTest> {
    try {
      const response = await axios.put<ExecutingTest>(`${API_URL}/tests/${testId}`, test)
      return response.data
    } catch (error) {
      console.error('Error fetching project:', error)
      throw error
    }
  }
}
