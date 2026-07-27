import request from '../utils/request'
import type { LoginResponse, Result } from '../types'

export const login = async (username: string, password: string): Promise<Result<LoginResponse>> => {
  return request.post('/auth/login', { username, password })
}

export const refreshToken = async (refreshToken: string): Promise<Result<LoginResponse>> => {
  return request.post('/auth/refresh', { refreshToken })
}

export const logout = async (): Promise<Result<void>> => {
  return request.post('/auth/logout')
}