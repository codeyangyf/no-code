import { Card, Typography } from 'antd'

const { Title } = Typography

function AuditLog() {
  return (
    <Card>
      <Title level={4}>审计日志</Title>
      <p style={{ color: '#666' }}>审计日志功能页面，开发中...</p>
    </Card>
  )
}

export default AuditLog
