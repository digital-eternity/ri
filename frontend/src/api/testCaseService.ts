import axios from './axiosConfig'
import { TestCase, TestCaseDetails } from '@/types/tmsTypes'

const API_URL = process.env.VUE_APP_API_URL + '/TestCases'

export const testCaseService = {
  async getTestCases (projectId?: string | number): Promise<TestCase[]> {
    try {
      const params = projectId ? { projectId: projectId } : {}
      const response = await axios.get<TestCase[]>(API_URL, { params })
      return response.data
    } catch (error) {
      console.error('Error fetching test cases:', error)
      throw error
    }
  },

  async getTestCase (testCaseId: string | number): Promise<TestCase> {
    try {
      const response = await axios.get<TestCase>(`${API_URL}/${testCaseId}`)
      return response.data
    } catch (error) {
      console.error('Error fetching test case:', error)
      throw error
    }
  },

  async createTestCase (testCase: TestCase): Promise<TestCase> {
    try {
      const response = await axios.post<TestCase>(API_URL, testCase)
      return response.data
    } catch (error) {
      console.error('Error creating test case:', error)
      throw error
    }
  },

  async updateTestCase (testCaseId: string | number, testCase: TestCase): Promise<TestCase> {
    try {
      const response = await axios.put<TestCase>(`${API_URL}/${testCaseId}`, testCase)
      return response.data
    } catch (error) {
      console.error('Error updating test case:', error)
      throw error
    }
  },

  async deleteTestCase (ids: string[] | number[]): Promise<void> {
    try {
      const params = new URLSearchParams()

      ids.forEach((id) => {
        params.append('ids', String(id))
      })

      await axios.delete(API_URL, {
        params,
        validateStatus: () => true // Allow checking status codes explicitly
      })
    } catch (error) {
      console.error('Error deleting test case:', error)
      throw error
    }
  },

  async getTestCaseDetails (testCaseId: string | number): Promise<TestCaseDetails> {
    try {
      const response = await axios.get<TestCaseDetails>(`${API_URL}/${testCaseId}/details`)
      return response.data
    } catch (error) {
      console.error('Error fetching test case details:', error)
      throw error
    }
  },

  async updateTestCaseDetails (testCaseId: string | number, testCase: TestCaseDetails): Promise<TestCaseDetails> {
    try {
      const response = await axios.put<TestCaseDetails>(`${API_URL}/${testCaseId}/details`, testCase)
      return response.data
    } catch (error) {
      console.error('Error updating test case details:', error)
      throw error
    }
  },

  async importTestCases  (projectId: string, file: File): Promise<any> {
    const formData = new FormData()
    formData.append('file', file)

    try {
      const response = await axios.post(`${API_URL}/import?projectId=${projectId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      return response.data
    } catch (error: any) {
      console.error('Error importing test cases:', error)
      throw error
    }
  },

  async exportTestCases (projectId: string, format: string): Promise<any> {
    try {
      const response = await axios.get(`${API_URL}/export?projectId=${projectId}&format=${format}`, {
        responseType: 'blob' // Important for downloading files
      })
      return response.data
    } catch (error: any) {
      console.error('Error exporting test cases:', error)
      throw error
    }
  }
}
