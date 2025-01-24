import express from 'express'
import {createProxyMiddleware} from 'http-proxy-middleware'

const app = express()

const backend = process.env.BACKEND_HOST
const frontend = process.env.FRONTEND_HOST

const backendUrl = /^\/api\/.*/

if (!backendUrl || !frontend) {
  console.log('Backend:', backend)
  console.log('Frontend:', frontend)
  console.error('BACKEND_HOST or FRONTEND_HOST not found')
  process.exit(1)
}

app.use((req, res, next) => {
  if (backendUrl.test(req.url)) {
    const proxy = createProxyMiddleware({
      target: backend,
      changeOrigin: true,
      pathRewrite: {
        '^/api': '',
      }
    })
    return proxy(req, res, next)
  }
  if (req.headers.upgrade?.toLowerCase() === 'websocket' && (frontend.includes('local') || frontend.includes('host.docker.internal'))) {
    const proxy = createProxyMiddleware({
      target: frontend,
      ws: true,
      changeOrigin: true,
    })
    return proxy(req, res, next)
  }

  const proxy = createProxyMiddleware({
    target: frontend,
    changeOrigin: true,
  })
  return proxy(req, res, next)
})

const port = Number(process.env.PORT) || 80
app.listen(port, () => {
  console.log(`Listening on port ${port}`)
})
