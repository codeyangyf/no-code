import React, { useState, useEffect } from 'react';
import { Card, Button, Input, Modal, Form, Select, Table, message, Row, Col, Space } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, EyeOutlined } from '@ant-design/icons';
import TextArea from 'antd/es/input/TextArea';
import type { ColumnsType } from 'antd/es/table';
import { projectApi, pageApi } from '../api';

interface ComponentConfig {
  id: string;
  type: string;
  name: string;
  parentId?: string;
  props?: Record<string, unknown>;
  style?: React.CSSProperties;
  sortOrder?: number;
}

interface PageConfig {
  id?: number;
  projectId: number;
  pageCode: string;
  pageName: string;
  path?: string;
  layout?: string;
  components: ComponentConfig[];
}

const COMPONENT_TYPES = [
  { value: 'Text', label: '文本' },
  { value: 'Input', label: '输入框' },
  { value: 'TextArea', label: '文本域' },
  { value: 'Number', label: '数字输入' },
  { value: 'Select', label: '下拉选择' },
  { value: 'Radio', label: '单选框' },
  { value: 'Checkbox', label: '复选框' },
  { value: 'DatePicker', label: '日期选择' },
  { value: 'DateTimePicker', label: '日期时间选择' },
  { value: 'Button', label: '按钮' },
  { value: 'Card', label: '卡片' },
  { value: 'Table', label: '表格' },
  { value: 'Form', label: '表单' },
  { value: 'Layout', label: '布局' },
  { value: 'Grid', label: '网格' },
  { value: 'Divider', label: '分割线' },
  { value: 'FileUpload', label: '文件上传' },
];

const PageDesigner: React.FC = () => {
  const [projects, setProjects] = useState<{ id: number; projectName: string }[]>([]);
  const [selectedProject, setSelectedProject] = useState<number | null>(null);
  const [pages, setPages] = useState<PageConfig[]>([]);
  const [selectedPage, setSelectedPage] = useState<PageConfig | null>(null);
  const [components, setComponents] = useState<ComponentConfig[]>([]);
  const [showAddPageModal, setShowAddPageModal] = useState(false);
  const [showAddComponentModal, setShowAddComponentModal] = useState(false);
  const [showPreviewModal, setShowPreviewModal] = useState(false);
  const [form] = Form.useForm();
  const [componentForm] = Form.useForm();

  useEffect(() => {
    loadProjects();
  }, []);

  useEffect(() => {
    if (selectedProject) {
      loadPages(selectedProject);
    }
  }, [selectedProject]);

  useEffect(() => {
    if (selectedPage) {
      setComponents(selectedPage.components || []);
    }
  }, [selectedPage]);

  const loadProjects = async () => {
    try {
      const res = await projectApi.list(1, 100);
      setProjects(res.data?.list || []);
    } catch (error) {
      message.error('加载项目列表失败');
    }
  };

  const loadPages = async (projectId: number) => {
    try {
      const res = await pageApi.list(projectId, 1, 100);
      setPages(res.data?.list || []);
    } catch (error) {
      message.error('加载页面列表失败');
    }
  };

  const handleSelectPage = (page: PageConfig) => {
    setSelectedPage(page);
  };

  const handleAddPage = () => {
    form.resetFields();
    setShowAddPageModal(true);
  };

  const handleSavePage = async () => {
    try {
      const values = await form.validateFields();
      const newPage: PageConfig = {
        projectId: selectedProject!,
        pageCode: values.pageCode,
        pageName: values.pageName,
        path: values.path,
        layout: values.layout,
        components: [],
      };

      await pageApi.create(selectedProject!, newPage as unknown as Record<string, unknown>);
      message.success('页面创建成功');
      setShowAddPageModal(false);
      loadPages(selectedProject!);
    } catch (error) {
      message.error('创建页面失败');
    }
  };

  const handleAddComponent = () => {
    componentForm.resetFields();
    setShowAddComponentModal(true);
  };

  const handleSaveComponent = async () => {
    try {
      const values = await componentForm.validateFields();
      const newComponent: ComponentConfig = {
        id: Date.now().toString(),
        type: values.type,
        name: values.name,
        props: values.props ? JSON.parse(values.props) : {},
        style: values.style ? JSON.parse(values.style) : {},
        sortOrder: components.length,
      };

      const newComponents = [...components, newComponent];
      setComponents(newComponents);

      if (selectedPage) {
        await pageApi.update(selectedProject!, selectedPage.id!, {
          components: newComponents,
        });
        setSelectedPage({ ...selectedPage, components: newComponents });
      }

      message.success('组件添加成功');
      setShowAddComponentModal(false);
    } catch (error) {
      message.error('添加组件失败');
    }
  };

  const handleDeleteComponent = async (componentId: string) => {
    const newComponents = components.filter((c) => c.id !== componentId);
    setComponents(newComponents);

    if (selectedPage) {
      await pageApi.update(selectedProject!, selectedPage.id!, {
        components: newComponents,
      });
      setSelectedPage({ ...selectedPage, components: newComponents });
    }
    message.success('组件删除成功');
  };

  const handlePreview = () => {
    setShowPreviewModal(true);
  };

  const pageColumns: ColumnsType<PageConfig> = [
    { title: '页面编码', dataIndex: 'pageCode', key: 'pageCode' },
    { title: '页面名称', dataIndex: 'pageName', key: 'pageName' },
    { title: '路径', dataIndex: 'path', key: 'path' },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: PageConfig) => (
        <Space>
          <Button type="link" onClick={() => handleSelectPage(record)}>
            编辑
          </Button>
          <Button type="link" danger>
            删除
          </Button>
        </Space>
      ),
    },
  ];

  const componentColumns: ColumnsType<ComponentConfig> = [
    { title: '组件名称', dataIndex: 'name', key: 'name' },
    { title: '组件类型', dataIndex: 'type', key: 'type' },
    { title: '排序', dataIndex: 'sortOrder', key: 'sortOrder' },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: ComponentConfig) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} />
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDeleteComponent(record.id)} />
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Card title="页面设计器" extra={selectedProject ? <Button icon={<PlusOutlined />} onClick={handleAddPage}>新建页面</Button> : null}>
        <Row gutter={16}>
          <Col span={6}>
            <Card title="选择项目" size="small">
              <Select
                style={{ width: '100%' }}
                placeholder="请选择项目"
                value={selectedProject}
                onChange={setSelectedProject}
                options={projects.map((p) => ({ value: p.id, label: p.projectName }))}
              />
            </Card>
          </Col>
          <Col span={18}>
            <Card title="页面列表" size="small">
              <Table columns={pageColumns} dataSource={pages} rowKey="id" pagination={false} />
            </Card>
          </Col>
        </Row>

        {selectedPage && (
          <Row gutter={16} style={{ marginTop: 16 }}>
            <Col span={6}>
              <Card title="组件列表" size="small" extra={<Button icon={<PlusOutlined />} onClick={handleAddComponent}>添加组件</Button>}>
                <Table columns={componentColumns} dataSource={components} rowKey="id" pagination={false} />
              </Card>
            </Col>
            <Col span={18}>
              <Card title={`预览: ${selectedPage.pageName}`} size="small" extra={<Button icon={<EyeOutlined />} onClick={handlePreview}>全屏预览</Button>}>
                <div style={{ padding: 16, backgroundColor: '#f5f5f5', minHeight: 400 }}>
                  {components.map((component) => (
                    <div key={component.id} style={{ marginBottom: 16 }}>
                      <div style={{ fontWeight: 500, marginBottom: 8 }}>{component.name} ({component.type})</div>
                      <div style={{ padding: 8, border: '1px dashed #ccc', backgroundColor: '#fff' }}>
                        <span>组件预览区域</span>
                      </div>
                    </div>
                  ))}
                  {components.length === 0 && (
                    <div style={{ textAlign: 'center', color: '#999', padding: 40 }}>
                      暂无组件，请添加组件
                    </div>
                  )}
                </div>
              </Card>
            </Col>
          </Row>
        )}
      </Card>

      <Modal
        title="新建页面"
        open={showAddPageModal}
        onOk={handleSavePage}
        onCancel={() => setShowAddPageModal(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="pageCode" label="页面编码" rules={[{ required: true }]}>
            <Input placeholder="请输入页面编码" />
          </Form.Item>
          <Form.Item name="pageName" label="页面名称" rules={[{ required: true }]}>
            <Input placeholder="请输入页面名称" />
          </Form.Item>
          <Form.Item name="path" label="路径">
            <Input placeholder="请输入页面路径" />
          </Form.Item>
          <Form.Item name="layout" label="布局">
            <Select options={[{ value: 'grid', label: '网格布局' }, { value: 'flex', label: '弹性布局' }]} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="添加组件"
        open={showAddComponentModal}
        onOk={handleSaveComponent}
        onCancel={() => setShowAddComponentModal(false)}
      >
        <Form form={componentForm} layout="vertical">
          <Form.Item name="type" label="组件类型" rules={[{ required: true }]}>
            <Select options={COMPONENT_TYPES} />
          </Form.Item>
          <Form.Item name="name" label="组件名称" rules={[{ required: true }]}>
            <Input placeholder="请输入组件名称" />
          </Form.Item>
          <Form.Item name="props" label="属性配置">
            <TextArea placeholder='{"placeholder": "请输入"}' rows={4} />
          </Form.Item>
          <Form.Item name="style" label="样式配置">
            <TextArea placeholder='{"width": "100%"}' rows={4} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="页面预览"
        open={showPreviewModal}
        onCancel={() => setShowPreviewModal(false)}
        width={800}
        footer={null}
      >
        <div style={{ padding: 24 }}>
          {components.map((component) => (
            <div key={component.id} style={{ marginBottom: 16 }}>
              <div style={{ fontWeight: 500, marginBottom: 8 }}>{component.name}</div>
              <div style={{ padding: 16, border: '1px solid #ddd', backgroundColor: '#fff' }}>
                <span>组件预览: {component.type}</span>
              </div>
            </div>
          ))}
        </div>
      </Modal>
    </div>
  );
};

export default PageDesigner;
