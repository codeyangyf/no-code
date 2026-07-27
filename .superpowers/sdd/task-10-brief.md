# Task 10: 前端登录页与首页

**Goal:** 创建前端登录页和首页组件，使用 Ant Design 构建。

**Files:**
- Create: `frontend/src/pages/Login.tsx`
- Create: `frontend/src/pages/Dashboard.tsx`

**Login.tsx:**

```tsx
import { useState } from 'react'
import { Card, Form, Input, Button, message } from 'antd'
import { User, Lock } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

function Login() {
  const [form] = Form.useForm()
  const navigate = useNavigate()
  const { loading, handleLogin } = useAuth()

  const onFinish = async (values: { username: string; password: string }) => {
    const success = await handleLogin(values.username, values.password)
    if (success) {
      message.success('登录成功')
      navigate('/dashboard')
    } else {
      message.error('用户名或密码错误')
    }
  }

  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    }}>
      <Card
        style={{ width: 400, boxShadow: '0 4px 20px rgba(0,0,0,0.1)' }}
        title={<div style={{ textAlign: 'center', fontSize: 20, fontWeight: 'bold' }}>
          低代码平台
        </div>}
      >
        <Form
          form={form}
          name="login"
          onFinish={onFinish}
          layout="vertical"
        >
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input
              prefix={<User style={{ color: 'rgba(0,0,0,0.45)' }} />}
              placeholder="请输入用户名"
            />
          </Form.Item>

          <Form.Item
            name="password"
            label="密码"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password
              prefix={<Lock style={{ color: 'rgba(0,0,0,0.45)' }} />}
              placeholder="请输入密码"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={loading}
              size="large"
            >
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}

export default Login
```

**Dashboard.tsx:**

```tsx
import { Layout, Menu, Avatar, Dropdown } from 'antd'
import { Dashboard, Settings, Users, FolderOpen, LogOut } from 'lucide-react'
import { useAuth } from '../hooks/useAuth'

const { Header, Sider, Content } = Layout

function Dashboard() {
  const { user, handleLogout } = useAuth()

  const menuItems = [
    { key: '1', icon: <Settings />, label: '设置' },
    { key: '2', icon: <Users />, label: '用户管理' },
    { type: 'divider' },
    {
      key: '3',
      icon: <LogOut />,
      label: '退出登录',
      onClick: handleLogout
    }
  ]

  const sideMenuItems = [
    { key: '1', icon: <Dashboard />, label: '仪表盘' },
    { key: '2', icon: <FolderOpen />, label: '项目管理' },
    { key: '3', icon: <Users />, label: '成员管理' },
    { key: '4', icon: <Settings />, label: '系统设置' }
  ]

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        theme="light"
        style={{ borderRight: '1px solid #f0f0f0' }}
      >
        <div
          style={{
            padding: '16px',
            fontSize: '18px',
            fontWeight: 'bold',
            textAlign: 'center',
            color: '#667eea'
          }}
        >
          低代码平台
        </div>
        <Menu
          mode="inline"
          defaultSelectedKeys={['1']}
          items={sideMenuItems}
        />
      </Sider>

      <Layout>
        <Header
          style={{
            padding: '0 24px',
            background: '#fff',
            borderBottom: '1px solid #f0f0f0',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between'
          }}
        >
          <span style={{ fontSize: '16px', fontWeight: 'bold' }}>仪表盘</span>
          <Dropdown menu={{ items: menuItems }}>
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                cursor: 'pointer',
                gap: '8px'
              }}
            >
              <Avatar>{user?.realName?.charAt(0) || user?.username?.charAt(0) || 'U'}</Avatar>
              <span>{user?.realName || user?.username}</span>
            </div>
          </Dropdown>
        </Header>

        <Content style={{ padding: '24px', background: '#f5f5f5' }}>
          <div style={{ fontSize: '14px', color: '#666' }}>
            欢迎使用低代码平台！这是您的仪表盘页面。
          </div>
        </Content>
      </Layout>
    </Layout>
  )
}

export default Dashboard
```

**Steps:**
1. 创建 Login.tsx
2. 创建 Dashboard.tsx
3. 验证构建：`cd frontend && npm run build`
4. Commit，提交信息："feat: 前端登录页与首页（Ant Design布局）"

**Global Constraints:**
- React: 18.2.0
- TypeScript: 5.4.0
- Vite: 6.0.0
- Ant Design: 5.15.0
- lucide-react: ^0.310.0

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-c6b7ffea1bfb46c3933c5e0df240715f/cwd.txt'; exit "$__tr_native_ec"