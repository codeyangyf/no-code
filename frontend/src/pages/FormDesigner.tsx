import React, { useState, useEffect } from 'react';
import { Card, Button, Input, Modal, Form, Select, Table, message, Row, Col, Space, Switch, Tooltip } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, EyeOutlined, SaveOutlined, CopyOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { projectApi, formApi } from '../api';

interface FieldConfig {
  id: string;
  fieldCode: string;
  fieldName: string;
  fieldType: string;
  fieldConfig?: Record<string, unknown>;
  sortOrder?: number;
  required?: boolean;
}

interface FormConfig {
  id?: number;
  projectId: number;
  formCode: string;
  formName: string;
  fields: FieldConfig[];
  status?: number;
}

const FIELD_TYPES = [
  { value: 'TEXT', label: '单行文本' },
  { value: 'TEXTAREA', label: '多行文本' },
  { value: 'NUMBER', label: '数字' },
  { value: 'DATE', label: '日期' },
  { value: 'SELECT', label: '下拉选择' },
  { value: 'RADIO', label: '单选' },
  { value: 'CHECKBOX', label: '复选' },
  { value: 'FILE', label: '文件上传' },
];

const FormDesigner: React.FC = () => {
  const [projects, setProjects] = useState<{ id: number; projectName: string }[]>([]);
  const [selectedProject, setSelectedProject] = useState<number | null>(null);
  const [forms, setForms] = useState<FormConfig[]>([]);
  const [selectedForm, setSelectedForm] = useState<FormConfig | null>(null);
  const [fields, setFields] = useState<FieldConfig[]>([]);
  const [showAddFormModal, setShowAddFormModal] = useState(false);
  const [showAddFieldModal, setShowAddFieldModal] = useState(false);
  const [showEditFieldModal, setShowEditFieldModal] = useState(false);
  const [showPreviewModal, setShowPreviewModal] = useState(false);
  const [editingField, setEditingField] = useState<FieldConfig | null>(null);
  const [form] = Form.useForm();
  const [fieldForm] = Form.useForm();

  useEffect(() => {
    loadProjects();
  }, []);

  useEffect(() => {
    if (selectedProject) {
      loadForms(selectedProject);
    }
  }, [selectedProject]);

  useEffect(() => {
    if (selectedForm) {
      setFields(selectedForm.fields || []);
    }
  }, [selectedForm]);

  const loadProjects = async () => {
    try {
      const res = await projectApi.list(1, 100);
      setProjects(res.data?.list || []);
    } catch (error) {
      message.error('加载项目列表失败');
    }
  };

  const loadForms = async (projectId: number) => {
    try {
      const res = await formApi.list(projectId, 1, 100);
      setForms(res.data?.list || []);
    } catch (error) {
      message.error('加载表单列表失败');
    }
  };

  const handleSelectForm = (formConfig: FormConfig) => {
    setSelectedForm(formConfig);
    setFields(formConfig.fields || []);
  };

  const handleAddForm = () => {
    form.resetFields();
    setShowAddFormModal(true);
  };

  const handleSaveForm = async () => {
    try {
      const values = await form.validateFields();
      const newForm: FormConfig = {
        projectId: selectedProject!,
        formCode: values.formCode,
        formName: values.formName,
        fields: [],
      };

      await formApi.create(selectedProject!, newForm as unknown as Record<string, unknown>);
      message.success('表单创建成功');
      setShowAddFormModal(false);
      loadForms(selectedProject!);
    } catch (error) {
      message.error('创建表单失败');
    }
  };

  const handleUpdateForm = async () => {
    try {
      const values = await form.validateFields();
      if (selectedForm) {
        await formApi.update(selectedProject!, selectedForm.id!, {
          formName: values.formName,
        });
        message.success('表单更新成功');
        loadForms(selectedProject!);
      }
    } catch (error) {
      message.error('更新表单失败');
    }
  };

  const handleDeleteForm = async (formId: number) => {
    try {
      await formApi.delete(selectedProject!, formId);
      message.success('表单删除成功');
      if (selectedForm?.id === formId) {
        setSelectedForm(null);
        setFields([]);
      }
      loadForms(selectedProject!);
    } catch (error) {
      message.error('删除表单失败');
    }
  };

  const handleAddField = () => {
    fieldForm.resetFields();
    setEditingField(null);
    setShowAddFieldModal(true);
  };

  const handleEditField = (field: FieldConfig) => {
    fieldForm.setFieldsValue({
      fieldCode: field.fieldCode,
      fieldName: field.fieldName,
      fieldType: field.fieldType,
      required: field.required,
      sortOrder: field.sortOrder,
    });
    setEditingField(field);
    setShowEditFieldModal(true);
  };

  const handleSaveField = async (isEdit = false) => {
    try {
      const values = await fieldForm.validateFields();
      const newField: FieldConfig = {
        id: isEdit && editingField ? editingField.id : Date.now().toString(),
        fieldCode: values.fieldCode,
        fieldName: values.fieldName,
        fieldType: values.fieldType,
        required: values.required,
        sortOrder: values.sortOrder || fields.length,
      };

      let newFields: FieldConfig[];
      if (isEdit && editingField) {
        newFields = fields.map((f) => (f.id === editingField.id ? newField : f));
      } else {
        newFields = [...fields, newField];
      }

      setFields(newFields);

      if (selectedForm) {
        await formApi.update(selectedProject!, selectedForm.id!, {
          fields: newFields,
        });
        setSelectedForm({ ...selectedForm, fields: newFields });
      }

      message.success(isEdit ? '字段更新成功' : '字段添加成功');
      setShowAddFieldModal(false);
      setShowEditFieldModal(false);
      setEditingField(null);
    } catch (error) {
      message.error(isEdit ? '更新字段失败' : '添加字段失败');
    }
  };

  const handleDeleteField = async (fieldId: string) => {
    const newFields = fields.filter((f) => f.id !== fieldId);
    setFields(newFields);

    if (selectedForm) {
      await formApi.update(selectedProject!, selectedForm.id!, {
        fields: newFields,
      });
      setSelectedForm({ ...selectedForm, fields: newFields });
    }
    message.success('字段删除成功');
  };

  const handlePreview = () => {
    setShowPreviewModal(true);
  };

  const handleCopyField = (field: FieldConfig) => {
    const newField: FieldConfig = {
      ...field,
      id: Date.now().toString(),
      fieldCode: field.fieldCode + '_copy',
      fieldName: field.fieldName + ' (副本)',
    };
    const newFields = [...fields, newField];
    setFields(newFields);

    if (selectedForm) {
      formApi.update(selectedProject!, selectedForm.id!, {
        fields: newFields,
      }).then(() => {
        setSelectedForm({ ...selectedForm, fields: newFields });
        message.success('字段复制成功');
      }).catch(() => {
        message.error('复制字段失败');
      });
    }
  };

  const formColumns: ColumnsType<FormConfig> = [
    { title: '表单编码', dataIndex: 'formCode', key: 'formCode' },
    { title: '表单名称', dataIndex: 'formName', key: 'formName' },
    { 
      title: '字段数量', 
      key: 'fieldCount',
      render: (_: unknown, record: FormConfig) => record.fields?.length || 0
    },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      render: (status: number) => status === 1 ? '启用' : '禁用'
    },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: FormConfig) => (
        <Space>
          <Button type="link" onClick={() => handleSelectForm(record)}>
            编辑
          </Button>
          <Button type="link" icon={<EyeOutlined />} onClick={() => {
            setSelectedForm(record);
            handlePreview();
          }} />
          <Button type="link" danger onClick={() => handleDeleteForm(record.id!)}>
            删除
          </Button>
        </Space>
      ),
    },
  ];

  const fieldColumns: ColumnsType<FieldConfig> = [
    { title: '字段编码', dataIndex: 'fieldCode', key: 'fieldCode' },
    { title: '字段名称', dataIndex: 'fieldName', key: 'fieldName' },
    { 
      title: '字段类型', 
      dataIndex: 'fieldType', 
      key: 'fieldType',
      render: (type: string) => FIELD_TYPES.find(t => t.value === type)?.label || type
    },
    { 
      title: '必填', 
      dataIndex: 'required', 
      key: 'required',
      render: (required: boolean) => <Switch checked={required || false} disabled />
    },
    { title: '排序', dataIndex: 'sortOrder', key: 'sortOrder' },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: FieldConfig) => (
        <Space>
          <Tooltip title="编辑">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEditField(record)} />
          </Tooltip>
          <Tooltip title="复制">
            <Button type="link" icon={<CopyOutlined />} onClick={() => handleCopyField(record)} />
          </Tooltip>
          <Tooltip title="删除">
            <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDeleteField(record.id)} />
          </Tooltip>
        </Space>
      ),
    },
  ];

  const renderFieldPreview = (field: FieldConfig) => {
    switch (field.fieldType) {
      case 'TEXT':
        return <Input placeholder={field.fieldName} />;
      case 'TEXTAREA':
        return <Input.TextArea placeholder={field.fieldName} rows={3} />;
      case 'NUMBER':
        return <Input type="number" placeholder={field.fieldName} />;
      case 'DATE':
        return <Input placeholder={field.fieldName} suffix={<span>📅</span>} />;
      case 'SELECT':
        return <Select placeholder={field.fieldName} />;
      case 'RADIO':
        return (
          <div>
            <label><input type="radio" name={field.fieldCode} /> 选项1</label>
            <label style={{ marginLeft: 16 }}><input type="radio" name={field.fieldCode} /> 选项2</label>
          </div>
        );
      case 'CHECKBOX':
        return (
          <div>
            <label><input type="checkbox" /> 选项1</label>
            <label style={{ marginLeft: 16 }}><input type="checkbox" /> 选项2</label>
          </div>
        );
      case 'FILE':
        return <Button type="dashed">上传文件</Button>;
      default:
        return <Input placeholder={field.fieldName} />;
    }
  };

  return (
    <div style={{ padding: 24 }}>
      <Card 
        title="表单设计器" 
        extra={selectedProject ? <Button icon={<PlusOutlined />} onClick={handleAddForm}>新建表单</Button> : null}
      >
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
            <Card title="表单列表" size="small">
              <Table columns={formColumns} dataSource={forms} rowKey="id" pagination={false} />
            </Card>
          </Col>
        </Row>

        {selectedForm && (
          <Row gutter={16} style={{ marginTop: 16 }}>
            <Col span={6}>
              <Card 
                title="字段列表" 
                size="small" 
                extra={<Button icon={<PlusOutlined />} onClick={handleAddField}>添加字段</Button>}
              >
                <Table columns={fieldColumns} dataSource={fields} rowKey="id" pagination={false} />
              </Card>
            </Col>
            <Col span={18}>
              <Card 
                title={`表单详情: ${selectedForm.formName}`} 
                size="small"
                extra={
                  <Space>
                    <Button icon={<SaveOutlined />} onClick={handleUpdateForm}>保存修改</Button>
                    <Button icon={<EyeOutlined />} onClick={handlePreview}>预览</Button>
                  </Space>
                }
              >
                <Form form={form} layout="vertical" initialValues={{
                  formCode: selectedForm.formCode,
                  formName: selectedForm.formName,
                }}>
                  <Row gutter={16}>
                    <Col span={12}>
                      <Form.Item name="formCode" label="表单编码" rules={[{ required: true }]}>
                        <Input disabled />
                      </Form.Item>
                    </Col>
                    <Col span={12}>
                      <Form.Item name="formName" label="表单名称" rules={[{ required: true }]}>
                        <Input />
                      </Form.Item>
                    </Col>
                  </Row>
                </Form>
                
                <div style={{ marginTop: 16, padding: 16, backgroundColor: '#f5f5f5', minHeight: 300 }}>
                  <h4 style={{ marginBottom: 16 }}>表单预览</h4>
                  <Form layout="vertical">
                    {fields.map((field) => (
                      <Form.Item 
                        key={field.id} 
                        label={field.fieldName + (field.required ? ' *' : '')}
                      >
                        {renderFieldPreview(field)}
                      </Form.Item>
                    ))}
                    {fields.length === 0 && (
                      <div style={{ textAlign: 'center', color: '#999', padding: 40 }}>
                        暂无字段，请添加字段
                      </div>
                    )}
                  </Form>
                </div>
              </Card>
            </Col>
          </Row>
        )}
      </Card>

      <Modal
        title="新建表单"
        open={showAddFormModal}
        onOk={handleSaveForm}
        onCancel={() => setShowAddFormModal(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="formCode" label="表单编码" rules={[{ required: true }]}>
            <Input placeholder="请输入表单编码" />
          </Form.Item>
          <Form.Item name="formName" label="表单名称" rules={[{ required: true }]}>
            <Input placeholder="请输入表单名称" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="添加字段"
        open={showAddFieldModal}
        onOk={() => handleSaveField(false)}
        onCancel={() => setShowAddFieldModal(false)}
      >
        <Form form={fieldForm} layout="vertical">
          <Form.Item name="fieldCode" label="字段编码" rules={[{ required: true }]}>
            <Input placeholder="请输入字段编码" />
          </Form.Item>
          <Form.Item name="fieldName" label="字段名称" rules={[{ required: true }]}>
            <Input placeholder="请输入字段名称" />
          </Form.Item>
          <Form.Item name="fieldType" label="字段类型" rules={[{ required: true }]}>
            <Select options={FIELD_TYPES} />
          </Form.Item>
          <Form.Item name="required" label="必填">
            <Switch />
          </Form.Item>
          <Form.Item name="sortOrder" label="排序">
            <Input type="number" placeholder="排序号" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="编辑字段"
        open={showEditFieldModal}
        onOk={() => handleSaveField(true)}
        onCancel={() => { setShowEditFieldModal(false); setEditingField(null); }}
      >
        <Form form={fieldForm} layout="vertical">
          <Form.Item name="fieldCode" label="字段编码" rules={[{ required: true }]}>
            <Input disabled />
          </Form.Item>
          <Form.Item name="fieldName" label="字段名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="fieldType" label="字段类型" rules={[{ required: true }]}>
            <Select options={FIELD_TYPES} />
          </Form.Item>
          <Form.Item name="required" label="必填">
            <Switch />
          </Form.Item>
          <Form.Item name="sortOrder" label="排序">
            <Input type="number" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title={`表单预览: ${selectedForm?.formName}`}
        open={showPreviewModal}
        onCancel={() => setShowPreviewModal(false)}
        width={600}
        footer={null}
      >
        <div style={{ padding: 24 }}>
          <Form layout="vertical">
            {fields.map((field) => (
              <Form.Item 
                key={field.id} 
                label={field.fieldName + (field.required ? ' *' : '')}
              >
                {renderFieldPreview(field)}
              </Form.Item>
            ))}
            {fields.length === 0 && (
              <div style={{ textAlign: 'center', color: '#999', padding: 40 }}>
                暂无字段
              </div>
            )}
            <Form.Item>
              <Button type="primary" block>提交</Button>
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </div>
  );
};

export default FormDesigner;