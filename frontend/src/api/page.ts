import request from './request';

export const pageApi = {
  list: (projectId: number, page: number, size: number) =>
    request.get(`/api/pages/project/${projectId}`, { params: { page, size } }),
  getById: (projectId: number, id: number) =>
    request.get(`/api/pages/project/${projectId}/${id}`),
  getByCode: (projectId: number, pageCode: string) =>
    request.get(`/api/pages/project/${projectId}/code/${pageCode}`),
  create: (projectId: number, data: Record<string, unknown>) =>
    request.post(`/api/pages/project/${projectId}`, data),
  update: (projectId: number, id: number, data: Record<string, unknown>) =>
    request.put(`/api/pages/project/${projectId}/${id}`, data),
  delete: (projectId: number, id: number) =>
    request.delete(`/api/pages/project/${projectId}/${id}`),
};
