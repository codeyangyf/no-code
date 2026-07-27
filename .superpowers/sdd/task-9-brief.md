# Task 9: 前端请求封装与认证

**Goal:** 创建前端的请求封装、Token 管理、认证 Hook 和 API 接口。

**Files:**
- Create: `frontend/src/utils/token.ts`
- Create: `frontend/src/utils/request.ts`
- Create: `frontend/src/hooks/useAuth.ts`
- Create: `frontend/src/api/auth.ts`
- Create: `frontend/src/api/index.ts`
- Create: `frontend/src/types/index.ts`

**types/index.ts:**

```typescript
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
```

**utils/token.ts:**

```typescript
const ACCESS_TOKEN_KEY = 'lc_access_token'
const REFRESH_TOKEN_KEY = 'lc_refresh_token'

export const getAccessToken = () => {
  return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export const setAccessToken = (token: string) => {
  localStorage.setItem(ACCESS_TOKEN_KEY, token)
}

export const removeAccessToken = () => {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
}

export const getRefreshToken = () => {
  return localStorage.getItem(REFRESH_TOKEN_KEY)
}

export const setRefreshToken = (token: string) => {
  localStorage.setItem(REFRESH_TOKEN_KEY, token)
}

export const removeRefreshToken = () => {
  localStorage.removeItem(REFRESH_TOKEN_KEY)
}

export const clearTokens = () => {
  removeAccessToken()
  removeRefreshToken()
}
```

**utils/request.ts:**

```typescript
import axios from 'axios'
import { getAccessToken, clearTokens } from './token'
import { message } from 'antd'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  (config) => {
    const token = getAccessToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      clearTokens()
      message.error('登录已过期，请重新登录')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request
```

**api/auth.ts:**

```typescript
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
```

**api/index.ts:**

```typescript
export { login, refreshToken, logout } from './auth'
```

**hooks/useAuth.ts:**

```typescript
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
```

**Steps:**
1. 创建 types/index.ts
2. 创建 utils/token.ts
3. 创建 utils/request.ts
4. 创建 api/auth.ts
5. 创建 api/index.ts
6. 创建 hooks/useAuth.ts
7. 验证构建：`cd frontend && npm run build`
8. Commit，提交信息："feat: 前端请求封装与认证（Token管理、Axios封装、认证Hook）"

**Global Constraints:**
- React: 18.2.0
- TypeScript: 5.4.0
- Vite: 6.0.0
- Ant Design: 5.15.0

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-9101e4ee981043cfb7f3328ac6460421/cwd.txt'; exit "$__tr_native_ec"