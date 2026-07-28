import request from './request'

export interface Menu {
  id: number
  tenantId: number
  parentId: number | null
  menuName: string
  path: string
  component: string | null
  icon: string | null
  menuType: string
  permission: string | null
  sortOrder: number
  status: number
  children?: Menu[]
}

export const getMenuTree = () => {
  return request.get<{ code: number; data: Menu[] }>('/system/menus/tree')
}
