import { useState, useEffect } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu, Avatar, Dropdown, Spin } from 'antd'
import type { MenuProps } from 'antd'
import {
  LayoutDashboard,
  Settings,
  Users,
  FolderOpen,
  LogOut,
  Users as Team,
  Menu as MenuIcon,
  Building,
  FileSearch,
  FolderOpen as FolderIcon
} from 'lucide-react'
import { useAuth } from '../hooks/useAuth'
import { getMenuTree, type Menu as MenuType } from '../api'

const { Header, Sider, Content } = Layout

const iconMap: Record<string, React.ReactNode> = {
  'LayoutDashboard': <LayoutDashboard />,
  'Settings': <Settings />,
  'User': <Users />,
  'Team': <Team />,
  'Menu': <MenuIcon />,
  'Building': <Building />,
  'FileSearch': <FileSearch />,
  'FolderOpen': <FolderIcon />
}

const getIcon = (iconName: string | null) => {
  if (!iconName) return <LayoutDashboard />
  return iconMap[iconName] || <LayoutDashboard />
}

const buildMenuItems = (
  menus: MenuType[],
  navigate: ReturnType<typeof useNavigate>
): MenuProps['items'] => {
  return menus.map(menu => {
    const fullPath = menu.path ? `/dashboard${menu.path}` : `/dashboard/${menu.id}`
    if (menu.menuType === 'DIRECTORY') {
      return {
        key: fullPath,
        icon: getIcon(menu.icon),
        label: menu.menuName,
        children: menu.children ? buildMenuItems(menu.children, navigate) : []
      }
    } else if (menu.menuType === 'MENU') {
      return {
        key: fullPath,
        icon: getIcon(menu.icon),
        label: menu.menuName,
        onClick: () => {
          navigate(fullPath)
        }
      }
    }
    return null
  }).filter(Boolean) as MenuProps['items']
}

function Dashboard() {
  const { user, handleLogout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [menus, setMenus] = useState<MenuType[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadMenus()
  }, [])

  const loadMenus = async () => {
    try {
      const result = await getMenuTree()
      if (result.code === 0) {
        setMenus(result.data)
      }
    } catch (error) {
      console.error('Failed to load menus:', error)
    } finally {
      setLoading(false)
    }
  }

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

  const sideMenuItems = buildMenuItems(menus, navigate)

  // 获取当前选中的菜单项
  const selectedKeys = [location.pathname]

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
        <Spin spinning={loading}>
          <Menu
            mode="inline"
            selectedKeys={selectedKeys}
            defaultOpenKeys={['/dashboard/system']}
            items={sideMenuItems.length > 0 ? sideMenuItems : [
              {
                key: '/dashboard',
                icon: <LayoutDashboard />,
                label: '仪表盘',
                onClick: () => navigate('/dashboard')
              }
            ]}
          />
        </Spin>
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
          {location.pathname === '/dashboard' ? (
            <div style={{ fontSize: '14px', color: '#666' }}>
              欢迎使用低代码平台！这是您的仪表盘页面。
            </div>
          ) : (
            <Outlet />
          )}
        </Content>
      </Layout>
    </Layout>
  )
}

export default Dashboard
