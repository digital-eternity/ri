import axios from './axiosConfig'

const API_URL = process.env.VUE_APP_API_URL + '/Presets'

export const presetsService = {
  async getPresetsList (): Promise<string[]> {
    try {
      const response = await axios.get<string[]>(`${API_URL}/getAll`)
      return response.data
    } catch (error) {
      console.error('Error fetching presets list:', error)
      throw error
    }
  },

  async getDefaultPreset (): Promise<string> {
    try {
      const response = await axios.get<string>(`${API_URL}/getInit`)
      return response.data
    } catch (error) {
      console.error('Error fetching default preset:', error)
      throw error
    }
  },

  async getPresetValues (name: string): Promise<string[]> {
    try {
      const response = await axios.get<string[]>(`${API_URL}/getPresetValues/${name}`)
      return response.data
    } catch (error) {
      console.error('Error fetching preset values:', error)
      throw error
    }
  }
}
