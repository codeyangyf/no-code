import request from './request';

export const projectApi = {
  list: (page: number, size: number) => request.get('/api/projects', { params: { page, size } }),
  getById: (id: number) => request.get(`/api/projects/${id}`),
  create: (data: Record<string, unknown>) => request.post('/api/projects', data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/api/projects/${id}`, data),
  delete: (id: number) => request.delete(`/api/projects/${id}`),
};
