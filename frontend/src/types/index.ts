export interface UserInfo {
  id: number
  username: string
  realName: string
  email: string | null
  phone: string | null
  tenantId: number | null
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  user: UserInfo
}

export interface Result<T = unknown> {
  code: number
  message: string
  data: T
  timestamp: number
}