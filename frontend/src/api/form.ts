import request from './request';

export const formApi = {
  list: (projectId: number, page: number, size: number) =>
    request.get(`/api/forms/project/${projectId}`, { params: { page, size } }),
  getById: (projectId: number, id: number) =>
    request.get(`/api/forms/project/${projectId}/${id}`),
  getByCode: (projectId: number, formCode: string) =>
    request.get(`/api/forms/project/${projectId}/code/${formCode}`),
  create: (projectId: number, data: Record<string, unknown>) =>
    request.post(`/api/forms/project/${projectId}`, data),
  update: (projectId: number, id: number, data: Record<string, unknown>) =>
    request.put(`/api/forms/project/${projectId}/${id}`, data),
  delete: (projectId: number, id: number) =>
    request.delete(`/api/forms/project/${projectId}/${id}`),
};

export const dataModelApi = {
  list: (projectId: number, page: number, size: number) =>
    request.get(`/api/data-models/project/${projectId}`, { params: { page, size } }),
  getById: (projectId: number, id: number) =>
    request.get(`/api/data-models/project/${projectId}/${id}`),
  getByCode: (projectId: number, modelCode: string) =>
    request.get(`/api/data-models/project/${projectId}/code/${modelCode}`),
  create: (projectId: number, data: Record<string, unknown>) =>
    request.post(`/api/data-models/project/${projectId}`, data),
  update: (projectId: number, id: number, data: Record<string, unknown>) =>
    request.put(`/api/data-models/project/${projectId}/${id}`, data),
  delete: (projectId: number, id: number) =>
    request.delete(`/api/data-models/project/${projectId}/${id}`),
};