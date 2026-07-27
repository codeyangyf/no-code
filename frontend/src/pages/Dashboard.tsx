import { Layout, Menu, Avatar, Dropdown } from 'antd'
import { LayoutDashboard, Settings, Users, FolderOpen, LogOut } from 'lucide-react'
import { useAuth } from '../hooks/useAuth'

const { Header, Sider, Content } = Layout

function Dashboard() {
  const { user, handleLogout } = useAuth()

  const menuItems = [
    { key: '1', icon: <Settings />, label: '设置' },
    { key: '2', icon: <Users />, label: '用户管理' },
    { type: 'divider' as const },
    {
      key: '3',
      icon: <LogOut />,
      label: '退出登录',
      onClick: handleLogout
    }
  ]

  const sideMenuItems = [
    { key: '1', icon: <LayoutDashboard />, label: '仪表盘' },
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