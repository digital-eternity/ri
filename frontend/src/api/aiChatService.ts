import axios from 'axios'
import { OpenAICompatibleRequest } from '@/types/aiTypes'

const API_URL = process.env.VUE_APP_API_URL + '/Ai'

export const aiChatService = {
  async chatWithAi (request: OpenAICompatibleRequest): Promise<string> {
    try {
      const response = await axios.post(`${API_URL}/chat`, request)
      const data: string = response.data
      return data
    } catch (error) {
      console.error('Error communicating with LLM:', error)
      throw error
    }
  }
}
