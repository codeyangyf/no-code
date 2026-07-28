import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import UserManagement from './pages/UserManagement'
import RoleManagement from './pages/RoleManagement'
import MenuManagement from './pages/MenuManagement'
import TenantManagement from './pages/TenantManagement'
import AuditLog from './pages/AuditLog'
import FileManagement from './pages/FileManagement'
import PageDesigner from './pages/PageDesigner'
import FormDesigner from './pages/FormDesigner'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />}>
          <Route path="system/users" element={<UserManagement />} />
          <Route path="system/roles" element={<RoleManagement />} />
          <Route path="system/menus" element={<MenuManagement />} />
          <Route path="system/tenants" element={<TenantManagement />} />
          <Route path="system/audit-logs" element={<AuditLog />} />
          <Route path="system/files" element={<FileManagement />} />
          <Route path="project/pages" element={<PageDesigner />} />
          <Route path="project/forms" element={<FormDesigner />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
