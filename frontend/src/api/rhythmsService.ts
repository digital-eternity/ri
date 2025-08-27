import axios from './axiosConfig'
import { Rhythm } from '@/types/riTypes'

const API_URL = process.env.VUE_APP_API_URL + '/Projects'

export const rhythmsService = {
  async getRhythmsList (): Promise<Rhythm[]> {
    try {
      const response = await axios.get<Rhythm[]>(API_URL)
      return response.data
    } catch (error) {
      console.error('Error fetching rhythms list:', error)
      throw error
    }
  },

  async getRhythm (rhythmId: string | number): Promise<Rhythm> {
    try {
      // const response = await axios.get<Rhythm>(`${API_URL}/getRhythm/${rhythmId}`)
      const r: Rhythm = {
        id: 1,
        name: 'My new Rhythm',
        rhythm: [true, false, false, true, false, true, false, true, false, false, false, false],
        length: 12,
        subDivision: 4
      }

      return r

      // return response.data
    } catch (error) {
      console.error('Error fetching rhythm:', error)
      throw error
    }
  },

  async createRhythm (project: Rhythm): Promise<Rhythm> {
    try {
      const response = await axios.post<Rhythm>(API_URL, project)
      return response.data
    } catch (error) {
      console.error('Error creating rhythm:', error)
      throw error
    }
  },

  async updateRhythm (projectId: string | number, project: Rhythm): Promise<Rhythm> {
    try {
      const response = await axios.put<Rhythm>(`${API_URL}/${projectId}`, project)
      return response.data
    } catch (error) {
      console.error('Error updating rhythm:', error)
      throw error
    }
  },

  async deleteRhythm (projectId: string | number): Promise<void> {
    try {
      await axios.delete(`${API_URL}/${projectId}`)
    } catch (error) {
      console.error('Error deleting rhythm:', error)
      throw error
    }
  }
}
