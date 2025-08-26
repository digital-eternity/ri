import axios from './axiosConfig'
import { Project, User, ProjectMember } from '@/types/tmsTypes'

const API_URL = process.env.VUE_APP_API_URL + '/Projects'

export const projectService = {
  async getProjects (): Promise<Project[]> {
    try {
      const response = await axios.get<Project[]>(API_URL)
      return response.data
    } catch (error) {
      console.error('Error fetching projects:', error)
      throw error
    }
  },

  async getProject (projectId: string | number): Promise<Project> {
    try {
      const response = await axios.get<Project>(`${API_URL}/${projectId}`)
      return response.data
    } catch (error) {
      console.error('Error fetching project:', error)
      throw error
    }
  },

  async createProject (project: Project): Promise<Project> {
    try {
      const response = await axios.post<Project>(API_URL, project)
      return response.data
    } catch (error) {
      console.error('Error creating project:', error)
      throw error
    }
  },

  async updateProject (projectId: string | number, project: Project): Promise<Project> {
    try {
      const response = await axios.put<Project>(`${API_URL}/${projectId}`, project)
      return response.data
    } catch (error) {
      console.error('Error updating project:', error)
      throw error
    }
  },

  async deleteProject (projectId: string | number): Promise<void> {
    try {
      await axios.delete(`${API_URL}/${projectId}`)
    } catch (error) {
      console.error('Error deleting project:', error)
      throw error
    }
  },

  async getUsers (projectId: string | number): Promise<User[]> {
    try {
      const response = await axios.get<User[]>(`${API_URL}/${projectId}/users`)
      return response.data
    } catch (error) {
      console.error('Error retrieving users for project:', error)
      throw error
    }
  },

  async addUser (user: ProjectMember, projectId: string | number): Promise<void> {
    try {
      user.username = ''
      await axios.post(`${API_URL}/${projectId}/users`, user)
    } catch (error) {
      console.error('Error adding user to project:', error)
      throw error
    }
  },

  async removeUser (userId: string | number, projectId: string | number): Promise<void> {
    try {
      await axios.delete(`${API_URL}/${projectId}/users/${userId}`, { params: { userId } })
    } catch (error) {
      console.error('Error removing user from project:', error)
      throw error
    }
  },

  async canEdit (projectId: string | number): Promise<boolean> {
    try {
      const response = await axios.get(`${API_URL}/${projectId}/canEdit`)
      return response.data
    } catch (error) {
      console.error('Error getting manage ability:', error)
      return false
    }
  }
}
