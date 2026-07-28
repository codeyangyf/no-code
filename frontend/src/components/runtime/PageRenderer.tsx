import React from 'react';
import { Card, Input, Button, DatePicker, Select, Checkbox, Radio, Upload, message, Divider, Table } from 'antd';
import type { UploadProps } from 'antd';

const { TextArea } = Input;

interface ComponentConfig {
  id: string;
  type: string;
  name?: string;
  props?: Record<string, unknown>;
  style?: React.CSSProperties;
}

interface PageRendererProps {
  components: ComponentConfig[];
}

const ComponentRegistry: Record<string, React.ComponentType<{ component: ComponentConfig }>> = {
  Text: ({ component }) => (
    <div style={component.style}>{String(component.props?.text || '')}</div>
  ),
  Input: ({ component }) => (
    <Input
      placeholder={component.props?.placeholder as string}
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  TextArea: ({ component }) => (
    <TextArea
      placeholder={component.props?.placeholder as string}
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Number: ({ component }) => (
    <Input
      type="number"
      placeholder={component.props?.placeholder as string}
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Select: ({ component }) => (
    <Select
      placeholder={component.props?.placeholder as string}
      style={component.style}
      options={(component.props?.options as { value: string; label: string }[]) || []}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Radio: ({ component }) => (
    <Radio.Group
      options={(component.props?.options as { value: string; label: string }[]) || []}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Checkbox: ({ component }) => (
    <Checkbox {...(component.props as Record<string, unknown>)}>
      {component.name}
    </Checkbox>
  ),
  DatePicker: ({ component }) => (
    <DatePicker
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  DateTimePicker: ({ component }) => (
    <DatePicker
      showTime
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Button: ({ component }) => (
    <Button
      type={component.props?.type as 'primary' | 'default' | 'dashed' | 'link' | 'text'}
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    >
      {component.name || String(component.props?.children || '')}
    </Button>
  ),
  Card: ({ component }) => (
    <Card
      title={component.name || (component.props?.title as React.ReactNode)}
      style={component.style}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Table: ({ component }) => (
    <Table
      columns={(component.props?.columns || []) as { title: string; dataIndex: string; key: string }[]}
      dataSource={(component.props?.dataSource || []) as unknown[]}
      style={component.style}
      pagination={{ pageSize: 10 }}
      {...(component.props as Record<string, unknown>)}
    />
  ),
  Form: ({ component }) => (
    <Card title={component.name}>
      <div style={{ padding: 16 }}>
        {((component.props?.fields || []) as ComponentConfig[]).map((field: ComponentConfig) => (
          <div key={field.id} style={{ marginBottom: 16 }}>
            <label style={{ display: 'block', marginBottom: 8, fontWeight: 500 }}>
              {field.name}
              {(field.props?.required as boolean) && <span style={{ color: 'red' }}>*</span>}
            </label>
            <RenderComponent component={field} />
          </div>
        ))}
      </div>
    </Card>
  ),
  Layout: ({ component }) => (
    <div style={{ display: 'flex', gap: 16, ...component.style }}>
      {((component.props?.children || []) as ComponentConfig[]).map((child: ComponentConfig) => (
        <div key={child.id} style={{ flex: 1 }}>
          <RenderComponent component={child} />
        </div>
      ))}
    </div>
  ),
  Grid: ({ component }) => (
    <div style={{ display: 'grid', gridTemplateColumns: (component.props?.columns as string) || 'repeat(2, 1fr)', gap: 16, ...component.style }}>
      {((component.props?.children || []) as ComponentConfig[]).map((child: ComponentConfig) => (
        <div key={child.id}>
          <RenderComponent component={child} />
        </div>
      ))}
    </div>
  ),
  Divider: ({ component }) => (
    <Divider style={component.style}>{component.name}</Divider>
  ),
  FileUpload: ({ component }) => {
    const props: UploadProps = {
      action: '/api/files/upload',
      listType: 'picture-card',
      onChange: (info) => {
        if (info.file.status === 'done') {
          message.success(`${info.file.name} 上传成功`);
        } else if (info.file.status === 'error') {
          message.error(`${info.file.name} 上传失败`);
        }
      },
      ...(component.props as UploadProps),
    };
    return <Upload {...props} />;
  },
};

const RenderComponent: React.FC<{ component: ComponentConfig }> = ({ component }) => {
  const Component = ComponentRegistry[component.type];
  if (!Component) {
    return <div style={component.style}>未知组件: {component.type}</div>;
  }
  return <Component component={component} />;
};

const PageRenderer: React.FC<PageRendererProps> = ({ components }) => {
  return (
    <div style={{ padding: 24 }}>
      {components.map((component) => (
        <div key={component.id} style={{ marginBottom: 24 }}>
          <RenderComponent component={component} />
        </div>
      ))}
    </div>
  );
};

export default PageRenderer;
