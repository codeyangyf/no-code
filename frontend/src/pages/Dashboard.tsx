import { useState, useEffect } from 'react'
import { Layout, Menu, Avatar, Dropdown, Spin } from 'antd'
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

const buildMenuItems = (menus: MenuType[]): any[] => {
  return menus.map(menu => {
    if (menu.menuType === 'DIRECTORY') {
      return {
        key: menu.id.toString(),
        icon: getIcon(menu.icon),
        label: menu.menuName,
        children: menu.children ? buildMenuItems(menu.children) : []
      }
    } else if (menu.menuType === 'MENU') {
      return {
        key: menu.id.toString(),
        icon: getIcon(menu.icon),
        label: menu.menuName
      }
    }
    return null
  }).filter(Boolean)
}

function Dashboard() {
  const { user, handleLogout } = useAuth()
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

  const sideMenuItems = buildMenuItems(menus)

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
            defaultSelectedKeys={sideMenuItems.length > 0 ? [sideMenuItems[0].key] : ['1']}
            items={sideMenuItems.length > 0 ? sideMenuItems : [
              { key: '1', icon: <LayoutDashboard />, label: '仪表盘' }
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
          <div style={{ fontSize: '14px', color: '#666' }}>
            欢迎使用低代码平台！这是您的仪表盘页面。
          </div>
        </Content>
      </Layout>
    </Layout>
  )
}

export default Dashboard
