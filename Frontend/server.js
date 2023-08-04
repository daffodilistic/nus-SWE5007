const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Add CORS headers to the responses
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET,HEAD,OPTIONS,POST,PUT,OPTIONS');
  res.header(
    'Access-Control-Allow-Headers',
    'Origin, X-Requested-With, Content-Type, Accept, Authorization'
  );
  next();
});

// Proxy /api2 requests to http://localhost:8082
app.use(
  '/api2',
  createProxyMiddleware({
    target: 'http://localhost:8082',
    changeOrigin: true,
    pathRewrite: { '^/api2': '' },
  })
);

app.post('http://localhost:8082', (req, res) => {
  // Assuming the request contains valid JSON data in the request body
  const requestData = req.body;

  // Perform any necessary operations with the request data
  // For example, you can save the data to a database or process it in any way

  // Prepare the response data
  const responseData = {
    message: "Request processed successfully",
    data: requestData
  };

  // Set the status to 200 "OK" and send the JSON response
  res.status(200).json(responseData);
});

// Start the server on port 8080
const port = 8083;
app.listen(port, () => {
  console.log(`Server listening on http://localhost:${port}`);
});
