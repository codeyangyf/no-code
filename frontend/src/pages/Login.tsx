
import { Card, Form, Input, Button, message } from 'antd'
import { User, Lock } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

function Login() {
  const [form] = Form.useForm()
  const navigate = useNavigate()
  const { loading, handleLogin } = useAuth()

  const onFinish = async (values: { username: string; password: string }) => {
    const success = await handleLogin(values.username, values.password)
    if (success) {
      message.success('登录成功')
      navigate('/dashboard')
    } else {
      message.error('用户名或密码错误')
    }
  }

  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    }}>
      <Card
        style={{ width: 400, boxShadow: '0 4px 20px rgba(0,0,0,0.1)' }}
        title={<div style={{ textAlign: 'center', fontSize: 20, fontWeight: 'bold' }}>
          低代码平台
        </div>}
      >
        <Form
          form={form}
          name="login"
          onFinish={onFinish}
          layout="vertical"
        >
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input
              prefix={<User style={{ color: 'rgba(0,0,0,0.45)' }} />}
              placeholder="请输入用户名"
            />
          </Form.Item>

          <Form.Item
            name="password"
            label="密码"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password
              prefix={<Lock style={{ color: 'rgba(0,0,0,0.45)' }} />}
              placeholder="请输入密码"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={loading}
              size="large"
            >
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}

export default Login