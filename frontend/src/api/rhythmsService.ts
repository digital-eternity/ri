import axios from './axiosConfig'
import { Rhythm } from '@/types/riTypes'

const API_URL = process.env.VUE_APP_API_URL + '/Rhythms'

export const rhythmsService = {
  async getRhythmsList (): Promise<Rhythm[]> {
    try {
      const response = await axios.get<Rhythm[]>(`${API_URL}/getAll`)
      return response.data
    } catch (error) {
      console.error('Error fetching rhythms list:', error)
      throw error
    }
  },

  async getRhythm (rhythmId: number): Promise<Rhythm> {
    try {
      const response = await axios.get<Rhythm>(`${API_URL}/get/${rhythmId}`)
      return response.data

      // const r: Rhythm = {
      //   id: rhythmId,
      //   name: 'La-Bamba Rhythm',
      //   description: 'This is my first cool rhythm',
      //   size: 8,
      //   baseNote: 'EIGHT',
      //   aRhythm:
      //     [
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'NOTE', duration: 'QUARTER' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'PAUSE', duration: 'EIGHT' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'PAUSE', duration: 'EIGHT' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'NOTE', duration: 'QUARTER' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'PAUSE', duration: 'EIGHT' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'PAUSE', duration: 'EIGHT' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'NOTE', duration: 'EIGHT' }] },
      //       { label: 'clap', isStrong: true, blockSubdivisions: [{ type: 'PAUSE', duration: 'EIGHT' }] }
      //     ]
      // }

      // return r
    } catch (error) {
      console.error('Error fetching rhythm:', error)
      throw error
    }
  },

  async createRhythm (rhythm: Rhythm): Promise<Rhythm> {
    try {
      const response = await axios.post<Rhythm>(`${API_URL}/add`, rhythm)
      return response.data
    } catch (error) {
      console.error('Error creating rhythm:', error)
      throw error
    }
  },

  async updateRhythm (rhythmId: string | number, rhythm: Rhythm): Promise<Rhythm> {
    try {
      const response = await axios.put<Rhythm>(`${API_URL}/update/${rhythmId}`, rhythm)
      return response.data
    } catch (error) {
      console.error('Error updating rhythm:', error)
      throw error
    }
  },

  async deleteRhythm (rhythmId: string | number): Promise<void> {
    try {
      await axios.delete(`${API_URL}/delete/${rhythmId}`)
    } catch (error) {
      console.error('Error deleting rhythm:', error)
      throw error
    }
  }
}
