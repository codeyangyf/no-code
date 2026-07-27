import { useState, useEffect } from 'react'
import { getAccessToken, setAccessToken, clearTokens } from '../utils/token'
import { login } from '../api/auth'
import type { UserInfo } from '../types'

export const useAuth = () => {
  const [user, setUser] = useState<UserInfo | null>(null)
  const [loading, setLoading] = useState(false)

  const isLoggedIn = () => !!getAccessToken()

  const handleLogin = async (username: string, password: string) => {
    setLoading(true)
    try {
      const result = await login(username, password)
      if (result.code === 0) {
        setAccessToken(result.data.accessToken)
        setUser(result.data.user)
        return true
      }
      return false
    } catch (error) {
      return false
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    clearTokens()
    setUser(null)
    window.location.href = '/login'
  }

  useEffect(() => {
    if (isLoggedIn()) {
      const token = getAccessToken()
      if (token) {
      }
    }
  }, [])

  return { user, loading, isLoggedIn, handleLogin, handleLogout }
}