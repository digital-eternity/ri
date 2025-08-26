import { computed, ComputedRef } from 'vue'
import { authService } from '../api/authService'

interface ParsedToken {
  'http://schemas.microsoft.com/ws/2008/06/identity/claims/role'?: string
  'http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier'?: string
  [key: string]: any // Allow other claims
}

export function hasAdminRights (): { isAdmin: ComputedRef<boolean> } {
  const isAdmin = computed<boolean>(() => {
    const token = authService.getToken()
    if (!token) return false

    try {
      const parsedToken = getUserJson(token)
      return parsedToken['http://schemas.microsoft.com/ws/2008/06/identity/claims/role'] === 'Administrator'
    } catch (error) {
      console.error('Error decoding token:', error)
      return false
    }
  })

  return { isAdmin }
}

export function getUserId (): { userId: ComputedRef<string | 0> } {
  const userId = computed<string | 0>(() => {
    const token = authService.getToken()
    if (!token) {
      return 0
    }

    try {
      const parsedToken = getUserJson(token)
      return parsedToken['http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier'] || 0 // Return the nameidentifier or 0 if it is undefiend
    } catch (error) {
      console.error('Error decoding token:', error)
      return 0
    }
  })

  return { userId }
}

function getUserJson (token: string): ParsedToken {
  const base64Url = token.split('.')[1]
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
  const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
  }).join(''))
  return JSON.parse(jsonPayload)
}
