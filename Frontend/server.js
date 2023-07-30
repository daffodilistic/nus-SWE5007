const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Add CORS headers to the responses
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET,HEAD,OPTIONS,POST,PUT');
  res.header(
    'Access-Control-Allow-Headers',
    'Origin, X-Requested-With, Content-Type, Accept, Authorization'
  );
  next();
});

// Proxy /api requests to http://localhost:8081
/* app.use(
  '/api',
  createProxyMiddleware({
    target: 'http://localhost:8081',
    changeOrigin: true,
    pathRewrite: { '^/api': '' },
  })
); */

// Proxy /api2 requests to http://localhost:8082
app.use(
  '/api2',
  createProxyMiddleware({
    target: 'http://localhost:8082',
    changeOrigin: true,
    pathRewrite: { '^/api2': '' },
  })
);

// Start the server on port 8080
const port = 8083;
app.listen(port, () => {
  console.log(`Server listening on http://localhost:${port}`);
});
