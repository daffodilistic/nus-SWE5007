const { defineConfig } = require('@vue/cli-service')
/* module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': 'true',
      'Access-Control-Allow-Methods': 'GET,HEAD,OPTIONS,POST,PUT',
      'Access-Control-Allow-Headers':
        'Origin, X-Requested-With, Content-Type, Accept, Authorization',
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8081', // Replace this with your backend server's URL
        changeOrigin: true,
        pathRewrite: { '^/api': '' },
      },
      '/api2': {
        target: 'http://localhost:8082', // Replace this with your backend server's URL
        changeOrigin: true,
        pathRewrite: { '^/api2': '' },
      },
    },
  }
})*/

module.exports = {
  devServer: {
    proxy: 'http://localhost:8083', // Disable the dev server proxy
    '/api': {
      target: 'http://localhost:8081', // Replace this with your backend server's URL
      changeOrigin: true,
      pathRewrite: { '^/api': '' },
    },
  },
};